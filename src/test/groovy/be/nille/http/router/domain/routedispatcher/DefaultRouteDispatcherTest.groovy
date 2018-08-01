package be.nille.http.router.domain.routedispatcher

import be.nille.http.router.domain.HttpRouterConfiguration
import be.nille.http.router.domain.Method
import be.nille.http.router.domain.Path
import be.nille.http.router.domain.Request
import be.nille.http.router.domain.Response
import be.nille.http.router.domain.Route
import be.nille.http.router.domain.RouteDispatcher
import be.nille.http.router.domain.routedispatcher.DefaultRouteDispatcher
import be.nille.http.router.integration.http.mock.MockRequestInvoker
import spock.lang.Specification
import spock.lang.Unroll

class DefaultRouteDispatcherTest extends Specification {

    RouteDispatcher dispatcher

    def setup() {
        dispatcher = new DefaultRouteDispatcher()
    }

    @Unroll
    def "should handle requests by method #method if the route is available"() {
        given:
        Request request = Mock(Request)
        request.method >> method
        request.uri >> requestUri
        Collection<Route> routes = [new Route.Builder()
                                            .withPath(new Path("/context/some/path"))
                                            .withMethod(Method.GET)
                                            .withResponseHandler({ it ->
            Response.create(
                    String.format("Hello, you performed a %s request", it.getMethod()))
        }).build()
        ]

        MockRequestInvoker invoker = MockRequestInvoker.create(new HttpRouterConfiguration.Builder()
                .withRoutes(routes)
                .build())
        when:
        Optional<Response> result = invoker.sendRequest(request)

        then:
        hasResult == result.isPresent()

        where:
        method      | requestUri           | hasResult
        Method.GET  | "/context/some/path" | true
        Method.GET  | "/context/some/pat"  | false
        Method.POST | "/context/some/path" | false
    }
}
