package software.ulpgc.katas.transform;

import software.ulpgc.katas.domain.model.User;

import java.util.List;

public record TransformResult(List<User> users, int discarded) {

    public TransformResult {
        users = List.copyOf(users);
    }

    public int processed() {
        return users.size() + discarded;
    }
}