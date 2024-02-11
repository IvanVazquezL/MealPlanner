package MealPlannerWriteFile.utils;

import java.util.ArrayList;

public enum TypeOfAction {
    ADD("ADD"),
    SHOW("SHOW"),
    PLAN("PLAN"),
    SAVE("SAVE"),
    EXIT("EXIT");

    private final String value;

    TypeOfAction(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ArrayList<String> getStringValues() {
        TypeOfAction[] actions = TypeOfAction.values();
        ArrayList<String> stringValues = new ArrayList<>();

        for (TypeOfAction action : actions) {
            stringValues.add(action.getValue());
        }

        return stringValues;
    }
}

