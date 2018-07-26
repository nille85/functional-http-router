package be.nille.http.router.domain;

public class Request {

    private final Method method;

    public Request(Method method) {
        this.method = method;
    }

    public Method getMethod() {
        return method;
    }
}
