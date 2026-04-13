package es.upm.miw.email;

import es.upm.miw.exception.InternalServerException;
import es.upm.miw.mail.EmailTemplateRenderer;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmailTemplateRendererTest {

    @Test
    void testRender() {
        String result = EmailTemplateRenderer.render(
                "templates/email/test-template.html",
                Map.of(
                        "NAME", "Jesus",
                        "DATE", "13/04/2026"
                )
        );
        assertThat(result)
                .contains("Jesus")
                .contains("13/04/2026")
                .doesNotContain("{{NAME}}")
                .doesNotContain("{{DATE}}");
    }

    @Test
    void testRenderTemplateNotFound() {
        assertThatThrownBy(() ->
                EmailTemplateRenderer.render("templates/email/not-exists.html", Map.of())
        ).isInstanceOf(InternalServerException.class)
                .hasMessageContaining("Cannot load email template");
    }
}
