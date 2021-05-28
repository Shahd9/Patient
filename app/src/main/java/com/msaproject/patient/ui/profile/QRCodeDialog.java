package com.msaproject.patient.ui.profile;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.msaproject.patient.R;
import com.msaproject.patient.databinding.DialogQrCodeBinding;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QRCodeDialog extends Dialog {


    private final DialogQrCodeBinding viewBinding;
    private final Context context;

    public QRCodeDialog(@NonNull Context context) {
        super(context, R.style.Theme_AppCompat_Light_Dialog_Alert);

        this.context = context;

        viewBinding = DialogQrCodeBinding.inflate(LayoutInflater.from(context), null, false);
        setContentView(viewBinding.getRoot());

        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        setCancelable(true);
        setCanceledOnTouchOutside(true);

        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    public void show(String userId) {
        QRGEncoder qrgEncoder = new QRGEncoder(userId, null, QRGContents.Type.TEXT, convertDpToPixel(175));
        Bitmap bitmap = qrgEncoder.getBitmap();
        viewBinding.ivQrCode.setImageBitmap(bitmap);
        show();
    }

    private int convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }
}