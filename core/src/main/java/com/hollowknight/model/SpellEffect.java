package com.hollowknight.model;

import com.badlogic.gdx.math.Rectangle;
import com.hollowknight.model.enemies.Enemy;
import com.hollowknight.model.enums.SpellType;

import java.util.ArrayList;

public class SpellEffect {
    private final ArrayList<Enemy> hitEnemies = new ArrayList<>();
    private SpellType type;
    private float x, y;
    private float width, height;
    private float velocityX, velocityY;
    private Rectangle hitBounds;
    private boolean isFinished = false;
    private float duration;
    private float stateTime = 0;
    private int damage;
    private boolean isFlipped;
    private float clearTime = 0;


    public SpellEffect(SpellType type, float x, float y, float width, float height, int damage, boolean isFlipped) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.damage = damage;
        this.isFlipped = isFlipped;

        this.hitBounds = new Rectangle(x,y,width,height);

        if (type == SpellType.VENGEFUL_SPIRIT) {
            this.duration = 3f;
            float speed = 200f;
            this.velocityX = isFlipped ? speed : -speed;
            this.velocityY = 0;
        } else if (type == SpellType.HOWLING_WRAITHS) {
            this.duration = 1f;
            this.velocityX = 0;
            this.velocityY = 0;
        }
    }

    public void update(float delta) {
        stateTime += delta;
        clearTime += delta;
        if (stateTime >= duration) {
            isFinished = true;
        }
        x += velocityX * delta;
        y += velocityY * delta;
        hitBounds.setPosition(x, y);
        if (clearTime >= duration/3){
            hitEnemies.clear();
            clearTime = 0;
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

    public SpellType getType() { return type; }
    public float getX() { return x; }
    public float getY() { return y; }
    public float getWidth() { return width; }
    public float getHeight() { return height; }
    public Rectangle getHitBounds() { return hitBounds; }
    public int getDamage() { return damage; }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }
}
