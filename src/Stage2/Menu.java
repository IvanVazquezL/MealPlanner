package Stage2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Menu {
    private ArrayList<Meal> meals;
    private final Scanner scanner = new Scanner(System.in);
    private ArrayList<String> validActions = TypeOfAction.getStringValues();
    private ArrayList<String> validMeals = TypeOfMeal.getStringValues();

    Menu() {
        meals = new ArrayList<>();
    }

    public void addMeal(Meal meal) {
        meals.add(meal);
    }

    public void runMenu() {
        String action;
        do {
            do {
                System.out.println("What would you like to do (add, show, exit)?");
                action = scanner.nextLine().toUpperCase();
            } while(!validActions.contains(action));

            switch (action) {
                case "ADD":
                    executeAddMealProcess();
                    break;
                case "SHOW":
                    showMeals();
                    break;
                case "EXIT":
                default:
                    System.out.println("Bye!");
                    break;
            }
            System.out.println();
        } while(!action.equals(TypeOfAction.EXIT.getValue()));
    }

    private void executeAddMealProcess() {
        System.out.println("Which meal do you want to add (breakfast, lunch, dinner)?");
        String mealCategory;

        do {
            mealCategory = scanner.nextLine().toUpperCase();

            if (!validMeals.contains(mealCategory)) {
                System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
            }
        } while(!validMeals.contains(mealCategory));

        System.out.println("Input the meal's name:");
        String mealName;
        do {
            mealName = scanner.nextLine().trim();

            if (mealName.isEmpty() || !CommonUtils.containsOnlyLettersAndSpaces(mealName)) {
                System.out.println("Wrong format. Use letters only!");
            }
        } while(mealName.isEmpty() || !CommonUtils.containsOnlyLettersAndSpaces(mealName));


        System.out.println("Input the ingredients:");
        ArrayList<String> ingredients;
        do {
            ingredients = (ArrayList<String>) Arrays.stream(scanner.nextLine().split(","))
                    .map(String::trim)
                    .collect(Collectors.toList());

            if (!CommonUtils.allStringsContainOnlyLetters(ingredients)) {
                System.out.println("Wrong format. Use letters only!");
            }
        } while(!CommonUtils.allStringsContainOnlyLetters(ingredients));

        Meal newMeal = new Meal(mealCategory);
        newMeal.setMealName(mealName);
        newMeal.setIngredients(ingredients);

        meals.add(newMeal);
        System.out.println("The meal has been added!");
    }

    private void showMeals() {
        if (meals.isEmpty()) {
            System.out.println("No meals saved. Add a meal first.");
        }

        for (Meal meal : meals) {
            System.out.println(meal);
        }
    }
}
