package be.nille.http.router.domain;

import java.util.*;

public class Request {

    private final Method method;
    private final String uri;
    private final Map<String,String> headers;
    private final List<String> bodyChunks;

    private Request(Builder builder) {
        Objects.requireNonNull(builder.method, "Method should not be null");
        method = builder.method;
        Objects.requireNonNull(builder.uri, "Uri should not be null");
        uri = builder.uri;
        Objects.requireNonNull(builder.headers, "Headers should not be null");
        headers = builder.headers;
        Objects.requireNonNull(builder.bodyChunks);
        bodyChunks = builder.bodyChunks;
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

    public List<String> getBodyChunks() {
        return bodyChunks;
    }

    public String getBody(){
        return String.join("", bodyChunks);
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
        private List<String> bodyChunks;

        public Builder() {
            bodyChunks = new ArrayList<>();
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

        public Builder addBodyChunk(String val){
            bodyChunks.add(val);
            return this;
        }

        public Request build() {
            return new Request(this);
        }
    }
}
