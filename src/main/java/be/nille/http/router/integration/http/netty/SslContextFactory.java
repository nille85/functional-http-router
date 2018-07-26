package be.nille.http.router.integration.http.netty;

import be.nille.http.router.domain.HttpRouterConfiguration;
import io.netty.handler.ssl.SslContext;

import java.util.Optional;

public interface SslContextFactory {

    Optional<SslContext> createSslContext(HttpRouterConfiguration httpRouterConfiguration);
}
