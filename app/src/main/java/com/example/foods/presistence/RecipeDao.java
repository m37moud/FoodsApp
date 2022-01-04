package com.example.foods.presistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.foods.models.Recipe;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;
import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface RecipeDao {
    @Insert(onConflict = IGNORE)
    Long[] insertRicepes(Recipe... recipes);
    @Insert(onConflict = REPLACE)
    void insertRecipe(Recipe recipe);

    @Query("UPDATE recipes SET title = :title ,publisher = :publisher , image_url = :image_url , social_rank = :social_rank " +
    "WHERE recipe_id = :recipe_id")//end of query
    void update(String recipe_id, String title, String publisher,String image_url,
                float social_rank);
    @Query("SELECT  * from recipes where title like  '%' || :query || '%' or ingredients like '%' || :query || '%'"+
            "order by social_rank DESC LIMIT (:pageNumber * 30) "   )//end of query

    LiveData<List<Recipe>> searchRecipes(String query , int pageNumber);
//
@Query("SELECT * FROM recipes WHERE recipe_id = :recipe_id")
LiveData<Recipe> getRecipe(String recipe_id);

}
