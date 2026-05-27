package es.upm.miw.pdf;

import es.upm.miw.exception.BadGatewayException;
import org.openpdf.text.*;
import org.openpdf.text.Font;
import org.openpdf.text.Image;
import org.openpdf.text.List;
import org.openpdf.text.Rectangle;
import org.openpdf.text.pdf.*;
import org.openpdf.text.pdf.draw.LineSeparator;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

public class PdfBuilder {

    // === Layout ===
    private static final float PAGE_MARGIN_LEFT_RIGHT = 48f;
    private static final float PAGE_MARGIN_TOP = 36f;
    private static final float PAGE_MARGIN_BOTTOM = 64f;
    private static final float FOOTER_TEXT_OFFSET = 36f;
    private static final float FOOTER_SEPARATOR_OFFSET = 24f;

    // === Paleta GOA (alineada con theme.scss) ===
    private static final Color PRIMARY             = new Color(0xBA, 0x79, 0x2E); // dorado
    private static final Color SECONDARY           = new Color(0x4A, 0x5D, 0x3A); // verde oliva
    private static final Color ON_SURFACE          = new Color(0x1F, 0x1D, 0x1A); // texto principal
    private static final Color ON_SURFACE_VARIANT  = new Color(0x6B, 0x65, 0x5E); // texto secundario
    private static final Color OUTLINE             = new Color(0x86, 0x80, 0x77); // bordes medios
    private static final Color OUTLINE_VARIANT     = new Color(0xE3, 0xDF, 0xD8); // bordes suaves
    private static final Color SURFACE_LOWEST      = new Color(0xFE, 0xFC, 0xF7); // casi blanco crema
    private static final Color SURFACE_LOW         = new Color(0xF5, 0xF1, 0xE9); // crema
    private static final Color PRIMARY_CONTAINER   = new Color(0xF5, 0xE6, 0xD3); // dorado claro
    private static final Color SIGNATURE_BLUE      = new Color(0, 51, 153);

    // === Fuentes ===
    private static final BaseFont BASE_FONT      = requiredBaseFont(BaseFont.HELVETICA);
    private static final BaseFont BASE_FONT_BOLD = requiredBaseFont(BaseFont.HELVETICA_BOLD);

    private static final Font FONT_NORMAL      = new Font(BASE_FONT,      8.5f, Font.NORMAL, ON_SURFACE);
    private static final Font FONT_BOLD        = new Font(BASE_FONT_BOLD, 8.5f, Font.BOLD,   ON_SURFACE);
    private static final Font FONT_MUTED       = new Font(BASE_FONT,       8f, Font.NORMAL, ON_SURFACE_VARIANT);
    private static final Font FONT_TITLE       = new Font(BASE_FONT_BOLD, 11f, Font.BOLD,   ON_SURFACE);
    private static final Font FONT_KICKER      = new Font(BASE_FONT_BOLD,  9f, Font.BOLD, PRIMARY);
    private static final Font FONT_SECTION     = new Font(BASE_FONT_BOLD, 10f, Font.BOLD,   ON_SURFACE);
    private static final Font FONT_SMALL       = new Font(BASE_FONT,       7f, Font.NORMAL, ON_SURFACE_VARIANT);
    private static final Font FONT_HEADER_BOLD = new Font(BASE_FONT_BOLD, 10f, Font.BOLD,   ON_SURFACE);
    private static final Font FONT_HEADER      = new Font(BASE_FONT,       8f, Font.NORMAL, ON_SURFACE_VARIANT);
    private static final Font FONT_TABLE_HEAD  = new Font(BASE_FONT_BOLD,  8f, Font.BOLD, ON_SURFACE_VARIANT);
    private static final Font FONT_TABLE_FOOT = new Font(BASE_FONT_BOLD, 8f, Font.BOLD, ON_SURFACE);
    private static final Font FONT_TABLE_CELL  = new Font(BASE_FONT,       7f, Font.NORMAL, ON_SURFACE);
    private static final Font FONT_TABLE_CELL_BOLD = new Font(BASE_FONT_BOLD, 7f, Font.BOLD, ON_SURFACE);
    private static final Font FONT_TABLE_TOTAL = new Font(BASE_FONT_BOLD, 8.5f, Font.BOLD, PRIMARY);
    private static final Font FONT_SIGNATURE   = new Font(BASE_FONT_BOLD,  7f, Font.BOLD,  SIGNATURE_BLUE);

