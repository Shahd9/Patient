package com.msaproject.patient.ui.doctor_details;

import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.ViewModelProvider;

import com.msaproject.patient.R;
import com.msaproject.patient.base.BaseActivity;
import com.msaproject.patient.databinding.ActivityDoctorsDetailsBinding;
import com.msaproject.patient.model.PatientDoctorModel;
import com.msaproject.patient.model.UserModel;
import com.msaproject.patient.model.types.Gender;
import com.msaproject.patient.ui.appointments.appointment_list.AppointmentsListActivity;
import com.msaproject.patient.utils.PicassoHelper;
import com.msaproject.patient.utils.StringUtils;
import com.squareup.picasso.Picasso;
import com.stfalcon.imageviewer.StfalconImageViewer;

import java.util.Locale;

public class DoctorsDetailsActivity extends BaseActivity<ActivityDoctorsDetailsBinding> {

    private final static String PATIENT_DOCTOR_MODEL = "PatientDoctorModel";

    public static Intent getDoctorsDetailsActivityIntent(Context context, PatientDoctorModel patientDoctorModel) {
        Intent intent = new Intent(context, DoctorsDetailsActivity.class);
        intent.putExtra(PATIENT_DOCTOR_MODEL, patientDoctorModel);
        return intent;
    }

    private DoctorDetailsViewModel viewModel;
    private UserModel doctorModel;
    private PatientDoctorModel patientDoctorModel;

    @Override
    protected ActivityDoctorsDetailsBinding getViewBinding() {
        return ActivityDoctorsDetailsBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void inject() {
        daggerComponent.inject(this);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(DoctorDetailsViewModel.class);
    }

    @Override
    protected void onViewCreated() {
        setTitleWithBack(StringUtils.getString(R.string.doctor_details));
        if (getDataFromIntent()) {
            setUpViews();
            fillUpUserData();
        }
    }

    private boolean getDataFromIntent() {
        try {
            patientDoctorModel = ((PatientDoctorModel) getIntent().getSerializableExtra(PATIENT_DOCTOR_MODEL));
            doctorModel = patientDoctorModel.getDoctorModel();
            return doctorModel != null;
        } catch (Exception e) {
            e.printStackTrace();
            showErrorMsg(StringUtils.getString(R.string.something_went_wrong));
            return false;
        }
    }

    private void setUpViews() {
        viewBinding.clImageHolder.setOnClickListener(v ->
                new StfalconImageViewer.Builder<>(this, new String[]{doctorModel.getProfilePicLink()}, (iv, image) ->
                        Picasso.get().load(image).into(iv))
                        .withTransitionFrom(viewBinding.ivImage)
                        .withBackgroundColorResource(R.color.colorBlack).show(true));

        viewBinding.cvAppointments.setOnClickListener(v -> startAppointmentListActivity());
    }

    private void fillUpUserData() {
        PicassoHelper.loadImageWithCache(doctorModel.getProfilePicLink(), viewBinding.ivImage, PicassoHelper.MODE.JUST_INTO, null, null, null);
        viewBinding.tvName.setText(doctorModel.getName());
        viewBinding.tvMobile.setText(doctorModel.getPhone());
        viewBinding.tvBirthDate.setText(StringUtils.formatDateToLocale(doctorModel.getBirthDate(), "dd-MM-yyyy", new Locale(StringUtils.getLanguage())));
        viewModel.getSpecializationById(doctorModel.getSpecializationId()).observe(this, specializationModel -> viewBinding.tvSpecialization.setText(specializationModel.getName()));
        viewBinding.tvGender.setText(StringUtils.getString(doctorModel.getGender() == Gender.MALE ? R.string.radio_male : R.string.radio_female));
    }

    private void startAppointmentListActivity() {
        startActivity(AppointmentsListActivity.getAppointmentsListActivityIntent(this, patientDoctorModel));
    }

}