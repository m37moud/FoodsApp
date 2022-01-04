package com.example.foods.requests.responses;

import com.example.foods.models.Recipe;

public class RecipeResponse {

    private Recipe recipe;

    public Recipe getRecipe() {
        return recipe;
    }

    @Override
    public String toString() {
        return "RecipeResponse{" +
                "recipe=" + recipe +
                '}';
    }
}