    // === Empresa ===
    private static final String COMPANY_NAME = "Ocaña Abogados";
    private static final String COMPANY_OWNER = "Nuria Ocaña Pérez";
    private static final String COMPANY_NIF = "46882956D";
    private static final String COMPANY_ADDRESS = "Paseo de la Castellana, 93-2ª, 28046 Madrid";
    private static final String COMPANY_PHONE = "+34 644 993 593";
    private static final String COMPANY_EMAIL = "nuria@ocanabogados.es";
    private static final String COMPANY_WEB = "www.ocanabogados.es";
    private static final String LOGO_PATH = "images/oa.png";
    private static final String STAMP_PATH = "images/stamp.png";

    private final Document document;
    private final ByteArrayOutputStream outputStream;
    private final PdfWriter writer;

    public PdfBuilder() {
        this.outputStream = new ByteArrayOutputStream();
        this.document = new Document(
                PageSize.A4,
                PAGE_MARGIN_LEFT_RIGHT,
                PAGE_MARGIN_LEFT_RIGHT,
                PAGE_MARGIN_TOP,
                PAGE_MARGIN_BOTTOM
        );
        try {
            this.writer = PdfWriter.getInstance(document, outputStream);
            this.writer.setPageEvent(new PageFooterEvent());
            document.open();
        } catch (Exception e) {
            throw this.onError("creating PDF", e);
        }
    }

