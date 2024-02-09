package MealPlannerPlans.model;

public class Plan {
    private int plan_id;
    private String day;
    private String meal;
    private String category;
    private int meal_id;

    public Plan(int plan_id, String day, String category, String meal, int meal_id) {
        this.plan_id = plan_id;
        this.day = day;
        this.meal = meal;
        this.category = category;
        this.meal_id = meal_id;
    }

    public int getPlan_id() {
        return plan_id;
    }

    public void setPlan_id(int plan_id) {
        this.plan_id = plan_id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMeal() {
        return meal;
    }

    public void setMeal(String meal) {
        this.meal = meal;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getMeal_id() {
        return meal_id;
    }

    public void setMeal_id(int meal_id) {
        this.meal_id = meal_id;
    }
}
