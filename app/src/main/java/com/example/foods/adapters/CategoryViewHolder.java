package com.example.foods.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foods.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    CircleImageView categoryImg;
    TextView categorytitle;
    OnRecipeListener onRecipeListener;
    public CategoryViewHolder(@NonNull View itemView ,OnRecipeListener onRecipeListener) {
        super(itemView);
        this.onRecipeListener = onRecipeListener;
        categoryImg = itemView.findViewById(R.id.category_img);
        categorytitle = itemView.findViewById(R.id.category_title);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        onRecipeListener.onCategoryClick(categorytitle.getText().toString());

    }
}
