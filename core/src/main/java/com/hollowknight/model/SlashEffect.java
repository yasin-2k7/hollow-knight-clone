package com.hollowknight.model;

import com.badlogic.gdx.math.Rectangle;
import com.hollowknight.model.enemies.Enemy;
import com.hollowknight.model.enums.SlashDirection;

import java.util.ArrayList;

public class SlashEffect {
    private final ArrayList<Enemy> hitEnemies = new ArrayList<>();
    private final ArrayList<Rectangle> hitSpikes = new ArrayList<>();
    private SlashDirection type;
    private float x,y;
    private float width, height;
    private float originalWidth, originalHeight;
    private Rectangle hitBounds;
    private boolean isFinished = false;
    private final float duration;
    private float stateTime = 0;
    private int damage;

    public SlashEffect(SlashDirection type, float x, float y, float width, float height, int damage, float duration) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        hitBounds = new Rectangle(x,y,width,height);
        this.damage = damage;
        this.duration = duration;
    }

    public void update(float delta) {
        stateTime += delta;
        if (stateTime >= duration) {
            isFinished = true;
        }
    }

    public boolean isFinished() {
        return isFinished;
    }

    public boolean hasHitAlready(Enemy enemy) {
        return hitEnemies.contains(enemy);
    }

    public void registerHit(Enemy enemy) {
        hitEnemies.add(enemy);
    }

    public void setOriginalSize(float originalWidth, float originalHeight) {
        this.originalWidth = originalWidth;
        this.originalHeight = originalHeight;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getOriginalWidth() {
        return originalWidth;
    }

    public float getOriginalHeight() {
        return originalHeight;
    }

    public SlashDirection getType() {
        return type;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Rectangle getHitBounds() {
        return hitBounds;
    }

    public ArrayList<Rectangle> getHitSpikes() {
        return hitSpikes;
    }

    public float getDuration() {
        return duration;
    }
}
