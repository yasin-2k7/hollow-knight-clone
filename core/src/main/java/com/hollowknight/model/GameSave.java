package com.hollowknight.model;

public class GameSave {
    private float startX, startY;
    private int soul;
    private int masks;
    private int playTime;

    public GameSave(float startX, float startY, int soul, int masks, int playMatch) {
        this.startX = startX;
        this.startY = startY;
        this.soul = soul;
        this.masks = masks;
        this.playTime = playMatch;
    }

    public float getStartX() {
        return startX;
    }

    public float getStartY() {
        return startY;
    }

    public int getSoul() {
        return soul;
    }

    public int getMasks() {
        return masks;
    }

    public int getPlayTime() {
        return playTime;
    }
}
