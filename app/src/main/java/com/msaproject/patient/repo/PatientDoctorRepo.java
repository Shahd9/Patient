package com.msaproject.patient.repo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.msaproject.patient.Constants;
import com.msaproject.patient.base.BaseRepo;
import com.msaproject.patient.model.PatientDoctorModel;
import com.msaproject.patient.model.UserModel;
import com.msaproject.patient.network.Endpoints;
import com.msaproject.patient.network.QueryBuilder;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class PatientDoctorRepo extends BaseRepo {

    @Inject
    public PatientDoctorRepo() {
    }

    public LiveData<List<PatientDoctorModel>> getPatientDoctors() {
        MutableLiveData<List<PatientDoctorModel>> liveData = new MutableLiveData<>();
        QueryBuilder queryBuilder = new QueryBuilder(firebaseManager, Endpoints.COLLECTION_PATIENT_DOCTOR)
                .addOperationToQuery(PatientDoctorModel.PATIENT_ID, QueryBuilder.Operators.OPERATOR_EQUAL, userPref.getId())
                .addOrderingToQuery(Constants.MAP_KEY_UPDATED_AT, QueryBuilder.OrderingDirections.DIRECTION_DESC);

        Single<List<PatientDoctorModel>> single =
                firebaseManager.getDocumentsByQuery(queryBuilder, PatientDoctorModel.class)
                        .flattenAsObservable(patientDoctorModels -> patientDoctorModels)
                        .flatMap(patientDoctorModel ->
                                firebaseManager.getDocumentSnapshot(Endpoints.COLLECTION_USERS, patientDoctorModel.getDoctorId(), UserModel.class)
                                        .toObservable()
                                        .map(userModelOptional -> {
                                            patientDoctorModel.setDoctorModel(userModelOptional.orElse(null));
                                            return patientDoctorModel;
                                        }))
                        .toList();

        disposable.add(single
                .subscribe(liveData::setValue, this::handleError));
        return liveData;
    }
}