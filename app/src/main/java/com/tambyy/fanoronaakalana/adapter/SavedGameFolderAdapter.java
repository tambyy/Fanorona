package com.tambyy.fanoronaakalana.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tambyy.fanoronaakalana.R;
import com.tambyy.fanoronaakalana.models.Folder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SavedGameFolderAdapter extends BaseAdapter {

    private final List<Folder> selectedFolders = new ArrayList<>();

    private final List<Folder> folders;
    private final Context context;
    private final LayoutInflater inflater;

    public SavedGameFolderAdapter(Context context, List<Folder> folders) {
        this.context = context;
        this.folders = folders;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    public int getCount() {
        return folders.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        SavedGameFolderAdapter.ViewHolder viewHolder = null;

        if (convertView == null) {
            viewHolder = new SavedGameFolderAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.saved_game_folder_item, parent, false);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SavedGameFolderAdapter.ViewHolder) convertView.getTag();
        }

        final Folder folder = folders.get(position);

        viewHolder.folderName = (TextView) convertView.findViewById(R.id.saved_folder_name);
        viewHolder.folderName.setText(folder.getName());

        viewHolder.folderCheck = (ImageView) convertView.findViewById(R.id.saved_folder_check);
        viewHolder.folderCheck.setVisibility(isFolderSelected(folder) ? View.VISIBLE : View.GONE);

        return convertView;
    }

    private static class ViewHolder {
        public TextView folderName;
        public ImageView folderCheck;
    }

    public int getSelectedFoldersCount() {
        return selectedFolders.size();
    }

    public boolean isFolderSelected(Folder folder) {
        return selectedFolders.contains(folder);
    }

    public void toggleSelectFolder(Folder folder) {
        if (isFolderSelected(folder)) {
            selectedFolders.remove(folder);
        } else {
            selectedFolders.add(folder);
        }
        notifyDataSetChanged();
    }

    public void clearSelectedFolders() {
        selectedFolders.clear();
        notifyDataSetChanged();
    }

    public List<Folder> getSelectedFolders() {
        return selectedFolders;
    }
}