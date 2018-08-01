package be.nille.http.router.domain.routematcher;

import be.nille.http.router.domain.Route;

import java.util.List;

public interface RouteMatchersFactory {

    List<RouteMatcher> createRouteMatchers(Route route);
}
