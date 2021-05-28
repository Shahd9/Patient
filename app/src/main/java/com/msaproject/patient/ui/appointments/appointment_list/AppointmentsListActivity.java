package com.msaproject.patient.ui.appointments.appointment_list;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.msaproject.patient.R;
import com.msaproject.patient.base.BaseActivity;
import com.msaproject.patient.databinding.ActivityAppointmentsListBinding;
import com.msaproject.patient.model.AppointmentModel;
import com.msaproject.patient.model.PatientDoctorModel;
import com.msaproject.patient.ui.appointments.AppointmentsViewModel;
import com.msaproject.patient.ui.appointments.appointment_details.AppointmentDetailsActivity;

public class AppointmentsListActivity extends BaseActivity<ActivityAppointmentsListBinding> {

    private static final String PATIENT_DOCTOR_MODEL = "PatientDoctorModel";

    public static Intent getAppointmentsListActivityIntent(Context context, @NonNull PatientDoctorModel model) {
        Intent intent = new Intent(context, AppointmentsListActivity.class);
        intent.putExtra(PATIENT_DOCTOR_MODEL, model);
        return intent;
    }

    private AppointmentsViewModel viewModel;
    private PatientDoctorModel patientDoctorModel;
    private AppointmentsAdapter adapter;

    @Override
    protected ActivityAppointmentsListBinding getViewBinding() {
        return ActivityAppointmentsListBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void inject() {
        daggerComponent.inject(this);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppointmentsViewModel.class);
    }

    @Override
    protected void onViewCreated() {
        setTitleWithBack(getString(R.string.doctor_appointments));

        patientDoctorModel = (PatientDoctorModel) getIntent().getSerializableExtra(PATIENT_DOCTOR_MODEL);

        adapter = new AppointmentsAdapter(patientDoctorModel.getDoctorModel(), this::openAppointmentDetailsActivity);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        viewBinding.rvAppointments.setLayoutManager(layoutManager);
        viewBinding.rvAppointments.setAdapter(adapter);

        viewModel.getErrorLiveData().observe(this, this::onError);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getAppointments();
    }

    private void getAppointments() {
        showLoading();
        viewModel.getAppointmentsList(patientDoctorModel.getDoctorId()).observe(this, appointmentModels -> {
            hideLoading();
            adapter.addAll(appointmentModels);
        });
    }

    private void openAppointmentDetailsActivity(AppointmentModel model) {
        startActivity(AppointmentDetailsActivity.getAppointmentDetailsActivityIntent(this, model));
    }

}