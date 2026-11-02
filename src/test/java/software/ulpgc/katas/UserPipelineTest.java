package software.ulpgc.katas;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import software.ulpgc.katas.io.GsonUserParser;
import software.ulpgc.katas.io.GsonUserSerializer;
import software.ulpgc.katas.io.ResourceTextSource;
import software.ulpgc.katas.io.TextSource;
import software.ulpgc.katas.io.UserReportDto;
import software.ulpgc.katas.transform.TransformResult;
import software.ulpgc.katas.transform.UserTransformer;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserPipelineTest {
    private static final Clock FIXED_CLOCK =
            Clock.fixed(Instant.parse("2026-01-01T00:00:00Z"), ZoneOffset.UTC);

    private String run(TextSource source) {
        TransformResult result = new UserTransformer().transform(new GsonUserParser().parse(source.read()));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new GsonUserSerializer(FIXED_CLOCK).write(result, out);
        return out.toString(StandardCharsets.UTF_8);
    }

    @Test
    void valid_input_produces_the_expected_json() {
        String actual = run(new ResourceTextSource("users.json"));
        String expected = new ResourceTextSource("expected-output.json").read();
        assertEquals(JsonParser.parseString(expected), JsonParser.parseString(actual));
    }

    @Test
    void generated_json_can_be_parsed_again() {
        String actual = run(new ResourceTextSource("users-invalid.json"));
        UserReportDto report = new Gson().fromJson(actual, UserReportDto.class);
        assertEquals(2, report.users.size());
        assertEquals(3, report.metadata.discarded);
        assertEquals("Vlade Glavaš", report.users.get(1).name);
    }

    @Test
    void empty_dataset_produces_valid_json_with_zero_counts() {
        String actual = run(() -> "");
        UserReportDto report = new Gson().fromJson(actual, UserReportDto.class);
        assertTrue(report.users.isEmpty());
        assertEquals(0, report.metadata.processed);
    }

    @Test
    void changing_the_source_does_not_change_the_output() {
        String text = new ResourceTextSource("users.json").read();
        TextSource inMemory = () -> text;
        assertEquals(run(new ResourceTextSource("users.json")), run(inMemory));
    }
}
