package software.ulpgc.katas.io;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.List;

public class GsonUserParser implements UserParser {
    private final Gson gson = new Gson();

    @Override
    public List<UserDto> parse(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }
        RandomUserResponse response;
        try {
            response = gson.fromJson(text, RandomUserResponse.class);
        } catch (JsonSyntaxException e) {
            throw new IllegalArgumentException("El texto recibido no es un JSON válido", e);
        }
        if (response == null || response.results == null) {
            return List.of();
        }
        return response.results;
    }
}