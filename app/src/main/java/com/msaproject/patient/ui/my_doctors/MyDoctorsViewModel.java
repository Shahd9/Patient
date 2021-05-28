package com.msaproject.patient.ui.my_doctors;

import androidx.lifecycle.LiveData;

import com.msaproject.patient.base.BaseViewModel;
import com.msaproject.patient.model.ErrorModel;
import com.msaproject.patient.model.PatientDoctorModel;
import com.msaproject.patient.repo.PatientDoctorRepo;

import java.util.List;

import javax.inject.Inject;

public class MyDoctorsViewModel extends BaseViewModel {

    @Inject
    PatientDoctorRepo patientDoctorRepo;

    @Inject
    public MyDoctorsViewModel() {
    }

    @Override
    protected LiveData<ErrorModel> getErrorLiveData() {
        addErrorObservers(patientDoctorRepo);
        return super.getErrorLiveData();
    }

    LiveData<List<PatientDoctorModel>> getPatientDoctors() {
        return patientDoctorRepo.getPatientDoctors();
    }

    @Override
    protected void onCleared() {
        patientDoctorRepo.dispose();
        super.onCleared();
    }
}
