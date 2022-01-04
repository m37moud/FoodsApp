package com.example.foods;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
//import androidx.lifecycle.ViewModelProviders;

import com.example.foods.adapters.OnRecipeListener;
import com.example.foods.adapters.RecipeRecyclerAdapter;
import com.example.foods.models.Recipe;
import com.example.foods.util.Resource;
import com.example.foods.util.Testing;
import com.example.foods.util.VerticalSpacingItemDecorator;
import com.example.foods.viewmodels.RecipeListViewModel;

import java.util.List;

import static com.example.foods.viewmodels.RecipeListViewModel.QUERY_EXUESTED;

public class RecipeListActivity extends BaseActivity implements OnRecipeListener {

    private static final String TAG = "RecipeListActivity";

    private RecipeListViewModel mRecipeListViewModel;
    private RecyclerView mRecyclerView;
    private RecipeRecyclerAdapter mAdapter;
    private SearchView mSearchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        mRecyclerView = findViewById(R.id.recycler_recipes);
        mSearchView = findViewById(R.id.search_view);

       // mRecipeListViewModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);
       mRecipeListViewModel = new ViewModelProvider(this).get(RecipeListViewModel.class);


        initRecyclerView();
        initSearchView();
        subscribeObservers();
        setSupportActionBar((Toolbar)findViewById(R.id.tool_bar));
    }

    private void subscribeObservers(){
        mRecipeListViewModel.getRecipes().observe(this, new Observer<Resource<List<Recipe>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<Recipe>> listResource) {
                if(listResource != null){
                    Log.d(TAG, "onChanged: status: " + listResource.status);

                    if(listResource.data != null){
                      //  Testing.printRecipes("data" , listResource.data);
                     switch ((listResource.status)){
                         case LOADING:{
                            if(mRecipeListViewModel.getPageNumbre()>1){
                                mAdapter.displayLoading();
                            }else{
                                mAdapter.displayOnlyLoadig();
                            }
                             break;
                         }
                         case ERROR:{

                             Log.d(TAG, "onChanged: ERROR : couldnt refresh the Data");
                             Log.d(TAG, "onChanged: ERROR messge : " +listResource.message);
                             Log.d(TAG, "onChanged: ERROR # recipe @  : " +listResource.data.size());
                             mAdapter.hidLoading();
                             mAdapter.setLists(listResource.data);
                             Toast.makeText(RecipeListActivity.this, listResource.message, Toast.LENGTH_SHORT).show();
                             if(listResource.message.equals(QUERY_EXUESTED)){
                                 mAdapter.setQueryExhousted();
                             }
                             break;
                         }
                         case SUCCESS:{
                             mAdapter.hidLoading();
                             Log.d(TAG, "onChanged: sucsses : Data has been refreshed ");
                             Log.d(TAG, "onChanged: sucsses # recipe @  : " +listResource.data.size());
                             mAdapter.setLists(listResource.data);
                             break;
                         }
                     }
                    }
                }
            }
        });

        mRecipeListViewModel.getViewstate().observe(this, new Observer<RecipeListViewModel.ViewState>() {
            @Override
            public void onChanged(@Nullable RecipeListViewModel.ViewState viewState) {
                if(viewState != null){
                    switch (viewState){

                        case RECIPES:{
                            // recipes will show automatically from other observer
                            break;
                        }

                        case CATEGORIES:{
                            displaySearchCategories();
                            break;
                        }
                    }
                }
            }
        });
    }

    private void searchRecipeApi(String query){
        mRecyclerView.smoothScrollToPosition(0);
        mRecipeListViewModel.searchRecipesApi(query, 1);
        mSearchView.clearFocus();
    }

    private void initRecyclerView(){
        mAdapter = new RecipeRecyclerAdapter(this);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(30);
        mRecyclerView.addItemDecoration(itemDecorator);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(!mRecyclerView.canScrollVertically(1) &&
                mRecipeListViewModel.getViewstate().getValue() == RecipeListViewModel.ViewState.RECIPES){
                    mRecipeListViewModel.searhNextPage();
                }
            }
        });
    }

    private void initSearchView(){
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchRecipeApi(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    @Override
    public void onRecipeClick(int position) {
        Intent intent = new Intent(this, RecipeActivty.class);
        intent.putExtra("recipe", mAdapter.getSelectedRecipe(position));
        startActivity(intent);
    }

    @Override
    public void onCategoryClick(String category) {
        searchRecipeApi(category);
    }

    private void displaySearchCategories(){
        mAdapter.displayCategory();
    }

    @Override
    public void onBackPressed() {
        if(mRecipeListViewModel.getViewstate().getValue()  == RecipeListViewModel.ViewState.CATEGORIES){
            super.onBackPressed();
        }else {
            mRecipeListViewModel.cancelSearchQyery();
            mRecipeListViewModel.setCategory();
        }

    }
}
