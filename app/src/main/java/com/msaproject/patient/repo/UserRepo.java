package com.msaproject.patient.repo;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.msaproject.patient.Constants;
import com.msaproject.patient.base.BaseRepo;
import com.msaproject.patient.model.UserModel;
import com.msaproject.patient.model.response.UploadStatusResponse;
import com.msaproject.patient.network.Endpoints;
import com.msaproject.patient.utils.Optional;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public class UserRepo extends BaseRepo {

    @Inject
    public UserRepo() {
    }

    public LiveData<Optional<UserModel>> getUserById(String id) {
        MutableLiveData<Optional<UserModel>> liveData = new MutableLiveData<>();
        Single<Optional<UserModel>> single = firebaseManager
                .getDocumentSnapshot(Endpoints.COLLECTION_USERS, id, UserModel.class);
        disposable.add(single
                .subscribe(liveData::setValue, this::handleError));
        return liveData;
    }

    public LiveData<Boolean> postNewUser(UserModel userModel) {
        Map<String, Object> userHashMap = userModel.getModelMap();
        firebaseManager.useCreatedAtServerTime(userHashMap);
        firebaseManager.useUpdatedAtServerTime(userHashMap);
        MutableLiveData<Boolean> liveData = new MutableLiveData<>();
        Completable completable = firebaseManager
                .setValueToDocument(Endpoints.COLLECTION_USERS, userModel.getId(), userHashMap);
        disposable.add(completable
                .subscribe(() -> liveData.setValue(true), this::handleError));
        return liveData;
    }

    public LiveData<UserModel> updateUserData(UserModel userModel) {
        Map<String, Object> userHashMap = userModel.getModelMap();
        userHashMap.remove(Constants.MAP_KEY_CREATED_AT);
        userHashMap.remove(UserModel.PHONE);
        userHashMap.remove(UserModel.ACCOUNT_TYPE);
        firebaseManager.useUpdatedAtServerTime(userHashMap);
        MutableLiveData<UserModel> liveData = new MutableLiveData<>();
        Completable completable = firebaseManager
                .updateDocument(Endpoints.COLLECTION_USERS, userModel.getId(), userHashMap);
        disposable.add(completable
                .subscribe(() -> liveData.setValue(userModel), this::handleError));
        return liveData;
    }

    public LiveData<UploadStatusResponse> uploadUserPhoto(Uri photoUri) {
        MutableLiveData<UploadStatusResponse> liveData = new MutableLiveData<>();
        Observable<UploadStatusResponse> observable = firebaseManager
                .uploadFileWithProgressObservable(Endpoints.FOLDER_PROFILE_PICTURES, userPref.getId(), Constants.STORAGE_EXTENSION_DEFAULT, photoUri);
        disposable.add(observable.subscribe(liveData::setValue, this::handleError));
        return liveData;
    }

    public LiveData<Boolean> deleteUserPhoto() {
        MutableLiveData<Boolean> liveData = new MutableLiveData<>();
        Completable completable = firebaseManager
                .deleteFileFromStorage(userPref.getUser().getProfilePicLink());
        disposable.add(completable.subscribe(() -> liveData.setValue(true), this::handleError));
        return liveData;
    }
}
