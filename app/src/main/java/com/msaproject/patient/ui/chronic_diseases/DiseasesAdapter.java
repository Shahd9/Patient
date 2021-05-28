package com.msaproject.patient.ui.chronic_diseases;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.msaproject.patient.databinding.ItemDiseaseSelectionBinding;
import com.msaproject.patient.model.DiseaseModel;

import java.util.List;
import java.util.Objects;

public class DiseasesAdapter extends RecyclerView.Adapter<DiseasesAdapter.ViewHolder> {

    @Nullable
    private String selectedId;
    private final List<DiseaseModel> diseaseModels;

    public DiseasesAdapter(@Nullable String selectedId, List<DiseaseModel> diseaseModels) {
        this.selectedId = selectedId;
        this.diseaseModels = diseaseModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemDiseaseSelectionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(diseaseModels.get(position));
    }

    @Override
    public int getItemCount() {
        return diseaseModels.size();
    }

    @Nullable
    public String getSelectedId() {
        return selectedId;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemDiseaseSelectionBinding viewBinding;

        public ViewHolder(ItemDiseaseSelectionBinding viewBinding) {
            super(viewBinding.getRoot());
            this.viewBinding = viewBinding;
        }

        public void bind(DiseaseModel model) {
            viewBinding.tvSpecialization.setText(model.getName());

            if (selectedId != null && selectedId.equals(model.getId())) {
                viewBinding.tvSpecialization.setTypeface(null, Typeface.BOLD);
                viewBinding.ivIsSelected.setVisibility(View.VISIBLE);
            } else {
                viewBinding.tvSpecialization.setTypeface(null, Typeface.NORMAL);
                viewBinding.ivIsSelected.setVisibility(View.INVISIBLE);
            }

            itemView.setOnClickListener(v -> {
                selectedId = (Objects.equals(selectedId, model.getId())) ? null : model.getId();
                notifyDataSetChanged();
            });
        }
    }
}