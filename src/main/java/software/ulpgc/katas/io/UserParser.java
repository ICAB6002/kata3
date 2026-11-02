package software.ulpgc.katas.io;

import java.util.List;

public interface UserParser {
    List<UserDto> parse(String text);
}