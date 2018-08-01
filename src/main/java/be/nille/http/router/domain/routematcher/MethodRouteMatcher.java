package be.nille.http.router.domain.routematcher;

import be.nille.http.router.domain.Request;
import be.nille.http.router.domain.Route;

public class MethodRouteMatcher implements RouteMatcher {
    @Override
    public boolean matches(Request request, Route route) {
        return route.getMethod().equals(request.getMethod());
    }
}
