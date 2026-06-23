package com.hollowknight.model;

public class Game {
    private float startX, startY;
    private Knight knight;
    private final float GRAVITY = -150f;

    public float getGRAVITY() {
        return GRAVITY;
    }

    public Game(float startX, float startY) {
        this.startX = startX;
        this.startY = startY;
        knight = new Knight(0, 0, startX, startY);
    }

    public Knight getKnight() {
        return knight;
    }
}
