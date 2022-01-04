package com.example.foods.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;



@Entity (tableName = "recipes")
public class Recipe implements Parcelable {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "recipe_id" )
    private String recipe_id;
    @ColumnInfo(name = "title" )
    private String title;
    @ColumnInfo(name = "publisher" )
    private String publisher;
    @ColumnInfo(name = "ingredients" )
    private String[] ingredients;
    @ColumnInfo(name = "image_url" )
    private String image_url;
    @ColumnInfo(name = "social_rank" )
    private float social_rank;
    @ColumnInfo(name = "timeStamp" )
    private int timeStamp;

    public Recipe(@NonNull String recipe_id, String title,
                  String publisher, String[] ingredients, String image_url, float social_rank, int timeStamp) {
        this.recipe_id = recipe_id;
        this.title = title;
        this.publisher = publisher;
        this.ingredients = ingredients;
        this.image_url = image_url;
        this.social_rank = social_rank;
        this.timeStamp = timeStamp;
    }

    public Recipe() {
    }


    protected Recipe(Parcel in) {
        recipe_id = in.readString();
        title = in.readString();
        publisher = in.readString();
        ingredients = in.createStringArray();
        image_url = in.readString();
        social_rank = in.readFloat();
        timeStamp = in.readInt();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }

    public String getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(String recipe_id) {
        this.recipe_id = recipe_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public float getSocial_rank() {
        return social_rank;
    }

    public void setSocial_rank(float social_rank) {
        this.social_rank = social_rank;
    }


    public int getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(recipe_id);
        parcel.writeString(title);
        parcel.writeString(publisher);
        parcel.writeStringArray(ingredients);
        parcel.writeString(image_url);
        parcel.writeFloat(social_rank);
        parcel.writeInt(timeStamp);
    }
}
