package com.msaproject.patient.di.baseview;
import com.msaproject.patient.base.BaseView;

import dagger.Module;
import dagger.Provides;

@Module
public class BaseViewModule {
    public BaseView baseView;

    public BaseViewModule(BaseView baseView){
        this.baseView = baseView;
    }

    @Provides
    public BaseView provideBaseView(){
        return baseView;
    }
}
