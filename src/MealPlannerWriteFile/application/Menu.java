package MealPlannerWriteFile.application;

import MealPlannerWriteFile.model.Ingredient;
import MealPlannerWriteFile.model.Meal;
import MealPlannerWriteFile.model.Plan;
import MealPlannerWriteFile.repository.DbIngredientDao;
import MealPlannerWriteFile.repository.DbMealDao;
import MealPlannerWriteFile.repository.DbPlanDao;
import MealPlannerWriteFile.utils.CommonUtils;
import MealPlannerWriteFile.utils.TypeOfAction;
import MealPlannerWriteFile.utils.TypeOfMeal;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.*;
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
                System.out.println("What would you like to do (add, show, plan, save, exit)?");
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
                case "SAVE":
                    executeSaveProcess();
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

    public static void executeSaveProcess() {
        List<Plan> plans = dbPlanDao.findAll();

        if (plans.size() == 0) {
            System.out.println("Unable to save. Plan your meals first.");
            return;
        }

        System.out.println("Input a filename:");
        String filename = scanner.nextLine();

        HashMap<String, Integer> ingredientsHashMap = new HashMap<>();

        for (Plan plan : plans) {
            int meal_id = plan.getMeal_id();
            List<Ingredient> ingredients = dbIngredientDao.selectForListByMealId(meal_id);

            for (Ingredient ingredient : ingredients) {
                ingredientsHashMap.compute(ingredient.getIngredient(), (key, value) -> (value == null) ? 1 : value + 1);
            }
        }

        File file = new File(filename);

        try (PrintWriter printWriter = new PrintWriter(file)) {
            for (Map.Entry<String, Integer> entry : ingredientsHashMap.entrySet()) {
                String quantity = entry.getValue() == 1 ?
                        "" :
                        String.format(" x%d", entry.getValue());
                printWriter.printf("%s%s\n", entry.getKey(), quantity);
            }
        } catch (IOException e) {
            System.out.printf("An exception occurred %s", e.getMessage());
        }

        System.out.println("Saved!");
    }
}
