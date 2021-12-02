package com.tambyy.fanoronaakalana.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class SavedGameReviewSpeedViewHolder extends RecyclerView.ViewHolder {
    public SavedGameReviewSpeedViewHolder(View itemView) {
        super(itemView);
    }

    public void update(String text, View.OnClickListener clickListener, boolean selected) {
        itemView.setOnClickListener(clickListener);
        ((TextView) itemView).setText(text);
        itemView.setAlpha(selected ? 1f : 0.5f);
    }

}
