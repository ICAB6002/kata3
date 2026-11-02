package software.ulpgc.katas.transform;

import org.junit.jupiter.api.Test;
import software.ulpgc.katas.domain.model.User;
import software.ulpgc.katas.io.GsonUserParser;
import software.ulpgc.katas.io.ResourceTextSource;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserTransformerTest {
    private final UserTransformer transformer = new UserTransformer();

    private TransformResult transform(String resource) {
        String json = new ResourceTextSource(resource).read();
        return transformer.transform(new GsonUserParser().parse(json));
    }

    @Test
    void valid_record_becomes_a_domain_user() {
        User first = transform("users.json").users().get(0);
        assertEquals("Ernesto Figueroa", first.name());
        assertEquals(LocalDate.of(1945, 10, 10), first.birthdate());
        assertEquals("ernesto.figueroa@example.com", first.email());
        assertEquals(User.Gender.MALE, first.gender());
    }

    @Test
    void malformed_records_are_discarded_and_counted() {
        TransformResult result = transform("users-invalid.json");
        assertEquals(2, result.users().size());
        assertEquals(3, result.discarded());
        assertEquals(5, result.processed());
    }

    @Test
    void empty_dataset_gives_empty_result() {
        TransformResult result = transformer.transform(List.of());
        assertTrue(result.users().isEmpty());
        assertEquals(0, result.discarded());
    }

    @Test
    void counts_valid_users_by_gender() {
        Map<User.Gender, Long> expected = Map.of(User.Gender.MALE, 4L, User.Gender.FEMALE, 1L);
        assertEquals(expected, transform("users.json").countByGender());
    }
}
