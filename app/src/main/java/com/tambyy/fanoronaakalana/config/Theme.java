package com.tambyy.fanoronaakalana.config;

import android.graphics.Bitmap;
import android.graphics.Color;

public class Theme {

    private int akalanaBgColor         = Color.rgb(131, 149, 155);
    private int akalanaLinesColor      = Color.rgb(184, 200, 204);

    private int whiteDefaultColor      = Color.rgb(255, 255, 255);
    private int whiteStrokeColor       = Color.argb(125, 120, 103, 86);

    private int blackDefaultColor      = Color.rgb(31, 16, 9);
    private int blackStrokeColor       = Color.argb(125, 31, 16, 9);

    private int movablePositionColor   = Color.rgb(110, 66, 39);
    private int removablePositionColor = Color.rgb(255, 150, 30);
    private int traveledPositionColor  = Color.rgb(255, 255, 255);

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

    public void setWhiteDefaultColor(int whiteDefaultColor) {
        this.whiteDefaultColor = whiteDefaultColor;
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
        this.backgroundBitmap = backgroundBitmap;
    }

    public Bitmap getAkalanaBitmap() {
        return akalanaBitmap;
    }

    public void setAkalanaBitmap(Bitmap akalanaBitmap) {
        this.akalanaBitmap = akalanaBitmap;
    }

    public Bitmap getBlackDefaultBitmap() {
        return blackDefaultBitmap;
    }

    public void setBlackDefaultBitmap(Bitmap blackDefaultBitmap) {
        this.blackDefaultBitmap = blackDefaultBitmap;
    }

    public Bitmap getBlackMovableBitmap() {
        return blackMovableBitmap;
    }

    public void setBlackMovableBitmap(Bitmap blackMovableBitmap) {
        this.blackMovableBitmap = blackMovableBitmap;
    }

    public Bitmap getBlackSelectedBitmap() {
        return blackSelectedBitmap;
    }

    public void setBlackSelectedBitmap(Bitmap blackSelectedBitmap) {
        this.blackSelectedBitmap = blackSelectedBitmap;
    }

    public Bitmap getWhiteDefaultBitmap() {
        return whiteDefaultBitmap;
    }

    public void setWhiteDefaultBitmap(Bitmap whiteDefaultBitmap) {
        this.whiteDefaultBitmap = whiteDefaultBitmap;
    }

    public Bitmap getWhiteMovableBitmap() {
        return whiteMovableBitmap;
    }

    public void setWhiteMovableBitmap(Bitmap whiteMovableBitmap) {
        this.whiteMovableBitmap = whiteMovableBitmap;
    }

    public Bitmap getWhiteSelectedBitmap() {
        return whiteSelectedBitmap;
    }

    public void setWhiteSelectedBitmap(Bitmap whiteSelectedBitmap) {
        this.whiteSelectedBitmap = whiteSelectedBitmap;
    }
}
