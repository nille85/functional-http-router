package be.nille.http.router.domain;

import java.util.Arrays;
import java.util.Optional;

public enum Method {
    GET("GET"), POST("POST");

    private final String name;

    Method(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Optional<Method> byName(String name){
        return Arrays.asList(Method.values()).stream()
                .filter(value -> value.equals(name))
                .findFirst();
    }
}
