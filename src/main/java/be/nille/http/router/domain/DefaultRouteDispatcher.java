package be.nille.http.router.domain;

import java.util.Optional;
import java.util.function.Predicate;

public class DefaultRouteDispatcher  implements RouteDispatcher{
    @Override
    public Optional<Response> dispatch(Request request, HttpRouterConfiguration httpRouterConfiguration) {
        Predicate<Route> routeFilter = (route) -> route.getMethod().equals(request.getMethod());
        return httpRouterConfiguration.getRoutes()
                .stream()
                .filter(routeFilter)
                .findFirst()
                .map(route -> route.getResponseHandler().handle(request));
    }
}
