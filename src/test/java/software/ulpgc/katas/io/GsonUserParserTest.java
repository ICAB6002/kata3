package software.ulpgc.katas.io;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GsonUserParserTest {
    private final UserParser parser = new GsonUserParser();

    @Test
    void parses_every_record_without_validating() {
        String json = new ResourceTextSource("users-invalid.json").read();
        assertEquals(5, parser.parse(json).size());
    }

    @Test
    void blank_text_is_an_empty_dataset() {
        assertTrue(parser.parse("").isEmpty());
        assertTrue(parser.parse("   \n").isEmpty());
    }

    @Test
    void json_without_users_is_an_empty_dataset() {
        assertTrue(parser.parse("{\"results\": []}").isEmpty());
        assertTrue(parser.parse("{}").isEmpty());
    }

    @Test
    void text_that_is_not_json_throws_a_clear_exception() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse("[1, 2, 3]"));
    }
}
