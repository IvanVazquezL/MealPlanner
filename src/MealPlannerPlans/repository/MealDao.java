package MealPlannerPlans.repository;

import MealPlannerPlans.model.Meal;

import java.sql.SQLException;
import java.util.List;

public interface MealDao {
    List<Meal> findAll();
    Meal findById(int id);
    Meal add(Meal meal) throws SQLException;
    void update(Meal meal) throws SQLException;
    void deleteById(int id) throws SQLException;
}
