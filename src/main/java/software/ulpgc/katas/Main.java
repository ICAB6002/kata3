package software.ulpgc.katas;

import software.ulpgc.katas.io.GsonUserParser;
import software.ulpgc.katas.io.GsonUserSerializer;
import software.ulpgc.katas.io.HttpTextSource;
import software.ulpgc.katas.io.TextSource;
import software.ulpgc.katas.io.UserParser;
import software.ulpgc.katas.io.UserSerializer;
import software.ulpgc.katas.transform.TransformResult;
import software.ulpgc.katas.transform.UserTransformer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    private static final String DEFAULT_URL =
            "https://randomuser.me/api/?results=5&seed=kata3&inc=gender,name,email,dob";
    private static final Path OUTPUT = Path.of("output.json");

    public static void main(String[] args) {
        URI api = URI.create(args.length > 0 ? args[0] : DEFAULT_URL);

        TextSource source = new HttpTextSource(api);
        UserParser parser = new GsonUserParser();
        UserTransformer transformer = new UserTransformer();
        UserSerializer serializer = new GsonUserSerializer();

        TransformResult result = transformer.transform(parser.parse(source.read()));

        try (OutputStream out = Files.newOutputStream(OUTPUT)) {
            serializer.write(result, out);
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo escribir " + OUTPUT, e);
        }

        System.out.println("Generado " + OUTPUT + " a partir de " + api + ": "
                + result.users().size() + " válidos, " + result.discarded() + " descartados");
    }
}