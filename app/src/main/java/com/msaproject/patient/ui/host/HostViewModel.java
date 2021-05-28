package com.msaproject.patient.ui.host;

import androidx.lifecycle.LiveData;

import com.msaproject.patient.base.BaseViewModel;
import com.msaproject.patient.model.ErrorModel;

import javax.inject.Inject;

public class HostViewModel extends BaseViewModel {


    @Inject
    public HostViewModel() {
    }

    @Override
    protected LiveData<ErrorModel> getErrorLiveData() {;
        return super.getErrorLiveData();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
