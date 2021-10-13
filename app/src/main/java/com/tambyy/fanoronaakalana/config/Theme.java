package com.tambyy.fanoronaakalana.config;

import android.graphics.Bitmap;
import android.graphics.Color;

public class Theme {

    private int akalanaBgColor         = Color.rgb(198, 178, 158);
    private int akalanaLinesColor      = Color.argb(100, 56, 20, 5);

    private int whiteDefaultColor      = Color.rgb(255, 255, 255);
    private int whiteStrokeColor       = Color.argb(200, 0, 0, 0);

    private int blackDefaultColor      = Color.rgb(31, 16, 9);
    private int blackStrokeColor       = Color.argb(200, 255, 255, 255);

    private int movablePositionColor   = Color.argb(80, 110, 66, 39);
    private int removablePositionColor = Color.argb(80, 255, 150, 30);
    private int traveledPositionColor  = Color.argb(80, 255, 255, 255);

    private int blackBorderWidth = 0;
    private int whiteBorderWidth = 0;

    private Bitmap backgroundBitmap = null;
    private Bitmap akalanaBitmap = null;

    private Bitmap blackDefaultBitmap = null;
    private Bitmap blackMovableBitmap = null;
    private Bitmap blackSelectedBitmap = null;

    private Bitmap whiteDefaultBitmap = null;
    private Bitmap whiteMovableBitmap = null;
    private Bitmap whiteSelectedBitmap = null;

    private Bitmap movablePositionBitmap = null;
    private Bitmap traveledPositionBitmap = null;
    private Bitmap removablePositionBitmap = null;

    private Bitmap scaledBackgroundBitmap = null;
    private Bitmap scaledAkalanaBitmap = null;

    private Bitmap scaledBlackDefaultBitmap = null;
    private Bitmap scaledBlackMovableBitmap = null;
    private Bitmap scaledBlackSelectedBitmap = null;

    private Bitmap scaledWhiteDefaultBitmap = null;
    private Bitmap scaledWhiteMovableBitmap = null;
    private Bitmap scaledWhiteSelectedBitmap = null;

    private Bitmap scaledMovablePositionBitmap = null;
    private Bitmap scaledTraveledPositionBitmap = null;
    private Bitmap scaledRemovablePositionBitmap = null;

    public int getAkalanaBgColor() {
        return akalanaBgColor;
    }

    public void setAkalanaBgColor(int akalanaBgColor) {
        this.akalanaBgColor = akalanaBgColor;
    }

    public int getAkalanaLinesColor() {
        return akalanaLinesColor;
    }

    public void setAkalanaLinesColor(int akalanalinesColor) {
        this.akalanaLinesColor = akalanalinesColor;
    }

    public int getBlackDefaultColor() {
        return blackDefaultColor;
    }

    public void setBlackDefaultColor(int blackDefaultColor) {
        this.blackDefaultColor = blackDefaultColor;
    }

    public int getBlackStrokeColor() {
        return blackStrokeColor;
    }

    public int getWhiteDefaultColor() {
        return whiteDefaultColor;
    }

    public int getWhiteStrokeColor() {
        return whiteStrokeColor;
    }

    public int getBlackBorderWidth() {
        return blackBorderWidth;
    }

    public void setBlackBorderWidth(int blackBorderWidth) {
        this.blackBorderWidth = blackBorderWidth;
    }

    public int getWhiteBorderWidth() {
        return whiteBorderWidth;
    }

    public void setWhiteBorderWidth(int whiteBorderWidth) {
        this.whiteBorderWidth = whiteBorderWidth;
    }

    public void setWhiteDefaultColor(int whiteDefaultColor) {
        this.whiteDefaultColor = whiteDefaultColor;
    }

    public void setBlackStrokeColor(int blackStrokeColor) {
        this.blackStrokeColor = blackStrokeColor;
    }

    public void setWhiteStrokeColor(int whiteStrokeColor) {
        this.whiteStrokeColor = whiteStrokeColor;
    }

    public int getMovablePositionColor() {
        return movablePositionColor;
    }

    public void setMovablePositionColor(int movablePositionColor) {
        this.movablePositionColor = movablePositionColor;
    }

    public int getRemovablePositionColor() {
        return removablePositionColor;
    }

