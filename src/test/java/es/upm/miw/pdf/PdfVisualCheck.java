package es.upm.miw.pdf;

import lombok.extern.log4j.Log4j2;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Ejecutar manualmente para generar PDFs de prueba.
 * NO es un test JUnit, no se ejecuta en CI.
 */
@Log4j2
public class PdfVisualCheck {

    private static final String OUTPUT_DIR = System.getProperty("user.home") + "/Desktop/pdf-tests/";

    public static void main(String[] args) throws IOException {
        log.info("Generando PDFs de prueba en: {}", OUTPUT_DIR);
        generateEngagementLetter();
        generateInvoice();
        generateSimpleDocument();
        log.info("Todos los PDFs generados correctamente.");
    }

    private static void generateEngagementLetter() throws IOException {
        byte[] pdf = new PdfBuilder("engagement-letter-test")
                .header()
                .title("HOJA DE ENCARGO PROFESIONAL")
                .twoColumns(
                        left -> left
                                .section("DATOS DEL CLIENTE")
                                .paragraph("Nombre: Juan García López")
                                .paragraph("DNI: 12345678A")
                                .paragraph("Email: juan.garcia@email.com")
                                .paragraph("Teléfono: +34 600 123 456"),
                        right -> right
                                .section("FECHA")
                                .paragraph("Fecha de creación: 13/04/2026")
                )
                .section("PROCEDIMIENTOS LEGALES")
                .table(
                        new String[]{"Descripción", "Base", "IVA", "Total"},
                        List.of(
                                new String[]{"Consulta inicial", "100,00 €", "21,00 €", "121,00 €"},
                                new String[]{"Redacción contrato", "250,00 €", "52,50 €", "302,50 €"},
                                new String[]{"Gestión administrativa", "150,00 €", "31,50 €", "181,50 €"}
                        )
                )
                .section("FORMAS DE PAGO")
                .list(List.of(
                        "Transferencia bancaria: ES12 1234 5678 9012 3456 7890",
                        "Pago con tarjeta en oficina",
                        "Bizum al 600 123 456"
                ))
                .space()
                .paragraph("Descuento aplicado: 10%")
                .paragraphBold("TOTAL A PAGAR: 544,50 €")
                .space()
                .twoColumnSignature("Firma del cliente", "Firma del abogado")
                .footer()
                .build();
        saveAndOpen(pdf, "engagement-letter.pdf");
    }

    private static void generateInvoice() throws IOException {
        byte[] pdf = new PdfBuilder("invoice-test")
                .header()
                .title("FACTURA Nº 2026-0042")
                .section("DATOS DEL CLIENTE")
                .paragraph("Empresa ABC S.L.")
                .paragraph("NIF: B87654321")
                .paragraph("C/ Comercial 88, 28002 Madrid")
                .section("CONCEPTOS")
                .tableWithTotal(
                        new String[]{"Concepto", "Cantidad", "Precio", "Total"},
                        List.of(
                                new String[]{"Asesoría legal mensual", "1", "500,00 €", "500,00 €"},
                                new String[]{"Redacción de contratos", "3", "150,00 €", "450,00 €"},
                                new String[]{"Consultas telefónicas", "5", "30,00 €", "150,00 €"}
                        ),
                        "TOTAL (IVA incluido)",
                        "1.331,00 €"
                )
                .footer()
                .build();
        saveAndOpen(pdf, "invoice.pdf");
    }

    private static void generateSimpleDocument() throws IOException {
        byte[] pdf = new PdfBuilder("simple-test")
                .header()
                .title("DOCUMENTO DE PRUEBA")
                .paragraph("Este es un párrafo normal con texto de ejemplo.")
                .paragraphBold("Este es un párrafo en negrita.")
                .line()
                .section("Sección con lista")
                .list(List.of("Elemento uno", "Elemento dos", "Elemento tres"))
                .section("Sección con lista numerada")
                .numberedList(List.of("Primer paso", "Segundo paso", "Tercer paso"))
                .signatureLine("Firma")
                .footer()
                .build();
        saveAndOpen(pdf, "simple.pdf");
    }

    private static void saveAndOpen(byte[] pdf, String filename) throws IOException {
        Path dir = Path.of(OUTPUT_DIR);
        Files.createDirectories(dir);

        Path file = dir.resolve(filename);
        Files.write(file, pdf);
        log.info("✓ " + filename);

        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(file.toFile());
        }
    }
}
