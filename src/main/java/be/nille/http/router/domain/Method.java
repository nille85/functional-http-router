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
        Optional<Method> methodOptional = Arrays.asList(Method.values()).stream()
                .filter(method -> method.getName().equals(name))
                .findFirst();
        return methodOptional;
    }
}
