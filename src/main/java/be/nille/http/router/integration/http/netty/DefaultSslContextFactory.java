package be.nille.http.router.integration.http.netty;

import be.nille.http.router.domain.HttpRouterConfiguration;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;
import java.util.Optional;

public class DefaultSslContextFactory implements SslContextFactory{

    public Optional<SslContext> createSslContext(HttpRouterConfiguration httpRouterConfiguration){
        if (httpRouterConfiguration.isUseSSL()) {
            try {
                SelfSignedCertificate ssc = new SelfSignedCertificate();
                return Optional.of(SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build());
            } catch (CertificateException | SSLException e) {
                throw new HttpServerException(HttpServerExceptionCode.SSL_EXCEPTION, e);
            }

        }
        return Optional.empty();
    }
}