    private static BaseFont requiredBaseFont(String fontName) {
        try {
            return BaseFont.createFont(fontName, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
        } catch (DocumentException | IOException e) {
            throw new IllegalStateException("PDF font initialization failed: " + fontName, e);
        }
    }

    // =========================================================================
    // HEADER / FOOTER
    // =========================================================================

    public PdfBuilder header() {
        return this.add("header", () -> {
            PdfPTable table = this.createTable(2, 50, 50);
            table.addCell(this.createLogoCell());
            table.addCell(this.createInfoCell());
            table.setSpacingAfter(14);
            document.add(table);
        }).softLine().space();
    }

    public PdfBuilder footer() {
        this.space().softLine();
        return this.add("footer", () -> {
            Paragraph info = new Paragraph();
            info.setAlignment(Element.ALIGN_CENTER);
            info.setSpacingBefore(8);
            info.add(new Chunk(COMPANY_NAME + "\n", FONT_BOLD));
            info.add(new Chunk(COMPANY_ADDRESS + "\n", FONT_SMALL));
            info.add(new Chunk("Tel: " + COMPANY_PHONE + " | " + COMPANY_EMAIL + " | " + COMPANY_WEB, FONT_SMALL));
            document.add(info);
        });
    }

    // =========================================================================
    // TÍTULOS Y SECCIONES
    // =========================================================================

    public PdfBuilder title(String text) {
        return this.add("title", () -> {
            Paragraph p = new Paragraph();
            p.setAlignment(Element.ALIGN_LEFT);
            Chunk titleChunk = new Chunk(text, FONT_TITLE);
            titleChunk.setCharacterSpacing(-0.3f); // tracking ajustado tipo display
            p.add(titleChunk);
            p.setSpacingBefore(4);
            p.setSpacingAfter(2);
            document.add(p);
            // Filete acento dorado bajo el título
            this.accentRule(PRIMARY, 120f, 1f);
        });
    }

    public PdfBuilder section(String text) {
        if (writer.getVerticalPosition(true) < document.bottomMargin() + 60) {
            document.newPage();
        }
        return this.add("section", () -> {
            Paragraph spacer = new Paragraph(" ");
            spacer.setLeading(10);
            document.add(spacer);

            // "Kicker" en mayúsculas con tracking, color primario
            Paragraph kicker = new Paragraph();
            Chunk c = new Chunk(text.toUpperCase(), FONT_KICKER);
            c.setCharacterSpacing(1.2f);
            kicker.add(c);
            kicker.setSpacingAfter(4);
            document.add(kicker);
        });
    }

    /** Línea fina horizontal en color outline-variant (separador visual suave). */
    public PdfBuilder line() {
        PdfContentByte cb = writer.getDirectContent();
        float y = writer.getVerticalPosition(true) - 5;
        cb.saveState();
        cb.setLineWidth(0.6f);
        cb.setColorStroke(OUTLINE_VARIANT);
        cb.moveTo(document.leftMargin(), y);
        cb.lineTo(document.right(), y);
        cb.stroke();
        cb.restoreState();
        return this;
    }

    /** Filete ultra-fino en outline-variant. */
    public PdfBuilder softLine() {
        PdfContentByte cb = writer.getDirectContent();
        float y = writer.getVerticalPosition(true) - 4;
        cb.saveState();
        cb.setLineWidth(0.4f);
        cb.setColorStroke(OUTLINE_VARIANT);
        cb.moveTo(document.leftMargin(), y);
        cb.lineTo(document.right(), y);
        cb.stroke();
        cb.restoreState();
        return this;
    }

    /** Pequeña barra de color (acento), normalmente bajo títulos. */
    private void accentRule(Color color, float width, float thickness) {
        PdfContentByte cb = writer.getDirectContent();
        float y = writer.getVerticalPosition(true) - 6;
        cb.saveState();
        cb.setLineWidth(thickness);
        cb.setColorStroke(color);
        cb.moveTo(document.leftMargin(), y);
        cb.lineTo(document.leftMargin() + width, y);
        cb.stroke();
        cb.restoreState();
    }

    public PdfBuilder space() {
        return this.space(1);
    }

    public PdfBuilder space(int times) {
        return this.add("space", () -> {
            for (int i = 0; i < times; i++) {
                Paragraph p = new Paragraph(" ");
                p.setLeading(8);
                document.add(p);
            }
        });
    }

    public PdfBuilder pageBreak() {
        document.newPage();
        return this;
    }

    // =========================================================================
    // PÁRRAFOS
    // =========================================================================

    public PdfBuilder paragraph(String text) {
        return this.add("paragraph", () -> {
            Paragraph p = new Paragraph(text, FONT_NORMAL);
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            p.setLeading(14);
            p.setSpacingAfter(3);
            document.add(p);
        });
    }

    public PdfBuilder paragraphBold(String text) {
        return this.paragraphBold(text, Element.ALIGN_JUSTIFIED);
    }

    public PdfBuilder paragraphBold(String text, int alignment) {
        return this.add("bold paragraph", () -> {
            Paragraph p = new Paragraph(text, FONT_BOLD);
            p.setAlignment(alignment);
            p.setLeading(14);
            p.setSpacingAfter(3);
            document.add(p);
        });
    }

    public PdfBuilder paragraphs(String text) {
        for (String block : text.split("\n\n")) {
            block = block.trim();
            if (!block.isEmpty()) {
                this.paragraph(block);
                this.space();
            }
        }
        return this;
    }

    public PdfBuilder labelValue(String label, String value) {
        return this.add("label-value", () -> {
            Paragraph p = new Paragraph();
            p.setLeading(14);
            p.add(new Chunk(label + "  ", FONT_MUTED));
            p.add(new Chunk(value != null ? value : "—", FONT_BOLD));
            document.add(p);
        });
    }

    // =========================================================================
    // LISTAS
    // =========================================================================

    public PdfBuilder list(java.util.List<String> items) {
        return this.add("list", () -> {
            List list = new List(List.UNORDERED);
            list.setListSymbol(new Chunk("•  ", new Font(BASE_FONT_BOLD, 10f, Font.BOLD, PRIMARY)));
            list.setIndentationLeft(15);
            items.forEach(item -> {
                ListItem li = new ListItem(item + ".", FONT_NORMAL);
                li.setLeading(14);
                list.add(li);
            });
            document.add(list);
        });
    }

    public PdfBuilder numberedList(java.util.List<String> items) {
        return this.add("numbered list", () -> {
            List list = new List(List.ORDERED);
            list.setIndentationLeft(15);
            items.forEach(item -> {
                ListItem li = new ListItem(item, FONT_NORMAL);
                li.setLeading(14);
                list.add(li);
            });
            document.add(list);
        });
    }

    // =========================================================================
    // COLUMNAS
    // =========================================================================

    public PdfBuilder twoColumns(Consumer<ColumnBuilder> leftContent, Consumer<ColumnBuilder> rightContent) {
        return this.add("two columns", () -> {
            PdfPTable table = this.createTable(2);
            table.addCell(this.createColumnCell(leftContent, 0, 10));
            table.addCell(this.createColumnCell(rightContent, 10, 0));
            document.add(table);
        });
    }

    // =========================================================================
    // TABLAS
    // =========================================================================

    public PdfBuilder table(String[] headers, java.util.List<String[]> rows, String[] footer) {
        return this.add("table", () -> {
            this.validateTableInput(headers, rows);
            if (footer != null && footer.length != headers.length) {
                throw new IllegalArgumentException("table footer size must match headers size");
            }
            PdfPTable table = this.createDataTable(headers.length);
            this.addTableHeaders(table, headers);
            this.addTableRows(table, rows, footer != null);
            if (footer != null) {
                this.addTableFooter(table, footer);
            }
            document.add(table);
        });
    }

    public PdfBuilder table(String[] headers, float[] widths, java.util.List<String[]> rows, String[] footer) {
        return this.add("table", () -> {
            this.validateTableInput(headers, rows);
            this.validateColumnWidths(headers, widths);
            if (footer != null && footer.length != headers.length) {
                throw new IllegalArgumentException("table footer size must match headers size");
            }
            PdfPTable table = new PdfPTable(widths);
            table.setWidthPercentage(100);
            table.setSpacingBefore(4);
            table.setSpacingAfter(8);
            this.addTableHeaders(table, headers);
            this.addTableRows(table, rows, footer != null);
            if (footer != null) {
                this.addTableFooter(table, footer);
            }
            document.add(table);
        });
    }

    public PdfBuilder table(String[] headers, java.util.List<String[]> rows) {
        return this.table(headers, rows, null);
    }

    public PdfBuilder table(String[] headers, float[] widths, java.util.List<String[]> rows) {
        return this.table(headers, widths, rows, null);
    }

    public PdfBuilder tableWithTotal(String[] headers, java.util.List<String[]> rows, String totalLabel, String totalValue) {
        return this.add("table with total", () -> {
            this.validateTableInput(headers, rows);
            if (headers.length < 2) {
                throw new IllegalArgumentException("tableWithTotal requires at least 2 header columns");
            }
            PdfPTable table = this.createDataTable(headers.length);
            this.addTableHeaders(table, headers);
            this.addTableRows(table, rows, true);

            // Fila de total: fondo primary-container suave, sin borde superior fuerte
            PdfPCell labelCell = new PdfPCell(new Phrase(totalLabel, FONT_TABLE_TOTAL));
            labelCell.setColspan(headers.length - 1);
            labelCell.setPadding(10);
            labelCell.setBorder(Rectangle.NO_BORDER);
            labelCell.setBorderColorTop(PRIMARY);
            labelCell.setBorderWidthTop(1.2f);
            labelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            labelCell.setBackgroundColor(PRIMARY_CONTAINER);
            table.addCell(labelCell);

            PdfPCell valueCell = new PdfPCell(new Phrase(totalValue, FONT_TABLE_TOTAL));
            valueCell.setPadding(10);
            valueCell.setBorder(Rectangle.NO_BORDER);
            valueCell.setBorderColorTop(PRIMARY);
            valueCell.setBorderWidthTop(1.2f);
            valueCell.setBackgroundColor(PRIMARY_CONTAINER);
            table.addCell(valueCell);

            document.add(table);
        });
    }

    // =========================================================================
    // FIRMAS
    // =========================================================================

    public PdfBuilder signatureLine(String label) {
        return this.add("signature", () -> {
            PdfPTable wrapper = new PdfPTable(1);
            wrapper.setWidthPercentage(100);
            wrapper.setKeepTogether(true);
            wrapper.setSpacingBefore(8);

            PdfPCell cell = this.noBorderCell();

            // Sello
            try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(STAMP_PATH)) {
                if (is != null) {
                    Image stamp = Image.getInstance(is.readAllBytes());
                    stamp.scaleToFit(80, 80);
                    stamp.setAlignment(Element.ALIGN_LEFT);
                    cell.addElement(stamp);
                }
            } catch (Exception e) {
                throw this.onError("loading stamp", e);
            }

            // Línea fina
            Paragraph rule = new Paragraph();
            rule.add(new Chunk(new LineSeparator(0.6f, 100f, OUTLINE, Element.ALIGN_LEFT, -2f)));
            cell.addElement(rule);

            // Nombre
            Paragraph p = new Paragraph(label, FONT_SMALL);
            p.setSpacingBefore(2);
            cell.addElement(p);

            wrapper.addCell(cell);
            document.add(wrapper);
        });
    }

