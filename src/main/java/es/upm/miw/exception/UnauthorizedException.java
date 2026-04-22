package es.upm.miw.exception;

public class UnauthorizedException extends ApiException {
    private static final String DESCRIPTION = "Unauthorized Exception";

    public UnauthorizedException(String detail) {
        super(DESCRIPTION, detail);
    }

    public UnauthorizedException(String detail, Throwable cause) {
        super(DESCRIPTION, detail, cause);
    }
}
