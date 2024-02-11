package MealPlannerWriteFile.repository;

import MealPlannerWriteFile.model.Plan;

import java.sql.SQLException;
import java.util.List;

public interface PlanDao {
    List<Plan> findAll();
    Plan findById(int id);
    Plan add(Plan plan) throws SQLException;
    void update(Plan plan) throws SQLException;
    void deleteById(int id) throws SQLException;
}
