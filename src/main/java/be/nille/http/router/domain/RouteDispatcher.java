package be.nille.http.router.domain;

import java.util.Optional;

public interface RouteDispatcher {
    Optional<Response> dispatch(Request request, HttpRouterConfiguration httpRouterConfiguration);
}
