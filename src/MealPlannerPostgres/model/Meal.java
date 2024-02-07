package MealPlannerPostgres.model;

public class Meal {
    private String category;
    private String meal;
    private int meal_id;

    public Meal(int meal_id, String category, String meal) {
        this.meal_id = meal_id;
        this.category = category;
        this.meal = meal;
    }

    public String getCategory() {
        return category;
    }

    public int getMeal_id() {
        return meal_id;
    }

    public void setMeal_id(int meal_id) {
        this.meal_id = meal_id;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMeal() {
        return meal;
    }

    public void setMeal(String meal) {
        this.meal = meal;
    }
}
