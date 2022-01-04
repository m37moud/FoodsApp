package com.example.foods;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.foods.models.Recipe;
import com.example.foods.util.Resource;
import com.example.foods.viewmodels.SingleRecipeViewModel;

public class RecipeActivty extends BaseActivity{
    private static final String TAG = "RecipeActivty";
    // UI components
    private AppCompatImageView mRecipeImage;
    private TextView mRecipeTitle, mRecipeRank;
    private LinearLayout mRecipeIngredientsContainer;
    private ScrollView mScrollView;


    private SingleRecipeViewModel mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_recipe);


        mRecipeImage = findViewById(R.id.recipe_image);
        mRecipeTitle = findViewById(R.id.recipe_title);
        mRecipeRank = findViewById(R.id.recipe_social_score);
        mRecipeIngredientsContainer = findViewById(R.id.ingredients_container);
        mScrollView = findViewById(R.id.parent);

        mViewModel = new ViewModelProvider(this).get(SingleRecipeViewModel.class);
        showProgressBar(true);
        getIncomingIntent();


    }

//

    private void getIncomingIntent(){
        if(getIntent().hasExtra("recipe")){
            Recipe recipe = getIntent().getParcelableExtra("recipe");
            Log.d(TAG, "getIncomingIntent--------------: "+ recipe.getTitle());
            getIngerediants(recipe.getRecipe_id());


        }
    }
    private void getIngerediants(final String recipeId){
        mViewModel.getRecipe(recipeId).observe(this, new Observer<Resource<Recipe>>() {
            @Override
            public void onChanged(Resource<Recipe> recipeResource) {
                if(recipeResource != null){
                    switch (recipeResource.status){
                        case LOADING:{
                            showProgressBar(true);
                            break;
                        }
                        case ERROR:{
                            Log.e(TAG, "onChanged: status: ERROR, Recipe: " + recipeResource.data.getTitle() );
                            Log.e(TAG, "onChanged: ERROR message: " + recipeResource.message );
                            showProgressBar(false);
                            showParent();
                            setProperties(recipeResource.data);
                            break;
                        }
                        case SUCCESS:{
                            Log.d(TAG, "onChanged: cache has been refreshed.");
                            Log.d(TAG, "onChanged: status: SUCCESS, Recipe: " + recipeResource.data.getTitle());
                            showProgressBar(false);
                            showParent();
                            setProperties(recipeResource.data);
                            break;
                        }
                    }
                }
            }
        });

    }
    private void setProperties(Recipe recipe){
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background);
                Glide.with(this)
                        .load(recipe.getImage_url())
                        .into(mRecipeImage);
                mRecipeTitle.setText(recipe.getTitle());
                mRecipeRank.setText(String.valueOf(Math.round(recipe.getSocial_rank())));
        mRecipeIngredientsContainer.removeAllViews();

        setIngerediants(recipe);

    }
	
    private void setIngerediants(Recipe recipe){
        if(recipe.getIngredients() != null){
            for(String ingerediants : recipe.getIngredients()){
                TextView textView = new TextView(this);
                textView.setText(ingerediants);
                textView.setTextSize(15);
                textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                mRecipeIngredientsContainer.addView(textView);
            }
        }else {
            TextView textView = new TextView(this);
            textView.setText("no ingerdiants retrived \n please check connection .");
            textView.setTextSize(15);
            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            mRecipeIngredientsContainer.addView(textView);
        }
    }
//
    private void showParent(){
        mScrollView.setVisibility(View.VISIBLE);
    }
}
