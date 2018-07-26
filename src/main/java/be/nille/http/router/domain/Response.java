package be.nille.http.router.domain;

public class Response {

    private final String value;

    private Response(String value) {
        this.value = value;
    }

    public static Response create(String value){
        return new Response(value);
    }

    public String getValue() {
        return value;
    }
}
