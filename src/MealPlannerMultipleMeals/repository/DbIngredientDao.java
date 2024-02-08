package MealPlannerMultipleMeals.repository;

import MealPlannerMultipleMeals.model.Ingredient;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DbIngredientDao implements IngredientDao{
    private DbClient dbClient;
    private static final String CREATE_DB =
            """
    CREATE TABLE IF NOT EXISTS INGREDIENTS (
        ingredient_id INT PRIMARY KEY,
        ingredient VARCHAR(50) NOT NULL,
        meal_id INT,
        FOREIGN KEY (meal_id) REFERENCES MEALS (meal_id)
    )
    """;
    private static final String INSERT_DATA =
            "INSERT INTO INGREDIENTS VALUES (%d, '%s', %d)";
    private static final String SELECT_ALL = "SELECT * FROM INGREDIENTS";
    private static final String SELECT = "SELECT * FROM INGREDIENTS WHERE ingredient_id = %d";
    private static final String SELECT_ALL_MEAL_ID = "SELECT * FROM INGREDIENTS WHERE meal_id = %d";
    private static final String UPDATE_DATA =
            "UPDATE INGREDIENTS SET ingredient = '%s', meal_id = %d WHERE ingredient_id = %d";
    private static final String DELETE_DATA = "DELETE FROM INGREDIENTS WHERE ingredient_id = %d";

    public DbIngredientDao() throws SQLException {
        dbClient = new DbClient();
        dbClient.executeUpdate(CREATE_DB);
        //System.out.println("INGREDIENTS table created");
    }

    @Override
    public List<Ingredient> findAll() {
        return selectForList(SELECT_ALL);
    }

    @Override
    public Ingredient findById(int id) {
        Ingredient ingredient = select(String.format(SELECT, id));

        if (ingredient != null) {
            System.out.println("Ingredient: Id " + id + ", found");
            return ingredient;
        } else {
            System.out.println("Ingredient: Id " + id + ", not found");
            return null;
        }
    }

    @Override
    public void add(Ingredient ingredient) throws SQLException {
        dbClient.executeUpdate(String.format(
                INSERT_DATA, ingredient.getIngredient_id(), ingredient.getIngredient(), ingredient.getMeal_id()));
        //System.out.printf("Ingredient: ingredient: %s, meal_id: %d added\n", ingredient.getIngredient(), ingredient.getMeal_id());
    }

    @Override
    public void update(Ingredient ingredient) throws SQLException {
        dbClient.executeUpdate(String.format(
                UPDATE_DATA, ingredient.getIngredient(), ingredient.getMeal_id(), ingredient.getIngredient_id()));
        //System.out.println("Ingredient: Id " + ingredient.getIngredient() + ", updated");
    }

    @Override
    public void deleteById(int id) throws SQLException {
        dbClient.executeUpdate(String.format(DELETE_DATA, id));
        //System.out.println("Ingredient: Id " + id + ", deleted");
    }

    public Ingredient select(String query) {
        List<Ingredient> ingredients = selectForList(query);
        if (ingredients.size() == 1) {
            return ingredients.get(0);
        } else if (ingredients.size() == 0) {
            return null;
        } else {
            throw new IllegalStateException("Query returned more than one object");
        }
    }

    public List<Ingredient> selectForListByMealId(int mealId) {
        return selectForList(String.format(SELECT_ALL_MEAL_ID, mealId));
    }

    public List<Ingredient> selectForList(String query) {
        List<Ingredient> ingredients = new ArrayList<>();

        try (
                ResultSet resultSetItem = dbClient.executeQuery(query)
        ) {
            while (resultSetItem.next()) {
                // Retrieve column values
                int ingredient_id = resultSetItem.getInt("ingredient_id");
                String ingredientName = resultSetItem.getString("ingredient");
                int meal_id = resultSetItem.getInt("meal_id");
                Ingredient ingredient = new Ingredient(ingredient_id, ingredientName, meal_id);
                ingredients.add(ingredient);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ingredients;
    }
}
