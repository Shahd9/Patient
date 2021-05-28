package com.msaproject.patient;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.msaproject.patient.di.application.ApplicationComponent;
import com.msaproject.patient.di.application.ApplicationModule;
import com.msaproject.patient.di.application.DaggerApplicationComponent;
import com.msaproject.patient.utils.LocaleHelper;
import com.yariksoffice.lingver.Lingver;

import es.dmoral.toasty.Toasty;
import timber.log.Timber;

public class ApplicationClass extends Application {
    protected ApplicationComponent applicationComponent;

    private static Context context;
    private static ApplicationClass app;

    public static ApplicationClass get(Context context) {
        return (ApplicationClass) context.getApplicationContext();
    }

    public static ApplicationClass getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        context = app.getApplicationContext();

        applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        Lingver.init(this, LocaleHelper.getLanguage(this));

        Toasty.Config.getInstance()
                .allowQueue(false)
                .apply();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    public void notifyLanguageChanged() {
        context = LocaleHelper.onAttach(app);
    }

    public static Context getContext() {
        return context;
    }

    public ApplicationComponent getComponent() {
        return applicationComponent;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        context = LocaleHelper.onAttach(this);
    }
}
