package com.example.foods.presistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.foods.models.Recipe;

@Database(entities = {Recipe.class} , version = 1 )
@TypeConverters({Converters.class})
public abstract class RecipeDatabase extends RoomDatabase {
    private static RecipeDatabase instance;
    private final static String DATABASE_NAME = "recipe_db";

    public static RecipeDatabase getInstance(final Context context){
        if(instance == null){
            instance =  Room.databaseBuilder(context.getApplicationContext(),
                    RecipeDatabase.class,
                    DATABASE_NAME).build();
        }
        return instance;
    }

    public abstract RecipeDao getRecipeDao();

}
