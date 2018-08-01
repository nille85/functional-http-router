package be.nille.http.router.domain.routematcher

import be.nille.http.router.domain.Path
import be.nille.http.router.domain.Request
import be.nille.http.router.domain.Route
import spock.lang.Specification
import spock.lang.Unroll

class PathRouteMatcherTest extends Specification {

    RouteMatcher subject

    def setup() {
        subject = new PathRouteMatcher()
    }

    @Unroll
    def "requestPath #requestUri should match routePath #routePath"() {
        given:
        Request request = Mock(Request)
        request.uri >> requestUri
        Route route = new Route.Builder()
                .withPath(new Path(routePath))
                .build()

        when:
        def result = subject.matches(request, route);

        then:
        result == matches
        where:
        requestUri            | routePath         | matches
        "/"                   | "/"               | true
        "/?m=d"               | "/"               | true
        "/abd"                | "/"               | false
        "/abd"                | "/abd/"           | false
        "/long/ldl/dll/d?m=d" | "/long/ldl/dll/d" | true
        "/abd/m/l?t=v"        | "/abd/m/l"        | true
        "/abd/m?t=v"          | "/abd/m"          | true
        "/abd/m?t=v&k=l"      | "/abd/m"          | true
        "/abd?t=v&k=l"        | "/abd"            | true

    }
}
