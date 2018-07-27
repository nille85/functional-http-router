package be.nille.http.router.domain;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class Request {

    private final Method method;
    private final String uri;
    private final Map<String,String> headers;
    private final Optional<String> body;

    private Request(Builder builder) {
        Objects.requireNonNull(builder.method, "Method should not be null");
        method = builder.method;
        Objects.requireNonNull(builder.uri, "Uri should not be null");
        uri = builder.uri;
        Objects.requireNonNull(builder.headers, "Headers should not be null");
        headers = builder.headers;
        body = Optional.ofNullable(builder.body);
    }

    public Method getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Optional<String> getHeaderValue(String headerName){
        return headers.entrySet().stream()
                .filter(entry -> entry.getKey().equals(headerName))
                .findFirst()
                .map(Map.Entry::getValue);
    }

    public static final class Builder {
        private Method method;
        private String uri;
        private Map<String, String> headers;
        private String body;

        public Builder() {
        }

        public Builder method(Method val) {
            method = val;
            return this;
        }

        public Builder uri(String val) {
            uri = val;
            return this;
        }

        public Builder headers(Map<String, String> val) {
            headers = val;
            return this;
        }

        public Builder body(String val){
            body = val;
            return this;
        }

        public Request build() {
            return new Request(this);
        }
    }
}
