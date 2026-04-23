package es.upm.miw.badge;

import java.time.LocalDateTime;

public record ApplicationInfoDto(String version, LocalDateTime timestamp) {
}

