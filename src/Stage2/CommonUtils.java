package Stage2;

import java.util.ArrayList;

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
}
