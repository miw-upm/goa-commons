package es.upm.miw.device;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceInfo {
    private String ipAddress;
    private String deviceType;
    private String operatingSystem;
    private String browser;
}
