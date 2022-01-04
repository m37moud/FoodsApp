package com.example.foods.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.foods.models.Recipe;
import com.example.foods.repostories.RecipeRepository;
import com.example.foods.util.Resource;


public class SingleRecipeViewModel  extends AndroidViewModel {
    private RecipeRepository recipeRepository;
    private String mRecipeId;
    private boolean ingredientsRetrived;
    public SingleRecipeViewModel( Application application) {
        super(application);
        recipeRepository = RecipeRepository.getInstance(application);
    }
    public LiveData<Resource<Recipe>> getRecipe(String recipeId){
        return recipeRepository.getSearchIngrediants(recipeId);
    }


}
