package com.tambyy.fanoronaakalana;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;

import com.tambyy.fanoronaakalana.adapter.OptionAiMaxSearchTimeAdapter;

import java.util.ArrayList;
import java.util.List;

public class OptionAiMaxSearchTimeActivity extends AppCompatActivity {

    @BindView(R.id.option_ai_max_search_time_grid_view)
    GridView optionsAiMaxSearchTimeGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_ai_max_search_time);
        ButterKnife.bind(this);

        List<Float> times = new ArrayList<>();

        for (float i = 0.5f; i <= 20f; i += 0.5f) {
            times.add(i);
        }

        optionsAiMaxSearchTimeGridView.setAdapter(new OptionAiMaxSearchTimeAdapter(this, times));
        optionsAiMaxSearchTimeGridView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent();
            intent.putExtra(OptionActivity.AI_MAX_SEARCh_TIME_VALUE_CODE, (float) times.get(position));
            setResult(RESULT_OK, intent);
            finish();
        });
    }
}