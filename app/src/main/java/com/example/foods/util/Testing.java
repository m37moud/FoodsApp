package com.example.foods.util;

import android.util.Log;

import com.example.foods.models.Recipe;

import java.util.List;

public class Testing {
    public static void printRecipes(String tag , List<Recipe> list )
    {

        for(Recipe recipe : list){
            Log.d(tag, "printRecipes: " + recipe.getTitle());
        }
    }
}
