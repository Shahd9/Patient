package com.msaproject.patient.ui.recommendations;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;

import com.msaproject.patient.R;
import com.msaproject.patient.base.BaseFragment;
import com.msaproject.patient.databinding.FragmentRecommendationsBinding;
import com.msaproject.patient.model.UserModel;
import com.msaproject.patient.pref.UserPref;
import com.msaproject.patient.ui.recipe_details.RecipeDetailsActivity;

import javax.inject.Inject;

public class RecommendationsFragment extends BaseFragment<FragmentRecommendationsBinding> {

    @Inject
    UserPref userPref;

    private RecommendationsViewModel viewModel;

    private RecipesAdapter adapter;

    @Override
    protected FragmentRecommendationsBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentRecommendationsBinding.inflate(inflater, container, false);
    }

    @Override
    protected void inject() {
        daggerComponent.inject(this);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(RecommendationsViewModel.class);
    }

    @Override
    protected void onViewCreated() {
        adapter = new RecipesAdapter(model -> startActivity(RecipeDetailsActivity.getRecipeDetailsActivityIntent(requireActivity(), model)));
        viewBinding.rvRecipes.setAdapter(adapter);

        checkUserDisease();
    }

    private void checkUserDisease() {
        UserModel model = userPref.getUser();
        if (model.getDiseaseId() != null)
            viewModel.getUserDisease().observe(getViewLifecycleOwner(), diseaseModel -> getUserRecommendedFood(diseaseModel.getFbId()));
        else {
            viewBinding.iviMessage.setVisibility(View.VISIBLE);
            viewBinding.tviMessage.setVisibility(View.VISIBLE);
            viewBinding.rvRecipes.setVisibility(View.GONE);
            viewBinding.tviMessage.setText(R.string.this_recommendation_service_is_based_on);
        }
    }

    private void getUserRecommendedFood(String diseaseFbId) {
        showLoading();
        viewModel.getDiseaseRecommendations(diseaseFbId).observe(getViewLifecycleOwner(), recipeModels -> {
            hideLoading();
            if(recipeModels.isEmpty()) {
                viewBinding.iviMessage.setVisibility(View.VISIBLE);
                viewBinding.tviMessage.setVisibility(View.VISIBLE);
                viewBinding.rvRecipes.setVisibility(View.GONE);
                viewBinding.tviMessage.setText(R.string.no_food_recommendations);
            }
            else {
                viewBinding.iviMessage.setVisibility(View.GONE);
                viewBinding.tviMessage.setVisibility(View.GONE);
                viewBinding.rvRecipes.setVisibility(View.VISIBLE);
                adapter.addAll(recipeModels);
            }

        });
    }
}
