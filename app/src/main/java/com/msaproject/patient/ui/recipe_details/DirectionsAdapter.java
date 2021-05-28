package com.msaproject.patient.ui.recipe_details;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.msaproject.patient.databinding.ItemCookingDirectionBinding;

import java.util.List;

public class DirectionsAdapter extends RecyclerView.Adapter<DirectionsAdapter.ViewHolder> {

    private final List<String> directions;

    public DirectionsAdapter(List<String> directions) {
        this.directions = directions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemCookingDirectionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(directions.get(position));
    }

    @Override
    public int getItemCount() {
        return directions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemCookingDirectionBinding viewBinding;

        public ViewHolder(ItemCookingDirectionBinding viewBinding) {
            super(viewBinding.getRoot());
            this.viewBinding = viewBinding;
        }

        public void bind(String direction) {
            viewBinding.tviDirectionTitle.setText(direction);

        }
    }
}