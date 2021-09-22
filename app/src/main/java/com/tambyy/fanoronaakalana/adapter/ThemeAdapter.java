package com.tambyy.fanoronaakalana.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tambyy.fanoronaakalana.Constants;
import com.tambyy.fanoronaakalana.R;
import com.tambyy.fanoronaakalana.engine.Engine;
import com.tambyy.fanoronaakalana.graphics.customview.AkalanaView;
import com.tambyy.fanoronaakalana.models.Theme;
import com.tambyy.fanoronaakalana.utils.ThemeManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.cardview.widget.CardView;

public class ThemeAdapter extends BaseAdapter {

    int selectedTheme = -1;

    // FOR DATA
    private final Engine engine;
    private final List<Theme> themes;
    private final Map<Long, com.tambyy.fanoronaakalana.config.Theme> configThemes = new HashMap<>();
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

        final ThemeAdapter.ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ThemeAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.theme_item, parent, false);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ThemeAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.container = (CardView) convertView.findViewById(R.id.theme_container);
        viewHolder.akalana = (AkalanaView) convertView.findViewById(R.id.theme_akalana);

        Theme theme = themes.get(position);

        viewHolder.container.setBackgroundTintList(position == selectedTheme ? Constants.SELECTED_OPTION_BC : Constants.OPTION_BC);

        viewHolder.akalana.setTouchable(false);
        viewHolder.akalana.setMovablePawnsShown(false);
        viewHolder.akalana.setMovablePositionsShown(false);
        viewHolder.akalana.setRemovablePawnsShown(false);
        viewHolder.akalana.setTraveledPositionsShown(false);
        viewHolder.akalana.getAkalana().setLineWidth(2);
        viewHolder.akalana.setEngine(engine);
        viewHolder.akalana.updateConfigFromEngine();

        com.tambyy.fanoronaakalana.config.Theme configTheme = configThemes.get(theme.getId());
        if (configTheme == null) {
            themeManager.getTheme(theme.getId(), th -> {
                if (th != null) {
                    configThemes.put(theme.getId(), th);
                    viewHolder.akalana.setTheme(th);
                }
            });
        } else {
            viewHolder.akalana.setTheme(configTheme);
        }

        return convertView;
    }

    private static class ViewHolder {
        public CardView container;
        public AkalanaView akalana;
    }

    public int getSelectedTheme() {
        return selectedTheme;
    }

    public void setSelectedTheme(int selectedTheme) {
        this.selectedTheme = selectedTheme;
        notifyDataSetChanged();
    }

}