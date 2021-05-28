package com.msaproject.patient.ui.profile;

import androidx.lifecycle.LiveData;

import com.msaproject.patient.base.BaseViewModel;
import com.msaproject.patient.model.ErrorModel;

import javax.inject.Inject;

public class ProfileViewModel extends BaseViewModel {

    @Inject
    public ProfileViewModel() {
    }


    @Override
    protected LiveData<ErrorModel> getErrorLiveData() {
        return super.getErrorLiveData();
    }

    @Override
    protected void onCleared() {
        super.onCleared();

    }

}
