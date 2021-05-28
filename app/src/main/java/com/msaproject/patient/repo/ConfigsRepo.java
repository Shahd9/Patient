package com.msaproject.patient.repo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.msaproject.patient.base.BaseRepo;
import com.msaproject.patient.model.ConfigsModel;
import com.msaproject.patient.network.Endpoints;
import com.msaproject.patient.utils.Optional;

import javax.inject.Inject;

import io.reactivex.Single;

public class ConfigsRepo extends BaseRepo {

    @Inject
    public ConfigsRepo() {
    }

    public LiveData<ConfigsModel> getConfigs() {
        MutableLiveData<ConfigsModel> liveData = new MutableLiveData<>();
        Single<ConfigsModel> single = firebaseManager
                .getDocumentSnapshot(Endpoints.COLLECTION_STATICS, Endpoints.DOCUMENT_APP_CURRENT_CONFIGS, ConfigsModel.class)
                .map(Optional::get);
        disposable.add(single
                .subscribe(liveData::setValue, this::handleError));
        return liveData;
    }
}