    public void setRemovablePositionColor(int removablePositionColor) {
        this.removablePositionColor = removablePositionColor;
    }

    public int getTraveledPositionColor() {
        return traveledPositionColor;
    }

    public void setTraveledPositionColor(int traveledPositionColor) {
        this.traveledPositionColor = traveledPositionColor;
    }

    public Bitmap getBackgroundBitmap() {
        return backgroundBitmap;
    }

    public void setBackgroundBitmap(Bitmap backgroundBitmap) {
        this.scaledBackgroundBitmap = this.backgroundBitmap = backgroundBitmap;
    }

    public Bitmap getAkalanaBitmap() {
        return akalanaBitmap;
    }

    public void setAkalanaBitmap(Bitmap akalanaBitmap) {
        this.scaledAkalanaBitmap = this.akalanaBitmap = akalanaBitmap;
    }

    public Bitmap getBlackDefaultBitmap() {
        return blackDefaultBitmap;
    }

    public void setBlackDefaultBitmap(Bitmap blackDefaultBitmap) {
        this.scaledBlackDefaultBitmap = this.blackDefaultBitmap = blackDefaultBitmap;
    }

    public Bitmap getBlackMovableBitmap() {
        return blackMovableBitmap;
    }

    public void setBlackMovableBitmap(Bitmap blackMovableBitmap) {
        this.scaledBlackMovableBitmap = this.blackMovableBitmap = blackMovableBitmap;
    }

    public Bitmap getBlackSelectedBitmap() {
        return blackSelectedBitmap;
    }

    public void setBlackSelectedBitmap(Bitmap blackSelectedBitmap) {
        this.scaledBlackSelectedBitmap = this.blackSelectedBitmap = blackSelectedBitmap;
    }

    public Bitmap getWhiteDefaultBitmap() {
        return whiteDefaultBitmap;
    }

    public void setWhiteDefaultBitmap(Bitmap whiteDefaultBitmap) {
        this.scaledWhiteDefaultBitmap = this.whiteDefaultBitmap = whiteDefaultBitmap;
    }

    public Bitmap getWhiteMovableBitmap() {
        return whiteMovableBitmap;
    }

    public void setWhiteMovableBitmap(Bitmap whiteMovableBitmap) {
        this.scaledWhiteMovableBitmap = this.whiteMovableBitmap = whiteMovableBitmap;
    }

    public Bitmap getWhiteSelectedBitmap() {
        return whiteSelectedBitmap;
    }

    public void setWhiteSelectedBitmap(Bitmap whiteSelectedBitmap) {
        this.scaledWhiteSelectedBitmap = this.whiteSelectedBitmap = whiteSelectedBitmap;
    }

    public Bitmap getMovablePositionBitmap() {
        return movablePositionBitmap;
    }

    public void setMovablePositionBitmap(Bitmap movablePositionBitmap) {
        this.scaledMovablePositionBitmap = this.movablePositionBitmap = movablePositionBitmap;
    }

    public Bitmap getTraveledPositionBitmap() {
        return traveledPositionBitmap;
    }

    public void setTraveledPositionBitmap(Bitmap traveledPositionBitmap) {
        this.scaledTraveledPositionBitmap = this.traveledPositionBitmap = traveledPositionBitmap;
    }

    public Bitmap getRemovablePositionBitmap() {
        return removablePositionBitmap;
    }

    public void setRemovablePositionBitmap(Bitmap removablePositionBitmap) {
        this.scaledRemovablePositionBitmap = this.removablePositionBitmap = removablePositionBitmap;
    }

    public Bitmap getScaledBackgroundBitmap() {
        return scaledBackgroundBitmap;
    }

    public void setScaledBackgroundBitmap(Bitmap scaledBackgroundBitmap) {
        this.scaledBackgroundBitmap = scaledBackgroundBitmap;
    }

    public Bitmap getScaledAkalanaBitmap() {
        return scaledAkalanaBitmap;
    }

    public void setScaledAkalanaBitmap(Bitmap scaledAkalanaBitmap) {
        this.scaledAkalanaBitmap = scaledAkalanaBitmap;
    }

    public Bitmap getScaledBlackDefaultBitmap() {
        return scaledBlackDefaultBitmap;
    }

    public void setScaledBlackDefaultBitmap(Bitmap scaledBlackDefaultBitmap) {
        this.scaledBlackDefaultBitmap = scaledBlackDefaultBitmap;
    }

