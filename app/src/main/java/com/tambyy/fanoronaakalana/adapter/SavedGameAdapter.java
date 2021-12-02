package com.tambyy.fanoronaakalana.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tambyy.fanoronaakalana.R;
import com.tambyy.fanoronaakalana.config.Theme;
import com.tambyy.fanoronaakalana.engine.Engine;
import com.tambyy.fanoronaakalana.engine.EngineAction;
import com.tambyy.fanoronaakalana.graphics.customview.AkalanaView;
import com.tambyy.fanoronaakalana.models.Game;
import com.tambyy.fanoronaakalana.utils.EngineActionsConverter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SavedGameAdapter extends BaseAdapter {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");

    private final List<Game> selectedGames = new ArrayList<>();

    // FOR DATA
    private final List<Game> games;
    private final Engine engine;
    private final Context context;
    private final LayoutInflater inflater;
    private boolean showSelectionBox = false;
    private Theme theme = new Theme();

    public SavedGameAdapter(Context context, List<Game> games, Engine engine) {
        this.context = context;
        this.games = games;
        this.engine = engine;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return games.size();
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

        SavedGameAdapter.ViewHolder viewHolder = null;

        if (convertView == null) {
            viewHolder = new SavedGameAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.item_saved_game, parent, false);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SavedGameAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.gameName = (TextView) convertView.findViewById(R.id.saved_game_name);
        viewHolder.gameDate = (TextView) convertView.findViewById(R.id.saved_game_date);
        viewHolder.akalana = (AkalanaView) convertView.findViewById(R.id.saved_game_akalana);

        Game game = games.get(position);

        viewHolder.gameName.setText(game.getName());
        viewHolder.gameDate.setText(DATE_FORMAT.format(game.getCreated_at()));

        viewHolder.akalana.setTouchable(false);
        viewHolder.akalana.setMovablePawnsShown(false);
        viewHolder.akalana.setMovablePositionsShown(false);
        viewHolder.akalana.setRemovablePawnsShown(false);
        viewHolder.akalana.setTraveledPositionsShown(false);
        viewHolder.akalana.getAkalana().setLineWidth(2f);
        viewHolder.akalana.setTheme(theme);

        int i = 0;
        List<EngineAction> actions = EngineActionsConverter.stringToEngineActions(game.getConfigs());
        for (EngineAction engineAction: actions) {
            engineAction.applyToEngine(engine);

            if (i == game.getImage_index())
                break;

            i++;
        }
        viewHolder.akalana.setEngine(engine);
        viewHolder.akalana.updateConfigFromEngine();

        viewHolder.gameCheck = (ImageView) convertView.findViewById(R.id.saved_game_check);
        viewHolder.gameChecked = (ImageView) convertView.findViewById(R.id.saved_game_checked);

        boolean gameSelected = isGameSelected(game);
        viewHolder.gameCheck.setVisibility(showSelectionBox && !gameSelected ? View.VISIBLE : View.GONE);
        viewHolder.gameChecked.setVisibility(gameSelected ? View.VISIBLE : View.GONE);

        return convertView;
    }

    private static class ViewHolder {
        public TextView gameName;
        public TextView gameDate;
        public AkalanaView akalana;
        public ImageView gameCheck;
        public ImageView gameChecked;
    }

    public int getSelectedGamesCount() {
        return selectedGames.size();
    }

    public boolean isGameSelected(Game game) {
        return selectedGames.contains(game);
    }

    public void toggleSelectGame(Game game) {
        if (isGameSelected(game)) {
            unselectGame(game);
        } else {
            selectGame(game);
        }
    }

    public void selectGame(Game game) {
        selectedGames.add(game);
        notifyDataSetChanged();
    }

    public void unselectGame(Game game) {
        selectedGames.remove(game);
        notifyDataSetChanged();
    }

    public void clearSelectedGames() {
        selectedGames.clear();
        notifyDataSetChanged();
    }

    public List<Game> getSelectedGames() {
        return selectedGames;
    }

    public void setShowSelectionBox(boolean showSelectionBox) {
        this.showSelectionBox = showSelectionBox;
        notifyDataSetChanged();
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
        notifyDataSetChanged();
    }
}