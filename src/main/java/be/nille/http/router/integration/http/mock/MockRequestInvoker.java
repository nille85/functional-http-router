package be.nille.http.router.integration.http.mock;

import be.nille.http.router.domain.*;
import be.nille.http.router.domain.routedispatcher.DefaultRouteDispatcher;
import be.nille.http.router.domain.routematcher.DefaultRouteMatcher;
import be.nille.http.router.domain.routematcher.DefaultRouteMatchersFactory;

import java.util.Optional;

public class MockRequestInvoker {


    private final RouteDispatcher routeDispatcher;
    private final HttpRouterConfiguration httpRouterConfiguration;

    private MockRequestInvoker(final RouteDispatcher routeDispatcher, final HttpRouterConfiguration httpRouterConfiguration) {
        this.routeDispatcher = routeDispatcher;
        this.httpRouterConfiguration = httpRouterConfiguration;
    }

    public static MockRequestInvoker create(final HttpRouterConfiguration httpRouterConfiguration) {
        return new MockRequestInvoker(
                new DefaultRouteDispatcher(
                        new DefaultRouteMatcher(new DefaultRouteMatchersFactory())),
                httpRouterConfiguration
        );
    }

    public Optional<Response> sendRequest(Request request) {
        return routeDispatcher.dispatch(request, httpRouterConfiguration);
    }
}
