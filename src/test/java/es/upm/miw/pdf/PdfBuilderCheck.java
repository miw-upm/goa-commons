package es.upm.miw.pdf;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openpdf.text.Element;

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
                        new String[]{"TOTAL", "", formatEuro(new BigDecimal("5450")), ""},
                        new String[]{"TOTAL (60%)", "", formatEuro(new BigDecimal("5450")), ""}
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

    @Test
    @DisplayName("Genera una hoja de encargo profesional completa y la guarda en target/pdf-tests")
    void shouldGenerateEngagementSheet() throws Exception {
        byte[] pdfBytes = new PdfBuilder()
                .header()
                .title("HOJA DE ENCARGO PROFESIONAL")
                .space()
                .paragraphBold("En Madrid, a 20 de mayo de 2026", Element.ALIGN_RIGHT)
                .space()
                .paragraph(
                        "D./Dña. Jesus PRUEBA 333333 Bermudez con N.I.F. nº 43020965V, en su nombre y derecho, " +
                                "encarga profesionalmente a la abogada Nuria Ocaña Pérez con número de N.I.F. n.º 46.882.956-D " +
                                "y número de colegiada 135.280 del ICAM con despacho profesional sito en el Paseo de la " +
                                "Castellana, 93 – 2ª planta de Madrid la realización de los siguientes trabajos profesionales."
                )
                .space()
                .section("Procedimiento 1")
                .paragraphBold("1.000,00 € (+ IVA)")
                .list(List.of("tarea 1", "tarea 2", "tarea 3"))
                .space()
                .paragraphBold(
                        "La ejecución de estos trabajos profesionales se efectuará en régimen de arrendamiento " +
                                "de servicios, con arreglo a las normas deontológicas de la Abogacía. En consecuencia, " +
                                "con carácter indicativo y al margen de las incidencias que puedan plantearse incluyendo " +
                                "los honorarios de otros profesionales que deban intervenir (procurador, peritos, costas de " +
                                "contrario, Notarios, Registros, etc.), no incluirán los gastos de desplazamiento o de otra " +
                                "naturaleza, ni suplidos que puedan ocasionarse en la ejecución de los trabajos objeto de este encargo."
                )
                .space()
                .section("Formas de pago")
                .list(List.of("Resto: Al finalizar el proceso."))
                .section("Datos bancarios")
                .list(List.of(
                        "Cuenta: ES09 1465 0100 96 1707148504",
                        "Entidad: ING",
                        "Titular: Nuria Ocaña Pérez"
                ))
                .section("Combinación de vías")
                .paragraph(
                        "Existe la posibilidad de que una fase (por ejemplo, la formación de inventario) se tramite " +
                                "de forma contenciosa, y la posterior liquidación se realice ante notario de común acuerdo. " +
                                "Por ello, se ha optado por desglosar los honorarios por fases y vías, para facilitar la " +
                                "adaptación del presupuesto al desarrollo real del procedimiento."
                )
                .section("Condiciones generales")
                .paragraph(
                        "Los honorarios pactados en la presente hoja de encargo profesional incluyen las consultas " +
                                "relacionadas con el encargo que tengan lugar durante los dos meses posteriores a la resolución " +
                                "o documento que ponga fin al procedimiento. Transcurrido dicho tiempo, las consultas que surjan " +
                                "relacionadas con el encargo y la aplicación de la resolución tendrán un coste de 150,00 € + IVA, " +
                                "que será abonado en el momento de efectuar la consulta."
                )
                .paragraph(
                        "Los honorarios de Letrado objeto de la presente se presupuestan al margen de incidencias y " +
                                "recursos que puedan plantearse y sin incluir los honorarios de otros profesionales que puedan " +
                                "o deban intervenir, como procuradores o peritos. El pago de las tasas y depósitos judiciales " +
                                "que pudieran existir será de cuenta del cliente y no se encuentra incluido en los honorarios descritos."
                )
                .pageBreak()
                .paragraph(
                        "Los honorarios pactados únicamente cubrirán las actuaciones realizadas en la Comunidad de Madrid. " +
                                "De igual modo, cualquier servicio jurídico que no esté incluido en el presente contrato se " +
                                "realizará previo presupuesto y aceptación por ambas partes."
                )
                .paragraph(
                        "La minuta de honorarios definitiva estará sujeta al régimen fiscal de retenciones e IVA " +
                                "procedentes que serán abonados por el cliente."
                )
                .paragraphBold(
                        "Nota: de ser varias las personas que realizan el encargo, dicha obligación será asumida " +
                                "con carácter solidario por todas ellas."
                )
                .paragraph(
                        "En el caso de que existieran desavenencias con relación al asunto encomendado, cualquiera " +
                                "de las partes podrá desistir unilateralmente, previo aviso por escrito a la otra parte, de " +
                                "la continuación en la prestación de los servicios por parte de la Letrada, debiéndose liquidar " +
                                "en ese momento las cantidades adeudadas por el cliente por los trabajos efectivamente realizados " +
                                "por la Letrada."
                )
                .section("Advertencias")
                .numberedList(List.of(
                        "El ejercicio de la acción puede ser infructuoso.",
                        "Los Letrados se encuentran sujetos a las normas sobre prevención de blanqueo de capitales " +
                                "y financiación del terrorismo establecidas en la Ley 10/2010.",
                        "El cliente autoriza a entregar copia de la documentación facilitada a terceros necesarios " +
                                "para la realización del encargo.",
                        "La Letrada podrá delegar tareas en los Abogados colaboradores de su despacho.",
                        "El cliente se compromete a facilitar información veraz y actualizada, así como a comunicar " +
                                "cualquier cambio relevante para el desarrollo del encargo."
                ))
                .section("Seguro de responsabilidad civil")
                .paragraph(
                        "La Letrada dispone de seguro de responsabilidad civil profesional conforme a la normativa " +
                                "vigente del Ilustre Colegio de Abogados de Madrid."
                )
                .section("Comunicaciones")
                .paragraph(
                        "Las partes acuerdan que las comunicaciones relativas al presente encargo podrán realizarse " +
                                "por correo electrónico a las direcciones facilitadas, considerándose válidas a todos los efectos."
                )
                .section("Protección de datos")
                .paragraph(
                        "Los datos personales facilitados serán tratados por Nuria Ocaña Pérez, con NIF 46.882.956-D, " +
                                "con la finalidad de gestionar el presente encargo profesional. La base jurídica del tratamiento " +
                                "es la ejecución del contrato. Los datos se conservarán durante la relación contractual y " +
                                "posteriormente durante los plazos legales aplicables. No se cederán datos a terceros salvo " +
                                "obligación legal o para la ejecución del encargo. Puede ejercer sus derechos de acceso, " +
                                "rectificación, supresión, oposición, limitación y portabilidad en derecho@ocanabogados.es. " +
                                "Más información en nuestra Política de Privacidad."
                )
                .section("Jurisdicción")
                .paragraph(
                        "Las partes se someten expresamente a la legislación española, sometiéndose a la jurisdicción " +
                                "de los Juzgados y Tribunales de Madrid."
                )
                .space()
                .section("Aviso importante")
                .paragraph(
                        "La validez de la presente hoja de encargo profesional es de 30 días naturales a contar desde " +
                                "su emisión. Cualquier modificación sustancial en las actuaciones del procedimiento será tenida " +
                                "en cuenta para una valoración de liquidación final, así como la existencia de nuevas actuaciones " +
                                "o procedimientos judiciales no contemplados en el presente presupuesto será origen de solicitud " +
                                "por parte del despacho de una nueva provisión de fondos."
                )
                .paragraph(
                        "En virtud de lo expuesto y en prueba de conformidad, se firma el presente encargo, " +
                                "en las fechas indicadas."
                )
                .space(3)
                .multiSignatureWithSignatures(
                        List.of(new PdfBuilder.LeftSignature("D./Dña. Jesus PRUEBA 333333 Bermudez")),
                        "Dña. Nuria Ocaña Pérez"
                )
                .footer()
                .build();

        Path output = OUTPUT_DIR.resolve("hoja-de-encargo.pdf");
        Files.write(output, pdfBytes);

        assertTrue(Files.exists(output), "El PDF debería haberse creado");
        assertTrue(Files.size(output) > 0, "El PDF no debería estar vacío");
    }

}
