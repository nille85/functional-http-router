package be.nille.http.router.domain.routematcher;

import be.nille.http.router.domain.Path;
import be.nille.http.router.domain.Request;
import be.nille.http.router.domain.Route;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PathRouteMatcher implements RouteMatcher {
    @Override
    public boolean matches(Request request, Route route) {

        Predicate<String> routePredicate = (routePath) -> {
            String requestPattern = String.format("^%s(\\?.*)?$", routePath);
            String requestPath = request.getUri();
            Pattern p = Pattern.compile(requestPattern);
            Matcher matcher = p.matcher(requestPath);
            return matcher.matches();
        };
        return Optional.of(route.getPath()).map(Path::getValue)
                .filter(routePredicate)
                .isPresent();
    }
}
