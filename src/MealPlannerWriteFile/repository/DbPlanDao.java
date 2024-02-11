package MealPlannerWriteFile.repository;

import MealPlannerWriteFile.model.Plan;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DbPlanDao implements PlanDao{
    private DbClient dbClient;
    private static final String CREATE_DB = """
        CREATE TABLE IF NOT EXISTS PLAN (
            plan_id INT, 
            day VARCHAR(50) NOT NULL,
            category VARCHAR(50) NOT NULL, 
            meal VARCHAR(50) NOT NULL, 
            meal_id INT,
            PRIMARY KEY (plan_id),
            FOREIGN KEY (meal_id) REFERENCES MEALS (meal_id)
        )""";
    private static final String INSERT_DATA =
            "INSERT INTO PLAN VALUES (%d, '%s', '%s', '%s', %d)";
    private static final String SELECT_ALL = "SELECT * FROM PLAN";
    private static final String SELECT = "SELECT * FROM PLAN WHERE meal_id = %d";
    private static final String SELECT_BY_CATEGORY =
            "SELECT * FROM PLAN WHERE category = '%s'";
    private static final String SELECT_BY_DAY_CATEGORY =
            "SELECT * FROM PLAN WHERE day = '%s' AND category = '%s'";
    private static final String SELECT_BY_DAY_MEAL_CATEGORY =
            "SELECT * FROM PLAN WHERE day = '%s' AND category = '%s' AND meal = '%s'";
    private static final String UPDATE_DATA =
            "UPDATE PLAN SET day = '%s', category = '%s', meal = '%s' WHERE plan_id = %d";
    private static final String DELETE_DATA = "DELETE FROM PLAN WHERE plan_id = %d";
    private static final String DELETE_ALL = "DELETE FROM PLAN";

    public DbPlanDao() throws SQLException {
        dbClient = new DbClient();
        dbClient.executeUpdate(CREATE_DB);
        //System.out.println("MEALS table created");
    }

    @Override
    public List<Plan> findAll() {
        return selectForList(SELECT_ALL);
    }

    @Override
    public Plan findById(int id) {
        Plan plan = select(String.format(SELECT, id));

        if (plan != null) {
            System.out.println("Plan: Id " + id + ", found");
            return plan;
        } else {
            System.out.println("Plan: Id " + id + ", not found");
            return null;
        }
    }

    @Override
    public Plan add(Plan plan) throws SQLException {
        dbClient.executeUpdate(String.format(
                INSERT_DATA, plan.getPlan_id(), plan.getDay(), plan.getCategory(), plan.getMeal(), plan.getMeal_id()));
        //System.out.printf("Meal: category: %s, meal: %s added\n", meal.getCategory(), meal.getMeal());
        Plan newPlanDB = select(String.format(SELECT_BY_DAY_MEAL_CATEGORY, plan.getDay(), plan.getMeal(), plan.getCategory()));
        return newPlanDB;
    }

    @Override
    public void update(Plan plan) throws SQLException {
        dbClient.executeUpdate(String.format(
                UPDATE_DATA, plan.getDay(), plan.getCategory(), plan.getMeal(), plan.getMeal_id()));
        //System.out.println("Meal: Id " + meal.getMeal_id() + ", updated");
    }

    @Override
    public void deleteById(int id) throws SQLException {
        dbClient.executeUpdate(String.format(DELETE_DATA, id));
        //System.out.println("Developer: Id " + id + ", deleted");
    }

    public Plan getByDayAndCategory(String day, String category) throws SQLException {
        return select(String.format(SELECT_BY_DAY_CATEGORY, day, category));
    }

    public Plan select(String query) {
        List<Plan> plans = selectForList(query);
        if (plans.size() == 1) {
            return plans.get(0);
        } else if (plans.size() == 0) {
            return null;
        } else {
            throw new IllegalStateException("Query returned more than one object");
        }
    }

    public void deleteAll() throws SQLException {
        dbClient.executeUpdate(DELETE_ALL);
    }

    public List<Plan> selectForList(String query) {
        List<Plan> plans = new ArrayList<>();

        try (
                ResultSet resultSetItem = dbClient.executeQuery(query)
        ) {
            while (resultSetItem.next()) {
                // Retrieve column values
                int plan_id = resultSetItem.getInt("plan_id");
                String day = resultSetItem.getString("day");
                String category = resultSetItem.getString("category");
                String mealName = resultSetItem.getString("meal");
                int meal_id = resultSetItem.getInt("meal_id");
                Plan plan = new Plan(plan_id, day, category, mealName, meal_id);
                plans.add(plan);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return plans;
    }
}