    public PdfBuilder multiSignature(java.util.List<String> leftLabels, String rightLabel) {
        java.util.List<LeftSignature> signatures = leftLabels.stream()
                .map(LeftSignature::new)
                .toList();
        return this.multiSignatureWithSignatures(signatures, rightLabel);
    }

    public PdfBuilder multiSignatureWithSignatures(java.util.List<LeftSignature> leftSignatures, String rightLabel) {
        return this.add("multi signature", () -> {
            PdfPTable table = this.createTable(2);

            PdfPCell leftCell = this.noBorderCell();
            for (LeftSignature ls : leftSignatures) {
                leftCell.addElement(this.createSignatureParagraph(ls, Element.ALIGN_LEFT));
            }
            table.addCell(leftCell);

            PdfPCell rightCell = this.noBorderCell();
            rightCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            addStampToCell(rightCell);
            Paragraph right = new Paragraph();
            right.setAlignment(Element.ALIGN_RIGHT);
            right.add(thinRuleChunk(120f));
            right.add(new Chunk("\n", FONT_SMALL));
            right.add(new Chunk(rightLabel, FONT_SMALL));
            rightCell.addElement(right);
            table.addCell(rightCell);

            document.add(table);
        });
    }

    public PdfBuilder multiSignatureWithSignatures(java.util.List<LeftSignature> signatures) {
        return this.add("multi signature split", () -> {
            PdfPTable table = this.createTable(2);
            PdfPCell leftCell = this.noBorderCell();
            PdfPCell rightCell = this.noBorderCell();
            rightCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

            for (int i = 0; i < signatures.size(); i++) {
                LeftSignature signature = signatures.get(i);
                if (i % 2 == 0) {
                    leftCell.addElement(this.createSignatureParagraph(signature, Element.ALIGN_LEFT));
                } else {
                    rightCell.addElement(this.createSignatureParagraph(signature, Element.ALIGN_RIGHT));
                }
            }

            table.addCell(leftCell);
            table.addCell(rightCell);
            document.add(table);
        });
    }

