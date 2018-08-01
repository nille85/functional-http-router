package be.nille.http.router.domain.routematcher;

import be.nille.http.router.domain.Request;
import be.nille.http.router.domain.Route;

import java.util.List;

public class DefaultRouteMatcher implements RouteMatcher {

    private final RouteMatchersFactory routeMatchersFactory;

    public DefaultRouteMatcher(RouteMatchersFactory routeMatchersFactory) {
        this.routeMatchersFactory = routeMatchersFactory;
    }

    @Override
    public boolean matches(Request request, Route route) {
        List<RouteMatcher> routeMatchers = routeMatchersFactory.createRouteMatchers(route);
        return routeMatchers.stream().allMatch(
                routeMatcher -> routeMatcher.matches(request, route)
        );
    }
}
