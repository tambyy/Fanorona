package com.tambyy.fanoronaakalana.models;

public class AIStat {
    private int level = 8;
    private int winsCount = 0;

    public AIStat() {

    }

    public AIStat(int level, int winsCount) {
        this.level = level;
        this.winsCount = winsCount;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getWinsCount() {
        return winsCount;
    }

    public void setWinsCount(int winsCount) {
        this.winsCount = winsCount;
    }
}
