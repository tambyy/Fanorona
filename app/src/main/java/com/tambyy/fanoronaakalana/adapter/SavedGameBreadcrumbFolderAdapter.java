package com.tambyy.fanoronaakalana.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tambyy.fanoronaakalana.R;
import com.tambyy.fanoronaakalana.SavedGamesActivity;
import com.tambyy.fanoronaakalana.models.Folder;
import com.tambyy.fanoronaakalana.viewholder.SavedGameBreadcrumbFolderViewHolder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SavedGameBreadcrumbFolderAdapter extends RecyclerView.Adapter<SavedGameBreadcrumbFolderViewHolder> {

    private final Context context;
    private final List<Folder> folders;

    public SavedGameBreadcrumbFolderAdapter(Context context, List<Folder> folders) {
        this.context = context;
        this.folders = folders;
    }

    @NonNull
    @Override
    public SavedGameBreadcrumbFolderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.saved_game_breadcrumb_folder_item, parent, false);

        return new SavedGameBreadcrumbFolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SavedGameBreadcrumbFolderViewHolder viewHolder, int position) {
        viewHolder.updateWithFolder(this.folders.get(position), v -> ((SavedGamesActivity) context).setCurrentFolder(folders.get(position)), position == getItemCount() - 1);
    }

    @Override
    public int getItemCount() {
        return this.folders.size();
    }
}
