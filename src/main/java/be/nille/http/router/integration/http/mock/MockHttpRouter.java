package be.nille.http.router.integration.http.mock;

import be.nille.http.router.domain.HttpRouter;
import be.nille.http.router.domain.HttpRouterConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MockHttpRouter implements HttpRouter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MockHttpRouter.class);

    private MockHttpRouter(){};

    public static HttpRouter create(){
        return new MockHttpRouter();
    }

    @Override
    public void start(HttpRouterConfiguration httpRouterConfiguration) {
        LOGGER.info("Started Mock Http Router");
        //httpRouterConfiguration.getRoutes()
    }
}
