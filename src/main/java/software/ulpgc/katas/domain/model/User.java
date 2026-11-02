package software.ulpgc.katas.domain.model;

import java.time.LocalDate;
import java.time.Period;

public record User(String name, LocalDate birthdate, String email, Gender gender) {

    public enum Gender {MALE, FEMALE}

    public int age() { return Period.between(birthdate, LocalDate.now()).getYears();}
}

