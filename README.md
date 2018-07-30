# Http Router 

## Usage
```java
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
```