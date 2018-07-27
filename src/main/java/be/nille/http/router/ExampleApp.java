package be.nille.http.router;

import be.nille.http.router.domain.*;
import be.nille.http.router.integration.http.mock.MockRequestInvoker;
import be.nille.http.router.integration.http.netty.NettyHttpRouter;
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

        NettyHttpRouter.create().start(
                new HttpRouterConfiguration.Builder()
                        .withRoutes(routes)
                        .build()
        );
    }
}
