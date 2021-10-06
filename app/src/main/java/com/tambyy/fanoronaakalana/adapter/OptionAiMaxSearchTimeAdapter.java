package com.tambyy.fanoronaakalana.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tambyy.fanoronaakalana.R;

import java.util.List;

public class OptionAiMaxSearchTimeAdapter extends BaseAdapter {

    // FOR DATA
    private List<Integer> times;

    Context context;
    LayoutInflater inflater;

    public OptionAiMaxSearchTimeAdapter(Context context, List<Integer> times) {
        this.context = context;
        this.times = times;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return times.size();
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

        OptionAiMaxSearchTimeAdapter.ViewHolder viewHolder = null;

        if (convertView == null) {
            viewHolder = new OptionAiMaxSearchTimeAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.item_option_ai_max_search_time, parent,false);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (OptionAiMaxSearchTimeAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.aiMaxSearchTimeValue = (TextView) convertView.findViewById(R.id.option_ia_max_search_time_value);

        viewHolder.aiMaxSearchTimeValue.setText(times.get(position) + "s");

        return convertView;
    }

    private static class ViewHolder{
        public TextView aiMaxSearchTimeValue;
    }
}
