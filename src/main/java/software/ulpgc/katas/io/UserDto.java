package software.ulpgc.katas.io;

public class UserDto {
    public String gender;
    public Name name;
    public String email;
    public Dob dob;

    public static class Name {
        public String first;
        public String last;
    }

    public static class Dob {
        public String date;
    }
}