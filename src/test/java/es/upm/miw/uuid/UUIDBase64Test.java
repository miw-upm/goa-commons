package es.upm.miw.uuid;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UUIDBase64Test {

    @Test
    void testEncodeDecodeBasic() {
        String encoded = UUIDBase64.BASIC.encode();
        assertThat(encoded).isNotNull().isNotBlank();
        UUID decoded = UUIDBase64.BASIC.decode(encoded);
        assertThat(decoded).isNotNull();
        assertThat(UUIDBase64.BASIC.decode(UUIDBase64.BASIC.encode()))
                .isNotEqualTo(UUIDBase64.BASIC.decode(UUIDBase64.BASIC.encode()));
    }

    @Test
    void testEncodeDecodeUrl() {
        String encoded = UUIDBase64.URL.encode();
        UUID decoded = UUIDBase64.URL.decode(encoded);
        assertThat(decoded).isNotNull();
        assertThat(encoded).doesNotContain("+", "/");
    }

    @Test
    void testEncodeDecodeMime() {
        String encoded = UUIDBase64.MIME.encode();
        UUID decoded = UUIDBase64.MIME.decode(encoded);
        assertThat(decoded).isNotNull();
    }

    @Test
    void testEncodedLengthIs22() {
        String encoded = UUIDBase64.BASIC.encode();
        assertThat(encoded).hasSize(22);
    }
}
