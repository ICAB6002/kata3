package software.ulpgc.katas.transform;

import software.ulpgc.katas.domain.model.User;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record TransformResult(List<User> users, int discarded) {

    public TransformResult {
        users = List.copyOf(users);
    }

    public int processed() {
        return users.size() + discarded;
    }

    public Map<User.Gender, Long> countByGender() {
        return users.stream().collect(Collectors.groupingBy(
                User::gender,
                () -> new EnumMap<>(User.Gender.class),
                Collectors.counting()));
    }
}