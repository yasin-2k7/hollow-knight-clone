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

public class HuskHornHeadEnemy extends AbstractEnemy{
    private float visionWidth = 80f;
    private float visionHeight = 10f;
    private final Rectangle visionBox;

    private int health = 4;

    private float attackCooldownTimer;
    private float attackAnticipateTimer;
    private float idleTimer;

    private final float IDLE_TIME = 2f;
    private final float ATTACK_COOLDOWN_TIME = 0.2f;
    private final float ATTACK_ANTICIPATE_TIME = 0.5f;
    private final float MOVE_SPEED = 30f;
    private final float ATTACK_SPEED = 60f;
    private final float DAMAGED_TIME = 0.5f;
    private final float TURN_TIME = 0.5f;
    private final float DEATH_AIR_TIME = 0.55f;

    public HuskHornHeadEnemy(Game game, float startX, float startY, float width, float height, float wOffset, float hOffsetUp, float hOffsetDown, EnemyType type) {
        super(width, height, wOffset, hOffsetUp, hOffsetDown, game, startX, startY, type);
        float newY = startY + this.hOffsetDown;
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

        if (state == EnemyState.IDLE || state == EnemyState.WALK) {
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

    private void checkHorizontalCollisions() {

        if (state == EnemyState.WALK || state == EnemyState.ATTACK) {
            for (Rectangle trigger : game.getCurrentMap().getTurnPositions()) {
                if (bounds.overlaps(trigger)) {
                    if (velocity.x > 0) {
                        position.x = trigger.x - bounds.width;
                    } else if (velocity.x < 0) {
                        position.x = trigger.x + trigger.width;
                    }
                    if ((flipped && velocity.x > 0) || (!flipped && velocity.x < 0)) {
                        if (state != EnemyState.DEATH_AIR && state != EnemyState.DEATH_LAND){
                            velocity.x = 0;
                            if (state == EnemyState.ATTACK){
                                state = EnemyState.ATTACK_COOLDOWN;
                                attackCooldownTimer = ATTACK_COOLDOWN_TIME;
                            }
                            else{
                                turn();
                            }
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
                    if ((velocity.x > 0 && flipped) || velocity.x < 0 && !flipped) {
                        velocity.x = 0;
                        if (state == EnemyState.ATTACK) {
                            state = EnemyState.ATTACK_COOLDOWN;
                            attackCooldownTimer = ATTACK_COOLDOWN_TIME;
                        } else {
                            turn();
                        }

                    }
                }
                bounds.setPosition(position.x, position.y);
            }
        }
    }

    private void handleStates(float delta) {
        switch (state){
            case WALK:
                velocity.x = flipped ? MOVE_SPEED : -MOVE_SPEED;
                break;
            case TURN:
                velocity.x = 0;
                turnTimer -= delta;
                if (turnTimer <= 0) {
                    flipped = !flipped;
                    state = EnemyState.IDLE;
                    idleTimer = IDLE_TIME;
                }
                break;
            case ATTACK_COOLDOWN:
                velocity.x = 0;
                attackCooldownTimer -= delta;
                if (attackCooldownTimer <= 0){
                    state = EnemyState.TURN;
                }
                break;
            case DAMAGED:
                damagedTimer -= delta;
                velocity.x = MathUtils.lerp(velocity.x, 0, delta * 10f);
                if (damagedTimer <= 0) {
                    state = EnemyState.WALK;
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
            case ATTACK:
                velocity.x = flipped ? ATTACK_SPEED : -ATTACK_SPEED;
                break;
            case IDLE:
                idleTimer -= delta;
                if (idleTimer <= 0){
                    state = EnemyState.WALK;
                }
                break;
            case ATTACK_ANTICIPATE:
                attackAnticipateTimer -= delta;
                if (attackAnticipateTimer <= 0){
                    state = EnemyState.ATTACK;
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
            position.x + bounds.width / 2f,
            position.y + bounds.height / 2
        );

        Vector2 knightCenter = new Vector2(
            knight.getPosition().x + knight.getBounds().width / 2f,
            knight.getPosition().y + knight.getBounds().height / 2f
        );

        for (Rectangle ground : App.getCurrentGame().getCurrentMap().getGrounds()) {
            if (!visionBox.overlaps(ground)) {
                continue;
            }

            if (Intersector.intersectSegmentRectangle(enemyEye, knightCenter, ground)) {
                return;
            }
        }

        attack(knight);

    }

    @Override
    public void attack(Knight knight) {
        state = EnemyState.ATTACK_ANTICIPATE;
        attackAnticipateTimer = ATTACK_ANTICIPATE_TIME;
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
        this.velocity.x = MOVE_SPEED;
        this.flipped = true;
        this.state = EnemyState.WALK;
        this.health = 4;
        this.dead = false;
    }

    public void turn(){
        state = EnemyState.TURN;
        turnTimer = TURN_TIME;
    }

    @Override
    public void knightHit(Knight knight) {
        attack(knight);
    }
}
