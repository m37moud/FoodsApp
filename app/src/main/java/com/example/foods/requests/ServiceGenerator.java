package com.example.foods.requests;

import com.example.foods.util.Constants;
import com.example.foods.util.LiveDataCallAdapter;
import com.example.foods.util.LiveDataCallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addCallAdapterFactory(new LiveDataCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = builder.build();

    private  static RecipeApi recipeApi = retrofit.create(RecipeApi.class);

    public static RecipeApi getRecipeApi()
    {
        return  recipeApi;
    }




}
