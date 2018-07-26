package be.nille.http.router.integration.http.netty;

public class HttpServerException extends RuntimeException {

    public HttpServerException(HttpServerExceptionCode code, Throwable ex) {
        super(
                String.format("An HTTP server exception occurred, code: %s", code.name()),
                ex
        );

    }
}
