package Stage2;

import java.util.ArrayList;

public enum TypeOfMeal {
    BREAKFAST("BREAKFAST"),
    LUNCH("LUNCH"),
    DINNER("EXIT");

    private final String value;

    TypeOfMeal(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ArrayList<String> getStringValues() {
        TypeOfMeal[] actions = TypeOfMeal.values();
        ArrayList<String> stringValues = new ArrayList<>();

        for (TypeOfMeal action : actions) {
            stringValues.add(action.getValue());
        }

        return stringValues;
    }
}
