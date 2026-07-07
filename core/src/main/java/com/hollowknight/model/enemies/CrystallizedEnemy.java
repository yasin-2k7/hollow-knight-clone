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

public class CrystallizedEnemy extends AbstractEnemy{
    private float visionWidth = 80f;
    private float visionHeight = 10f;
    private final Rectangle visionBox;

    private boolean attacked = false;

    private float movedAmount = 0f;

    private int health = 5;

    private float evadeTimer;
    private float shootTimer;

    private EnemyState previousState;


    private final float EVADE_TIME = 1.5f;
    private final float SHOOT_TIME = 1.5f;
    private final float MOVE_SPEED = 30f;
    private final float ATTACK_SPEED = 60f;
    private final float DAMAGED_TIME = 0.5f;
    private final float TURN_TIME = 0.5f;
    private final float DEATH_AIR_TIME = 0.55f;

    public CrystallizedEnemy(Game game, float startX, float startY, float width, float height, float wOffset, float hOffsetUp, float hOffsetDown) {
        super(width, height, wOffset, hOffsetUp, hOffsetDown, game, startX, startY);
        float newY = startY + this.hOffsetDown;
        state = EnemyState.IDLE;
        previousState = state;
        visionBox = new Rectangle(bounds.x + bounds.width, newY + bounds.height/2 - visionHeight/2, visionWidth, visionHeight);
        reset();
    }

    @Override
    public void update(float delta) {
        if (dead){
            if (!onGround){
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

        if (state == EnemyState.IDLE) {
            checkVision(App.getCurrentGame().getKnight());
        }
        handleStates(delta);

        velocity.y += game.getGRAVITY() * delta;
        position.y += velocity.y * delta;
        bounds.setPosition(position.x, position.y);
        checkVerticalCollisions();

        position.x += velocity.x * delta;
        bounds.setPosition(position.x, position.y);
        checkHorizontalCollisions();

        visionBox.setPosition(
            flipped ? bounds.x + bounds.width : bounds.x - visionWidth,
            bounds.y + bounds.height/2 - visionHeight/2
        );
    }

    private void checkVerticalCollisions() {
        onGround = false;
        for (Rectangle rectangle : game.getGrounds()) {
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

    private void checkHorizontalCollisions() {

        if (state == EnemyState.WALK) {
            if (movedAmount >= 200f){
                velocity.x = 0;
                previousState = state;
                state = EnemyState.EVADE;
                evadeTimer = EVADE_TIME;
                movedAmount = 0f;
            }
        }

        for (Rectangle rectangle : game.getGrounds()) {
            if (Intersector.overlaps(rectangle, bounds)) {
                if (velocity.x > 0) {
                    position.x = rectangle.x - bounds.width;
                } else if (velocity.x < 0) {
                    position.x = rectangle.x + rectangle.width;
                }
                bounds.setPosition(position.x, position.y);
                if (state == EnemyState.WALK){
                    previousState = state;
                    state = EnemyState.EVADE;
                    evadeTimer = EVADE_TIME;
                    movedAmount = 0f;
                    break;
                }
            }
        }
    }

    private void handleStates(float delta) {
        switch (state){
            case WALK:
                velocity.x = flipped ? MOVE_SPEED : -MOVE_SPEED;
                movedAmount += Math.abs(velocity.x) * delta;
                break;
            case TURN:
                velocity.x = 0;
                turnTimer -= delta;
                if (turnTimer <= 0) {
                    flipped = !flipped;
                    previousState = state;
                    state = EnemyState.IDLE;
                }
                break;
            case EVADE:
                velocity.x = 0;
                evadeTimer -= delta;
                if (evadeTimer <= 0){
                    turn();
                }
                break;
            case DAMAGED:
                damagedTimer -= delta;
                velocity.x = MathUtils.lerp(velocity.x, 0, delta * 10f);
                if (damagedTimer <= 0) {
                    if (previousState == EnemyState.WALK || previousState == EnemyState.SHOOT){
                        state = EnemyState.WALK;
                    }
                    else{
                        if (previousState == EnemyState.TURN){
                            flipped = !flipped;
                        }
                        state = EnemyState.IDLE;
                    }
                }
                break;
            case DEATH_AIR:
                velocity.x = MathUtils.lerp(velocity.x, 0, delta * 10f);
                deathAirTimer -= delta;
                if (deathAirTimer <= 0){
                    state = EnemyState.DEATH_LAND;
                    dead = true;
                }
                break;
            case IDLE:
                velocity.x = 0;
                break;
            case SHOOT:
                shootTimer -= delta;
                if (shootTimer <= 0.7f && !attacked){
                    attack(App.getCurrentGame().getKnight());
                    attacked = true;
                }
                if (shootTimer <= 0){
                    state = EnemyState.WALK;
                    attacked = false;
                }
                break;

        }
    }

    @Override
    public void checkVision(Knight knight) {
        if (!visionBox.overlaps(knight.getBounds())) {
            return;
        }

        Vector2 enemyEye = new Vector2(
            position.x + bounds.x / 2f,
            position.y + bounds.y / 2
        );

        Vector2 knightCenter = new Vector2(
            knight.getPosition().x + knight.getBounds().width / 2f,
            knight.getPosition().y + knight.getBounds().height / 2f
        );

        for (Rectangle ground : App.getCurrentGame().getGrounds()) {
            if (!visionBox.overlaps(ground)) {
                continue;
            }

            if (Intersector.intersectSegmentRectangle(enemyEye, knightCenter, ground)) {
                return;
            }
        }

        previousState = state;
        state = EnemyState.SHOOT;
        shootTimer = SHOOT_TIME;


    }

    @Override
    public void attack(Knight knight) {

        GameController.createLaser(position.x + bounds.width/2, position.y+ bounds.height, flipped );
    }



    public EnemyState getState() {
        return state;
    }

    public boolean isFlipped() {
        return flipped;
    }

    @Override
    public void takeDamage(int amount, float sourceX, float knockbackSpeed) {
        if (dead) return;
        audioListener.onAudioEvent(AudioAction.ENEMY_TAKE_DAMAGE);
        velocity.x = sourceX < position.x ? knockbackSpeed : -knockbackSpeed;
        health -= amount;
        if (health == 0){
            state = EnemyState.DEATH_AIR;
            velocity.y = 60f;
            deathAirTimer = DEATH_AIR_TIME;
            return;
        }
        state = EnemyState.DAMAGED;
        damagedTimer = DAMAGED_TIME;
    }

    public void reset(){
        this.position.x = this.startX;
        this.position.y = this.startY;
        this.velocity.y = 0;
        this.velocity.x = 0;
        this.flipped = true;
        this.state = EnemyState.IDLE;
        this.health = 5;
        this.dead = false;
    }

    public void turn(){
        previousState = state;
        state = EnemyState.TURN;
        turnTimer = TURN_TIME;
    }

    @Override
    public void knightHit(Knight knight) {
        attack(knight);
    }
}
