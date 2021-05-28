package com.msaproject.patient.ui.profile;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;

import com.msaproject.patient.R;
import com.msaproject.patient.base.BaseFragment;
import com.msaproject.patient.databinding.FragmentProfileBinding;
import com.msaproject.patient.model.UserModel;
import com.msaproject.patient.model.types.Gender;
import com.msaproject.patient.pref.UserPref;
import com.msaproject.patient.ui.chronic_diseases.ChronicDiseasesActivity;
import com.msaproject.patient.ui.edit_profile.EditProfileActivity;
import com.msaproject.patient.ui.medical_files.MedicalFilesActivity;
import com.msaproject.patient.ui.splash.SplashActivity;
import com.msaproject.patient.ui.view.InfoDialog;
import com.msaproject.patient.utils.PicassoHelper;
import com.msaproject.patient.utils.StringUtils;

import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

public class ProfileFragment extends BaseFragment<FragmentProfileBinding> {

    @Inject
    UserPref userPref;

    private ProfileViewModel viewModel;
    private InfoDialog infoDialog;

    @Override
    protected FragmentProfileBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentProfileBinding.inflate(inflater, container, false);
    }

    @Override
    protected void inject() {
        daggerComponent.inject(this);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(ProfileViewModel.class);
    }

    @Override
    public void onStart() {
        super.onStart();
        fillUpUserData();
    }

    @Override
    protected void onViewCreated() {
        setUpViews();
    }

    private void setUpViews() {
        viewBinding.ibEditProfile.setOnClickListener(v -> startActivity(new Intent(requireActivity(), EditProfileActivity.class)));
        viewBinding.cvMyAccountQrCode.setOnClickListener(v -> showQrCodeDialog());
        viewBinding.cvMyChronicDiseases.setOnClickListener(v -> startActivity(new Intent(requireActivity(), ChronicDiseasesActivity.class)));
        viewBinding.cvMyMedicalFiles.setOnClickListener(v -> startActivity(new Intent(requireActivity(), MedicalFilesActivity.class)));
        viewBinding.btnLogout.setOnClickListener(v -> logout());
    }

    private void fillUpUserData() {
        UserModel model = userPref.getUser();
        PicassoHelper.loadImageWithCache(model.getProfilePicLink(), viewBinding.ivImage, PicassoHelper.MODE.FIT_AND_CENTER_CROP,
                null, null, () ->viewBinding.ivImage.setImageResource(R.drawable.ic_user_placeholder));
        viewBinding.tvName.setText(model.getName());
        viewBinding.tvMobile.setText(model.getPhone());
        viewBinding.tvWeight.setText(StringUtils.getString(R.string.weight_placeholder, model.getWeight()));
        viewBinding.tvHeight.setText(StringUtils.getString(R.string.height_placeholder, model.getHeight()));
        viewBinding.tvBirthDate.setText(StringUtils.formatDateToLocale(model.getBirthDate(), "dd-MM-yyyy", new Locale(StringUtils.getLanguage())));
        viewBinding.tvGender.setText(StringUtils.getString(model.getGender() == Gender.MALE ? R.string.radio_male : R.string.radio_female));
    }

    private void showQrCodeDialog() {
        QRCodeDialog dialog = new QRCodeDialog(requireContext());
        dialog.show(userPref.getId());
    }

    private void logout() {
        infoDialog = new InfoDialog(requireContext(), new InfoDialog.OnDialogActionListener() {
            @Override
            public void onCancel() {
                infoDialog.dismiss();
            }

            @Override
            public void onConfirm() {
                infoDialog.dismiss();

                userPref.logout();

                NotificationManager notificationManager = (NotificationManager) requireActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                Objects.requireNonNull(notificationManager).cancelAll();

                Intent intent = new Intent(getContext(), SplashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        infoDialog.show(R.drawable.warning, StringUtils.getString(R.string.dialog_logout_msg),
                StringUtils.getString(R.string.dialog_logout), StringUtils.getString(R.string.cancel));
    }
}
