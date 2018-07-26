package be.nille.http.router;

import be.nille.http.router.domain.*;
import be.nille.http.router.integration.http.mock.MockRequestInvoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;

public class ExampleApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExampleApp.class);

    public static void main(String[] args) {

        Collection<Route> routes = new ArrayList<>();
        routes.add(new Route.Builder()
                .withMethod(Method.GET)
                .withResponseHandler(request -> Response.create(
                        String.format("Hello, you performed a %s request", request.getMethod()))
                )
                .build()
        );

        MockRequestInvoker invoker = MockRequestInvoker.create(new HttpRouterConfiguration.Builder()
                        .withRoutes(routes)
                        .build());

        LOGGER.info(invoker.sendRequest(new Request(Method.GET))
                .map(Response::getValue)
                .orElse("Request could not be matched with route"));

        LOGGER.info(invoker.sendRequest(new Request(Method.POST))
                .map(Response::getValue)
                .orElse("Request could not be matched with route"));
    }
}
