package com.hollowknight.model.enemies;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.hollowknight.controller.GameController;
import com.hollowknight.model.App;
import com.hollowknight.model.EntityAudioListener;
import com.hollowknight.model.Game;
import com.hollowknight.model.Knight;
import com.hollowknight.model.enums.AudioAction;
import com.hollowknight.model.enums.EnemyState;
import com.hollowknight.model.enums.EnemyType;

public abstract class AbstractEnemy implements Enemy{
    protected float width,height,wOffset,hOffsetUp,hOffsetDown;
    protected Rectangle bounds;
    protected boolean onGround = false;
    protected boolean flipped = false;
    protected boolean dead = false;
    protected EnemyType type;

    protected EntityAudioListener audioListener;

    protected Game game;

    protected int health;

    protected float damagedTimer = 0f;
    protected float turnTimer = 0f;
    protected float deathAirTimer = 0f;
    protected float spikeCooldown = 0f;

    private final float DAMAGED_TIME = 0.5f;
    private final float DEATH_AIR_TIME = 0.55f;

    protected float startX, startY;

    protected EnemyState state;

    protected Vector2 position;
    protected Vector2 velocity;

    public AbstractEnemy(float width, float height, float wOffset, float hOffsetUp, float hOffsetDown, Game game, float startX, float startY, EnemyType type) {
        float scale = App.getUnitScale();
        this.width = width*scale;
        this.height = height*scale;
        this.wOffset = wOffset*scale;
        this.hOffsetUp = hOffsetUp*scale;
        this.hOffsetDown = hOffsetDown*scale;
        this.game = game;
        this.startX = startX;
        this.startY = startY;
        position = new Vector2(startX, startY);
        velocity = new Vector2();
        float newWidth = this.width - 2 * this.wOffset;
        float newHeight = this.height - this.hOffsetDown - this.hOffsetUp;
        this.bounds = new Rectangle(startX, startY + this.hOffsetDown, newWidth, newHeight);
        this.type = type;
    }

    @Override
    public abstract void checkVision(Knight knight);

    @Override
    public abstract void attack(Knight knight);



    @Override
    public Rectangle getBounds() {
        return bounds;
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public boolean isDead() {
        return dead;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getwOffset() {
        return wOffset;
    }

    public float gethOffsetUp() {
        return hOffsetUp;
    }

    public float gethOffsetDown() {
        return hOffsetDown;
    }

    public Game getGame() {
        return game;
    }

    @Override
    public void setAudioListener(EntityAudioListener audioListener) {
        this.audioListener = audioListener;
    }

    @Override
    public EnemyState getState() {
        return state;
    }

    public void takeSpikeDamage(int damage, float spikeCenterX) {
        if (dead) return;
        if (spikeCooldown > 0) return;
        audioListener.onAudioEvent(AudioAction.ENEMY_TAKE_DAMAGE);
        this.health -= damage;
        this.velocity.x = (this.position.x > spikeCenterX) ? 250f : -250f;
        this.velocity.y = 200f;

        this.spikeCooldown = 0.5f;

        if (this.health <= 0) {
            state = EnemyState.DEATH_AIR;
            deathAirTimer = DEATH_AIR_TIME;
            return;
        }
        state = EnemyState.DAMAGED;
        damagedTimer = DAMAGED_TIME;
    }

    @Override
    public void setPatrolRect(Rectangle rectangle) {

    }

    @Override
    public EnemyType getType() {
        return type;
    }
}
