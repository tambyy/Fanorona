package com.tambyy.fanoronaakalana.engine;

import android.content.res.AssetManager;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.RequiresApi;

public class Engine {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private native long init(int searchCount);
    private native void initBoard(long fanoronaEngineManagerID);
    private native void terminate(long fanoronaEngineManagerID);
    private native void loadTTFile(long fanoronaEngineManagerID, AssetManager assetManager);

    private native void setMode(long fanoronaEngineManagerID, int mode);
    private native int getVela(long fanoronaEngineManagerID);
    private native int getVelaBlack(long fanoronaEngineManagerID);
    private native void setFirstPlayerBlack(long fanoronaEngineManagerID, int black);
    private native int getFirstPlayerBlack(long fanoronaEngineManagerID);
    private native int getCurrentPlayerBlack(long fanoronaEngineManagerID);
    private native void setCurrentPlayerBlack(long fanoronaEngineManagerID, int black);

    private native String blackPosition(long fanoronaEngineManagerID);
    private native String whitePosition(long fanoronaEngineManagerID);
    private native void setPositions(long fanoronaEngineManagerID, String black, String white);

    private native int selectPiece(long fanoronaEngineManagerID, int x, int y, String traveledPositions, int lastVector);
    private native String selectedPiece(long fanoronaEngineManagerID);
    private native String movePiece(long fanoronaEngineManagerID, int vector, int percute);
    private native String removedPieces(long fanoronaEngineManagerID, int vector, int percute);
    private native int stopMove(long fanoronaEngineManagerID);
    private native String movablePieces(long fanoronaEngineManagerID);
    private native String movableVectors(long fanoronaEngineManagerID);
    private native int canCatch(long fanoronaEngineManagerID, int vector, int percute);
    private native int currentBlack(long fanoronaEngineManagerID);
    private native int moveSessionOpened(long fanoronaEngineManagerID);
    private native String traveledPositions(long fanoronaEngineManagerID);
    private native int lastVector(long fanoronaEngineManagerID);

    private native String search(long fanoronaEngineManagerID, int index);
    private native void ponder(long fanoronaEngineManagerID, int index);
    private native void setSearchDepth(long fanoronaEngineManagerID, int index, int depth);
    private native void setSearchMaxTime(long fanoronaEngineManagerID, int index, int maxTime);
    private native int getLastSearchTime(long fanoronaEngineManagerID, int index);
    private native int getLastSearchDepth(long fanoronaEngineManagerID, int index);
    private native int getLastNodesCount(long fanoronaEngineManagerID, int index);
    private native void stopSearch(long fanoronaEngineManagerID, int index);
    private native void clearTT(long fanoronaEngineManagerID, int index);

    private native int gameOver(long fanoronaEngineManagerID);
    private native int winner(long fanoronaEngineManagerID);


    /**
     *
     */
    public static class Point {

        public int x, y;

        public Point() {
            this.x = 0;
            this.y = 0;
        }

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Point(Point p) {
            x = p.x;
            y = p.y;
        }

        public Point add(Point p) {
            return new Point(x + p.x, y + p.y);
        }

        public Point minus(Point p) {
            return new Point(x - p.x, y - p.y);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Point) {
                return x == ((Point) obj).x && y == ((Point) obj).y;
            }

            return false;
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    /**
     *
     */
    public static class Move {

        public final Point vector;
        public final boolean percute;

        public Move(Point vector, boolean percute) {
            this.vector = vector;
            this.percute = percute;
        }

        @Override
        public String toString() {
            return "(" + vectorToString(vector) + ", " + percute + ")";
        }

    }

    /**
     *
     */
    public static class PieceMoveSession {

        public final Point originPosition;
        public final List<Move> moves;

        public PieceMoveSession(Point originPosition, Move[] moves) {
            this.originPosition = originPosition;
            this.moves = new ArrayList<>();

            for (Move move : moves) {
                this.moves.add(move);
            }
        }

        @Override
        public String toString() {
            String s = originPosition.toString();

            for (Move move : moves) {
                s += " > " + move;
            }

            return s;
        }

    }


    public static final Point TOP    = new Point(-1, 0);
    public static final Point RIGHT  = new Point(0, 1);
    public static final Point BOTTOM = new Point(1, 0);
    public static final Point LEFT   = new Point(0, -1);

    public static final Point LEFT_TOP     = new Point(-1, -1);
    public static final Point RIGHT_TOP    = new Point(-1, 1);
    public static final Point RIGHT_BOTTOM = new Point(1, 1);
    public static final Point LEFT_BOTTOM  = new Point(1, -1);

    public static final Point NULL_VECTOR = new Point(0, 0);


    public static final int MODE_CLASSIQUE = 0;
    public static final int MODE_VELA_BLACK = 1;
    public static final int MODE_VELA_WHITE = 2;


