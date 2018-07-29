package be.nille.http.router.exception;

import java.util.Objects;

public class Failure {

    private final ErrorMessage errorMessage;

    Failure(ErrorMessage errorMessage) {
        Objects.requireNonNull(errorMessage);
        this.errorMessage = errorMessage;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }
}
