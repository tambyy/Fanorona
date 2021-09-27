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
import com.tambyy.fanoronaakalana.engine.EngineAction;
import com.tambyy.fanoronaakalana.engine.EngineActionSelectPiece;
import com.tambyy.fanoronaakalana.graphics.customview.AkalanaView;
import com.tambyy.fanoronaakalana.models.Game;
import com.tambyy.fanoronaakalana.utils.EngineActionsConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EditGameImageAdapter extends BaseAdapter {

    int selectedImage = 0;

    // FOR DATA
    private final List<EngineAction> actions;
    private final List<EngineAction> actionsToShow;
    private final Engine engine;
    private final Context context;
    private final LayoutInflater inflater;

    public EditGameImageAdapter(Context context, List<EngineAction> actions, Engine engine) {
        this.context = context;
        this.actions = actions;
        this.engine = engine;
        this.actionsToShow = getEngineActionToShow(actions);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return actionsToShow.size();
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

        EditGameImageAdapter.ViewHolder viewHolder = null;

        if (convertView == null) {
            viewHolder = new EditGameImageAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.edit_game_image_item, parent, false);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (EditGameImageAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.akalana = (AkalanaView) convertView.findViewById(R.id.edit_game_akalana);
        viewHolder.gameCheck = (ImageView) convertView.findViewById(R.id.edit_game_check);
        viewHolder.gameChecked = (ImageView) convertView.findViewById(R.id.edit_game_checked);

        viewHolder.akalana.setTouchable(false);
        viewHolder.akalana.setMovablePawnsShown(false);
        viewHolder.akalana.setMovablePositionsShown(false);
        viewHolder.akalana.setRemovablePawnsShown(false);
        viewHolder.akalana.setTraveledPositionsShown(false);
        viewHolder.akalana.getAkalana().setLineWidth(2);

        EngineAction currentAction = actionsToShow.get(position);
        for (EngineAction engineAction: actions) {
            engineAction.applyToEngine(engine);

            if (currentAction.equals(engineAction))
                break;
        }
        viewHolder.akalana.setEngine(engine);
        viewHolder.akalana.updateConfigFromEngine();

        viewHolder.gameCheck.setVisibility(position != selectedImage ? View.VISIBLE : View.GONE);
        viewHolder.gameChecked.setVisibility(position == selectedImage ? View.VISIBLE : View.GONE);

        return convertView;
    }

    private static class ViewHolder {
        public AkalanaView akalana;
        public ImageView gameCheck;
        public ImageView gameChecked;
    }

    /**
     *
     * @param actions
     * @return
     */
    private List<EngineAction> getEngineActionToShow(List<EngineAction> actions) {
        List<EngineAction> act = new ArrayList<>();
        for (EngineAction action : actions) {
            if (action instanceof EngineActionSelectPiece) {
                act.add(action);
            }
        }

        EngineAction lastAction = actions.get(actions.size() - 1);

        if (!(lastAction instanceof EngineActionSelectPiece)) {
            act.add(lastAction);
        }

        return act;
    }

    public int getSelectedImage() {
        return selectedImage;
    }

    public void setSelectedImage(int selectedImage) {
        this.selectedImage = selectedImage;
        notifyDataSetChanged();
    }

    public List<EngineAction> getActionsToShow() {
        return actionsToShow;
    }

    public List<EngineAction> getActions() {
        return actions;
    }
}