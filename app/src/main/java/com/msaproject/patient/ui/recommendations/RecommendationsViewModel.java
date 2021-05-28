package com.msaproject.patient.ui.recommendations;

import androidx.lifecycle.LiveData;

import com.msaproject.patient.base.BaseViewModel;
import com.msaproject.patient.model.DiseaseModel;
import com.msaproject.patient.model.ErrorModel;
import com.msaproject.patient.model.RecipeModel;
import com.msaproject.patient.pref.UserPref;
import com.msaproject.patient.repo.RecommendationsRepo;
import com.msaproject.patient.repo.SpecializationsAndDiseasesRepo;

import java.util.List;

import javax.inject.Inject;

public class RecommendationsViewModel extends BaseViewModel {

    @Inject
    RecommendationsRepo recommendationsRepo;

    @Inject
    SpecializationsAndDiseasesRepo specializationsAndDiseasesRepo;

    @Inject
    UserPref userPref;

    @Inject
    public RecommendationsViewModel() {
    }

    @Override
    protected LiveData<ErrorModel> getErrorLiveData() {
        addErrorObservers(recommendationsRepo);
        addErrorObservers(specializationsAndDiseasesRepo);
        return super.getErrorLiveData();
    }

    LiveData<DiseaseModel> getUserDisease(){
        return specializationsAndDiseasesRepo.getDiseaseById(userPref.getUser().getDiseaseId());
    }

    LiveData<List<RecipeModel>> getDiseaseRecommendations(String diseaseFbId) {
        return recommendationsRepo.getDiseaseRecommendations(diseaseFbId);
    }

    @Override
    protected void onCleared() {
        recommendationsRepo.dispose();
        specializationsAndDiseasesRepo.dispose();
        super.onCleared();
    }
}
