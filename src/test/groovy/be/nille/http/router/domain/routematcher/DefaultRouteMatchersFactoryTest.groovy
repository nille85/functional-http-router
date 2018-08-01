package be.nille.http.router.domain.routematcher

import be.nille.http.router.domain.Method
import be.nille.http.router.domain.Path
import be.nille.http.router.domain.Route
import spock.lang.Specification
import spock.lang.Unroll

class DefaultRouteMatchersFactoryTest extends Specification {

    RouteMatchersFactory subject

    def setup() {
        subject = new DefaultRouteMatchersFactory()
    }

    @Unroll
    def "should contain the expected matchers"() {
        given:
        Route route = new Route.Builder()
                .withPath(routePath)
                .withMethod(routeMethod)
                .build()

        when:
        def result = subject.createRouteMatchers(route);

        then:
        containsPathMatcher == result.find { it instanceof PathRouteMatcher } != null
        containsMethodMatcher == result.find { it instanceof MethodRouteMatcher } != null
        where:
        routePath        | routeMethod | containsPathMatcher | containsMethodMatcher
        Path.create("/") | Method.GET  | true                | true
        Path.create("/") | null        | true                | false
        null             | Method.POST | false               | true
        null             | null        | false               | false
    }
}
