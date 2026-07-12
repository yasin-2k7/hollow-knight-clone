package com.hollowknight.model.enemies;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.hollowknight.model.EntityAudioListener;
import com.hollowknight.model.Knight;
import com.hollowknight.model.enums.EnemyState;
import com.hollowknight.model.enums.EnemyType;

public interface Enemy {
    void takeDamage(int amount, float sourceX, float knockbackSpeed);
    void takeSpikeDamage(int damage, float sourceX);
    void update(float delta);
    Rectangle getBounds();
    Vector2 getPosition();
    boolean isDead();
    void checkVision(Knight knight);
    void attack(Knight knight);
    void knightHit(Knight knight);
    void setAudioListener(EntityAudioListener audioListener);
    EnemyState getState();
    void setPatrolRect(Rectangle rectangle);
    EnemyType getType();
    Rectangle getWeapon();
    void setActive();
    void reset();
}
