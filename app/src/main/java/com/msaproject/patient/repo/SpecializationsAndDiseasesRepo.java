package com.msaproject.patient.repo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.msaproject.patient.base.BaseRepo;
import com.msaproject.patient.database.AppDatabase;
import com.msaproject.patient.database.DiseaseDao;
import com.msaproject.patient.database.SpecializationDao;
import com.msaproject.patient.model.DiseaseModel;
import com.msaproject.patient.model.SpecializationModel;
import com.msaproject.patient.network.Endpoints;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;

public class SpecializationsAndDiseasesRepo extends BaseRepo {

    @Inject
    public SpecializationsAndDiseasesRepo() {
    }

    // Firebase

    public LiveData<Boolean> updateSpecializationsAndDiseases() {
        MutableLiveData<Boolean> liveData = new MutableLiveData<>();
        SpecializationDao specializationDao = AppDatabase.getInstance().getSpecializationDao();
        DiseaseDao diseaseDao = AppDatabase.getInstance().getDiseaseDao();
        Completable completable =
                firebaseManager.getDocumentsInCollection(Endpoints.COLLECTION_SPECIALIZATIONS, SpecializationModel.class)
                        .flatMap(specializationModels ->
                                firebaseManager.getDocumentsInCollection(Endpoints.COLLECTION_DISEASES, DiseaseModel.class)
                                        .map(diseaseModels -> {
                                            specializationDao.insertUpdatedSpecializations(specializationModels);
                                            diseaseDao.insertUpdatedDiseases(diseaseModels);
                                            return diseaseModels;
                                        }))
                        .ignoreElement();
        disposable.add(completable
                .subscribe(() -> liveData.setValue(true), this::handleError));
        return liveData;
    }

    // Local Database

    public LiveData<List<DiseaseModel>> getAllDiseases() {
        DiseaseDao diseaseDao = AppDatabase.getInstance().getDiseaseDao();
        return diseaseDao.getAllDiseases();
    }

    public LiveData<DiseaseModel> getDiseaseById(String diseaseId) {
        DiseaseDao diseaseDao = AppDatabase.getInstance().getDiseaseDao();
        return diseaseDao.getDiseaseById(diseaseId);
    }

    public LiveData<SpecializationModel> getSpecializationById(String specializationId) {
        SpecializationDao specializationDao = AppDatabase.getInstance().getSpecializationDao();
        return specializationDao.getSpecializationById(specializationId);
    }
}
