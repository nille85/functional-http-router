package be.nille.http.router.domain.routematcher;

import be.nille.http.router.domain.Request;
import be.nille.http.router.domain.Route;

public interface RouteMatcher {

    boolean matches(Request request, Route route);
}
