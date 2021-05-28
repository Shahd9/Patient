package com.msaproject.patient.ui.recommendations;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.msaproject.patient.R;
import com.msaproject.patient.base.BaseRecyclerAdapter;
import com.msaproject.patient.base.ItemClickListener;
import com.msaproject.patient.databinding.ItemRecipeBinding;
import com.msaproject.patient.model.RecipeModel;
import com.msaproject.patient.utils.PicassoHelper;
import com.msaproject.patient.utils.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder>
        implements BaseRecyclerAdapter<RecipeModel> {

    private final ItemClickListener<RecipeModel> itemClickListener;

    @NonNull
    private final ArrayList<RecipeModel> list;

    public RecipesAdapter(ItemClickListener<RecipeModel> itemClickListener) {
        list = new ArrayList<>();
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemRecipeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void clear(boolean notifyDataSetChanged) {
        list.clear();
        if (notifyDataSetChanged)
            notifyDataSetChanged();
    }

    @Override
    public void add(RecipeModel item) {
        list.add(item);
        notifyDataSetChanged();
    }

    @Override
    public void addAll(List<RecipeModel> items) {
        Collections.shuffle(items);
        list.clear();
        list.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemRecipeBinding viewBinding;

        ViewHolder(ItemRecipeBinding viewBinding) {
            super(viewBinding.getRoot());
            this.viewBinding = viewBinding;
        }

        private void bind(int position) {
            RecipeModel model = list.get(position);

            viewBinding.getRoot().setOnClickListener(v -> itemClickListener.onItemClicked(model));

            viewBinding.tvName.setText(model.getName());

            viewBinding.ratingBar.setRating(model.getRate());

            viewBinding.tvAvgRating.setText(String.format(Locale.US, "%.1f", model.getRate()));

            viewBinding.tvNoOfReviews.setText(StringUtils.getString(R.string.no_of_reviews, model.getReviews()));

            viewBinding.tvCookingTime.setText(StringUtils.getString(R.string.minutes_count, new Random().nextInt(16) + 30));

            PicassoHelper.loadImageWithCache(model.getImage(), viewBinding.ivImage, PicassoHelper.MODE.FIT_AND_CENTER_CROP, null, null, null);
        }
    }
}