    public PdfBuilder twoColumnSignature(String leftLabel, String rightLabel) {
        return this.add("signature lines", () -> {
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);
            PdfPTable table = this.createTable(2);
            table.addCell(this.createSignatureCell(leftLabel, Element.ALIGN_LEFT));
            table.addCell(this.createSignatureCell(rightLabel, Element.ALIGN_RIGHT));
            document.add(table);
        });
    }

    public PdfBuilder image(byte[] imageBytes, float width) {
        try {
            Image img = Image.getInstance(imageBytes);
            img.scaleToFit(width, 1000);
            img.setAlignment(Element.ALIGN_CENTER);
            document.add(img);
        } catch (IOException | DocumentException e) {
            throw this.onError("adding image", e);
        }
        return this;
    }

    public byte[] build() {
        document.close();
        return outputStream.toByteArray();
    }

    // =========================================================================
    // INTERNAL HELPERS
    // =========================================================================

    private PdfBuilder add(String action, DocumentAction runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            throw this.onError(action, e);
        }
        return this;
    }

    private PdfPTable createTable(int columns, float... widths) {
        try {
            PdfPTable table = widths.length > 0 ? new PdfPTable(widths) : new PdfPTable(columns);
            table.setWidthPercentage(100);
            return table;
        } catch (DocumentException e) {
            throw this.onError("creating table", e);
        }
    }

    /**
     * Tabla "moderna": sin bordes verticales, sólo separadores horizontales finos.
     * Padding generoso y filas con ritmo visual claro.
     */
    private PdfPTable createDataTable(int columns) {
        PdfPTable table = new PdfPTable(columns);
        table.setWidthPercentage(100);
        table.setSpacingBefore(4);
        table.setSpacingAfter(8);
        return table;
    }

    private void addTableHeaders(PdfPTable table, String[] headers) {
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h.toUpperCase(), FONT_TABLE_HEAD));
            cell.setPaddingTop(8);
            cell.setPaddingBottom(8);
            cell.setPaddingLeft(10);
            cell.setPaddingRight(10);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setBorderColorBottom(OUTLINE);
            cell.setBorderWidthBottom(0.8f);
            cell.setBackgroundColor(Color.WHITE);
            table.addCell(cell);
        }
    }

    private void addTableRows(PdfPTable table, java.util.List<String[]> rows, boolean hasFooter) {
        for (int r = 0; r < rows.size(); r++) {
            String[] row = rows.get(r);
            boolean lastRow = (r == rows.size() - 1);
            boolean noBottomBorder = lastRow && !hasFooter;
            Color bg = (r % 2 == 1) ? SURFACE_LOW : Color.WHITE;
            for (String value : row) {
                PdfPCell cell = new PdfPCell(new Phrase(value != null ? value : "", FONT_TABLE_CELL));
                cell.setPaddingTop(5);
                cell.setPaddingBottom(5);
                cell.setPaddingLeft(10);
                cell.setPaddingRight(10);
                cell.setBackgroundColor(bg);
                cell.setBorder(noBottomBorder ? Rectangle.NO_BORDER : Rectangle.BOTTOM);
                cell.setBorderColorBottom(OUTLINE_VARIANT);
                cell.setBorderWidthBottom(0.5f);
                table.addCell(cell);
            }
        }
    }

    private void addTableFooter(PdfPTable table, String[] footer) {
        for (String value : footer) {
            PdfPCell cell = new PdfPCell(new Phrase(value != null ? value : "", FONT_TABLE_FOOT));
            cell.setPaddingTop(8);
            cell.setPaddingBottom(8);
            cell.setPaddingLeft(10);
            cell.setPaddingRight(10);
            cell.setBackgroundColor(SURFACE_LOW);
            cell.setBorder(Rectangle.TOP);
            cell.setBorderColorTop(OUTLINE);
            cell.setBorderWidthTop(0.6f);
            table.addCell(cell);
        }
    }

    private void validateTableInput(String[] headers, java.util.List<String[]> rows) {
        if (headers == null || headers.length == 0) {
            throw new IllegalArgumentException("table headers must not be null or empty");
        }
        if (rows == null) {
            throw new IllegalArgumentException("table rows must not be null");
        }
        for (int i = 0; i < rows.size(); i++) {
            String[] row = rows.get(i);
            if (row == null) {
                throw new IllegalArgumentException("table row " + i + " must not be null");
            }
            if (row.length != headers.length) {
                throw new IllegalArgumentException("table row " + i + " has " + row.length
                        + " columns but expected " + headers.length);
            }
        }
    }

    private void validateColumnWidths(String[] headers, float[] widths) {
        if (widths == null || widths.length == 0) {
            throw new IllegalArgumentException("table widths must not be null or empty");
        }
        if (widths.length != headers.length) {
            throw new IllegalArgumentException("table widths size (" + widths.length
                    + ") must match headers size (" + headers.length + ")");
        }
    }

    private PdfPCell noBorderCell() {
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(0);
        return cell;
    }

    private PdfPCell createLogoCell() {
        PdfPCell cell = this.noBorderCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(LOGO_PATH)) {
            if (is != null) {
                Image logo = Image.getInstance(is.readAllBytes());
                logo.scaleToFit(60, 60); // 66% del tamaño anterior (90 -> 60)

                PdfPTable inner = new PdfPTable(new float[]{35, 65});
                inner.setWidthPercentage(100);

                PdfPCell logoCell = this.noBorderCell();
                logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                logoCell.addElement(logo);
                inner.addCell(logoCell);

                PdfPCell nameCell = this.noBorderCell();
                nameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                Paragraph names = new Paragraph();
                names.setLeading(12);
                names.add(new Chunk(COMPANY_NAME + "\n", FONT_HEADER_BOLD));
                names.add(new Chunk(COMPANY_OWNER, FONT_HEADER));
                nameCell.addElement(names);
                inner.addCell(nameCell);

                cell.addElement(inner);
            }
        } catch (Exception e) {
            throw this.onError("loading logo", e);
        }
        return cell;
    }

    private PdfPCell createInfoCell() {
        PdfPCell cell = this.noBorderCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        Paragraph info = new Paragraph();
        info.setAlignment(Element.ALIGN_RIGHT);
        info.setLeading(13);
        info.add(new Chunk("NIF: " + COMPANY_NIF + "\n", FONT_HEADER));
        info.add(new Chunk(COMPANY_ADDRESS + "\n", FONT_HEADER));
        info.add(new Chunk("Tel: " + COMPANY_PHONE + "\n", FONT_HEADER));
        info.add(new Chunk(COMPANY_EMAIL + " | " + COMPANY_WEB, FONT_HEADER));
        cell.addElement(info);
        return cell;
    }

    private PdfPCell createColumnCell(Consumer<ColumnBuilder> content, int paddingLeft, int paddingRight) {
        PdfPCell cell = this.noBorderCell();
        cell.setPaddingLeft(paddingLeft);
        cell.setPaddingRight(paddingRight);
        content.accept(new ColumnBuilder(cell));
        return cell;
    }

    private PdfPCell createSignatureCell(String label, int alignment) {
        PdfPCell cell = this.noBorderCell();
        Paragraph p = new Paragraph();
        p.setAlignment(alignment);
        p.add(thinRuleChunk(140f));
        p.add(new Chunk("\n", FONT_SMALL));
        p.add(new Chunk(label, FONT_SMALL));
        cell.addElement(p);
        return cell;
    }

    private Paragraph createSignatureParagraph(LeftSignature signature, int alignment) {
        Paragraph p = new Paragraph();
        p.setAlignment(alignment);
        p.add(new Chunk("\n\n"));
        if (signature.signatureImage() != null && signature.signatureImage().length > 0) {
            try {
                Image signatureImage = Image.getInstance(signature.signatureImage());
                signatureImage.scaleToFit(120, 50);
                signatureImage.setAlignment(alignment);
                p.add(new Chunk(signatureImage, 0, 0, false));
                p.add(new Chunk("\n"));
            } catch (Exception e) {
                throw this.onError("loading signature image", e);
            }
        }
        if (signature.signature() != null && !signature.signature().isBlank()) {
            p.add(new Chunk(signature.signature() + "\n", FONT_SIGNATURE));
        }
        p.add(thinRuleChunk(140f));
        p.add(new Chunk("\n", FONT_SMALL));
        p.add(new Chunk(signature.label(), FONT_SMALL));
        return p;
    }

    /**
     * Devuelve un Chunk que dibuja una línea fina del color outline,
     * en lugar de los underscores "_______".
     */
    private Chunk thinRuleChunk(float width) {
        LineSeparator sep = new LineSeparator(0.6f, width / 100f * 100f, OUTLINE, Element.ALIGN_LEFT, -2f);
        // forzamos ancho aproximado mediante la línea de texto
        return new Chunk(sep);
    }

    /** Dibuja una línea horizontal de ancho fijo en color outline. */
    private void thinRule(float width) {
        try {
            Paragraph p = new Paragraph();
            LineSeparator sep = new LineSeparator(0.6f, 100f, OUTLINE, Element.ALIGN_LEFT, -2f);
            p.add(new Chunk(sep));
            document.add(p);
        } catch (DocumentException e) {
            throw this.onError("drawing rule", e);
        }
    }

    private void addStamp() {
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(STAMP_PATH)) {
            if (is != null) {
                Image stamp = Image.getInstance(is.readAllBytes());
                stamp.scaleToFit(80, 80);
                stamp.setAlignment(Element.ALIGN_LEFT);
                document.add(stamp);
            }
        } catch (Exception e) {
            throw this.onError("loading stamp", e);
        }
    }

    private void addStampToCell(PdfPCell cell) {
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(STAMP_PATH)) {
            if (is != null) {
                Image stamp = Image.getInstance(is.readAllBytes());
                stamp.scaleToFit(80, 80);
                stamp.setAlignment(Element.ALIGN_RIGHT);
                cell.addElement(stamp);
            }
        } catch (Exception e) {
            throw this.onError("loading stamp", e);
        }
    }

    private BadGatewayException onError(String action, Exception e) {
        return new BadGatewayException("Error " + action + ": " + e.getMessage());
    }

    @FunctionalInterface
    private interface DocumentAction {
        void run() throws DocumentException;
    }

    public record LeftSignature(String label, String signature, byte[] signatureImage) {
        public LeftSignature(String label) {
            this(label, null, null);
        }

        public LeftSignature(String label, String signature) {
            this(label, signature, null);
        }
    }

    // =========================================================================
    // ColumnBuilder
    // =========================================================================
    public static class ColumnBuilder {
        private final PdfPCell cell;

        ColumnBuilder(PdfPCell cell) {
            this.cell = cell;
        }

        public ColumnBuilder paragraph(String text) {
            Paragraph p = new Paragraph(text, FONT_NORMAL);
            p.setLeading(14);
            cell.addElement(p);
            return this;
        }

        public ColumnBuilder paragraphBold(String text) {
            return this.paragraphBold(text, Element.ALIGN_LEFT);
        }

        public ColumnBuilder paragraphBold(String text, int alignment) {
            Paragraph p = new Paragraph(text, FONT_BOLD);
            p.setAlignment(alignment);
            p.setLeading(14);
            cell.addElement(p);
            return this;
        }

        public ColumnBuilder section(String text) {
            Paragraph p = new Paragraph();
            Chunk c = new Chunk(text.toUpperCase(), FONT_KICKER);
            c.setCharacterSpacing(1.2f);
            p.add(c);
            p.setSpacingAfter(4);
            cell.addElement(p);
            return this;
        }

        public ColumnBuilder space() {
            cell.addElement(Chunk.NEWLINE);
            return this;
        }

        public ColumnBuilder list(java.util.List<String> items) {
            List list = new List(List.UNORDERED);
            list.setListSymbol(new Chunk("•  ", new Font(BASE_FONT_BOLD, 10f, Font.BOLD, PRIMARY)));
            items.forEach(item -> {
                ListItem li = new ListItem(item + ".", FONT_NORMAL);
                li.setLeading(14);
                list.add(li);
            });
            cell.addElement(list);
            return this;
        }

        public ColumnBuilder labelValue(String label, String value) {
            Paragraph p = new Paragraph();
            p.setLeading(14);
            p.add(new Chunk(label + "  ", FONT_MUTED));
            p.add(new Chunk(value != null ? value : "—", FONT_BOLD));
            cell.addElement(p);
            return this;
        }
    }

    // =========================================================================
    // PageFooterEvent
    // =========================================================================
    private static class PageFooterEvent extends PdfPageEventHelper {

        private PdfTemplate totalPages;

        @Override
        public void onOpenDocument(PdfWriter writer, Document document) {
            totalPages = writer.getDirectContent().createTemplate(30, 12);
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte cb = writer.getDirectContent();
            float footerTextY = document.bottomMargin() - FOOTER_TEXT_OFFSET;
            float footerSeparatorY = document.bottomMargin() - FOOTER_SEPARATOR_OFFSET;

            // Línea ultra-fina con color suave
            cb.saveState();
            cb.setLineWidth(0.4f);
            cb.setColorStroke(OUTLINE_VARIANT);
            cb.moveTo(document.leftMargin(), footerSeparatorY);
            cb.lineTo(document.right(), footerSeparatorY);
            cb.stroke();
            cb.restoreState();

            // Contacto centrado
            Phrase footer = new Phrase(COMPANY_EMAIL + "  ·  " + COMPANY_PHONE, FONT_SMALL);
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, footer,
                    (document.right() + document.leftMargin()) / 2, footerTextY, 0);

            // Número de página a la derecha
            String pageText = "Página " + writer.getPageNumber() + " de ";
            float pageTextWidth = BASE_FONT.getWidthPoint(pageText, 8);
            cb.beginText();
            cb.setFontAndSize(BASE_FONT, 8);
            cb.setColorFill(ON_SURFACE_VARIANT);
            cb.setTextMatrix(document.right() - pageTextWidth - 20, footerTextY);
            cb.showText(pageText);
            cb.endText();
            cb.addTemplate(totalPages, document.right() - 20, footerTextY);
        }

        @Override
        public void onCloseDocument(PdfWriter writer, Document document) {
            int totalPageCount = Math.max(1, writer.getPageNumber() - 1);
            totalPages.beginText();
            totalPages.setFontAndSize(BASE_FONT, 8);
            totalPages.setColorFill(ON_SURFACE_VARIANT);
            totalPages.showText(String.valueOf(totalPageCount));
            totalPages.endText();
        }
    }
}
