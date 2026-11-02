package software.ulpgc.katas.transform;

import software.ulpgc.katas.domain.model.User;
import software.ulpgc.katas.io.UserDto;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UserTransformer {

    public TransformResult transform(List<UserDto> dtos) {
        List<User> users = new ArrayList<>();
        int discarded = 0;
        for (UserDto dto : dtos) {
            try {
                users.add(toUser(dto));
            } catch (IllegalArgumentException | DateTimeException e) {
                discarded++;
                System.err.println("Registro descartado: " + e.getMessage());
            }
        }
        return new TransformResult(users, discarded);
    }

    private User toUser(UserDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("registro nulo");
        }
        return new User(nameOf(dto), birthdateOf(dto), emailOf(dto), genderOf(dto));
    }

    private String nameOf(UserDto dto) {
        if (dto.name == null || isBlank(dto.name.first) || isBlank(dto.name.last)) {
            throw new IllegalArgumentException("nombre incompleto");
        }
        return dto.name.first + " " + dto.name.last;
    }

    private LocalDate birthdateOf(UserDto dto) {
        if (dto.dob == null || isBlank(dto.dob.date)) {
            throw new IllegalArgumentException("falta la fecha de nacimiento");
        }
        return LocalDate.ofInstant(Instant.parse(dto.dob.date), ZoneOffset.UTC);
    }

    private String emailOf(UserDto dto) {
        if (isBlank(dto.email) || !dto.email.contains("@")) {
            throw new IllegalArgumentException("email no válido: '" + dto.email + "'");
        }
        return dto.email;
    }

    private User.Gender genderOf(UserDto dto) {
        if (isBlank(dto.gender)) {
            throw new IllegalArgumentException("falta el género");
        }
        return User.Gender.valueOf(dto.gender.toUpperCase(Locale.ROOT));
    }

    private static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}