package com.msaproject.patient.ui.medical_files;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.msaproject.patient.R;
import com.msaproject.patient.base.BaseRecyclerAdapter;
import com.msaproject.patient.base.ItemClickListener;
import com.msaproject.patient.databinding.ItemMedicalFileBinding;
import com.msaproject.patient.model.MedicalFileModel;
import com.msaproject.patient.utils.PicassoHelper;
import com.msaproject.patient.utils.UIUtils;
import com.squareup.picasso.Picasso;
import com.stfalcon.imageviewer.StfalconImageViewer;

import java.util.ArrayList;
import java.util.List;

public class MedicalFilesRecyclerAdapter extends RecyclerView.Adapter<MedicalFilesRecyclerAdapter.MedicalFilesViewHolder>
        implements BaseRecyclerAdapter<MedicalFileModel> {

    @NonNull
    private final ItemClickListener<MedicalFileModel> onRemoveClickListener;
    private final ArrayList<MedicalFileModel> list;
    private final FragmentActivity context;
    private final int cardSideLength, layoutMargin;

    public MedicalFilesRecyclerAdapter(FragmentActivity context, @NonNull ItemClickListener<MedicalFileModel> onRemoveClickListener) {
        this.context = context;
        this.onRemoveClickListener = onRemoveClickListener;
        list = new ArrayList<>();
        int netWidth = UIUtils.getScreenDisplayMetrics(context).widthPixels - (context.getResources().getDimensionPixelSize(R.dimen.padding) * 4);
        cardSideLength = (int) (netWidth / 3.0);
        layoutMargin = context.getResources().getDimensionPixelSize(R.dimen.half_padding);
    }

    @NonNull
    @Override
    public MedicalFilesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MedicalFilesViewHolder(ItemMedicalFileBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MedicalFilesViewHolder holder, int position) {
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
    public void add(MedicalFileModel item) {
        list.add(item);
        notifyDataSetChanged();
    }

    @Override
    public void addAll(List<MedicalFileModel> items) {
        list.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    class MedicalFilesViewHolder extends RecyclerView.ViewHolder {

        private final ItemMedicalFileBinding viewBinding;

        MedicalFilesViewHolder(ItemMedicalFileBinding viewBinding) {
            super(viewBinding.getRoot());
            this.viewBinding = viewBinding;
            this.viewBinding.getRoot().getLayoutParams().width = cardSideLength;
            this.viewBinding.getRoot().getLayoutParams().height = cardSideLength;
        }

        private void bind(int position) {
            CardView.LayoutParams layoutParams = new CardView.LayoutParams(cardSideLength, cardSideLength);
            layoutParams.setMargins(layoutMargin, layoutMargin, layoutMargin, layoutMargin);
            viewBinding.getRoot().setLayoutParams(layoutParams);
            MedicalFileModel model = list.get(position);
            PicassoHelper.loadImageWithCache(model.getDownloadLink(), viewBinding.ivMedicalFileImage, PicassoHelper.MODE.JUST_INTO, null, null, null);
            viewBinding.ivMedicalFileImage.setOnClickListener(v ->
                    new StfalconImageViewer.Builder<>(context, new String[]{model.getDownloadLink()}, (iv, image) ->
                            Picasso.get().load(image).into(iv))
                            .withTransitionFrom(viewBinding.ivMedicalFileImage)
                            .withBackgroundColorResource(R.color.colorBlack).show(true));
            viewBinding.ivRemove.setOnClickListener(v -> onRemoveClickListener.onItemClicked(model));
        }
    }
}
