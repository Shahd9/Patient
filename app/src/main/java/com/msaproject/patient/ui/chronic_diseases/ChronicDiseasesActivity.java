package com.msaproject.patient.ui.chronic_diseases;

import androidx.lifecycle.ViewModelProvider;

import com.msaproject.patient.R;
import com.msaproject.patient.base.BaseActivity;
import com.msaproject.patient.databinding.ActivityChronicDiseasesListBinding;
import com.msaproject.patient.pref.UserPref;
import com.msaproject.patient.utils.StringUtils;

import javax.inject.Inject;

public class ChronicDiseasesActivity extends BaseActivity<ActivityChronicDiseasesListBinding> {

    @Inject
    UserPref userPref;

    private ChronicDiseasesViewModel viewModel;
    private DiseasesAdapter adapter;

    @Override
    protected ActivityChronicDiseasesListBinding getViewBinding() {
        return ActivityChronicDiseasesListBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void inject() {
        daggerComponent.inject(this);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(ChronicDiseasesViewModel.class);
    }

    @Override
    protected void onViewCreated() {
        setTitleWithBack(StringUtils.getString(R.string.my_chronic_diseases));

        viewModel.getErrorLiveData().observe(this, this::onError);

        viewModel.getAllDiseases().observe(this, diseaseModels -> {
            adapter = new DiseasesAdapter(viewModel.getUserChronicDiseaseId(), diseaseModels);
            viewBinding.rvBottomSheet.setAdapter(adapter);
        });

        viewBinding.btnApplyChanges.setOnClickListener(v -> viewModel.updateUserChronicDisease(adapter.getSelectedId())
                .observe(ChronicDiseasesActivity.this, updatedUserModel -> {
                    userPref.setUserModel(updatedUserModel);
                    hideLoading();
                    showSuccessMsg(StringUtils.getString(R.string.data_updated_successfully));
                    finish();
                })
        );
    }
}