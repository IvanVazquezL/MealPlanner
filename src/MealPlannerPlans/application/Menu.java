package MealPlannerPlans.application;

import MealPlannerPlans.model.Ingredient;
import MealPlannerPlans.model.Meal;
import MealPlannerPlans.model.Plan;
import MealPlannerPlans.repository.DbIngredientDao;
import MealPlannerPlans.repository.DbMealDao;
import MealPlannerPlans.repository.DbPlanDao;
import MealPlannerPlans.utils.CommonUtils;
import MealPlannerPlans.utils.TypeOfAction;
import MealPlannerPlans.utils.TypeOfMeal;

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
    private static DbPlanDao dbPlanDao;

    public static void main(String[] args) throws SQLException {
        dbMealDao = new DbMealDao();
        dbIngredientDao = new DbIngredientDao();
        dbPlanDao = new DbPlanDao();
        runMenu();
    }

    public void addMeal(Meal meal) {
        meals.add(meal);
    }

    public static void runMenu() throws SQLException {
        String action;
        do {
            do {
                System.out.println("What would you like to do (add, show, plan, exit)?");
                action = scanner.nextLine().toUpperCase();
            } while(!validActions.contains(action));

            switch (action) {
                case "ADD":
                    executeAddMealProcess();
                    break;
                case "SHOW":
                    showMeals();
                    break;
                case "PLAN":
                    executePlanProcess();
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

    private static void executePlanProcess() throws SQLException {
        dbPlanDao.deleteAll();

        for (String dayOfTheWeek : CommonUtils.daysOfTheWeek) {
            System.out.println(dayOfTheWeek);
            for (String mealCategory : validMeals) {
                List<Meal> meals = dbMealDao.getByCategory(mealCategory.toLowerCase());
                List<String> mealNames = meals.stream()
                        .map(Meal::getMeal)
                        .collect(Collectors.toList());
                mealNames.sort(String::compareTo);

                for(String meal : mealNames) {
                    System.out.println(meal);
                }
                System.out.printf("Choose the %s for %s from the list above:\n", mealCategory.toLowerCase(), dayOfTheWeek);

                String mealName;
                do {
                    mealName = scanner.nextLine();
                    if (!mealNames.contains(mealName)) {
                        System.out.println("This meal doesn’t exist. Choose a meal from the list above.");
                    }
                } while(!mealNames.contains(mealName));

                Meal meal = dbMealDao.getByMeal(mealName);
                Plan plan = new Plan(
                    CommonUtils.generateId(),
                    dayOfTheWeek,
                    meal.getCategory(),
                    meal.getMeal(),
                    meal.getMeal_id()
                );
                dbPlanDao.add(plan);
            }
            System.out.printf("Yeah! We planned the meals for %s.\n", dayOfTheWeek);
        }

        for (String dayOfTheWeek : CommonUtils.daysOfTheWeek) {
            System.out.println(dayOfTheWeek);

            for (String mealCategory : validMeals) {
                Plan plan = dbPlanDao.getByDayAndCategory(dayOfTheWeek, mealCategory.toLowerCase());
                System.out.printf("%s: %s\n", mealCategory.toLowerCase(), plan.getMeal());
            }
        }
    }

    private static void showMeals() {
        System.out.println("Which category do you want to print (breakfast, lunch, dinner)?");
        String mealCategory;

        do {
            mealCategory = scanner.nextLine().toUpperCase();

            if (!validMeals.contains(mealCategory)) {
                System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
            }
        } while(!validMeals.contains(mealCategory));

        List<Meal> meals = dbMealDao.getByCategory(mealCategory.toLowerCase());

        if (meals.size() == 0) {
            System.out.println("No meals found.");
             return;
        }

        System.out.printf("Category: %s\n", mealCategory.toLowerCase());

        for (Meal meal : meals) {
            List<Ingredient> ingredients = dbIngredientDao.selectForListByMealId(meal.getMeal_id());
            StringBuilder ingredientsString = new StringBuilder();

            for (Ingredient ingredient : ingredients) {
                ingredientsString.append(ingredient.getIngredient()).append("\n");
            }
            System.out.printf("""
            Name: %s
            Ingredients:
            %s
                """, meal.getMeal(), ingredientsString.toString());
        }
    }
}
