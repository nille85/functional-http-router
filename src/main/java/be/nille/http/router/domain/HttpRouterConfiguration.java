package be.nille.http.router.domain;

import java.util.Collection;
import java.util.Objects;

public class HttpRouterConfiguration {

    private final int portNumber;
    private final boolean useSSL;
    private final Collection<Route> routes;

    private HttpRouterConfiguration(Builder builder) {
        portNumber = builder.portNumber;
        useSSL = builder.useSSL;
        Objects.requireNonNull(builder.routes, "routes must not be null");
        routes = builder.routes;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public boolean isUseSSL() {
        return useSSL;
    }

    public Collection<Route> getRoutes() {
        return routes;
    }

    public static final class Builder {
        private int portNumber;
        private boolean useSSL;
        private Collection<Route> routes;

        public Builder() {
            this.portNumber = 80;
            this.useSSL = false;
        }

        public Builder withPortNumber(int val) {
            portNumber = val;
            return this;
        }

        public Builder withUseSSL(boolean val) {
            useSSL = val;
            return this;
        }

        public Builder withRoutes(Collection<Route> val){
            routes = val;
            return this;
        }

        public HttpRouterConfiguration build() {
            return new HttpRouterConfiguration(this);
        }
    }
}
