package com.hollowknight.model.enemies;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.hollowknight.model.EntityAudioListener;
import com.hollowknight.model.Knight;

public interface Enemy {
    void takeDamage();
    void update(float delta);
    Rectangle getBounds();
    Vector2 getPosition();
    boolean isDead();
    void checkVision(Knight knight);
    void attack(Knight knight);
    void knightHit(Knight knight);
    void setAudioListener(EntityAudioListener audioListener);
}
