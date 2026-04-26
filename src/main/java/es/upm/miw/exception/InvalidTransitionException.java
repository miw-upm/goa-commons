package es.upm.miw.exception;

public class InvalidTransitionException extends ApiException {
    private static final String DESCRIPTION = "Invalid Transition Exception";

    public InvalidTransitionException(String detail) {
        super(DESCRIPTION, detail);
    }

    public InvalidTransitionException(String detail, Throwable cause) {
        super(DESCRIPTION, detail, cause);
    }
}
