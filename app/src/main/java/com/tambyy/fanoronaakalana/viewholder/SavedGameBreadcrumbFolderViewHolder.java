package com.tambyy.fanoronaakalana.viewholder;

import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tambyy.fanoronaakalana.R;
import com.tambyy.fanoronaakalana.models.Folder;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SavedGameBreadcrumbFolderViewHolder extends RecyclerView.ViewHolder {
    private static int folderColor = Color.rgb(204, 204, 204);
    private static int folderLastColor = Color.rgb(255, 211, 135);

    TextView textViewFolderName;
    ImageView textViewFolderNext;

    public SavedGameBreadcrumbFolderViewHolder(View itemView) {
        super(itemView);
        textViewFolderName = (TextView) itemView.findViewById(R.id.saved_game_breadcrumb_folder_name);
        textViewFolderNext = (ImageView) itemView.findViewById(R.id.saved_game_breadcrumb_folder_next);
    }

    public void updateWithFolder(Folder folder, View.OnClickListener clickListener, boolean last) {
        if (textViewFolderName != null) {
            if (folder != null) {
                textViewFolderName.setText(folder.getName());
            }

            if (last) {
                textViewFolderName.setTextColor(folderLastColor);
                textViewFolderName.setTypeface(textViewFolderName.getTypeface(), Typeface.BOLD);
                textViewFolderNext.setVisibility(View.GONE);
            } else {
                textViewFolderName.setTextColor(folderColor);
                textViewFolderName.setTypeface(textViewFolderName.getTypeface(), Typeface.NORMAL);
                textViewFolderNext.setVisibility(View.VISIBLE);
            }

            itemView.setOnClickListener(clickListener);
        }
    }

}
