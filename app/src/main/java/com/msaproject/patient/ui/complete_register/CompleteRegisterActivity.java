package com.msaproject.patient.ui.complete_register;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.msaproject.patient.R;
import com.msaproject.patient.base.BaseActivity;
import com.msaproject.patient.databinding.ActivityCompleteRegisterBinding;
import com.msaproject.patient.model.UserModel;
import com.msaproject.patient.model.types.AccountType;
import com.msaproject.patient.model.types.Gender;
import com.msaproject.patient.pref.UserPref;
import com.msaproject.patient.ui.host.HostActivity;
import com.msaproject.patient.utils.StringUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

public class CompleteRegisterActivity extends BaseActivity<ActivityCompleteRegisterBinding> {

    @Inject
    UserPref userPref;

    private CompleteRegisterViewModel viewModel;
    @Nullable
    private Uri userPhoto = null;
    private Integer year, monthOfYear, dayOfMonth;
    private UserModel userModel;
    @Gender
    private int gender = Gender.MALE;

    @Override
    protected ActivityCompleteRegisterBinding getViewBinding() {
        return ActivityCompleteRegisterBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void inject() {
        daggerComponent.inject(this);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(CompleteRegisterViewModel.class);
    }

    @Override
    protected void onViewCreated() {
        setTitle(StringUtils.getString(R.string.complete_register));
        setUpViews();
        userModel = userPref.getUser();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImagePicker.REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                showErrorMsg(getString(R.string.no_image_selected));
                return;
            }

            try {
                File photoFile = Objects.requireNonNull(ImagePicker.Companion.getFile(data));
                userPhoto = Uri.fromFile(photoFile);
                Picasso.get().load((photoFile)).fit().centerCrop().into(viewBinding.ivImage);
                viewBinding.ibRemoveImage.setVisibility(View.VISIBLE);
            } catch (NullPointerException e) {
                showErrorMsg(getString(R.string.something_went_wrong));
                e.printStackTrace();
            }

        }
    }

    private void setUpViews() {
        viewBinding.ibAddImage.setOnClickListener(v -> ImagePicker
                .Companion
                .with(this)
                .crop()
                .start());

        viewBinding.ibRemoveImage.setOnClickListener(v -> removeImage());
        viewBinding.clDate.setOnClickListener(v -> showDatePicker());
        viewBinding.rgGender.setOnCheckedChangeListener((group, checkedId) -> gender = (checkedId == viewBinding.rbMale.getId()) ? Gender.MALE : Gender.FEMALE);
        viewBinding.btnSignUp.setOnClickListener(v -> {
            if (!validateInput())
                return;
            if (userPhoto == null)
                postUserData();
            else
                viewModel.uploadUserPhotoAndGetDownloadLink(userPhoto).observe(this, CompleteRegisterActivity.this::postUserData);
        });
    }

    private void removeImage() {
        userPhoto = null;
        viewBinding.ivImage.setImageResource(R.drawable.ic_user_placeholder);
        viewBinding.ibRemoveImage.setVisibility(View.GONE);
    }

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        int year2, monthOfYear2, dayOfMonth2;
        year2 = (year == null) ? c.get(Calendar.YEAR) : year;
        monthOfYear2 = (monthOfYear == null) ? c.get(Calendar.YEAR) : monthOfYear;
        dayOfMonth2 = (dayOfMonth == null) ? c.get(Calendar.YEAR) : dayOfMonth;
        DatePickerDialog mDate = new DatePickerDialog(this, (view, y, m, d) -> {

            String selectedDate = y + "-" + (m + 1 < 10 ? "0" : "") + (m + 1) + "-"
                    + (d < 10 ? "0" : "") + d;

            year = y;
            monthOfYear = m;
            dayOfMonth = d;
            viewBinding.tvDate.setText(StringUtils.formatDateToLocale(selectedDate, "dd-MM-yyyy", new Locale(StringUtils.getLanguage())));
        }, year2, monthOfYear2, dayOfMonth2);
        c.set(1800, 0, 1);
        mDate.getDatePicker().setMinDate(c.getTimeInMillis());
        mDate.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        mDate.show();
    }

    private boolean validateInput() {
        hideKeyboard();

        // Name
        String name = viewBinding.etName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            showErrorMsg(StringUtils.getString(R.string.please_enter_valid_name));
            return false;
        }
        // Weight
        float weight;
        try {
            weight = Float.parseFloat(viewBinding.etWeight.getText().toString());
            if (weight < 1f || weight > 500f) {
                showErrorMsg(StringUtils.getString(R.string.please_enter_valid_weight));
                return false;
            }
        } catch (NumberFormatException e) {
            showErrorMsg(StringUtils.getString(R.string.please_enter_valid_weight));
            return false;
        }
        // Height
        float height;
        try {
            height = Float.parseFloat(viewBinding.etHeight.getText().toString());
            if (height < 20f || height > 300f) {
                showErrorMsg(StringUtils.getString(R.string.please_enter_valid_height));
                return false;
            }
        } catch (NumberFormatException e) {
            showErrorMsg(StringUtils.getString(R.string.please_enter_valid_height));
            return false;
        }
        // BirthDateInMillis
        if (year == null || monthOfYear == null || dayOfMonth == null) {
            showErrorMsg(StringUtils.getString(R.string.please_enter_your_birth_date));
            return false;
        }
        Calendar c = Calendar.getInstance();
        c.set(year, monthOfYear, dayOfMonth);

        userModel.setName(name);
        userModel.setWeight(weight);
        userModel.setHeight(height);
        userModel.setBirthDate(c.getTime());
        userModel.setGender(gender);
        userModel.setAccountType(AccountType.PATIENT);

        return true;
    }

    private void postUserData() {
        postUserData(null);
    }

    private void postUserData(@Nullable String userPhotoDownloadLink) {
        showLoading();
        userModel.setProfilePicLink(userPhotoDownloadLink);
        viewModel.postNewUser(userModel).observe(this, aBoolean -> {
            hideLoading();
            userPref.setUserModel(userModel);
            startActivity(new Intent(CompleteRegisterActivity.this, HostActivity.class));
            finish();
        });
    }

}