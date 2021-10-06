package com.tambyy.fanoronaakalana.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tambyy.fanoronaakalana.R;
import com.tambyy.fanoronaakalana.models.AIStat;

import java.util.List;

public class OptionAiLevelAdapter extends BaseAdapter {

    // FOR DATA
    private List<AIStat> aiStats;

    Context context;
    LayoutInflater inflater;

    public OptionAiLevelAdapter(Context context, List<AIStat> aiStats) {
        this.context = context;
        this.aiStats = aiStats;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return aiStats.size();
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

        ViewHolder viewHolder = null;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_option_ai_level, parent,false);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.aiLevelValue = (TextView) convertView.findViewById(R.id.option_ai_level_value);

        viewHolder.aiLevelValue.setText(aiStats.get(position).getLevel() + "");

        return convertView;
    }

    private static class ViewHolder{
        public TextView aiLevelValue;
    }
}