    public Bitmap getScaledBlackMovableBitmap() {
        return scaledBlackMovableBitmap;
    }

    public void setScaledBlackMovableBitmap(Bitmap scaledBlackMovableBitmap) {
        this.scaledBlackMovableBitmap = scaledBlackMovableBitmap;
    }

    public Bitmap getScaledBlackSelectedBitmap() {
        return scaledBlackSelectedBitmap;
    }

    public void setScaledBlackSelectedBitmap(Bitmap scaledBlackSelectedBitmap) {
        this.scaledBlackSelectedBitmap = scaledBlackSelectedBitmap;
    }

    public Bitmap getScaledWhiteDefaultBitmap() {
        return scaledWhiteDefaultBitmap;
    }

    public void setScaledWhiteDefaultBitmap(Bitmap scaledWhiteDefaultBitmap) {
        this.scaledWhiteDefaultBitmap = scaledWhiteDefaultBitmap;
    }

    public Bitmap getScaledWhiteMovableBitmap() {
        return scaledWhiteMovableBitmap;
    }

    public void setScaledWhiteMovableBitmap(Bitmap scaledWhiteMovableBitmap) {
        this.scaledWhiteMovableBitmap = scaledWhiteMovableBitmap;
    }

    public Bitmap getScaledWhiteSelectedBitmap() {
        return scaledWhiteSelectedBitmap;
    }

    public void setScaledWhiteSelectedBitmap(Bitmap scaledWhiteSelectedBitmap) {
        this.scaledWhiteSelectedBitmap = scaledWhiteSelectedBitmap;
    }

    public Bitmap getScaledMovablePositionBitmap() {
        return scaledMovablePositionBitmap;
    }

    public void setScaledMovablePositionBitmap(Bitmap scaledMovablePositionBitmap) {
        this.scaledMovablePositionBitmap = scaledMovablePositionBitmap;
    }

    public Bitmap getScaledTraveledPositionBitmap() {
        return scaledTraveledPositionBitmap;
    }

    public void setScaledTraveledPositionBitmap(Bitmap scaledTraveledPositionBitmap) {
        this.scaledTraveledPositionBitmap = scaledTraveledPositionBitmap;
    }

    public Bitmap getScaledRemovablePositionBitmap() {
        return scaledRemovablePositionBitmap;
    }

    public void setScaledRemovablePositionBitmap(Bitmap scaledRemovablePositionBitmap) {
        this.scaledRemovablePositionBitmap = scaledRemovablePositionBitmap;
    }

    public void setUnitSize(int unitSize) {
        if (blackDefaultBitmap != null)
            this.scaledBlackDefaultBitmap = Bitmap.createScaledBitmap(blackDefaultBitmap, unitSize, unitSize, true);

        if (blackMovableBitmap != null)
            this.scaledBlackMovableBitmap = Bitmap.createScaledBitmap(blackMovableBitmap, unitSize, unitSize, true);

        if (blackSelectedBitmap != null)
            this.scaledBlackSelectedBitmap = Bitmap.createScaledBitmap(blackSelectedBitmap, unitSize, unitSize, true);

        if (whiteDefaultBitmap != null)
            this.scaledWhiteDefaultBitmap = Bitmap.createScaledBitmap(whiteDefaultBitmap, unitSize, unitSize, true);

        if (whiteMovableBitmap != null)
            this.scaledWhiteMovableBitmap = Bitmap.createScaledBitmap(whiteMovableBitmap, unitSize, unitSize, true);

        if (whiteSelectedBitmap != null)
            this.scaledWhiteSelectedBitmap = Bitmap.createScaledBitmap(whiteSelectedBitmap, unitSize, unitSize, true);

        if (movablePositionBitmap != null)
            this.scaledMovablePositionBitmap = Bitmap.createScaledBitmap(movablePositionBitmap, unitSize, unitSize, true);

        if (traveledPositionBitmap != null)
            this.scaledTraveledPositionBitmap = Bitmap.createScaledBitmap(traveledPositionBitmap, unitSize, unitSize, true);

        if (removablePositionBitmap != null)
            this.scaledRemovablePositionBitmap = Bitmap.createScaledBitmap(removablePositionBitmap, unitSize, unitSize, true);
    }
}
