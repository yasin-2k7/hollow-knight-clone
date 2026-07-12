package com.hollowknight.model;

import com.badlogic.gdx.maps.tiled.TiledMap;

public class GameSave {
    private float startX, startY;
    private float mapStartX, mapStartY;
    private int soul;
    private int masks;
    private float playTime;
    private int saveIndex;
    private int deathNumber;
    private int enemyDeathNumber;
    private String tiledMapAddress;
    private String loadButtonBgAddress;

    public GameSave() {}

    public GameSave(float startX, float startY, int soul, int masks, float playMatch, String tiledMapAddress, float mapStartX, float mapStartY, String loadButtonBgAddress, int saveIndex, int deathNumber, int enemyDeathNumber) {
        this.startX = startX;
        this.startY = startY;
        this.mapStartX = mapStartX;
        this.mapStartY = mapStartY;
        this.soul = soul;
        this.masks = masks;
        this.saveIndex = saveIndex;
        this.deathNumber = deathNumber;
        this.enemyDeathNumber = enemyDeathNumber;
        this.playTime = playMatch;
        this.tiledMapAddress = tiledMapAddress;
        this.loadButtonBgAddress = loadButtonBgAddress;
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

    public int getDeathNumber() {
        return deathNumber;
    }

    public int getEnemyDeathNumber() {
        return enemyDeathNumber;
    }

    public int getSaveIndex() {
        return saveIndex;
    }

    public String getLoadButtonBgAddress() {
        return loadButtonBgAddress;
    }
}
