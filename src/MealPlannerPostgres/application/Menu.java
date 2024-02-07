package MealPlannerPostgres.application;

import MealPlannerPostgres.model.Ingredient;
import MealPlannerPostgres.model.Meal;
import MealPlannerPostgres.repository.DbIngredientDao;
import MealPlannerPostgres.repository.DbMealDao;
import MealPlannerPostgres.utils.CommonUtils;
import MealPlannerPostgres.utils.TypeOfAction;
import MealPlannerPostgres.utils.TypeOfMeal;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Menu {
    private static ArrayList<Meal> meals = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);
    private static ArrayList<String> validActions = TypeOfAction.getStringValues();
    private static ArrayList<String> validMeals = TypeOfMeal.getStringValues();
    private static DbMealDao dbMealDao;
    private static DbIngredientDao dbIngredientDao;

    public static void main(String[] args) throws SQLException {
        dbMealDao = new DbMealDao();
        dbIngredientDao = new DbIngredientDao();
        runMenu();
    }

    public void addMeal(Meal meal) {
        meals.add(meal);
    }

    public static void runMenu() throws SQLException {
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

    private static void executeAddMealProcess() throws SQLException {
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

        Meal newMeal = new Meal(CommonUtils.generateId(), mealCategory.toLowerCase(), mealName);
        newMeal = dbMealDao.add(newMeal);

        for (String ingredientName : ingredients) {
            Ingredient ingredient = new Ingredient(CommonUtils.generateId(), ingredientName, newMeal.getMeal_id());
            dbIngredientDao.add(ingredient);
        }

        System.out.println("The meal has been added!");
    }

    private static void showMeals() {
        List<Meal> meals = dbMealDao.findAll();

        if (meals.isEmpty()) {
            System.out.println("No meals saved. Add a meal first.");
        }

        for (Meal meal : meals) {
            List<Ingredient> ingredients = dbIngredientDao.selectForListByMealId(meal.getMeal_id());
            StringBuilder ingredientsString = new StringBuilder();

            for (Ingredient ingredient : ingredients) {
                ingredientsString.append(ingredient.getIngredient()).append("\n");
            }
            System.out.printf("""
                Category: %s
                Name: %s
                Ingredients:
                %s
                    """, meal.getCategory(), meal.getMeal(), ingredientsString.toString());
        }
    }
}
