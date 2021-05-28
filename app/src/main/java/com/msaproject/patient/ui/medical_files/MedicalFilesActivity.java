package com.msaproject.patient.ui.medical_files;

import android.content.Intent;
import android.net.Uri;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.msaproject.patient.R;
import com.msaproject.patient.base.BaseActivity;
import com.msaproject.patient.databinding.ActivityMedicalFilesBinding;
import com.msaproject.patient.model.MedicalFileModel;
import com.msaproject.patient.pref.UserPref;
import com.msaproject.patient.utils.RandomString;
import com.msaproject.patient.utils.StringUtils;

import java.io.File;
import java.util.Objects;

import javax.inject.Inject;

public class MedicalFilesActivity extends BaseActivity<ActivityMedicalFilesBinding> {

    @Inject
    UserPref userPref;
    private MedicalFilesViewModel viewModel;
    private MedicalFilesRecyclerAdapter adapter;

    @Override
    protected ActivityMedicalFilesBinding getViewBinding() {
        return ActivityMedicalFilesBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void inject() {
        daggerComponent.inject(this);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(MedicalFilesViewModel.class);
    }

    @Override
    protected void onViewCreated() {
        setTitleWithBack(StringUtils.getString(R.string.my_medical_files));

        viewBinding.fabAdd.setOnClickListener(v ->ImagePicker
                .Companion
                .with(this)
                .crop()
                .start());

        adapter = new MedicalFilesRecyclerAdapter(this, this::deleteMedicalFile);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        viewBinding.rvMedicalFiles.setLayoutManager(layoutManager);
        viewBinding.rvMedicalFiles.setAdapter(adapter);

        getUserMedicalFiles();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImagePicker.REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                showErrorMsg(getString(R.string.no_image_selected));
                return;
            }

            try {
                File photoFile = Objects.requireNonNull(ImagePicker.Companion.getFile(data));
                uploadPhotoMedicalFile(Uri.fromFile(photoFile));
            } catch (NullPointerException e) {
                showErrorMsg(getString(R.string.something_went_wrong));
                e.printStackTrace();
            }

        }
    }

    private void getUserMedicalFiles() {
        showLoading();
        viewModel.getUserMedicalFiles().observe(this, medicalFileModels -> {
            hideLoading();
            adapter.clear(false);
            adapter.addAll(medicalFileModels);
        });
    }

    private void uploadPhotoMedicalFile(Uri photoUri) {
        String photoId = new RandomString().nextString();
        viewModel.uploadUserPhotoAndGetDownloadLink(photoId, photoUri).observe(this, downloadLink -> {
            MedicalFileModel model = new MedicalFileModel();
            model.setId(photoId);
            model.setPatientId(userPref.getId());
            model.setDownloadLink(downloadLink);
            postNewMedicalFile(model);
        });
    }

    private void postNewMedicalFile(MedicalFileModel model) {
        showLoading();
        viewModel.postNewMedicalFile(model).observe(this, aBoolean -> getUserMedicalFiles());
    }

    private void deleteMedicalFile(MedicalFileModel model) {
        showLoading();
        viewModel.deleteMedicalFile(model).observe(this, aBoolean -> getUserMedicalFiles());
    }

}