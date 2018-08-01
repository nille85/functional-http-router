package be.nille.http.router.domain.routedispatcher;

import be.nille.http.router.domain.*;
import be.nille.http.router.domain.routematcher.RouteMatcher;

import java.util.Optional;
import java.util.function.Predicate;

public class DefaultRouteDispatcher implements RouteDispatcher {

    private final RouteMatcher routeMatcher;

    public DefaultRouteDispatcher(RouteMatcher routeMatcher) {
        this.routeMatcher = routeMatcher;
    }

    @Override
    public Optional<Response> dispatch(Request request, HttpRouterConfiguration httpRouterConfiguration) {
        //TODO support for request interceptors
        Predicate<Route> routeFilter = (route) -> routeMatcher.matches(request, route);
        Optional<Route> optionalRoute = httpRouterConfiguration.getRoutes()
                .stream()
                .filter(routeFilter)
                .findFirst();
        //TODO add variables to request from route
        Optional<Response> optionalResponse = optionalRoute.map(route -> route.getResponseHandler().handle(request));
        //TODO support for response interceports
        return optionalResponse;



    }
}
