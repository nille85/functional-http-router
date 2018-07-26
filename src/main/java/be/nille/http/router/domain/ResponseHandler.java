package be.nille.http.router.domain;

@FunctionalInterface
public interface ResponseHandler {

    Response handle(Request request);
}
