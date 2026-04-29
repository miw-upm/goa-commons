package es.upm.miw.base64url;

import org.junit.jupiter.api.Test;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class Base64UrlGeneratorTest {

    @Test
    void testEncodeDecode() {
        UUID uuid = UUID.randomUUID();

        String encoded = Base64UrlGenerator.encode();
        UUID decoded = Base64UrlGenerator.decode(Base64UrlGenerator.encode(uuid));

        assertThat(encoded)
                .isNotBlank()
                .hasSize(22)
                .doesNotContain("+", "/", "=");

        assertThat(decoded).isEqualTo(uuid);
    }

    @Test
    void testEncodeUuid() {
        UUID uuid = UUID.randomUUID();

        String encoded = Base64UrlGenerator.encode(uuid);

        assertThat(encoded)
                .isNotBlank()
                .hasSize(22)
                .doesNotContain("+", "/", "=");

        assertThat(Base64UrlGenerator.decode(encoded)).isEqualTo(uuid);
    }

    @Test
    void testDecodeInvalidValue() {
        assertThatThrownBy(() -> Base64UrlGenerator.decode("invalid"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testToken() {
        String token = Base64UrlGenerator.token();

        assertThat(token)
                .isNotBlank()
                .hasSize(43)
                .doesNotContain("+", "/", "=");
    }
}