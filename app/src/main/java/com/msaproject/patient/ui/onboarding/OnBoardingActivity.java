package com.msaproject.patient.ui.onboarding;

import android.content.Intent;

import com.msaproject.patient.base.BaseActivity;
import com.msaproject.patient.databinding.ActivityOnboardingBinding;
import com.msaproject.patient.pref.SettingPref;
import com.msaproject.patient.ui.phone_auth.PhoneAuthActivity;

import javax.inject.Inject;

public class OnBoardingActivity extends BaseActivity<ActivityOnboardingBinding> {

    @Inject
    SettingPref settingPref;

    @Override
    protected ActivityOnboardingBinding getViewBinding() {
        return ActivityOnboardingBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void inject() {
        daggerComponent.inject(this);
    }

    @Override
    protected void onViewCreated() {
        viewBinding.viewPager.setAdapter(new OnBoardingPagerAdapter(this, this::gotoPhoneAuth));
        viewBinding.dotsIndicator.setViewPager2(viewBinding.viewPager);
        viewBinding.tvLogin.setOnClickListener(v -> gotoPhoneAuth());
    }

    public void gotoPhoneAuth(){
        settingPref.setShowTutorial(false);
        startActivity(new Intent(this, PhoneAuthActivity.class));
        finish();
    }

}