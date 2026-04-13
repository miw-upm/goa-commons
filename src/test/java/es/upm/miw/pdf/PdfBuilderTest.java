package es.upm.miw.pdf;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PdfBuilderTest {

    @Test
    void shouldGeneratePdfWithAllElements() {
        byte[] pdf = new PdfBuilder("test-complete")
                .header()
                .title("Test Title")
                .section("Section")
                .paragraph("Paragraph text")
                .paragraphBold("Bold text")
                .line()
                .table(
                        new String[]{"Col1", "Col2"},
                        List.<String[]>of(new String[]{"A", "B"})
                )
                .list(List.of("Item 1", "Item 2"))
                .numberedList(List.of("First", "Second"))
                .signatureLine("Signature")
                .footer()
                .build();

        assertNotNull(pdf);
        assertTrue(pdf.length > 0);
        // PDF magic bytes: %PDF
        assertEquals('%', (char) pdf[0]);
        assertEquals('P', (char) pdf[1]);
        assertEquals('D', (char) pdf[2]);
        assertEquals('F', (char) pdf[3]);
    }

    @Test
    void shouldGeneratePdfWithTableWithTotal() {
        byte[] pdf = new PdfBuilder("test-table-total")
                .tableWithTotal(
                        new String[]{"Item", "Price"},
                        List.of(
                                new String[]{"Product A", "100 €"},
                                new String[]{"Product B", "200 €"}
                        ),
                        "TOTAL",
                        "300 €"
                )
                .build();

        assertNotNull(pdf);
        assertTrue(pdf.length > 0);
    }

    @Test
    void shouldGenerateMultiPagePdf() {
        PdfBuilder builder = new PdfBuilder("test-multipage");
        
        for (int i = 0; i < 3; i++) {
            builder.paragraph("Page content ".repeat(100));
            if (i < 2) builder.pageBreak();
        }

        byte[] pdf = builder.build();

        assertNotNull(pdf);
        assertTrue(pdf.length > 0);
    }

    @Test
    void shouldHandleNullValuesInTable() {
        byte[] pdf = new PdfBuilder("test-null-table")
                .table(
                        new String[]{"Col1", "Col2"},
                        List.<String[]>of(new String[]{null, "Value"})
                )
                .build();

        assertNotNull(pdf);
        assertTrue(pdf.length > 0);
    }

    @Test
    void shouldGenerateTwoColumnSignature() {
        byte[] pdf = new PdfBuilder("test-signature")
                .twoColumnSignature("Left", "Right")
                .build();

        assertNotNull(pdf);
        assertTrue(pdf.length > 0);
    }
}
