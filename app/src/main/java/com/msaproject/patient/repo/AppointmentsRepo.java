package com.msaproject.patient.repo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.msaproject.patient.Constants;
import com.msaproject.patient.base.BaseRepo;
import com.msaproject.patient.model.AppointmentImageModel;
import com.msaproject.patient.model.AppointmentModel;
import com.msaproject.patient.network.Endpoints;
import com.msaproject.patient.network.QueryBuilder;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class AppointmentsRepo extends BaseRepo {

    @Inject
    public AppointmentsRepo() {
    }

    public LiveData<List<AppointmentModel>> getAppointmentsList(String doctorId) {
        MutableLiveData<List<AppointmentModel>> liveData = new MutableLiveData<>();
        QueryBuilder queryBuilder = new QueryBuilder(firebaseManager, Endpoints.COLLECTION_APPOINTMENTS)
                .addOperationToQuery(AppointmentModel.DOCTOR_ID, QueryBuilder.Operators.OPERATOR_EQUAL, doctorId)
                .addOperationToQuery(AppointmentModel.PATIENT_ID, QueryBuilder.Operators.OPERATOR_EQUAL, userPref.getId())
                .addOrderingToQuery(Constants.MAP_KEY_CREATED_AT, QueryBuilder.OrderingDirections.DIRECTION_DESC);
        Single<List<AppointmentModel>> single =
                firebaseManager.getDocumentsByQuery(queryBuilder, AppointmentModel.class);
        disposable.add(single
                .subscribe(liveData::setValue, this::handleError));
        return liveData;
    }

    public LiveData<List<AppointmentImageModel>> getAppointmentImages(String appointmentId) {
        MutableLiveData<List<AppointmentImageModel>> liveData = new MutableLiveData<>();
        QueryBuilder queryBuilder = new QueryBuilder(firebaseManager, Endpoints.COLLECTION_APPOINTMENT_IMAGES)
                .addOperationToQuery(AppointmentImageModel.APPOINTMENT_ID, QueryBuilder.Operators.OPERATOR_EQUAL, appointmentId)
                .addOrderingToQuery(Constants.MAP_KEY_CREATED_AT, QueryBuilder.OrderingDirections.DIRECTION_DESC);
        Single<List<AppointmentImageModel>> single =
                firebaseManager.getDocumentsByQuery(queryBuilder, AppointmentImageModel.class);
        disposable.add(single
                .subscribe(liveData::setValue, this::handleError));
        return liveData;
    }
}
