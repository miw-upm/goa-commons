package es.upm.miw.pdf;

import org.junit.jupiter.api.Test;
import org.openpdf.text.pdf.PdfReader;
import org.openpdf.text.pdf.parser.PdfTextExtractor;

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
}
