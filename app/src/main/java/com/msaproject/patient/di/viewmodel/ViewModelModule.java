package com.msaproject.patient.di.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


import com.msaproject.patient.ui.appointments.AppointmentsViewModel;
import com.msaproject.patient.ui.chronic_diseases.ChronicDiseasesActivity;
import com.msaproject.patient.ui.chronic_diseases.ChronicDiseasesViewModel;
import com.msaproject.patient.ui.complete_register.CompleteRegisterViewModel;
import com.msaproject.patient.ui.doctor_details.DoctorDetailsViewModel;
import com.msaproject.patient.ui.edit_profile.EditProfileViewModel;
import com.msaproject.patient.ui.host.HostViewModel;
import com.msaproject.patient.ui.medical_files.MedicalFilesViewModel;
import com.msaproject.patient.ui.my_doctors.MyDoctorsViewModel;
import com.msaproject.patient.ui.recommendations.RecommendationsViewModel;
import com.msaproject.patient.ui.phone_auth.PhoneAuthViewModel;
import com.msaproject.patient.ui.profile.ProfileViewModel;
import com.msaproject.patient.ui.splash.SplashViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**** IMPORTANT ****
 The module must have at least one @IntoMap ViewModel
 Otherwise the HashMap Won't be created
 And you'll get a dagger compiling error
 */
@Module
abstract public class ViewModelModule {

    @Binds
    abstract ViewModelProvider.Factory bindsViewModelFactory(DaggerViewModelFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel.class)
    abstract ViewModel provideSplashViewModel(SplashViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PhoneAuthViewModel.class)
    abstract ViewModel providePhoneAuthViewModel(PhoneAuthViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CompleteRegisterViewModel.class)
    abstract ViewModel provideCompleteRegisterViewModel(CompleteRegisterViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(HostViewModel.class)
    abstract ViewModel provideHostViewModel(HostViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MyDoctorsViewModel.class)
    abstract ViewModel provideMyDoctorsViewModel(MyDoctorsViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RecommendationsViewModel.class)
    abstract ViewModel provideNotificationsViewModel(RecommendationsViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel.class)
    abstract ViewModel provideProfileViewModel(ProfileViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MedicalFilesViewModel.class)
    abstract ViewModel provideMedicalFilesViewModel(MedicalFilesViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(EditProfileViewModel.class)
    abstract ViewModel provideEditProfileViewModel(EditProfileViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(DoctorDetailsViewModel.class)
    abstract ViewModel provideDoctorDetailsViewModel(DoctorDetailsViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(AppointmentsViewModel.class)
    abstract ViewModel provideAppointmentsViewModel(AppointmentsViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ChronicDiseasesViewModel.class)
    abstract ViewModel provideChronicDiseasesViewModel(ChronicDiseasesViewModel viewModel);

}
