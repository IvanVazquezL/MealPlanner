package Stage2;

import java.util.ArrayList;

public class Meal {
    private String mealCategory;
    private String mealName;
    private ArrayList<String> ingredients = new ArrayList<>();

    Meal(String mealCategory) {
        this.mealCategory = mealCategory;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public String toString() {
        StringBuilder ingredientsString = new StringBuilder();
        for (String ingredient : ingredients) {
            ingredientsString.append(ingredient).append("\n");
        }

        return String.format("""
                Category: %s
                Name: %s
                Ingredients:
                %s
                """, mealCategory.toLowerCase(), mealName, ingredientsString.toString());
    }
}