    public static String vectorToString(Point vector) {
        if (vector.equals(TOP)) {
            return "TOP";
        } else if (vector.equals(RIGHT)) {
            return "RIGHT";
        } else if (vector.equals(BOTTOM)) {
            return "BOTTOM";
        } else if (vector.equals(LEFT)) {
            return "LEFT";
        } else if (vector.equals(LEFT_TOP)) {
            return "LEFT_TOP";
        } else if (vector.equals(RIGHT_TOP)) {
            return "RIGHT_TOP";
        } else if (vector.equals(RIGHT_BOTTOM)) {
            return "RIGHT_BOTTOM";
        } else if (vector.equals(LEFT_BOTTOM)) {
            return "LEFT_BOTTOM";
        } else {
            return "NULL";
        }
    }

    public static int vectorToInt(Point vector) {
        if (vector.equals(TOP)) {
            return 8;
        } else if (vector.equals(RIGHT)) {
            return 6;
        } else if (vector.equals(BOTTOM)) {
            return 2;
        } else if (vector.equals(LEFT)) {
            return 4;
        } else if (vector.equals(LEFT_TOP)) {
            return 7;
        } else if (vector.equals(RIGHT_TOP)) {
            return 9;
        } else if (vector.equals(RIGHT_BOTTOM)) {
            return 3;
        } else if (vector.equals(LEFT_BOTTOM)) {
            return 1;
        } else {
            return 0;
        }
    }

    private static Point intToVector(int i) {
        switch (i) {
            case 8:
                return TOP;
            case 6:
                return RIGHT;
            case 2:
                return BOTTOM;
            case 4:
                return LEFT;
            case 7:
                return LEFT_TOP;
            case 9:
                return RIGHT_TOP;
            case 3:
                return RIGHT_BOTTOM;
            case 1:
                return LEFT_BOTTOM;
            default:
                return NULL_VECTOR;
        }
    }

    private static int charToInt(char c) {
        return c - 48;
    }

    private static char intToChar(int i) {
        return (char) (i + 48);
    }

    private static Point[] positionsToArrayPoint(String s) {
        Point positions[] = new Point[s.length() / 2];

        for (int i = 0, length = s.length(); i < length; i += 2) {
            positions[i / 2] = new Point(
                    charToInt(s.charAt(i)),
                    charToInt(s.charAt(i + 1))
            );
        }

        return positions;
    }

    private static String arrayPointToString(Point[] positions) {
        String s = "";

        for (Point p : positions) {
            s += intToChar(p.x);
            s += intToChar(p.y);
        }

        return s;
    }

    private static Move[] movesToArrayMove(String s) {
        Move moves[] = new Move[s.length() / 2];

        for (int i = 0, length = s.length(); i < length; i += 2) {
            moves[i / 2] = new Move(
                    intToVector(charToInt(s.charAt(i))),
                    charToInt(s.charAt(i + 1)) == 1
            );
        }

        return moves;
    }

    private static PieceMoveSession stringToPieceMoveSession(String s) {
        return new PieceMoveSession(
                new Point(
                        charToInt(s.charAt(0)),
                        charToInt(s.charAt(1))
                ),
                movesToArrayMove(s.substring(2))
        );
    }

    long fanoronaEngineManagerID = 0;
    boolean searching = false;

    public Engine(int searchCount) {
        fanoronaEngineManagerID = init(searchCount);
    }

    private boolean available() {
        return
                fanoronaEngineManagerID != 0 &&
                        !searching;
    }

    public void initBoard() {
        if (this.available()) {
            initBoard(fanoronaEngineManagerID);
        }
    }

    public void terminate() {
        if (fanoronaEngineManagerID != 0) {
            terminate(fanoronaEngineManagerID);
            fanoronaEngineManagerID = 0;
        }
    }

    public void loadTTFile(AssetManager assetManager) {
        loadTTFile(fanoronaEngineManagerID, assetManager);
    }

    public int getMode() {
        if (this.available()) {
            if (this.getVela(fanoronaEngineManagerID) == 0) {
                return MODE_CLASSIQUE;
            } else if (this.getVelaBlack(fanoronaEngineManagerID) == 0) {
                return MODE_VELA_WHITE;
            } else {
                return MODE_VELA_BLACK;
            }
        }

        return MODE_CLASSIQUE;
    }

    public void setMode(int mode) {
        if (this.available()) {
            setMode(fanoronaEngineManagerID, mode);
        }
    }

    public Boolean getVela() {
        if (this.available()) {
            return getVela(fanoronaEngineManagerID) == 1 ? getVelaBlack(fanoronaEngineManagerID) == 1 : null;
        }

        return null;
    }

    public void setFirstPlayerBlack(boolean black) {
        if (this.available()) {
            setFirstPlayerBlack(fanoronaEngineManagerID, black ? 1 : 0);
        }
    }

    public boolean getFirstPlayerBlack() {
        if (this.available()) {
            return getFirstPlayerBlack(fanoronaEngineManagerID) == 1;
        }

        return false;
    }

    public void setCurrentPlayerBlack(boolean black) {
        if (this.available()) {
            setCurrentPlayerBlack(fanoronaEngineManagerID, black ? 1 : 0);
        }
    }

    public boolean getCurrentPlayerBlack() {
        if (this.available()) {
            return getCurrentPlayerBlack(fanoronaEngineManagerID) == 1;
        }

        return false;
    }

