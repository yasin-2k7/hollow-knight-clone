package com.hollowknight.model.enemies;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.hollowknight.controller.GameController;
import com.hollowknight.model.App;
import com.hollowknight.model.Game;
import com.hollowknight.model.Knight;
import com.hollowknight.model.enums.AudioAction;
import com.hollowknight.model.enums.EnemyState;
import com.hollowknight.model.enums.EnemyType;

public class SimpleEnemy extends AbstractEnemy{

    private int health = 3;

    private final float MOVE_SPEED = 40f;
    private final float DAMAGED_TIME = 0.5f;
    private final float TURN_TIME = 0.5f;
    private final float DEATH_AIR_TIME = 0.8f;

    public SimpleEnemy(Game game, float startX, float startY, float width, float height, float wOffset, float hOffsetUp, float hOffsetDown, EnemyType type) {
        super(width, height, wOffset, hOffsetUp, hOffsetDown, game, startX, startY, type);
        reset();
    }

    @Override
    public void update(float delta) {
        if (dead){
            if (!onGround) {
                velocity.y += game.getGRAVITY() * delta;
                position.y += velocity.y * delta;
                bounds.setPosition(position.x, position.y);
                checkVerticalCollisions();
            }
            if (App.getCurrentGame().getKnight().getPosition().dst(position) > 500f){
                reset();
            }
            return;
        }
        handleStates(delta);

        position.x += velocity.x * delta;
        bounds.setPosition(position.x, position.y);
        checkHorizontalCollisions();

        velocity.y += game.getGRAVITY() * delta;
        position.y += velocity.y * delta;
        bounds.setPosition(position.x, position.y);
        checkVerticalCollisions();
    }


    private void handleStates(float delta){
        if (state == EnemyState.DAMAGED) {
            damagedTimer -= delta;
            velocity.x = MathUtils.lerp(velocity.x, 0, delta * 10f);
            if (damagedTimer <= 0) {
                state = EnemyState.WALK;
                velocity.x = flipped ? MOVE_SPEED : -MOVE_SPEED;
            }
        }

        if (state == EnemyState.TURN) {
            velocity.x = 0;
            turnTimer -= delta;
            if (turnTimer <= 0) {
                state = EnemyState.WALK;
                flipped = !flipped;
                velocity.x = flipped ? MOVE_SPEED : -MOVE_SPEED;
            }
        }

        if (state == EnemyState.DEATH_AIR){
            velocity.x = MathUtils.lerp(velocity.x, 0, delta * 10f);
            deathAirTimer -= delta;
            if (deathAirTimer <= 0){
                state = EnemyState.DEATH_LAND;
                dead = true;
            }
        }
    }

    private void checkHorizontalCollisions(){
        if (state == EnemyState.WALK) {
            for (Rectangle trigger : game.getCurrentMap().getTurnPositions()) {
                if (bounds.overlaps(trigger)) {
                    if ((flipped && velocity.x > 0) || (!flipped && velocity.x < 0)) {
                        if (state != EnemyState.DEATH_AIR && state != EnemyState.DEATH_LAND){
                            turn();
                        }
                    }
                    break;
                }
            }
        }

        for (Rectangle rectangle : game.getCurrentMap().getGrounds()) {
            if (Intersector.overlaps(rectangle, bounds)) {
                if (velocity.x > 0) {
                    position.x = rectangle.x - bounds.width;
                } else if (velocity.x < 0) {
                    position.x = rectangle.x + rectangle.width;
                }
                if (state != EnemyState.DEATH_AIR && state != EnemyState.DEATH_LAND){
                    turn();
                }
                bounds.setPosition(position.x, position.y);
            }
        }
    }

    private void checkVerticalCollisions(){
        onGround = false;
        for (Rectangle rectangle : game.getCurrentMap().getGrounds()) {
            if (Intersector.overlaps(rectangle, bounds)) {
                if (velocity.y > 0) {
                    position.y = rectangle.y - bounds.height;
                } else {
                    position.y = rectangle.y + rectangle.height;
                    onGround = true;
                }
                velocity.y = 0;
                bounds.setPosition(position.x, position.y);
            }
        }
    }

    public EnemyState getState() {
        return state;
    }

    public boolean isFlipped() {
        return flipped;
    }

    public float getWidth() {
        return super.getWidth();
    }

    public float getHeight() {
        return super.getHeight();
    }

    public float getwOffset() {
        return super.getwOffset();
    }

    public float gethOffsetUp() {
        return super.gethOffsetUp();
    }

    public float gethOffsetDown() {
        return super.gethOffsetDown();
    }

    @Override
    public void checkVision(Knight knight) {

    }

    @Override
    public void attack(Knight knight) {

    }

    public Vector2 getPosition() {
        return position;
    }

    @Override
    public void takeDamage(int amount, float sourceX, float knockbackSpeed) {
        if (dead) return;
        audioListener.onAudioEvent(AudioAction.ENEMY_TAKE_DAMAGE);
        velocity.x = sourceX < position.x ? knockbackSpeed : -knockbackSpeed;
        health -= amount;
        if (health <= 0){
            App.getCurrentGame().setEnemyDeathNumber(App.getCurrentGame().getEnemyDeathNumber() + 1);
            App.enemyList.put(type, true);
            state = EnemyState.DEATH_AIR;
            velocity.y = 60f;
            deathAirTimer = DEATH_AIR_TIME;
            return;
        }
        state = EnemyState.DAMAGED;
        damagedTimer = DAMAGED_TIME;
        velocity.y = 20f;
    }

    @Override
    public void reset(){
        this.position.x = this.startX;
        this.position.y = this.startY;
        this.velocity.y = 0;
        this.velocity.x = MOVE_SPEED;
        this.flipped = true;
        this.state = EnemyState.WALK;
        this.health = 3;
        this.dead = false;
    }

    public void turn(){
        state = EnemyState.TURN;
        turnTimer = TURN_TIME;
    }

    @Override
    public void knightHit(Knight knight) {
        return;
    }
}
