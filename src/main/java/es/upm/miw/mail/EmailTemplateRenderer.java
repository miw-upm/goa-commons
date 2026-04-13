package es.upm.miw.mail;


import es.upm.miw.exception.InternalServerException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class EmailTemplateRenderer {

    private EmailTemplateRenderer() {
    }

    public static String render(String templatePath, Map<String, String> variables) {
        String html = readClasspathFile(templatePath);
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            html = html.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
        return html;
    }

    private static String readClasspathFile(String path) {
        try (InputStream inputStream = EmailTemplateRenderer.class
                .getClassLoader().getResourceAsStream(path)) {
            if (inputStream == null) {
                throw new InternalServerException("Cannot load email template: " + path);
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new InternalServerException("Cannot load email template: " + path);
        }
    }
}