    public Point[] blackPosition() {
        if (this.available()) {
            return positionsToArrayPoint(
                    blackPosition(fanoronaEngineManagerID)
            );
        }

        return new Point[0];
    }

    public Point[] whitePosition() {
        if (this.available()) {
            return positionsToArrayPoint(
                    whitePosition(fanoronaEngineManagerID)
            );
        }

        return new Point[0];
    }

    public void setPositions(Point[] black, Point[] white) {
        if (this.available()) {
            setPositions(
                    fanoronaEngineManagerID,
                    arrayPointToString(black),
                    arrayPointToString(white)
            );
        }
    }


    public boolean selectPiece(int x, int y) {
        return selectPiece(x, y, new Point[]{}, NULL_VECTOR);
    }

    public boolean selectPiece(int x, int y, Point[] traveledPositions, Point lastVector) {
        if (this.available()) {
            return selectPiece(fanoronaEngineManagerID, x, y, arrayPointToString(traveledPositions), vectorToInt(lastVector)) == 1;
        }

        return false;
    }

    public Point lastVector() {
        if (this.available()) {
            return intToVector(lastVector(fanoronaEngineManagerID));
        }

        return null;
    }

    public Point selectedPiece() {
        if (this.available()) {
            String s = selectedPiece(fanoronaEngineManagerID);

            Point p = new Point(charToInt(s.charAt(0)), charToInt(s.charAt(1)));

            return p.x == -1 || p.y == -1 ? null : p;
        }

        return null;
    }

    public Point[] movePiece(Point vector, boolean percute) {
        if (this.available()) {
            return positionsToArrayPoint(
                    movePiece(
                            fanoronaEngineManagerID,
                            vectorToInt(vector),
                            percute ? 1 : 0
                    )
            );
        }

        return new Point[0];
    }

    public Point[] removedPieces(Point vector, boolean percute) {
        if (this.available()) {
            return positionsToArrayPoint(
                    removedPieces(
                            fanoronaEngineManagerID,
                            vectorToInt(vector),
                            percute ? 1 : 0
                    )
            );
        }

        return new Point[0];
    }

    public boolean stopMove() {
        if (this.available()) {
            return stopMove(fanoronaEngineManagerID) == 1;
        }

        return false;
    }

    public Point[] movablePieces() {
        if (this.available()) {
            return positionsToArrayPoint(
                    movablePieces(fanoronaEngineManagerID)
            );
        }

        return new Point[0];
    }

    public Move[] movableVectors() {
        if (this.available()) {
            return movesToArrayMove(
                    movableVectors(fanoronaEngineManagerID)
            );
        }

        return new Move[0];
    }

    public boolean canCatch(Point vector, boolean percute) {
        if (this.available()) {
            return canCatch(fanoronaEngineManagerID, vectorToInt(vector), percute ? 1 : 0) == 1;
        }

        return false;
    }

    public boolean currentBlack() {
        if (this.available()) {
            return currentBlack(fanoronaEngineManagerID) == 1;
        }

        return false;
    }

    public boolean moveSessionOpened() {
        if (this.available()) {
            return moveSessionOpened(fanoronaEngineManagerID) == 1;
        }

        return false;
    }

    public Point[] traveledPositions() {
        if (this.available()) {
            return positionsToArrayPoint(
                    traveledPositions(fanoronaEngineManagerID)
            );
        }

        return new Point[0];
    }


    public PieceMoveSession search(int index) {
        if (this.available()) {
            searching = true;

            PieceMoveSession pms = stringToPieceMoveSession(
                    search(fanoronaEngineManagerID, index)
            );

            searching = false;

            return pms;
        }

        return null;
    }

    public void ponder(int index) {
        if (this.available()) {
            ponder(fanoronaEngineManagerID, index);
        }
    }

    public void setSearchDepth(int index, int depth) {
        if (this.available()) {
            setSearchDepth(fanoronaEngineManagerID, index, depth);
        }
    }

    public void setSearchMaxTime(int index, int maxTime) {
        if (this.available()) {
            setSearchMaxTime(fanoronaEngineManagerID, index, maxTime);
        }
    }

    public int getLastSearchTime(int index) {
        if (this.available()) {
            return getLastSearchTime(fanoronaEngineManagerID, index);
        }

        return 0;
    }

    public int getLastSearchDepth(int index) {
        if (this.available()) {
            return getLastSearchDepth(fanoronaEngineManagerID, index);
        }

        return 0;
    }

    public int getLastNodesCount(int index) {
        if (this.available()) {
            return getLastNodesCount(fanoronaEngineManagerID, index);
        }

        return 0;
    }

    public void stopSearch(int index) {
        if (fanoronaEngineManagerID != 0) {
            stopSearch(fanoronaEngineManagerID, index);
        }
    }

    public void clearTT(int index) {
        if (this.available()) {
            clearTT(fanoronaEngineManagerID, index);
        }
    }

    public boolean gameOver() {
        if (this.available()) {
            return gameOver(fanoronaEngineManagerID) == 1;
        }

        return true;
    }

    public boolean winner() {
        if (this.available()) {
            return winner(fanoronaEngineManagerID) == 1;
        }

        return true;
    }
}
