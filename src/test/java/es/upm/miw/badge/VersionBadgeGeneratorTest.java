package es.upm.miw.badge;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class VersionBadgeGeneratorTest {
    @Test
    void generatesSvgStartingWithSvgTag() {
        byte[] result = VersionBadgeGenerator.generate("service", "v1.0.0");
        assertThat(new String(result, StandardCharsets.UTF_8)).startsWith("<svg");
    }
}
