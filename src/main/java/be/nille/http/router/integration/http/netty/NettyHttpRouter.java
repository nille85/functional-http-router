package be.nille.http.router.integration.http.netty;

import be.nille.http.router.domain.routedispatcher.DefaultRouteDispatcher;
import be.nille.http.router.domain.HttpRouter;
import be.nille.http.router.domain.HttpRouterConfiguration;
import be.nille.http.router.domain.RouteDispatcher;
import be.nille.http.router.domain.routematcher.DefaultRouteMatcher;
import be.nille.http.router.domain.routematcher.DefaultRouteMatchersFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyHttpRouter implements HttpRouter {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyHttpRouter.class);
    private final RouteDispatcher routeDispatcher;
    private final SslContextFactory sslContextFactory;

    public NettyHttpRouter(RouteDispatcher routeDispatcher, SslContextFactory sslContextFactory) {
        this.routeDispatcher = routeDispatcher;
        this.sslContextFactory = sslContextFactory;
    }

    public static HttpRouter create() {
        return new NettyHttpRouter(
                new DefaultRouteDispatcher(
                        new DefaultRouteMatcher(new DefaultRouteMatchersFactory())
                ),
                new DefaultSslContextFactory()
        );
    }

    public void start(HttpRouterConfiguration configuration) {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            boolean useSSL = configuration.isUseSSL();
            int portNumber = configuration.getPortNumber();
            SslContext sslContext = sslContextFactory.createSslContext(configuration).orElse(null);
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new HttpServerInitializer(routeDispatcher, configuration, sslContext));
            Channel ch = b.bind(configuration.getPortNumber()).sync().channel();
            LOGGER.info("Open your web browser and navigate to " +
                    (useSSL ? "https" : "http") + "://127.0.0.1:" + portNumber + '/');

            ch.closeFuture().sync();
        } catch (InterruptedException ex) {
            throw new HttpServerException(HttpServerExceptionCode.SOCKET_EXCEPTION, ex);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


}
