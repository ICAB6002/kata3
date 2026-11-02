package software.ulpgc.katas.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import software.ulpgc.katas.domain.model.User;
import software.ulpgc.katas.transform.TransformResult;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.util.Locale;

public class GsonUserSerializer implements UserSerializer {
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();
    private final Clock clock;

    public GsonUserSerializer() {
        this(Clock.systemUTC());
    }

    public GsonUserSerializer(Clock clock) {
        this.clock = clock;
    }

    @Override
    public void write(TransformResult result, OutputStream out) {
        try {
            Writer writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
            gson.toJson(toReport(result), writer);
            writer.flush();
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo escribir el JSON de salida", e);
        }
    }

    private UserReportDto toReport(TransformResult result) {
        UserReportDto report = new UserReportDto();
        report.metadata = metadataOf(result);
        report.users = result.users().stream().map(this::toEntry).toList();
        return report;
    }

    private UserReportDto.Metadata metadataOf(TransformResult result) {
        UserReportDto.Metadata metadata = new UserReportDto.Metadata();
        metadata.generatedAt = Instant.now(clock).toString();
        metadata.processed = result.processed();
        metadata.valid = result.users().size();
        metadata.discarded = result.discarded();
        return metadata;
    }

    private UserReportDto.UserEntry toEntry(User user) {
        UserReportDto.UserEntry entry = new UserReportDto.UserEntry();
        entry.name = user.name();
        entry.birthdate = user.birthdate().toString();
        entry.email = user.email();
        entry.gender = user.gender().name().toLowerCase(Locale.ROOT);
        return entry;
    }
}