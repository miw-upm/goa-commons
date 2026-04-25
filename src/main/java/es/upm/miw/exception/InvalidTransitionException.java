package es.upm.miw.exception;

public class InvalidTransitionException extends ApiException{
    private static final String DESCRIPTION = "Bad Gateway Exception";

    protected InvalidTransitionException(String detail) {
        super(DESCRIPTION, detail);
    }

    protected InvalidTransitionException(String detail, Throwable cause) {
        super(DESCRIPTION, detail, cause);
    }
}
