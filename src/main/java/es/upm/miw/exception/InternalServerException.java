package es.upm.miw.exception;

public class InternalServerException extends RuntimeException {
    private static final String DESCRIPTION = "Internal Server Exception";

    public InternalServerException(String detail) {
        super(DESCRIPTION + ". " + detail);
    }
}
