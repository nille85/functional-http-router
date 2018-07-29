package be.nille.http.router.exception;

import java.util.Objects;

public class ErrorMessage {

    public enum Severity {
        CRITICAL, MODERATE, MINOR, WARNING, DEBUG, UNKNOWN
    }

    private final String message;

    private final Severity severity;

    private ErrorMessage(
            String message,
            ErrorMessage.Severity severity
            ) {
        this.message = Objects.requireNonNull(message, "message");
        this.severity = Objects.requireNonNull(severity, "severity");
    }

    public static ErrorMessage of(
            String message,
            ErrorMessage.Severity severity
         ) {
        return new ErrorMessage(message, severity);
    }

    public String getMessage() {
        return message;
    }

    public Severity getSeverity() {
        return severity;
    }

    @Override
    public String toString() {
        return "ErrorMessage{" +
                "message='" + message + '\'' +
                ", severity=" + severity +
                '}';
    }
}
