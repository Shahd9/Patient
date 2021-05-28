package com.msaproject.patient.ui.appointments;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.msaproject.patient.base.BaseViewModel;
import com.msaproject.patient.model.AppointmentImageModel;
import com.msaproject.patient.model.AppointmentModel;
import com.msaproject.patient.model.ErrorModel;
import com.msaproject.patient.repo.AppointmentsRepo;

import java.util.List;

import javax.inject.Inject;

public class AppointmentsViewModel extends BaseViewModel {

    @Inject
    AppointmentsRepo appointmentsRepo;

    @Inject
    public AppointmentsViewModel() {
    }

    @Override
    public LiveData<ErrorModel> getErrorLiveData() {
        addErrorObservers(appointmentsRepo);
        return super.getErrorLiveData();
    }

    public LiveData<List<AppointmentModel>> getAppointmentsList(String doctorId) {
        return appointmentsRepo.getAppointmentsList(doctorId);
    }

    public LiveData<List<AppointmentImageModel>> getAppointmentImages(String appointmentId) {
        return appointmentsRepo.getAppointmentImages(appointmentId);
    }

    @Override
    protected void onCleared() {
        appointmentsRepo.dispose();
        super.onCleared();
    }
}
