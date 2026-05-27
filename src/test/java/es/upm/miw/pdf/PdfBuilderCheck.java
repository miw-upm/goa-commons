package es.upm.miw.pdf;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PdfBuilderCheck {

    private static final Path OUTPUT_DIR = Paths.get("target", "pdf-tests");
    private static final NumberFormat EUR =
            NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-ES"));

    private static String formatEuro(BigDecimal amount) {
        return EUR.format(amount);
    }

    @BeforeAll
    static void setUp() throws Exception {
        Files.createDirectories(OUTPUT_DIR);
    }

    @Test
    @DisplayName("Genera una factura proforma completa y la guarda en target/pdf-tests")
    void shouldGenerateProformaInvoice() throws Exception {
        byte[] pdfBytes = new PdfBuilder()
                .header()
                .title("FACTURA PROFORMA")
                .space()
                .paragraphBold("Nº 2026 · 33")

                .section("Facturar A, con participación del: 100%")
                .paragraphBold("Jesús Martínez Ruiz")
                .paragraph("N.I.F. 00000001R")
                .paragraph("C/ Sierpes, 101, Sevilla, SEVILLA, 41001")

                .section("Concepto")
                .paragraphBold("FACTURA por ingreso de Provisión de Fondos.")
                .paragraph("Procedimiento de ejecución hipotecaria — 5.000,00 €")
                .list(List.of(
                        "Estudio de antecedentes y documentación",
                        "Asesoramiento jurídico",
                        "Tramitación de la venta de las viviendas de la herencia con la inmobiliaria"
                ))

                .section("Importe de la presente factura")
                .table(
                        new String[]{"Concepto", "Importe"},
                        List.of(
                                new String[]{"Base imponible", formatEuro(new BigDecimal("4400"))},
                                new String[]{"IVA (21 %)",     formatEuro(new BigDecimal("1050"))}
                        ),
                        new String[]{"TOTAL", formatEuro(new BigDecimal("5450"))}
                )
                .paragraphHighlight("PENDIENTE DE INGRESAR: " + formatEuro(new BigDecimal("2400")))
                .paragraphHighlight("Ruego que ingrese en la cuenta bancaria: ES00 1111 2222 3333 4444 5555")
                .signatureLine("Doña Nuria Ocaña Pérez")


                .pageBreak()
                .section("Información detallada")
                .paragraphBold("Descuentos aplicados a la Hoja de Encargo")
                .table(
                        new String[]{"Base original", "Descuentos", "Base final"},
                        List.of(
                                new String[]{"", formatEuro(new BigDecimal("100")), "" },
                                new String[]{"",          formatEuro(new BigDecimal("200")),  ""},
                                new String[]{"",         formatEuro(new BigDecimal("300")),  ""}
                        ),
                                new String[]{formatEuro(new BigDecimal("5000")), formatEuro(new BigDecimal("600")), formatEuro(new BigDecimal("4400")) }

                )
                .paragraphBold("Ingresos asociados a la Hoja de Encargo, facturados aqui")
                .table(
                        new String[]{"Cliente", "Fecha", "Importe", "Tipo de Ingreso"},
                        List.of(
                                new String[]{"Jesús Martínez Ruiz", "miércoles, 27 de mayo de 2026", formatEuro(new BigDecimal("400")),  "TRANSFER"},
                                new String[]{"Jesús Martínez Ruiz", "miércoles, 27 de mayo de 2026", formatEuro(new BigDecimal("5050")), "BIZUM"}
                        ),
                        new String[]{"TOTAL", "", formatEuro(new BigDecimal("5450")), ""}
                )
                .space(2)

                .footer()
                .build();

        Path output = OUTPUT_DIR.resolve("factura-proforma.pdf");
        Files.write(output, pdfBytes);

        assertTrue(Files.exists(output), "El PDF debería haberse creado");
        assertTrue(Files.size(output) > 0, "El PDF no debería estar vacío");
        System.out.println("PDF generado en: " + output.toAbsolutePath());
    }

}
