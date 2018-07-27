package be.nille.http.router.domain

import be.nille.http.router.integration.http.mock.MockRequestInvoker
import com.google.common.collect.Lists
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
        Request request = new Request.Builder()
                .method(method)
                .uri("/dsfsdf")
                .headers(new HashMap<String, String>())
                .build()
        Collection<Route> routes = [new Route.Builder()
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
        method      | hasResult
        Method.GET  | true
        Method.POST | false
    }
}
