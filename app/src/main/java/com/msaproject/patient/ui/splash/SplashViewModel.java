package com.msaproject.patient.ui.splash;

import androidx.lifecycle.LiveData;

import com.msaproject.patient.base.BaseViewModel;
import com.msaproject.patient.model.ConfigsModel;
import com.msaproject.patient.model.ErrorModel;
import com.msaproject.patient.repo.ConfigsRepo;
import com.msaproject.patient.repo.SpecializationsAndDiseasesRepo;

import javax.inject.Inject;

public class SplashViewModel extends BaseViewModel {

    @Inject
    ConfigsRepo configsRepo;
    @Inject
    SpecializationsAndDiseasesRepo specializationsAndDiseasesRepo;

    @Inject
    public SplashViewModel() {
    }

    @Override
    protected LiveData<ErrorModel> getErrorLiveData() {
        addErrorObservers(configsRepo);
        addErrorObservers(specializationsAndDiseasesRepo);
        return super.getErrorLiveData();
    }

    LiveData<ConfigsModel> getConfigs(){
        return configsRepo.getConfigs();
    }

    LiveData<Boolean> updateDiseasesAndSpecializations(){
        return specializationsAndDiseasesRepo.updateSpecializationsAndDiseases();
    }

    @Override
    protected void onCleared() {
        configsRepo.dispose();
        specializationsAndDiseasesRepo.dispose();
        super.onCleared();
    }
}
