package software.ulpgc.katas.io;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class HttpTextSource implements TextSource {
    private final URI uri;

    public HttpTextSource(URI uri) {
        this.uri = uri;
    }

    @Override
    public String read() {
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (response.statusCode() != 200) {
                throw new IllegalStateException("La API respondió " + response.statusCode() + " para " + uri);
            }
            return response.body();
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo conectar con " + uri, e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Lectura interrumpida: " + uri, e);
        }
    }
}