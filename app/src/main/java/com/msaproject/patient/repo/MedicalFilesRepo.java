package com.msaproject.patient.repo;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.msaproject.patient.Constants;
import com.msaproject.patient.base.BaseRepo;
import com.msaproject.patient.model.MedicalFileModel;
import com.msaproject.patient.model.response.UploadStatusResponse;
import com.msaproject.patient.network.Endpoints;
import com.msaproject.patient.network.QueryBuilder;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public class MedicalFilesRepo extends BaseRepo {

    @Inject
    public MedicalFilesRepo() {
    }

    public LiveData<List<MedicalFileModel>> getUserMedicalFiles() {
        MutableLiveData<List<MedicalFileModel>> liveData = new MutableLiveData<>();
        QueryBuilder queryBuilder = new QueryBuilder(firebaseManager, Endpoints.COLLECTION_MEDICAL_FILES)
                .addOperationToQuery(MedicalFileModel.PATIENT_ID, QueryBuilder.Operators.OPERATOR_EQUAL, userPref.getId())
                .addOrderingToQuery(Constants.MAP_KEY_CREATED_AT, QueryBuilder.OrderingDirections.DIRECTION_DESC);
        Single<List<MedicalFileModel>> single =
                firebaseManager.getDocumentsByQuery(queryBuilder, MedicalFileModel.class);
        disposable.add(single
                .subscribe(liveData::setValue, this::handleError));
        return liveData;
    }

    public LiveData<Boolean> postNewMedicalFile(MedicalFileModel medicalFileModel) {
        Map<String, Object> medicalFileHashMap = medicalFileModel.getModelMap();
        firebaseManager.useCreatedAtServerTime(medicalFileHashMap);
        MutableLiveData<Boolean> liveData = new MutableLiveData<>();
        Completable completable = firebaseManager
                .setValueToDocument(Endpoints.COLLECTION_MEDICAL_FILES, medicalFileModel.getId(), medicalFileHashMap);
        disposable.add(completable
                .subscribe(() -> liveData.setValue(true), this::handleError));
        return liveData;
    }

    public LiveData<UploadStatusResponse> uploadMedicalFilePhoto(String photoId, Uri photoUri) {
        MutableLiveData<UploadStatusResponse> liveData = new MutableLiveData<>();
        Observable<UploadStatusResponse> observable = firebaseManager
                .uploadFileWithProgressObservable(Endpoints.FOLDER_MEDICAL_FILES, photoId, Constants.STORAGE_EXTENSION_DEFAULT, photoUri);
        disposable.add(observable.subscribe(liveData::setValue, this::handleError));
        return liveData;
    }

    public LiveData<Boolean> deleteMedicalFile(MedicalFileModel medicalFileModel) {
        MutableLiveData<Boolean> liveData = new MutableLiveData<>();
        Completable completable = firebaseManager
                .deleteFileFromStorage(medicalFileModel.getDownloadLink())
                .concatWith(firebaseManager.deleteDocument(Endpoints.COLLECTION_MEDICAL_FILES, medicalFileModel.getId()));
        disposable.add(completable.subscribe(() -> liveData.setValue(true), this::handleError));
        return liveData;
    }
}
