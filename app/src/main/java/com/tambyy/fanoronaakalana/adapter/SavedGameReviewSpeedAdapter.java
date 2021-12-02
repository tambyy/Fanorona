package com.tambyy.fanoronaakalana.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tambyy.fanoronaakalana.R;
import com.tambyy.fanoronaakalana.SavedGamesActivity;
import com.tambyy.fanoronaakalana.SavedGamesReviewActivity;
import com.tambyy.fanoronaakalana.models.Folder;
import com.tambyy.fanoronaakalana.viewholder.SavedGameBreadcrumbFolderViewHolder;
import com.tambyy.fanoronaakalana.viewholder.SavedGameReviewSpeedViewHolder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SavedGameReviewSpeedAdapter extends RecyclerView.Adapter<SavedGameReviewSpeedViewHolder> {

    private final Context context;
    private final List<Float> speeds;
    private int selectedPosition = 0;

    public SavedGameReviewSpeedAdapter(Context context, List<Float> speeds) {
        this.context = context;
        this.speeds = speeds;
    }

    @NonNull
    @Override
    public SavedGameReviewSpeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_saved_game_review_speed, parent, false);

        return new SavedGameReviewSpeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SavedGameReviewSpeedViewHolder viewHolder, int position) {
        final float speed = this.speeds.get(position);
        viewHolder.update("x " + speed,
            v -> {
                ((SavedGamesReviewActivity) context).setAnimationSpeed(speed);
            setSelectedPosition(position);
            },
            selectedPosition == position);
    }

    @Override
    public int getItemCount() {
        return this.speeds.size();
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
    }
}

