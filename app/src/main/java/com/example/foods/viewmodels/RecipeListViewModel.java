package com.example.foods.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.foods.models.Recipe;
import com.example.foods.repostories.RecipeRepository;
import com.example.foods.util.Resource;

import java.util.List;

public class RecipeListViewModel extends AndroidViewModel {

    private static final String TAG = "RecipeListViewModel";
    public static final String QUERY_EXUESTED = " no recipes to view ";

    public enum ViewState {CATEGORIES, RECIPES}

    private MutableLiveData<ViewState> viewState;
    private RecipeRepository recipeRepository;
    private MediatorLiveData<Resource<List<Recipe>>> recipes = new MediatorLiveData<>();

    //extra query
    private boolean isExusted;
    private boolean isPerformingQuery;
    private int pageNumbre;
    private String query;
    private boolean cancelSearchQuery;


    public RecipeListViewModel(@NonNull Application application) {
        super(application);
        recipeRepository = RecipeRepository.getInstance(application);
        init();
    }

    private void init(){
        if(viewState == null){
            viewState = new MutableLiveData<>();
            viewState.setValue(ViewState.CATEGORIES);
        }
    }

    public LiveData<ViewState> getViewstate(){
        return viewState;
    }

    public LiveData<Resource<List<Recipe>>> getRecipes(){
        return recipes;
    }

    public void searchRecipesApi(String query, int pageNumber){
        if(pageNumber == 0){
            pageNumber = 1;
        }
        this.pageNumbre=pageNumber;
        this.query=query;
        executeSearchQuery();

    }
    public void searhNextPage(){
        if(!isPerformingQuery&&!isExusted){
            pageNumbre++;
            executeSearchQuery();
        }
    }

    private void executeSearchQuery(){
        cancelSearchQuery = false;
        isPerformingQuery = true;
        viewState.setValue(ViewState.RECIPES);
        final LiveData<Resource<List<Recipe>>> repositorySource = recipeRepository.searchRecipeApi(query, pageNumbre);
        recipes.addSource(repositorySource, new Observer<Resource<List<Recipe>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<Recipe>> listResource) {
                if(!cancelSearchQuery){

                    if(listResource != null){
                        recipes.setValue(listResource);
                        if(listResource.status == Resource.Status.SUCCESS){
                            isPerformingQuery = false;
                            if(listResource.data.size() == 0 ){
                                recipes.setValue(new Resource<List<Recipe>>(Resource.Status.ERROR,
                                        listResource.data,
                                        QUERY_EXUESTED));
                               // isExusted = true;
                            }
                            recipes.removeSource(repositorySource);
                        }
                        else if(listResource.status == Resource.Status.ERROR){
                            isPerformingQuery = false;

                            recipes.removeSource(repositorySource);
                        }

                    }else{
                        recipes.removeSource(repositorySource);
                    }
                }else {
                    recipes.removeSource(repositorySource);
                }
            }
        });

    }

    public void setCategory(){
        viewState.setValue(ViewState.CATEGORIES);
    }

    public int getPageNumbre(){
        return pageNumbre;
    }
    
    public void cancelSearchQyery(){
        if(isPerformingQuery){
            Log.d(TAG, "cancelSearchQyery: canceling search query ...");
            cancelSearchQuery = true;
            isPerformingQuery = false;
            pageNumbre = 1;
        }
    }


}
