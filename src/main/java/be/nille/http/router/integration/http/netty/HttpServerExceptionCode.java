package be.nille.http.router.integration.http.netty;

public enum HttpServerExceptionCode {

    SSL_EXCEPTION(1), SOCKET_EXCEPTION(2), UNSUPPORTED_METHOD(3), REQUEST_DECODING_EXCEPTION(4);

    private final int code;

    HttpServerExceptionCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
