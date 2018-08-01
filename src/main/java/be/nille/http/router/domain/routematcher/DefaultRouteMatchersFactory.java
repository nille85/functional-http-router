package be.nille.http.router.domain.routematcher;

import be.nille.http.router.domain.Route;

import java.util.ArrayList;
import java.util.List;

public class DefaultRouteMatchersFactory implements RouteMatchersFactory {
    @Override
    public List<RouteMatcher> createRouteMatchers(Route route) {
        List<RouteMatcher> routeMatchers = new ArrayList<>();
        if(route.getPath() != null){
            routeMatchers.add(new PathRouteMatcher());
        }
        if(route.getMethod() != null){
            routeMatchers.add(new MethodRouteMatcher());
        }
        return routeMatchers;
    }
}
