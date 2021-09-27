package com.tambyy.fanoronaakalana;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
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

        setupPopupSize();

        List<Integer> times = new ArrayList<>();

        for (int i = 1; i <= 30; i++) {
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

    private void setupPopupSize() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;

        getWindow().setLayout((int) (width * 0.4), WindowManager.LayoutParams.WRAP_CONTENT);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;

        getWindow().setAttributes(params);
    }

}