package com.example.foods.repostories;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.example.foods.AppExecutors;
import com.example.foods.models.Recipe;
import com.example.foods.presistence.RecipeDao;
import com.example.foods.presistence.RecipeDatabase;
import com.example.foods.requests.ServiceGenerator;
import com.example.foods.requests.responses.ApiResponse;
import com.example.foods.requests.responses.RecipeResponse;
import com.example.foods.requests.responses.RecipeSearchResponse;
import com.example.foods.util.Constants;
import com.example.foods.util.NetworkBoundResource;
import com.example.foods.util.Resource;

import java.util.List;

public class RecipeRepository {
    private static final String TAG = "RecipeRepository";
    private static RecipeRepository instanc;
    private RecipeDao recipeDao ;

    private RecipeRepository(Context context){
        recipeDao = RecipeDatabase.getInstance(context).getRecipeDao();
    }
    public static RecipeRepository getInstance(Context context){
        if(instanc == null){
            instanc = new RecipeRepository(context);
        }
        return  instanc;
    }
    public LiveData<Resource<List<Recipe>>> searchRecipeApi(final String query ,final int pageNumbre){

        return new NetworkBoundResource<List<Recipe>, RecipeSearchResponse>(AppExecutors.getInstance()){

            @Override
            public void saveCallResult(@NonNull RecipeSearchResponse item) {
                if(item.getRecipes() != null){ // recipe list will be null if api key is expired
                    Recipe[] recipes = new Recipe[item.getRecipes().size()];

                    int index = 0;
                    for(long rowId: recipeDao.insertRicepes((Recipe[])(item.getRecipes().toArray(recipes)))){
                        if(rowId == -1){ // conflict detected
                            Log.d(TAG, "saveCallResult: CONFLICT... This recipe is already in cache.");
                            // if already exists, I don't want to set the ingredients or timestamp b/c they will be erased
                            recipeDao.update(
                                    recipes[index].getRecipe_id(),
                                    recipes[index].getTitle(),
                                    recipes[index].getPublisher(),
                                    recipes[index].getImage_url(),
                                    recipes[index].getSocial_rank()
                            );
                        }
                        index++;
                    }
                }

            }

            @Override
            public boolean shouldFetch(@Nullable List<Recipe> data) {
                return true;
            }

            @NonNull
            @Override
            public LiveData<List<Recipe>> loadFromDb() {
                return recipeDao.searchRecipes(query,pageNumbre);
            }

            @NonNull
            @Override
            public LiveData<ApiResponse<RecipeSearchResponse>> createCall() {
                return ServiceGenerator.getRecipeApi().searchRecipe(Constants.API_KEY,
                        query,
                        String.valueOf(pageNumbre));
            }
        }.getAsLiveData();
    }

    public LiveData<Resource<Recipe>> getSearchIngrediants(String recipeId){
       return new NetworkBoundResource<Recipe, RecipeResponse>(AppExecutors.getInstance()) {
            @Override
            public void saveCallResult(@NonNull RecipeResponse item) {

                if(item.getRecipe()!=null){
                    Log.d(TAG, "saveCallResult: get ingerdiants method ");
                    item.getRecipe().setTimeStamp((int)System.currentTimeMillis()/1000);

                    recipeDao.insertRecipe(item.getRecipe());
                }

            }

            @Override
            public boolean shouldFetch(@Nullable Recipe data) {
                Log.d(TAG, "shouldFetch: recipe: " + data.toString());
                int currentTime = (int)((System.currentTimeMillis() / 1000));
                Log.d(TAG, "shouldFetch: current time: " + currentTime);
                int lastRefresh = data.getTimeStamp();
                Log.d(TAG, "shouldFetch: last refresh: " + lastRefresh);
                Log.d(TAG, "shouldFetch: it's been " + ((currentTime - lastRefresh) / 60 / 60 / 24) +
                        " days since this recipe was refreshed. 30 days must elapse before refreshing. ");
                if((currentTime - data.getTimeStamp()) >= Constants.RECIPE_REFRESH_TIME){
                    Log.d(TAG, "shouldFetch: SHOULD REFRESH RECIPE?! " + true);
                    return true;
                }
                Log.d(TAG, "shouldFetch: SHOULD REFRESH RECIPE?! " + false);
                return false;
            }

            @NonNull
            @Override
            public LiveData<Recipe> loadFromDb() {
                return recipeDao.getRecipe(recipeId);
            }

            @NonNull
            @Override
            public LiveData<ApiResponse<RecipeResponse>> createCall() {
                return ServiceGenerator.getRecipeApi().getRecipe(Constants.API_KEY,
                        recipeId);
            }
        }.getAsLiveData();
    }
}
