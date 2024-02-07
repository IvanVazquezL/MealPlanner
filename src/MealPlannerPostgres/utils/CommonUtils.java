package MealPlannerPostgres.utils;

import java.util.ArrayList;
import java.util.Random;

public class CommonUtils {
    public static boolean containsOnlyLetters(String str) {
        return str != null && str.matches("^[a-zA-Z]*$");
    }

    public static boolean containsOnlyLettersAndSpaces(String str) {
        return str != null && str.matches("^[a-zA-Z\\s]*$");
    }

    public static boolean allStringsContainOnlyLetters(ArrayList<String> stringList) {
        if (stringList == null || stringList.isEmpty()) {
            return false; // Handle null or empty list
        }

        for (String str : stringList) {
            if (str.isEmpty() || !containsOnlyLettersAndSpaces(str)) {
                return false; // At least one string doesn't contain only letters
            }
        }

        return true; // All strings contain only letters
    }

    public static int generateId() {
        Random random = new Random();
        int min = 100000; // Minimum value (inclusive)
        int max = 999999; // Maximum value (exclusive)
        int randomNumber = random.nextInt(max - min + 1) + min;
        return randomNumber;
    }
}
