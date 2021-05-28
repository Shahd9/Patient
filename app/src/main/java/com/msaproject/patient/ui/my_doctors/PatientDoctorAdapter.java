package com.msaproject.patient.ui.my_doctors;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.msaproject.patient.R;
import com.msaproject.patient.base.BaseRecyclerAdapter;
import com.msaproject.patient.base.ItemClickListener;
import com.msaproject.patient.databinding.ItemDoctorPatientBinding;
import com.msaproject.patient.model.PatientDoctorModel;
import com.msaproject.patient.utils.PicassoHelper;
import com.msaproject.patient.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PatientDoctorAdapter extends RecyclerView.Adapter<PatientDoctorAdapter.ViewHolder>
        implements BaseRecyclerAdapter<PatientDoctorModel> {

    private final ItemClickListener<PatientDoctorModel> itemClickListener;

    @NonNull
    private final ArrayList<PatientDoctorModel> list;

    public PatientDoctorAdapter(ItemClickListener<PatientDoctorModel> itemClickListener) {
        list = new ArrayList<>();
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemDoctorPatientBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void clear(boolean notifyDataSetChanged) {
        list.clear();
        if (notifyDataSetChanged)
            notifyDataSetChanged();
    }

    @Override
    public void add(PatientDoctorModel item) {
        list.add(item);
        notifyDataSetChanged();
    }

    @Override
    public void addAll(List<PatientDoctorModel> items) {
        list.clear();
        list.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemDoctorPatientBinding viewBinding;

        ViewHolder(ItemDoctorPatientBinding viewBinding) {
            super(viewBinding.getRoot());
            this.viewBinding = viewBinding;
        }

        private void bind(int position) {
            PatientDoctorModel model = list.get(position);

            viewBinding.getRoot().setOnClickListener(v -> itemClickListener.onItemClicked(model));

            if (model.getDoctorModel() != null) {
                PicassoHelper.loadImageWithCache(model.getDoctorModel().getProfilePicLink(), viewBinding.ivPatientDoctorPicture, PicassoHelper.MODE.FIT_AND_CENTER_CROP, null, null, null);
                viewBinding.tvPatientDoctorName.setText(model.getDoctorModel().getName());
            }

            viewBinding.tvLastAppointment.setText(model.getLastAppointment() == null
                    ? StringUtils.getString(R.string.not_available)
                    : StringUtils.formatDateToLocale(model.getLastAppointment(), "E dd/MMM/yyyy", new Locale(StringUtils.getLanguage())));

            viewBinding.tvNextAppointment.setText(model.getNextAppointment() == null
                    ? StringUtils.getString(R.string.not_available)
                    : StringUtils.formatDateToLocale(model.getNextAppointment(), "E dd/MMM/yyyy", new Locale(StringUtils.getLanguage())));
        }
    }
}