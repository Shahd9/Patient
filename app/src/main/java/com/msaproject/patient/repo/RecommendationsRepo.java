package com.msaproject.patient.repo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.msaproject.patient.base.BaseRepo;
import com.msaproject.patient.model.RecipeModel;
import com.msaproject.patient.model.response.RecommendationResponseModel;
import com.msaproject.patient.network.Endpoints;
import com.msaproject.patient.utils.Optional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Single;

public class RecommendationsRepo extends BaseRepo {

    @Inject
    public RecommendationsRepo() {
    }

    public LiveData<List<RecipeModel>> getDiseaseRecommendations(String diseaseFbId) {
        MutableLiveData<List<RecipeModel>> liveData = new MutableLiveData<>();
        Map<String, Object> query = new HashMap<>();
        query.put("disease_user_id", diseaseFbId);
        Single<List<RecipeModel>> single =
                networkManager.getRequest(Endpoints.RECOMMENDATION_API, query, RecommendationResponseModel.class)
                        .map(RecommendationResponseModel::getData)
                        .flatMapIterable(recipeIds -> recipeIds)
                        .flatMap(recipeId ->
                                firebaseManager.getSingleEventReferenceSnapshot(Endpoints.REFERENCE_RECIPES.replace("X", recipeId), RecipeModel.class)
                                        .toObservable())
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .toList();
        disposable.add(single
                .subscribe(liveData::setValue, this::handleError));
        return liveData;
    }
}
