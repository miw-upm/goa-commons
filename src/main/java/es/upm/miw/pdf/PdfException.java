package es.upm.miw.pdf;

import es.upm.miw.exception.ApiException;

public class PdfException extends ApiException {

    private static final String DESCRIPTION = "PDF Exception";

    public PdfException(String detail) {
        super(DESCRIPTION, detail);
    }

    public PdfException(String detail, Throwable cause) {
        super(DESCRIPTION, detail, cause);
    }
}
