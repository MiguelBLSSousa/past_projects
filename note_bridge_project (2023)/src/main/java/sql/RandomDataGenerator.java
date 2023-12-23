package sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RandomDataGenerator {

    public static final List<String> INSTRUMENTS = Arrays.asList("piano", "guitar", "bass", "drums", "accordion", "bagpipes", "banjo", "bongo drums", "bugle", "cello", "clarinet", "cymbals", "flute", "french horn", "harmonica", "harp", "maracas", "organ", "pan flute", "recorder", "saxophone", "sitar", "tambourine", "triangle", "trombone", "trumpet", "tuba", "ukulele", "violin", "xylophone");

    public static List<TeacherData> generateData() {
        List<TeacherData> results = new ArrayList<>();
        Random random = new Random();

        int personId = 20;
        int teacherId = 30;

        for (int i = 0; i < 100; i++) {
            int houseNr = generateHouseNumber();
            String zipCode = generateZipCode();
            String street = generateStreet();
            String city = generateCity();

            int currentPersonId = personId + i;
            String firstName = generateFirstName();
            String surname = generateSurname();
            String phoneNr = generatePhoneNumber();
            String email = generateEmail(firstName, surname);
            String password = generatePassword();

            int currentTeacherId = teacherId + i;
            int rating = generateRating();
            int rate = generateRate();

            List<String> instruments = generateInstruments();

            TeacherData data = new TeacherData(houseNr, zipCode, street, city, currentPersonId, firstName, surname, phoneNr, email, password, currentTeacherId, rating, rate, instruments);
            results.add(data);
        }

        return results;
    }

    private static Integer generateHouseNumber() {
        Random random = new Random();
        int houseNumber = random.nextInt(100) + 1;
        return houseNumber;
    }

    private static String generateZipCode() {
        Random random = new Random();
        int[] zipCodes = { 7961, 7543, 7511, 7620, 780, 740, 6590, 7980 };
        int zipCode1 = zipCodes[random.nextInt(zipCodes.length)];
        int zipCode2 = random.nextInt(8) + 65;
        int zipCode3 = random.nextInt(8) + 65;
        char zipCode2Char = (char) zipCode2;
        char zipCode3Char = (char) zipCode3;
        return zipCode1 + " " + zipCode2Char + zipCode3Char;
    }

    private static String generateStreet() {
        String[] streets = { "Main Street", "Park Avenue", "Broadway", "Elm Street", "Oak Avenue" };
        Random random = new Random();
        int index = random.nextInt(streets.length);
        return streets[index];
    }

    private static String generateCity() {
        String[] cities = { "Amsterdam", "Rotterdam", "The Hague", "Utrecht", "Eindhoven" };
        Random random = new Random();
        int index = random.nextInt(cities.length);
        return cities[index];
    }

    private static String generateFirstName() {
        String[] firstNames = { "John", "Jane", "David", "Emma", "Michael" };
        Random random = new Random();
        int index = random.nextInt(firstNames.length);
        return firstNames[index];
    }

    private static String generateSurname() {
        String[] surnames = { "Smith", "Johnson", "Brown", "Taylor", "Anderson" };
        Random random = new Random();
        int index = random.nextInt(surnames.length);
        return surnames[index];
    }

    private static String generateEmail(String firstName, String surname) {
        Random random = new Random();
        int randomInt = random.nextInt(1000);
        return firstName + "." + surname + randomInt + "@notebridge.com";
    }

    private static String generatePhoneNumber() {
        Random random = new Random();
        StringBuilder phoneNumber = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            phoneNumber.append(random.nextInt(10));
        }
        return phoneNumber.toString();
    }

    private static String generatePassword() {
        String[] passwords = { "password1", "secret123", "qwerty", "123456", "letmein" };
        Random random = new Random();
        int index = random.nextInt(passwords.length);
        return passwords[index];
    }

    private static int generateRating() {
        Random random = new Random();
        int rating = random.nextInt(5) + 1;
        return rating;
    }

    private static int generateRate() {
        Random random = new Random();
        int rate = random.nextInt(76) + 5;
        return rate;
    }

    private static List<String> generateInstruments() {
        Random random = new Random();
        List<String> instruments = new ArrayList<>();
        int amount = random.nextInt(5) + 1;
        for (int i = 0; i < amount; i++) {
            int index = random.nextInt(INSTRUMENTS.size());
            instruments.add(INSTRUMENTS.get(index));
        }
        return instruments;
    }

    public static class TeacherData {
        private int houseNr;
        private String zipCode;
        private String street;
        private String city;
        private int currentPersonId;
        private String firstName;
        private String surname;
        private String phoneNr;
        private String email;
        private String password;
        private int currentTeacherId;
        private int rating;
        private int rate;
        private List<String> instruments;

        public int getHouseNr() {
            return houseNr;
        }

        public void setHouseNr(int houseNr) {
            this.houseNr = houseNr;
        }

        public String getZipCode() {
            return zipCode;
        }

        public void setZipCode(String zipCode) {
            this.zipCode = zipCode;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public int getCurrentPersonId() {
            return currentPersonId;
        }

        public void setCurrentPersonId(int currentPersonId) {
            this.currentPersonId = currentPersonId;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getSurname() {
            return surname;
        }

        public void setSurname(String surname) {
            this.surname = surname;
        }

        public String getPhoneNr() {
            return phoneNr;
        }

        public void setPhoneNr(String phoneNr) {
            this.phoneNr = phoneNr;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public int getCurrentTeacherId() {
            return currentTeacherId;
        }

        public void setCurrentTeacherId(int currentTeacherId) {
            this.currentTeacherId = currentTeacherId;
        }

        public int getRating() {
            return rating;
        }

        public void setRating(int rating) {
            this.rating = rating;
        }

        public int getRate() {
            return rate;
        }

        public void setRate(int rate) {
            this.rate = rate;
        }

        public List<String> getInstruments() {
            return instruments;
        }

        public void setInstruments(List<String> instruments) {
            this.instruments = instruments;
        }

        public TeacherData(int houseNr, String zipCode, String street, String city, int currentPersonId, String firstName,
                           String surname, String phoneNr, String email, String password, int currentTeacherId,
                           int rating, int rate, List<String> instruments) {
            this.houseNr = houseNr;
            this.zipCode = zipCode;
            this.street = street;
            this.city = city;
            this.currentPersonId = currentPersonId;
            this.firstName = firstName;
            this.surname = surname;
            this.phoneNr = phoneNr;
            this.email = email;
            this.password = password;
            this.currentTeacherId = currentTeacherId;
            this.rating = rating;
            this.rate = rate;
            this.instruments = instruments;
        }
    }
}
