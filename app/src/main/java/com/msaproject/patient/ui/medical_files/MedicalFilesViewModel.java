package com.msaproject.patient.ui.medical_files;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.msaproject.patient.R;
import com.msaproject.patient.base.BaseViewModel;
import com.msaproject.patient.model.ErrorModel;
import com.msaproject.patient.model.MedicalFileModel;
import com.msaproject.patient.repo.MedicalFilesRepo;
import com.msaproject.patient.utils.StringUtils;

import java.util.List;

import javax.inject.Inject;

public class MedicalFilesViewModel extends BaseViewModel {

    @Inject
    MedicalFilesRepo medicalFilesRepo;

    @Inject
    public MedicalFilesViewModel() {
    }

    @Override
    protected LiveData<ErrorModel> getErrorLiveData() {
        addErrorObservers(medicalFilesRepo);
        return super.getErrorLiveData();
    }

    LiveData<List<MedicalFileModel>> getUserMedicalFiles() {
        return medicalFilesRepo.getUserMedicalFiles();
    }

    LiveData<Boolean> postNewMedicalFile(MedicalFileModel medicalFileModel) {
        return medicalFilesRepo.postNewMedicalFile(medicalFileModel);
    }

    LiveData<String> uploadUserPhotoAndGetDownloadLink(String photoId, Uri photoUri) {
        MediatorLiveData<String> liveData = new MediatorLiveData<>();
        liveData.addSource(medicalFilesRepo.uploadMedicalFilePhoto(photoId, photoUri), uploadStatusResponse -> {
            view.showLoading(StringUtils.getString(R.string.loading_dialog_uploading_msg, uploadStatusResponse.getProgress()));
            if (uploadStatusResponse.getDone()) {
                view.hideLoading();
                liveData.setValue(uploadStatusResponse.getDownloadLink());
            }
        });
        return liveData;
    }

    LiveData<Boolean> deleteMedicalFile(MedicalFileModel medicalFileModel) {
        return medicalFilesRepo.deleteMedicalFile(medicalFileModel);
    }

    @Override
    protected void onCleared() {
        medicalFilesRepo.dispose();
        super.onCleared();
    }
}
