package es.upm.miw.exception;

public class ForbiddenException extends ApiException {
    private static final String DESCRIPTION = "Forbidden Exception";

    public ForbiddenException(String detail) {
        super(DESCRIPTION, detail);
    }

    public ForbiddenException(String detail, Throwable cause) {
        super(DESCRIPTION, detail, cause);
    }
}
