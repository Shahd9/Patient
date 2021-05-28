package com.msaproject.patient.di.application;

import android.app.Application;
import android.content.Context;

import com.msaproject.patient.ApplicationClass;
import com.msaproject.patient.network.FirebaseManager;
import com.msaproject.patient.network.NetworkManager;
import com.msaproject.patient.pref.SettingPref;
import com.msaproject.patient.pref.UserPref;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component( modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(ApplicationClass app);

    Application getApplication();

    @ApplicationContext
    Context getContext();

    NetworkManager getNetworkManager();

    FirebaseManager getFirebaseManager();

    SettingPref getSettingPref();

    UserPref getUserPref();
}
