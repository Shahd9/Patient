package com.msaproject.patient.ui.chronic_diseases;

import androidx.lifecycle.LiveData;

import com.msaproject.patient.base.BaseViewModel;
import com.msaproject.patient.model.DiseaseModel;
import com.msaproject.patient.model.ErrorModel;
import com.msaproject.patient.model.UserModel;
import com.msaproject.patient.pref.UserPref;
import com.msaproject.patient.repo.SpecializationsAndDiseasesRepo;
import com.msaproject.patient.repo.UserRepo;

import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;

public class ChronicDiseasesViewModel extends BaseViewModel {

    @Inject
    UserPref userPref;

    @Inject
    SpecializationsAndDiseasesRepo specializationsAndDiseasesRepo;

    @Inject
    UserRepo userRepo;

    @Inject
    public ChronicDiseasesViewModel() {
    }

    @Override
    protected LiveData<ErrorModel> getErrorLiveData() {
        addErrorObservers(specializationsAndDiseasesRepo);
        addErrorObservers(userRepo);
        return super.getErrorLiveData();
    }

    LiveData<List<DiseaseModel>> getAllDiseases() {
        return specializationsAndDiseasesRepo.getAllDiseases();
    }

    @Nullable
    String getUserChronicDiseaseId() {
        return userPref.getUser().getDiseaseId();
    }

    LiveData<UserModel> updateUserChronicDisease(@Nullable String diseaseModelId) {
        view.showLoading();
        final UserModel userModel = userPref.getUser();
        userModel.setDiseaseId(diseaseModelId);
        return userRepo.updateUserData(userModel);
    }

    @Override
    protected void onCleared() {
        specializationsAndDiseasesRepo.dispose();
        userRepo.dispose();
        super.onCleared();
    }
}
