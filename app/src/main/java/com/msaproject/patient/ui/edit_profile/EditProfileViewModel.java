package com.msaproject.patient.ui.edit_profile;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.msaproject.patient.R;
import com.msaproject.patient.base.BaseViewModel;
import com.msaproject.patient.model.ErrorModel;
import com.msaproject.patient.model.UserModel;
import com.msaproject.patient.repo.UserRepo;
import com.msaproject.patient.utils.StringUtils;

import javax.inject.Inject;

public class EditProfileViewModel extends BaseViewModel {

    @Inject
    UserRepo userRepo;

    @Inject
    public EditProfileViewModel() {
    }

    @Override
    protected LiveData<ErrorModel> getErrorLiveData() {
        addErrorObservers(userRepo);
        return super.getErrorLiveData();
    }

    LiveData<UserModel> updateUserData(UserModel model){
       return userRepo.updateUserData(model);
    }

    LiveData<String> uploadUserPhotoAndGetDownloadLink(Uri photoUri) {
        MediatorLiveData<String> liveData = new MediatorLiveData<>();
        liveData.addSource(userRepo.uploadUserPhoto(photoUri), uploadStatusResponse -> {
            view.showLoading(StringUtils.getString(R.string.loading_dialog_uploading_msg, uploadStatusResponse.getProgress()));
            if (uploadStatusResponse.getDone()) {
                view.hideLoading();
                liveData.setValue(uploadStatusResponse.getDownloadLink());
            }
        });
        return liveData;
    }

    @Override
    protected void onCleared() {
        userRepo.dispose();
        super.onCleared();
    }
}
