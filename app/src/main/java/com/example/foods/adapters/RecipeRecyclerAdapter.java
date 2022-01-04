package com.example.foods.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.foods.R;
import com.example.foods.models.Recipe;
import com.example.foods.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int RECIPE_TYPE =1;
    private static final int LOADING_TYPE =2;
    private static final int CATEGORY_TYPE =3;
    private static final int Exhausted_TYPE =4;

    private List<Recipe> mRecipes;
    private OnRecipeListener onRecipeListener;

    public RecipeRecyclerAdapter( OnRecipeListener onRecipeListener) {
        this.onRecipeListener = onRecipeListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType){
            case LOADING_TYPE :{
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_loading_list_item,parent,false);
                return new LoadingViewHolder(view);
            }
            case Exhausted_TYPE :{
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_search_exhausted,parent,false);
                return new ExhaustedViewHolder(view);
            }
            case RECIPE_TYPE:{
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recipe_list_item,parent,false);
                return new RecipeViewHolder(view,onRecipeListener);
            }case CATEGORY_TYPE:{
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_category_list_item,parent,false);
                return new CategoryViewHolder(view,onRecipeListener);
            }
            default:{
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recipe_list_item,parent,false);
                return new RecipeViewHolder(view,onRecipeListener);
            }

        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        int itemType = getItemViewType(position);
        if(itemType == RECIPE_TYPE){
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background);

            Glide.with(holder.itemView.getContext())
                    .applyDefaultRequestOptions(requestOptions)
                    .load(mRecipes.get(position).getImage_url())
                    .into(((RecipeViewHolder) holder).image);

            ((RecipeViewHolder) holder).title.setText(mRecipes.get(position).getTitle());
            ((RecipeViewHolder) holder).publisher.setText(mRecipes.get(position).getPublisher());
            ((RecipeViewHolder) holder).socialScore.setText(String.valueOf(Math.round(mRecipes.get(position).getSocial_rank())));
        }else if(itemType == CATEGORY_TYPE){
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background);

            Uri path = Uri.parse("android.resource://com.example.foods/drawable/"+mRecipes.get(position).getImage_url());
            Glide.with(holder.itemView.getContext())
                    .applyDefaultRequestOptions(requestOptions)
                    .load(path)
                    .into(((CategoryViewHolder) holder).categoryImg);

            ((CategoryViewHolder) holder).categorytitle.setText(mRecipes.get(position).getTitle());
        }

    }

    @Override
    public int getItemViewType(int position) {
        if(mRecipes.get(position).getSocial_rank() == -1)
        {
            return CATEGORY_TYPE;
        }
        else if(mRecipes.get(position).getTitle().equals("LOADING...")){
            return LOADING_TYPE;
        }
        else if(mRecipes.get(position).getTitle().equals("EXOUSTED...")){
            return Exhausted_TYPE;
        }else
            return RECIPE_TYPE;
    }

    public void setQueryExhousted()
    {
        hidLoading();
        Recipe recipeExousted = new Recipe();
        recipeExousted.setTitle("EXOUSTED...");
        mRecipes.add(recipeExousted);
        notifyDataSetChanged();
    }
    public void hidLoading(){
        if(isLoading()){
            if(mRecipes.get(0).getTitle().equals("LOADING...")){
                mRecipes.remove(0);
            }else if(mRecipes.get(mRecipes.size()-1).equals("LOADING...")){
                mRecipes.remove(mRecipes.size()-1);
            }
            notifyDataSetChanged();

        }
    }
//display loading in pagination
    public void displayLoading()
    {

        if(mRecipes == null){
            mRecipes= new ArrayList<>();
        }

        if(!isLoading()){

            Recipe recipe = new Recipe();
            recipe.setTitle("LOADING...");
            mRecipes.add(recipe);
            notifyDataSetChanged();
        }

    }
    //display first loading in query search
    public void displayOnlyLoadig(){
        clearLoading();
        Recipe recipe = new Recipe();
        recipe.setTitle("LOADING...");
        mRecipes.add(recipe);
        notifyDataSetChanged();
    }

    private void clearLoading(){
        if(mRecipes == null){
            mRecipes= new ArrayList<>();
        }else {
            mRecipes.clear();
        }
        notifyDataSetChanged();
    }
    private boolean isLoading(){

        if(mRecipes!=null){
            if(mRecipes.size() > 0)
            {
                if(mRecipes.get(mRecipes.size() -1).equals("LOADING...")){
                    return true;
                }
            }
        }
        return  false;
    }
    public void displayCategory(){
        List<Recipe> list = new ArrayList<>();
        for(int i = 0 ; i< Constants.DEFAULT_SEARCH_CATEGORIES.length ; i ++){
            Recipe recipe = new Recipe();
            recipe.setTitle(Constants.DEFAULT_SEARCH_CATEGORIES[i]);
            recipe.setImage_url(Constants.DEFAULT_SEARCH_CATEGORY_IMAGES[i]);
            recipe.setSocial_rank(-1);
            list.add(recipe);
        }
        mRecipes=list;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        if(mRecipes != null){
            return mRecipes.size();
        }
        return 0;

    }
    public void setLists(List<Recipe> lists){
        mRecipes=lists;
        notifyDataSetChanged();
    }
    public Recipe getSelectedRecipe(int position){
        if(mRecipes != null){
            if(mRecipes.size() > 0){
                return mRecipes.get(position);
            }
        }
        return null;
    }


}
