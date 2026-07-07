package com.hollowknight.model;

import com.badlogic.gdx.maps.tiled.TiledMap;

public class GameSave {
    private float startX, startY;
    private float mapStartX, mapStartY;
    private int soul;
    private int masks;
    private float playTime;
    private String tiledMapAddress;

    public GameSave() {}

    public GameSave(float startX, float startY, int soul, int masks, float playMatch, String tiledMapAddress, float mapStartX, float mapStartY) {
        this.startX = startX;
        this.startY = startY;
        this.mapStartX = mapStartX;
        this.mapStartY = mapStartY;
        this.soul = soul;
        this.masks = masks;
        this.playTime = playMatch;
        this.tiledMapAddress = tiledMapAddress;
    }

    public String getTiledMapAddress() {
        return tiledMapAddress;
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

    public float getPlayTime() {
        return playTime;
    }

    public float getMapStartX() {
        return mapStartX;
    }

    public float getMapStartY() {
        return mapStartY;
    }
}
