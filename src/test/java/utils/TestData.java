package utils;

import java.util.Random;

public class TestData {

    private static final Random random = new Random();

    public static String generateStreet() {
        int number = 100 + random.nextInt(900);
        return number + " Automation Street";
    }

    public static String generateCity() {
        return "Hyderabad";
    }

    public static String generateState() {
        return "Telangana";
    }

    public static String generateZipCode() {
        int zip = 10000 + random.nextInt(90000);
        return String.valueOf(zip);
    }

    public static String generatePhoneNumber() {
        int number = 100000000 + random.nextInt(900000000);
        return "9" + number;
    }
}