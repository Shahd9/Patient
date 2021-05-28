package com.msaproject.patient.ui.my_doctors;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.msaproject.patient.base.BaseFragment;
import com.msaproject.patient.databinding.FragmentMyDoctorsBinding;
import com.msaproject.patient.model.PatientDoctorModel;
import com.msaproject.patient.ui.doctor_details.DoctorsDetailsActivity;

public class MyDoctorsFragment extends BaseFragment<FragmentMyDoctorsBinding> {

    private MyDoctorsViewModel viewModel;
    private PatientDoctorAdapter adapter;
    private boolean searchBarEnabled;

    @Override
    protected FragmentMyDoctorsBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentMyDoctorsBinding.inflate(inflater, container, false);
    }

    @Override
    protected void inject() {
        daggerComponent.inject(this);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(MyDoctorsViewModel.class);
    }

    @Override
    protected void onViewCreated() {
        setupViews();
        viewModel.getErrorLiveData().observe(getViewLifecycleOwner(), errorModel -> {
            hideLoading();
            showErrorMsg(errorModel.getMessage());
        });
        getPatientDoctors();
    }

    private void setupViews() {
        enableDisableSearchBar(false);
        adapter = new PatientDoctorAdapter(this::startDoctorDetailsActivity);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        viewBinding.rvPatientDoctor.setLayoutManager(layoutManager);
        viewBinding.rvPatientDoctor.setAdapter(adapter);
    }

    private void enableDisableSearchBar(boolean enabled) {
        searchBarEnabled = enabled;
        viewBinding.etSearch.setEnabled(enabled);
        viewBinding.ivSearch.setEnabled(enabled);
        if (enabled)
            viewBinding.etSearch.setFocusableInTouchMode(true);
        else
            viewBinding.etSearch.setFocusable(false);
    }

    private void getPatientDoctors() {
        showLoading();
        viewModel.getPatientDoctors().observe(getViewLifecycleOwner(), patientDoctorModels -> {
            hideLoading();
            adapter.addAll(patientDoctorModels);
        });
    }

    private void startDoctorDetailsActivity(PatientDoctorModel patientDoctorModel) {
        startActivity(DoctorsDetailsActivity.getDoctorsDetailsActivityIntent(requireContext(), patientDoctorModel));
    }


}
