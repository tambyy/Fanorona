package com.tambyy.fanoronaakalana.utils;

import android.util.Log;

import com.tambyy.fanoronaakalana.OptionActivity;
import com.tambyy.fanoronaakalana.engine.Engine;
import com.tambyy.fanoronaakalana.engine.EngineAction;
import com.tambyy.fanoronaakalana.engine.EngineActionMovesSequenceOver;
import com.tambyy.fanoronaakalana.engine.EngineActionSelectPiece;

import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class EngineActionsHistoryManager {

    /**
     * Engine action history
     */
    private final Stack<EngineAction> history = new Stack<>();

    /**
     *
     */
    private int historyIndex = -1;

    /**
     * append a new action in history
     * @param action
     */
    public void addHistory(EngineAction action) {

        // we don't need a move sequence over action
        // because it does nothing
        if (!(action instanceof EngineActionMovesSequenceOver)) {

            // before adding a new action to history
            // pop all action in history that we don't need anymore
            while (historyIndex < history.size() - 1) {
                history.pop();
            }

            // if action is a piece selection
            // pop last engine action
            // if it is also a piece selection action
            if (history.size() > 0 && action instanceof EngineActionSelectPiece && history.lastElement() instanceof EngineActionSelectPiece) {
                history.pop();
                --historyIndex;
            }

            // add we finally push new action
            history.push(action);
            ++historyIndex;
        }
    }

    /**
     *
     */
    public void clearHistory() {
        history.clear();
        historyIndex = -1;
    }

    /**
     *
     * @param actions
     * @param historyIndex
     */
    public void restoreHistory(List<EngineAction> actions, int historyIndex) {
        clearHistory();

        // get previous game history
        for (EngineAction action : actions) {
            addHistory(action);
        }

        // get previous game history index
        setHistoryIndex(historyIndex);
    }

    /**
     * move to previous action
     */
    public void backwardHistory() {
        historyIndex = 0;
    }

    /**
     * move to previous action
     */
    public void forwardHistory() {
        historyIndex = history.size() - 1;
    }

    /**
     * move to previous action
     */
    public boolean prevHistory(boolean vsAi, boolean aiBlack) {
        if (hasPrevHistory()) {
            do {
                --historyIndex;
                // we ignore piece selection action
                // and AI action when we want to go to the previous action
            } while (hasPrevHistory() && ((vsAi && history.get(historyIndex + 1).isBlack() == aiBlack) || history.get(historyIndex) instanceof EngineActionSelectPiece));

            return true;
        }

        return false;
    }

    /**
     * move to next action
     */
    public boolean nextHistory(boolean vsAi, boolean aiBlack) {
        if (hasNextHistory()) {
            do {
                ++historyIndex;
                // we ignore piece selection action
                // and AI action when we want to go to the next action
            } while (hasNextHistory() && ((vsAi && history.get(historyIndex + 1).isBlack() == aiBlack) || history.get(historyIndex) instanceof EngineActionSelectPiece));

            return true;
        }

        return false;
    }

    /**
     * has prev action
     */
    public boolean hasPrevHistory() {
        return historyIndex > 0;
    }

    /**
     * has next action
     */
    public boolean hasNextHistory() {
        return historyIndex < history.size() - 1;
    }

    /**
     *
     * @param engine
     */
    public void applyToEngine(Engine engine) {

        // we apply all action from 0 to historyIndex
        // to the engine
        // to obtain the current state of thz engine
        Iterator<EngineAction> it = history.iterator();
        for (int i = 0; i <= historyIndex; ++i) {
            it.next().applyToEngine(engine);
        }
    }

    /**
     *
     * @return
     */
    public Stack<EngineAction> getHistory() {
        return history;
    }

    /**
     *
     * @return
     */
    public int getHistoryIndex() {
        return historyIndex;
    }

    /**
     *
     * @param historyIndex
     */
    public void setHistoryIndex(int historyIndex) {
        this.historyIndex = historyIndex;
    }
}
