package be.nille.http.router.domain.routematcher

import be.nille.http.router.domain.Method
import be.nille.http.router.domain.Request
import be.nille.http.router.domain.Route
import spock.lang.Specification
import spock.lang.Unroll

class MethodRouteMatcherTest extends Specification {

    RouteMatcher subject

    def setup() {
        subject = new MethodRouteMatcher()
    }

    @Unroll
    def "requestMethod #requestMethod should match routeMethod #routeMethod"() {
        given:
        Request request = Mock(Request)
        request.method >> requestMethod
        Route route = new Route.Builder()
                .withMethod(routeMethod)
                .build()

        when:
        def result = subject.matches(request, route);

        then:
        result == matches
        where:
        requestMethod | routeMethod | matches
        Method.GET    | Method.GET  | true
        Method.GET    | Method.POST | false
        Method.POST   | Method.GET  | false
        Method.POST   | Method.POST | true
    }
}
