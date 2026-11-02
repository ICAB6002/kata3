package software.ulpgc.katas.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

public class ResourceTextSource implements TextSource {
    private final String resourceName;

    public ResourceTextSource(String resourceName) {
        this.resourceName = resourceName;
    }

    @Override
    public String read() {
        try (InputStream is = getClass().getResourceAsStream("/" + resourceName)) {
            if (is == null) {
                throw new IllegalArgumentException("Recurso no encontrado: " + resourceName);
            }
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo leer el recurso: " + resourceName, e);
        }
    }
}