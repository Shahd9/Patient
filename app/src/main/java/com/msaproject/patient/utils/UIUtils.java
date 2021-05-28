package com.msaproject.patient.utils;

import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class UIUtils {

    public interface LoadMoreListener {
        void loadMore();
    }

    public static void addLoadMoreListener(RecyclerView recyclerView, LoadMoreListener listener){
        if (recyclerView == null || listener == null)
            return;

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager= LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
                int totalItemCount = layoutManager.getItemCount();
                int lastVisible = layoutManager.findLastVisibleItemPosition();

                boolean endHasBeenReached = lastVisible + 2 >= totalItemCount;
                if (totalItemCount > 0 && endHasBeenReached) {
                    listener.loadMore();
                }
            }
        });
    }

    public static DisplayMetrics getScreenDisplayMetrics(FragmentActivity activity){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        try {
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return displayMetrics;
    }

    // Method to ellisize TextView to just it's constrained bounds (no maxLines attribute given)
    public static void ellipsizeTextViewToBounds(@NonNull TextView tvDesc){
        tvDesc.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                tvDesc.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int noOfLinesVisible = tvDesc.getHeight() / tvDesc.getLineHeight();
                tvDesc.setMaxLines(noOfLinesVisible);
                tvDesc.setEllipsize(TextUtils.TruncateAt.END);
            }
        });
    }
}
