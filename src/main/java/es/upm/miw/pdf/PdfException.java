package es.upm.miw.pdf;

public class PdfException extends RuntimeException {

    private static final String DESCRIPTION = "PDF Exception";

    public PdfException(String detail) {
        super(DESCRIPTION + ": " + detail);
    }
}
