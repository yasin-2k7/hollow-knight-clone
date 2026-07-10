package com.hollowknight.model;

import com.badlogic.gdx.math.Rectangle;
import com.hollowknight.controller.GameController;

public class SwitchPoint {
    private Rectangle bounds;
    private float x;
    private float y;
    private String address;

    public SwitchPoint(Rectangle bounds, float x, float y, String address) {
        this.bounds = bounds;
        this.x = x;
        this.y = y;
        this.address = address;
    }

    public void switchMap(){
        GameController.switchMap(this);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public String getAddress() {
        return address;
    }
}
