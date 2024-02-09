package MealPlannerPlans.repository;

import MealPlannerPlans.model.Meal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DbMealDao implements MealDao{
    private DbClient dbClient;
    private static final String CREATE_DB = """
        CREATE TABLE IF NOT EXISTS MEALS (
            meal_id INT, 
            category VARCHAR(50) NOT NULL, 
            meal VARCHAR(50) NOT NULL, 
            PRIMARY KEY (meal_id)
        )""";
    private static final String INSERT_DATA =
        "INSERT INTO MEALS VALUES (%d, '%s', '%s')";
    private static final String SELECT_ALL = "SELECT * FROM MEALS";
    private static final String SELECT = "SELECT * FROM MEALS WHERE meal_id = %d";
    private static final String SELECT_BY_CATEGORY =
        "SELECT * FROM MEALS WHERE category = '%s'";
    private static final String SELECT_BY_MEAL =
            "SELECT * FROM MEALS WHERE meal = '%s'";
    private static final String SELECT_BY_CATEGORY_AND_MEAL =
        "SELECT * FROM MEALS WHERE category = '%s' AND meal = '%s'";
    private static final String UPDATE_DATA =
            "UPDATE MEALS SET category = '%s', meal = '%s' WHERE meal_id = %d";
    private static final String DELETE_DATA = "DELETE FROM MEALS WHERE meal_id = %d";

    public DbMealDao() throws SQLException {
        dbClient = new DbClient();
        dbClient.executeUpdate(CREATE_DB);
        //System.out.println("MEALS table created");
    }

    @Override
    public List<Meal> findAll() {
        return selectForList(SELECT_ALL);
    }

    @Override
    public Meal findById(int id) {
        Meal meal = select(String.format(SELECT, id));

        if (meal != null) {
            System.out.println("Meal: Id " + id + ", found");
            return meal;
        } else {
            System.out.println("Meal: Id " + id + ", not found");
            return null;
        }
    }

    @Override
    public Meal add(Meal meal) throws SQLException {
        dbClient.executeUpdate(String.format(
                INSERT_DATA, meal.getMeal_id(), meal.getCategory(), meal.getMeal()));
        //System.out.printf("Meal: category: %s, meal: %s added\n", meal.getCategory(), meal.getMeal());
        Meal newMealDB = select(String.format(SELECT_BY_CATEGORY_AND_MEAL, meal.getCategory(), meal.getMeal()));
        return newMealDB;
    }

    @Override
    public void update(Meal meal) throws SQLException {
        dbClient.executeUpdate(String.format(
                UPDATE_DATA, meal.getCategory(), meal.getMeal(), meal.getMeal_id()));
        //System.out.println("Meal: Id " + meal.getMeal_id() + ", updated");
    }

    @Override
    public void deleteById(int id) throws SQLException {
        dbClient.executeUpdate(String.format(DELETE_DATA, id));
        //System.out.println("Developer: Id " + id + ", deleted");
    }

    public List<Meal> getByCategory(String category) {
        return selectForList(String.format(SELECT_BY_CATEGORY, category));
    }

    public Meal getByMeal(String meal) {
        return select(String.format(SELECT_BY_MEAL, meal));
    }

    public Meal select(String query) {
        List<Meal> meals = selectForList(query);
        if (meals.size() == 1) {
            return meals.get(0);
        } else if (meals.size() == 0) {
            return null;
        } else {
            throw new IllegalStateException("Query returned more than one object");
        }
    }

    public List<Meal> selectForList(String query) {
        List<Meal> meals = new ArrayList<>();

        try (
                ResultSet resultSetItem = dbClient.executeQuery(query)
        ) {
            while (resultSetItem.next()) {
                // Retrieve column values
                int meal_id = resultSetItem.getInt("meal_id");
                String category = resultSetItem.getString("category");
                String mealName = resultSetItem.getString("meal");
                Meal meal = new Meal(meal_id, category, mealName);
                meals.add(meal);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return meals;
    }
}
