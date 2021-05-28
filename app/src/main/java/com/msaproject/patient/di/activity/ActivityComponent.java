package com.msaproject.patient.di.activity;

import com.msaproject.patient.di.application.ApplicationComponent;
import com.msaproject.patient.di.baseview.BaseViewModule;
import com.msaproject.patient.di.viewmodel.ViewModelModule;
import com.msaproject.patient.ui.appointments.appointment_details.AppointmentDetailsActivity;
import com.msaproject.patient.ui.appointments.appointment_list.AppointmentsListActivity;
import com.msaproject.patient.ui.chronic_diseases.ChronicDiseasesActivity;
import com.msaproject.patient.ui.complete_register.CompleteRegisterActivity;
import com.msaproject.patient.ui.doctor_details.DoctorsDetailsActivity;
import com.msaproject.patient.ui.edit_profile.EditProfileActivity;
import com.msaproject.patient.ui.host.HostActivity;
import com.msaproject.patient.ui.medical_files.MedicalFilesActivity;
import com.msaproject.patient.ui.onboarding.OnBoardingActivity;
import com.msaproject.patient.ui.phone_auth.PhoneAuthActivity;
import com.msaproject.patient.ui.recipe_details.RecipeDetailsActivity;
import com.msaproject.patient.ui.splash.SplashActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = {ViewModelModule.class, BaseViewModule.class,})
public interface ActivityComponent {
    void inject(SplashActivity activity);
    void inject(OnBoardingActivity activity);
    void inject(PhoneAuthActivity activity);
    void inject(CompleteRegisterActivity activity);
    void inject(HostActivity activity);
    void inject(MedicalFilesActivity activity);
    void inject(EditProfileActivity activity);
    void inject(DoctorsDetailsActivity activity);
    void inject(AppointmentsListActivity activity);
    void inject(AppointmentDetailsActivity activity);
    void inject(ChronicDiseasesActivity activity);
    void inject(RecipeDetailsActivity activity);
}
