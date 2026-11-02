package software.ulpgc.katas.io;

import java.util.List;

public class UserReportDto {
    public Metadata metadata;
    public List<UserEntry> users;

    public static class Metadata {
        public String generatedAt;
        public int processed;
        public int valid;
        public int discarded;
    }

    public static class UserEntry {
        public String name;
        public String birthdate;
        public String email;
        public String gender;
    }
}