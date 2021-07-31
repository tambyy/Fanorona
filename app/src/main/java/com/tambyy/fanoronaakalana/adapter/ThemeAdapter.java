package com.tambyy.fanoronaakalana.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tambyy.fanoronaakalana.R;
import com.tambyy.fanoronaakalana.engine.Engine;
import com.tambyy.fanoronaakalana.graphics.customview.AkalanaView;
import com.tambyy.fanoronaakalana.models.Theme;
import com.tambyy.fanoronaakalana.utils.ThemeManager;

import java.util.List;

public class ThemeAdapter extends BaseAdapter {

    int selectedTheme = -1;

    // FOR DATA
    private final Engine engine;
    private final List<Theme> themes;
    private final Context context;
    private final LayoutInflater inflater;
    private final ThemeManager themeManager;

    public ThemeAdapter(Context context, List<Theme> themes, Engine engine) {
        this.context = context;
        this.engine = engine;
        this.themes = themes;
        this.themeManager = ThemeManager.getInstance(context);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return themes.size();
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

        ThemeAdapter.ViewHolder viewHolder = null;

        if (convertView == null) {
            viewHolder = new ThemeAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.theme_item, parent, false);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ThemeAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.akalana = (AkalanaView) convertView.findViewById(R.id.theme_akalana);
        viewHolder.name = (TextView) convertView.findViewById(R.id.theme_name);
        viewHolder.gameCheck = (ImageView) convertView.findViewById(R.id.theme_check);
        viewHolder.gameChecked = (ImageView) convertView.findViewById(R.id.theme_checked);

        Theme theme = themes.get(position);

        viewHolder.akalana.setTheme(this.themeManager.getTheme(theme.getId()));
        viewHolder.akalana.setTouchable(false);
        viewHolder.akalana.setMovablePawnsShown(false);
        viewHolder.akalana.setMovablePositionsShown(false);
        viewHolder.akalana.setRemovablePawnsShown(false);
        viewHolder.akalana.setTraveledPositionsShown(false);
        viewHolder.akalana.getAkalana().setLineWidth(2);
        viewHolder.akalana.setEngine(engine);
        viewHolder.akalana.updateConfigFromEngine();

        viewHolder.name.setText(theme.getName());

        viewHolder.gameCheck.setVisibility(position != selectedTheme ? View.VISIBLE : View.GONE);
        viewHolder.gameChecked.setVisibility(position == selectedTheme ? View.VISIBLE : View.GONE);

        return convertView;
    }

    private static class ViewHolder {
        public AkalanaView akalana;
        public TextView name;
        public ImageView gameCheck;
        public ImageView gameChecked;
    }

    public int getSelectedTheme() {
        return selectedTheme;
    }

    public void setSelectedTheme(int selectedTheme) {
        this.selectedTheme = selectedTheme;
        notifyDataSetChanged();
    }

}