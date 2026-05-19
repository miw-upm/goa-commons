package es.upm.miw.pdf;

import org.junit.jupiter.api.Test;
import org.openpdf.text.pdf.PdfReader;
import org.openpdf.text.pdf.parser.PdfTextExtractor;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PdfBuilderTest {

    @Test
    void shouldRenderFooterOnFirstPage() throws Exception {
        PdfBuilder pdfBuilder = new PdfBuilder()
                .header()
                .title("PRUEBA PDF");

        for (int i = 0; i < 160; i++) {
            pdfBuilder.paragraph("Parrafo de prueba para forzar varias paginas y validar el footer.");
        }

        byte[] pdfBytes = pdfBuilder.footer().build();
        assertThat(pdfBytes).isNotEmpty();

        PdfReader reader = new PdfReader(pdfBytes);
        try {
            assertThat(reader.getNumberOfPages()).isGreaterThan(1);
            PdfTextExtractor extractor = new PdfTextExtractor(reader);
            String firstPageText = extractor.getTextFromPage(1);
            assertThat(firstPageText).contains("PRUEBA PDF");
            assertThat(firstPageText).contains("nuria@ocanabogados.es");
            assertThat(firstPageText).contains("+34 644 993 593");
        } finally {
            reader.close();
        }
    }

    @Test
    void shouldRenderMultiSignatureWithImage() throws Exception {
        byte[] signatureImage = this.requiredResourceBytes("images/only-sign.png");

        byte[] pdfBytes = new PdfBuilder()
                .header()
                .title("MULTI SIGNATURE")
                .multiSignatureWithSignatures(
                        List.of(
                                new PdfBuilder.LeftSignature("Firmante 1", "Firma Texto 1", signatureImage),
                                new PdfBuilder.LeftSignature("Firmante 2", "Firma Texto 2")
                        ),
                        "Representante"
                )
                .footer()
                .build();

        assertThat(pdfBytes).isNotEmpty();
        this.writePdf("multi-signature-with-image.pdf", pdfBytes);

        PdfReader reader = new PdfReader(pdfBytes);
        try {
            PdfTextExtractor extractor = new PdfTextExtractor(reader);
            String firstPageText = extractor.getTextFromPage(1);
            assertThat(firstPageText).contains("MULTI SIGNATURE");
            assertThat(firstPageText).contains("Firma Texto 1");
            assertThat(firstPageText).contains("Firmante 1");
            assertThat(firstPageText).contains("Firma Texto 2");
            assertThat(firstPageText).contains("Firmante 2");
            assertThat(firstPageText).contains("Representante");
        } finally {
            reader.close();
        }
    }

    @Test
    void shouldRenderMultiSignatureWithoutRightLabelSplitInTwoColumns() throws Exception {
        byte[] signatureImage = this.requiredResourceBytes("images/only-sign.png");

        byte[] pdfBytes = new PdfBuilder()
                .header()
                .title("MULTI SIGNATURE SPLIT")
                .multiSignatureWithSignatures(
                        List.of(
                                new PdfBuilder.LeftSignature("Firmante 1", "Firma Texto 1", signatureImage),
                                new PdfBuilder.LeftSignature("Firmante 2", null, signatureImage),
                                new PdfBuilder.LeftSignature("Firmante 3", "Firma Texto 3")
                        )
                )
                .footer()
                .build();

        assertThat(pdfBytes).isNotEmpty();
        this.writePdf("multi-signature-split.pdf", pdfBytes);

        PdfReader reader = new PdfReader(pdfBytes);
        try {
            PdfTextExtractor extractor = new PdfTextExtractor(reader);
            String firstPageText = extractor.getTextFromPage(1);
            assertThat(firstPageText).contains("MULTI SIGNATURE SPLIT");
            assertThat(firstPageText).contains("Firmante 1");
            assertThat(firstPageText).contains("Firmante 2");
            assertThat(firstPageText).contains("Firmante 3");
            assertThat(firstPageText).contains("Firma Texto 1");
            assertThat(firstPageText).contains("Firma Texto 3");
            assertThat(firstPageText).doesNotContain("Firma Texto 2");
        } finally {
            reader.close();
        }
    }

    private byte[] requiredResourceBytes(String resourcePath) throws Exception {
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            assertThat(inputStream).as("resource: " + resourcePath).isNotNull();
            return inputStream.readAllBytes();
        }
    }

    private void writePdf(String fileName, byte[] pdfBytes) throws Exception {
        Path outputDir = Path.of("target", "pdf-tests");
        Files.createDirectories(outputDir);
        Files.write(outputDir.resolve(fileName), pdfBytes);
    }
}
