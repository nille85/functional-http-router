package be.nille.http.router.exception;

import java.util.Objects;

public final class Success<T> {

    private final T value;

    Success(T value) {
        Objects.requireNonNull(value);
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}
