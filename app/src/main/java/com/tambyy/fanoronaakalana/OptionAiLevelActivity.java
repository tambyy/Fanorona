package com.tambyy.fanoronaakalana;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;

import com.tambyy.fanoronaakalana.adapter.OptionAiLevelAdapter;
import com.tambyy.fanoronaakalana.models.AIStat;

import java.util.ArrayList;
import java.util.List;

public class OptionAiLevelActivity extends AppCompatActivity {

    @BindView(R.id.option_ai_level_grid_view)
    GridView optionsAiLevelGridView;

    private final List<AIStat> aiStats = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_ai_level);
        ButterKnife.bind(this);

        for (int i = 1; i <= 20; ++i) {
            aiStats.add(new AIStat(i, 0));
        }

        optionsAiLevelGridView.setAdapter(new OptionAiLevelAdapter(this, aiStats));
        optionsAiLevelGridView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent();
            intent.putExtra(OptionActivity.AI_LEVEL_VALUE_CODE, aiStats.get(position).getLevel());
            setResult(RESULT_OK, intent);
            finish();
        });
    }
}