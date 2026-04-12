package es.upm.miw.mail;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EmailTest {

    @Test
    void testBuilder() {
        Email email = Email.builder()
                .to("test@example.com")
                .subject("Asunto")
                .body("Cuerpo del mensaje")
                .build();
        assertThat(email.getTo()).isEqualTo("test@example.com");
        assertThat(email.getSubject()).isEqualTo("Asunto");
        assertThat(email.getBody()).isEqualTo("Cuerpo del mensaje");
    }

    @Test
    void testNoArgsConstructor() {
        Email email = new Email();
        assertThat(email.getTo()).isNull();
        assertThat(email.getSubject()).isNull();
        assertThat(email.getBody()).isNull();
    }
}
