package es.upm.miw.device;

import jakarta.servlet.http.HttpServletRequest;

public class DeviceInfoResolver {

    private DeviceInfoResolver() {
    }

    public static DeviceInfo resolve(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        return DeviceInfo.builder()
                .ipAddress(resolveIp(request))
                .deviceType(resolveDeviceType(userAgent))
                .operatingSystem(resolveOS(userAgent))
                .browser(resolveBrowser(userAgent))
                .build();
    }

    private static String resolveIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isBlank()) {
            return xForwardedFor.split(",")[0].trim();
        }
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isBlank()) {
            return xRealIp.trim();
        }
        return request.getRemoteAddr();
    }

    private static String resolveBrowser(String ua) {
        if (ua == null) return "Desconocido";
        if (ua.contains("Edg")) return "Edge";
        if (ua.contains("Chrome")) return "Chrome";
        if (ua.contains("Firefox")) return "Firefox";
        if (ua.contains("Safari")) return "Safari";
        return "Otro";
    }

    private static String resolveOS(String ua) {
        if (ua == null) return "Desconocido";
        if (ua.contains("Android")) return "Android";
        if (ua.contains("iPhone") || ua.contains("iPad")) return "iOS";
        if (ua.contains("Windows")) return "Windows";
        if (ua.contains("Mac")) return "MacOS";
        if (ua.contains("Linux")) return "Linux";
        return "Otro";
    }

    private static String resolveDeviceType(String ua) {
        if (ua == null) return "Desconocido";
        if (ua.contains("Mobile") || ua.contains("Android")
                || ua.contains("iPhone")) return "Móvil";
        if (ua.contains("iPad") || ua.contains("Tablet")) return "Tablet";
        return "Escritorio";
    }
}