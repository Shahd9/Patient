package com.msaproject.patient.ui.doctor_details;

import androidx.lifecycle.LiveData;

import com.msaproject.patient.base.BaseViewModel;
import com.msaproject.patient.model.ErrorModel;
import com.msaproject.patient.model.SpecializationModel;
import com.msaproject.patient.repo.SpecializationsAndDiseasesRepo;

import javax.inject.Inject;

public class DoctorDetailsViewModel extends BaseViewModel {

    @Inject
    SpecializationsAndDiseasesRepo specializationsAndDiseasesRepo;

    @Inject
    public DoctorDetailsViewModel() {
    }

    @Override
    protected LiveData<ErrorModel> getErrorLiveData() {
        addErrorObservers(specializationsAndDiseasesRepo);
        return super.getErrorLiveData();
    }

    LiveData<SpecializationModel> getSpecializationById(String specializationId) {
        return specializationsAndDiseasesRepo.getSpecializationById(specializationId);
    }

    @Override
    protected void onCleared() {
        specializationsAndDiseasesRepo.dispose();
        super.onCleared();
    }
}
