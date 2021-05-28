package com.msaproject.patient.ui.recipe_details;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.msaproject.patient.databinding.ItemIngredientBinding;

import java.util.List;

public class IngredientsAdapter  extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {

    private final List<String> ingredients;

    public IngredientsAdapter(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemIngredientBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(ingredients.get(position));
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemIngredientBinding viewBinding;

        public ViewHolder(ItemIngredientBinding viewBinding) {
            super(viewBinding.getRoot());
            this.viewBinding = viewBinding;
        }

        public void bind(String ingredient) {
            viewBinding.tvName.setText(ingredient);

        }
    }
}