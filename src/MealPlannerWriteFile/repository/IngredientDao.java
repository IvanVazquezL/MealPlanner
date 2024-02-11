package MealPlannerWriteFile.repository;

import MealPlannerWriteFile.model.Ingredient;

import java.sql.SQLException;
import java.util.List;

public interface IngredientDao {
    List<Ingredient> findAll();
    Ingredient findById(int id);
    void add(Ingredient ingredient) throws SQLException;
    void update(Ingredient ingredient) throws SQLException;
    void deleteById(int id) throws SQLException;
}
