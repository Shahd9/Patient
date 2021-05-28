package com.msaproject.patient.ui.recipe_details;

import android.content.Context;
import android.content.Intent;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.msaproject.patient.R;
import com.msaproject.patient.base.BaseActivity;
import com.msaproject.patient.databinding.ActivityRecipeDetailsBinding;
import com.msaproject.patient.model.RecipeModel;
import com.msaproject.patient.utils.PicassoHelper;
import com.msaproject.patient.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class RecipeDetailsActivity extends BaseActivity<ActivityRecipeDetailsBinding> {

    private final static String RECIPE_MODEL = "RecipeModel";

    public static Intent getRecipeDetailsActivityIntent(Context context, RecipeModel recipeModel) {
        Intent intent = new Intent(context, RecipeDetailsActivity.class);
        intent.putExtra(RECIPE_MODEL, recipeModel);
        return intent;
    }

    private RecipeModel model = null;

    @Override
    protected ActivityRecipeDetailsBinding getViewBinding() {
        return ActivityRecipeDetailsBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void inject() {
        daggerComponent.inject(this);
    }

    @Override
    protected void onViewCreated() {
        model = (RecipeModel) getIntent().getSerializableExtra(RECIPE_MODEL);

        viewBinding.toolbar.setNavigationOnClickListener(v -> finish());

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        viewBinding.rvIngredients.setLayoutManager(layoutManager);

        setUIData();
    }

    private void setUIData() {
        viewBinding.tvName.setText(model.getName());

        viewBinding.ratingBar.setRating(model.getRate());

        viewBinding.tvAvgRating.setText(String.format(Locale.US, "%.1f", model.getRate()));

        viewBinding.tvNoOfReviews.setText(StringUtils.getString(R.string.no_of_reviews, model.getReviews()));

        viewBinding.tvCookingTime.setText(StringUtils.getString(R.string.minutes_count, new Random().nextInt(16) + 30));

        PicassoHelper.loadImageWithCache(model.getImage(), viewBinding.ivImage, PicassoHelper.MODE.FIT_AND_CENTER_CROP, null, null, null);

        fillIngredientsRV();

        fillDirectionsRV();
    }

    private void fillIngredientsRV(){
        List<String> ingredientsList = new ArrayList<>(Arrays.asList(model.getIngredients().split("\\^")));
        IngredientsAdapter adapter = new IngredientsAdapter(ingredientsList);
        viewBinding.rvIngredients.setAdapter(adapter);
    }

    private void fillDirectionsRV(){
        List<String> directions = new ArrayList<>(Arrays.asList(model.getDirections().split("\n")));
        DirectionsAdapter adapter = new DirectionsAdapter(directions.subList(directions.size() > 6 ? 6 : 0, directions.size() - 1));
        viewBinding.rvDirections.setAdapter(adapter);
    }
}