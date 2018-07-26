package be.nille.http.router.domain;

public class Route {

    private final Method method;
    private final Path path;
    private final ResponseHandler responseHandler;

    private Route(Builder builder) {
        method = builder.method;
        path = builder.path;
        responseHandler = builder.responseHandler;
    }

    public Method getMethod() {
        return method;
    }

    public Path getPath() {
        return path;
    }

    public ResponseHandler getResponseHandler() {
        return responseHandler;
    }

    public static final class Builder {
        private Method method;
        private Path path;
        private ResponseHandler responseHandler;

        public Builder() {
        }

        public Builder withMethod(Method val) {
            method = val;
            return this;
        }

        public Builder withPath(Path val) {
            path = val;
            return this;
        }

        public Builder withResponseHandler(ResponseHandler val) {
            responseHandler = val;
            return this;
        }

        public Route build() {
            return new Route(this);
        }
    }
}
