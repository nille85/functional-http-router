package be.nille.http.router.integration.http.netty;

import be.nille.http.router.domain.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import io.netty.handler.codec.http.cookie.ServerCookieEncoder;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HttpRouterHandler extends SimpleChannelInboundHandler<Object> {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRouterHandler.class);

    private final HttpRouterConfiguration httpRouterConfiguration;
    private final Request.Builder requestBuilder;
    private final RouteDispatcher routeDispatcher;
    private final StringBuilder bodyFuffer;

    public HttpRouterHandler(final HttpRouterConfiguration httpRouterConfiguration, Request.Builder requestBuilder, RouteDispatcher routeDispatcher) {
        this.httpRouterConfiguration = httpRouterConfiguration;
        this.requestBuilder = requestBuilder;
        this.routeDispatcher = routeDispatcher;
        this.bodyFuffer = new StringBuilder();
    }

    public HttpRouterHandler(final HttpRouterConfiguration httpRouterConfiguration, final RouteDispatcher routeDispatcher) {
        this(httpRouterConfiguration, new Request.Builder(), routeDispatcher);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        LOGGER.debug(String.format("Reading a message from channel with id %s", ctx.channel().id().asShortText()));
        if (msg instanceof HttpObject) {
            HttpObject httpObject = (HttpObject) msg;
            if (!this.canBeDecoded(httpObject)) {
                writeInternalServerErrorResponse(ctx, HttpServerExceptionCode.REQUEST_DECODING_EXCEPTION);
                return;
            }
            if (msg instanceof HttpRequest) {
                HttpRequest request = (HttpRequest) msg;
                handleHttpRequest(ctx, request);
            }

            /**
             * This is a chunk of the body
             */
            if (msg instanceof HttpContent) {
                HttpContent httpContent = (HttpContent) msg;
                handleHttpContent(ctx, httpContent);
            }
        }
    }

    private void handleHttpRequest(ChannelHandlerContext ctx, HttpRequest httpRequest) {
        if (HttpUtil.is100ContinueExpected(httpRequest)) {
            send100Continue(ctx);
        }
        Map<String, String> headers = httpRequest.headers().entries().stream().collect(
                Collectors.toMap(
                        e -> e.getKey(),
                        e -> e.getValue()
                ));


        //TODO do not throw an error but write a bad request response
        requestBuilder.method(Method.byName(httpRequest.method().name()).orElseThrow(
                () -> new HttpServerException(HttpServerExceptionCode.UNSUPPORTED_METHOD)))
                .uri(httpRequest.uri())
                .headers(headers);

    }

    private void handleHttpContent(ChannelHandlerContext ctx, HttpContent httpContent) {

        ByteBuf content = httpContent.content();
        if (content.isReadable()) {
            bodyFuffer.append(content.toString(CharsetUtil.UTF_8));
        }
        if (httpContent instanceof LastHttpContent) {
            Request request = requestBuilder.body(bodyFuffer.toString()).build();
            boolean isKeepAlive = isKeepAlive(request);

            Optional<Response> responseOptional = routeDispatcher.dispatch(request, httpRouterConfiguration);

            FullHttpResponse httpResponse = responseOptional.map(response ->
                    creatOkHttpResponse(response)
            ).orElse(createBadRequestResponse());
            httpResponse = addKeepAliveHeadersAndCookies(httpResponse, request);
            // Write the response.
            ctx.write(httpResponse);
            if (!isKeepAlive) {
                // If keep-alive is off, close the connection once the content is fully written.
                ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
            }
        }
    }

    private boolean canBeDecoded(HttpObject o) {
        DecoderResult result = o.decoderResult();
        return result.isSuccess();
    }

    private boolean isKeepAlive(Request request) {
        return request.getHeaderValue("connection")
                .map(value -> value.equals("keep-alive")).orElse(false);

    }

    private FullHttpResponse addKeepAliveHeadersAndCookies(FullHttpResponse httpResponse, Request request) {
        FullHttpResponse copy = httpResponse.copy();
        boolean isKeepAlive = isKeepAlive(request);
        if (isKeepAlive) {
            // Add 'Content-Length' header only for a keep-alive connection.
            copy.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, copy.content().readableBytes());
            // Add keep alive header as per:
            // - http://www.w3.org/Protocols/HTTP/1.1/draft-ietf-http-v11-spec-01.html#Connection
            copy.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
        // Encode the cookie.
        request.getHeaderValue("cookie")
                .ifPresent(cookieString -> {
                    Set<Cookie> cookies = ServerCookieDecoder.STRICT.decode(cookieString);
                    if (!cookies.isEmpty()) {
                        // Reset the cookies if necessary.
                        for (Cookie cookie : cookies) {
                            copy.headers().add(HttpHeaderNames.SET_COOKIE, ServerCookieEncoder.STRICT.encode(cookie));
                        }
                    }
                });
        return copy;
    }

    private FullHttpResponse creatOkHttpResponse(Response response) {
        FullHttpResponse httpResponse = new DefaultFullHttpResponse(
                HTTP_1_1, OK,
                Unpooled.copiedBuffer(response.getValue(), CharsetUtil.UTF_8));

        httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
        return httpResponse;
    }

    private FullHttpResponse createBadRequestResponse() {
        FullHttpResponse httpResponse = new DefaultFullHttpResponse(
                HTTP_1_1, BAD_REQUEST,
                Unpooled.copiedBuffer("Bad Request", CharsetUtil.UTF_8));
        return httpResponse;
    }

    private void writeInternalServerErrorResponse(ChannelHandlerContext channelHandlerContext, HttpServerExceptionCode httpServerExceptionCode) {
        FullHttpResponse fullHttpResponse = createInternalServerErrorResponse(httpServerExceptionCode);
        channelHandlerContext.write(fullHttpResponse);
        channelHandlerContext.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    private FullHttpResponse createInternalServerErrorResponse(HttpServerExceptionCode httpServerExceptionCode) {
        FullHttpResponse httpResponse = new DefaultFullHttpResponse(
                HTTP_1_1, INTERNAL_SERVER_ERROR,
                Unpooled.copiedBuffer(String.format("Internal Server Error, reason: %s", httpServerExceptionCode.name()), CharsetUtil.UTF_8));
        return httpResponse;
    }


    private static void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, CONTINUE);
        ctx.write(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
