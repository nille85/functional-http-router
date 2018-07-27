package be.nille.http.router.integration.http.netty;

import be.nille.http.router.domain.HttpRouterConfiguration;
import be.nille.http.router.domain.RouteDispatcher;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpServerInitializer.class);
    private final RouteDispatcher routeDispatcher;
    private final HttpRouterConfiguration httpRouterConfiguration;
    private final SslContext sslCtx;

    public HttpServerInitializer(RouteDispatcher routeDispatcher, HttpRouterConfiguration httpRouterConfiguration, SslContext sslCtx) {
        this.routeDispatcher = routeDispatcher;
        this.httpRouterConfiguration = httpRouterConfiguration;
        this.sslCtx = sslCtx;
    }

    @Override
    public void initChannel(SocketChannel ch) {
        LOGGER.debug(
                String.format("initializing server socket channel with id %s", ch.id().asShortText())
        );
        ChannelPipeline p = ch.pipeline();
        if (sslCtx != null) {
            p.addLast(sslCtx.newHandler(ch.alloc()));
        }
        p.addLast(new HttpRequestDecoder());
        // Uncomment the following line if you don't want to handle HttpChunks.
        //p.addLast(new HttpObjectAggregator(1048576));
        p.addLast(new HttpResponseEncoder());
        // Remove the following line if you don't want automatic content compression.
        //p.addLast(new HttpContentCompressor());
        p.addLast(new HttpRouterHandler(httpRouterConfiguration, routeDispatcher));
    }
}
