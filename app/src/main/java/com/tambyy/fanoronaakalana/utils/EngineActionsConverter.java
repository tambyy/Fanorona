package com.tambyy.fanoronaakalana.utils;

import com.tambyy.fanoronaakalana.engine.Engine;
import com.tambyy.fanoronaakalana.engine.EngineAction;
import com.tambyy.fanoronaakalana.engine.EngineActionInit;
import com.tambyy.fanoronaakalana.engine.EngineActionMoveSelectedPiece;
import com.tambyy.fanoronaakalana.engine.EngineActionSelectPiece;
import com.tambyy.fanoronaakalana.engine.EngineActionStopMove;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class EngineActionsConverter {

    private static final String DEFAULT_WHITE_POSITIONS = "A1,A2,A3,A4,A5,A6,A7,A8,A9,B1,B2,B3,B4,B5,B6,B7,B8,B9,C2,C4,C7,C9";
    private static final String DEFAULT_BLACK_POSITIONS = "C1,C3,C6,C8,D1,D2,D3,D4,D5,D6,D7,D8,D9,E1,E2,E3,E4,E5,E6,E7,E8,E9";

    public static String positionToString(Engine.Point point) {
        return ((char) (point.x + 65)) + "" + (point.y + 1);
    }

    public static Engine.Point stringToPosition(String point) {
        return new Engine.Point((int) (point.charAt(0) - 65), (int) (point.charAt(1) - 49));
    }

    public static String positionsToString(Engine.Point[] positions) {
        if (positions.length == 0)  {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (Engine.Point position : positions) {
            sb.append(positionToString(position));
            sb.append(',');
        }
        return sb.substring(0, sb.length() - 1);
    }

    public static Engine.Point[] stringToPositions(String positions) {
        List<Engine.Point> list = new ArrayList<>();
        for (String s : positions.split(",")) {
            Engine.Point point = stringToPosition(s);
            list.add(point);
        }
        return list.toArray(new Engine.Point[0]);
    }

    private static List<EngineAction> stringToMovesSequence(String actions, boolean black) {
        List<EngineAction> engineActions = new ArrayList<>();

        Engine.Point selectedPosition = null, prevSelectedPosition = null;

        boolean first = true;
        for (String s: actions.split("(?<=\\:|<|>)")) {
            EngineAction action;
            long time = -1;

            int indexOfTimeSep = s.indexOf('\'');
            if (indexOfTimeSep >= 0) {
                time = Long.parseLong(s.substring(0, indexOfTimeSep));
                s = s.substring(indexOfTimeSep + 1);
            }

            if (first) {
                first = false;
                selectedPosition = stringToPosition(s);
                action = new EngineActionSelectPiece(black, selectedPosition);
            } else if (s.startsWith("X")) {
                action = new EngineActionStopMove(black);
            } else {
                prevSelectedPosition = selectedPosition;
                selectedPosition = stringToPosition(s);
                action = new EngineActionMoveSelectedPiece(black, new Engine.Move(selectedPosition.minus(prevSelectedPosition), s.charAt(2) == '>'));
            }

            if (time >= 0) {
                action.setTime(time);
            }
            engineActions.add(action);
        }

        return engineActions;
    }

    public static String engineActionsToString(Collection<? extends EngineAction> actions) {
        return engineActionsToString(actions, false);
    }

    public static String engineActionsToString(Collection<? extends EngineAction> actions, boolean withTimes) {
        StringBuilder result = new StringBuilder();
        Engine.Point selectedPosition = null;

        for (final java.util.Iterator<? extends EngineAction> it = actions.iterator(); it.hasNext();) {
            EngineAction action = it.next();

            String timeMarker = withTimes ? action.getTime() + "'" : "";

            if (action instanceof EngineActionInit) {
                EngineActionInit actionInit = ((EngineActionInit) action);
                String blackPos = positionsToString(actionInit.getBlackPositions()),
                        whitePos = positionsToString(actionInit.getWhitePositions());

                if (!blackPos.equals(DEFAULT_BLACK_POSITIONS) || !whitePos.equals(DEFAULT_WHITE_POSITIONS)) {
                    result.append('\n').append("A:").append(blackPos);
                    result.append('\n').append("B:").append(whitePos);
                }
                if (!actionInit.isBlackFirst()) {
                    result.append('\n').append("P:").append(actionInit.isBlackFirst() ? 'A' : 'B');
                }
                if (!(actionInit.getMode() == 0)) {
                    result.append('\n').append("V:").append(actionInit.getMode());
                }
            } else if (action instanceof EngineActionSelectPiece) {
                EngineActionSelectPiece actionSelectPiece = ((EngineActionSelectPiece) action);
                selectedPosition = actionSelectPiece.getPosition();
                result.append('\n').append(timeMarker).append(positionToString(selectedPosition)).append(':');
            } else if (action instanceof EngineActionMoveSelectedPiece) {
                EngineActionMoveSelectedPiece actionMoveSelectedPiece = ((EngineActionMoveSelectedPiece) action);
                selectedPosition = selectedPosition.add(actionMoveSelectedPiece.getMove().vector);
                result.append(timeMarker).append(positionToString(selectedPosition)).append(actionMoveSelectedPiece.getMove().percute ? '>' : '<');
            } else if (action instanceof EngineActionStopMove) {
                result.append(timeMarker).append('X');
            }
        }

        return result.toString().trim();
    }

    public static List<EngineAction> stringToEngineActions(String actions) {
        List<EngineAction> engineActions = new ArrayList<>();

        boolean currentBlack = true;
        EngineActionInit actionInit = new EngineActionInit(
                stringToPositions(DEFAULT_BLACK_POSITIONS),
                stringToPositions(DEFAULT_WHITE_POSITIONS),
                0,
                currentBlack
        );
        engineActions.add(actionInit);

        boolean first = true;
        for (String s: actions.split("\n")) {
            s = s.trim();

            if (s.startsWith("A:")) {
                actionInit.setBlackPositions(stringToPositions(s.substring(2)));
            } else if (s.startsWith("B:")) {
                actionInit.setWhitePositions(stringToPositions(s.substring(2)));
            } else if (s.startsWith("P:")) {
                actionInit.setBlackFirst(currentBlack = (s.charAt(2) == 'A'));
            } else if (s.startsWith("V:")) {
                actionInit.setMode((int) (s.charAt(2) - 48));
            } else if (!s.isEmpty()) {
                engineActions.addAll(stringToMovesSequence(s, currentBlack));
                currentBlack = !currentBlack;
            }
        }

        return engineActions;
    }

}
