package com.msaproject.patient.ui.appointments.appointment_list;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.msaproject.patient.base.BaseRecyclerAdapter;
import com.msaproject.patient.base.ItemClickListener;
import com.msaproject.patient.databinding.ItemAppointmentBinding;
import com.msaproject.patient.model.AppointmentModel;
import com.msaproject.patient.model.UserModel;
import com.msaproject.patient.utils.PicassoHelper;
import com.msaproject.patient.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.ViewHolder>
        implements BaseRecyclerAdapter<AppointmentModel> {

    private final UserModel doctorModel;
    private final ItemClickListener<AppointmentModel> itemClickListener;

    @NonNull
    private final ArrayList<AppointmentModel> list;

    public AppointmentsAdapter(UserModel doctorModel, ItemClickListener<AppointmentModel> itemClickListener) {
        list = new ArrayList<>();
        this.doctorModel = doctorModel;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemAppointmentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
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
    public void add(AppointmentModel item) {
        list.add(item);
        notifyDataSetChanged();
    }

    @Override
    public void addAll(List<AppointmentModel> items) {
        list.clear();
        list.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemAppointmentBinding viewBinding;

        ViewHolder(ItemAppointmentBinding viewBinding) {
            super(viewBinding.getRoot());
            this.viewBinding = viewBinding;
        }

        private void bind(int position) {
            AppointmentModel model = list.get(position);

            viewBinding.getRoot().setOnClickListener(v -> itemClickListener.onItemClicked(model));

            PicassoHelper.loadImageWithCache(doctorModel.getProfilePicLink(), viewBinding.ivPatientDoctorPicture, PicassoHelper.MODE.FIT_AND_CENTER_CROP, null, null, null);

            viewBinding.tvAppointmentTitle.setText(model.getTitle());

            viewBinding.tvDate.setText(
                    StringUtils.formatDateToLocale(model.getCreatedAt(), "E dd/MM/yyyy", new Locale(StringUtils.getLanguage())));

            viewBinding.tvLastUpdated.setText(
                    StringUtils.formatDateToLocale(model.getUpdatedAt(), "E dd/MM/yyyy", new Locale(StringUtils.getLanguage())));
        }
    }
}