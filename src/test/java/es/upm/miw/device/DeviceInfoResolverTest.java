package es.upm.miw.device;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DeviceInfoResolverTest {

    @Test
    void testResolveChromWindows() {
        String ua = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 Chrome/120.0.0.0 Safari/537.36";
        DeviceInfo info = DeviceInfoResolver.resolve(ua, "1.2.3.4");
        assertThat(info.getBrowser()).isEqualTo("Chrome");
        assertThat(info.getOperatingSystem()).isEqualTo("Windows");
        assertThat(info.getDeviceType()).isEqualTo("Escritorio");
        assertThat(info.getIpAddress()).isEqualTo("1.2.3.4");
    }

    @Test
    void testResolveFirefoxLinux() {
        String ua = "Mozilla/5.0 (X11; Linux x86_64; rv:109.0) Gecko/20100101 Firefox/115.0";
        DeviceInfo info = DeviceInfoResolver.resolve(ua, "5.6.7.8");
        assertThat(info.getBrowser()).isEqualTo("Firefox");
        assertThat(info.getOperatingSystem()).isEqualTo("Linux");
        assertThat(info.getDeviceType()).isEqualTo("Escritorio");
    }

    @Test
    void testResolveSafariMacOS() {
        String ua = "Mozilla/5.0 (Macintosh; Intel Mac OS X 13_0) AppleWebKit/605.1.15 Version/16.0 Safari/605.1.15";
        DeviceInfo info = DeviceInfoResolver.resolve(ua, "9.10.11.12");
        assertThat(info.getBrowser()).isEqualTo("Safari");
        assertThat(info.getOperatingSystem()).isEqualTo("MacOS");
        assertThat(info.getDeviceType()).isEqualTo("Escritorio");
    }

    @Test
    void testResolveChromeAndroidMobile() {
        String ua = "Mozilla/5.0 (Linux; Android 13; Pixel 7) AppleWebKit/537.36 Chrome/120.0.0.0 Mobile Safari/537.36";
        DeviceInfo info = DeviceInfoResolver.resolve(ua, "13.14.15.16");
        assertThat(info.getBrowser()).isEqualTo("Chrome");
        assertThat(info.getOperatingSystem()).isEqualTo("Android");
        assertThat(info.getDeviceType()).isEqualTo("Móvil");
    }

    @Test
    void testResolveSafariIPhone() {
        String ua = "Mozilla/5.0 (iPhone; CPU iPhone OS 17_0 like Mac OS X) AppleWebKit/605.1.15 Version/17.0 Mobile Safari/604.1";
        DeviceInfo info = DeviceInfoResolver.resolve(ua, "17.18.19.20");
        assertThat(info.getBrowser()).isEqualTo("Safari");
        assertThat(info.getOperatingSystem()).isEqualTo("iOS");
        assertThat(info.getDeviceType()).isEqualTo("Móvil");
    }

    @Test
    void testResolveEdgeWindows() {
        String ua = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 Chrome/120.0.0.0 Safari/537.36 Edg/120.0.0.0";
        DeviceInfo info = DeviceInfoResolver.resolve(ua, "21.22.23.24");
        assertThat(info.getBrowser()).isEqualTo("Edge");
        assertThat(info.getOperatingSystem()).isEqualTo("Windows");
        assertThat(info.getDeviceType()).isEqualTo("Escritorio");
    }

    @Test
    void testResolveNullUserAgent() {
        DeviceInfo info = DeviceInfoResolver.resolve(null, "1.2.3.4");
        assertThat(info.getBrowser()).isEqualTo("Desconocido");
        assertThat(info.getOperatingSystem()).isEqualTo("Desconocido");
        assertThat(info.getDeviceType()).isEqualTo("Desconocido");
        assertThat(info.getIpAddress()).isEqualTo("1.2.3.4");
    }
}
