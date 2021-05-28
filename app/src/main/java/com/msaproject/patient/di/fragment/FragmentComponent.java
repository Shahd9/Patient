package com.msaproject.patient.di.fragment;

import com.msaproject.patient.di.application.ApplicationComponent;
import com.msaproject.patient.di.baseview.BaseViewModule;
import com.msaproject.patient.di.viewmodel.ViewModelModule;
import com.msaproject.patient.ui.my_doctors.MyDoctorsFragment;
import com.msaproject.patient.ui.recommendations.RecommendationsFragment;
import com.msaproject.patient.ui.profile.ProfileFragment;

import dagger.Component;

@FragmentScope
@Component(dependencies = ApplicationComponent.class, modules = {ViewModelModule.class, BaseViewModule.class,})
public interface FragmentComponent {
    void inject(MyDoctorsFragment fragment);
    void inject(RecommendationsFragment fragment);
    void inject(ProfileFragment fragment);
}
