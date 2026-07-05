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

public class FlyerEnemy extends AbstractEnemy {
    private float visionWidth = 100f;
    private float visionHeight = 100f;
    private final Rectangle visionBox;
    private Rectangle patrolRect;

    private int health = 3;

    private float attackTimer = 0f;

    private EnemyState previousState;

    private Vector2 target;

    private final float ATTACK_TIME = 0.5f;
    private final float MOVE_SPEED = 30f;
    private final float CHASE_SPEED = 40f;
    private final float DAMAGED_TIME = 0.5f;
    private final float TURN_TIME = 0.5f;
    private final float DEATH_AIR_TIME = 0.8f;

    public FlyerEnemy(Game game, float startX, float startY, float width, float height, float wOffset, float hOffsetUp, float hOffsetDown) {
        super(width, height, wOffset, hOffsetUp, hOffsetDown, game, startX, startY);

        target = new Vector2();
        float newWidth = (this.width - 2*this.wOffset);
        float newHeight = (this.height - this.hOffsetDown - this.hOffsetUp);
        float newX = startX;
        float newY = startY + this.hOffsetDown;
        visionBox = new Rectangle(newX - (visionWidth-newWidth)/2, newY - (visionHeight-newHeight)/2, visionWidth, visionHeight);
        previousState = state;
        reset();
    }

    @Override
    public void update(float delta) {
        if (state == EnemyState.DEATH_AIR){
            velocity.y += game.getGRAVITY() * delta;
        }
        if (dead){
            velocity.y += game.getGRAVITY() * delta;
            return;
        }

        if (state != EnemyState.TURN && state != EnemyState.DAMAGED && state != EnemyState.CHASE && state != EnemyState.DEATH_AIR) {
            checkVision(App.getCurrentGame().getKnight());
        }
        handleStates(delta);

        if (state == EnemyState.CHASE || state == EnemyState.BACK) {
            velocity.x = 0;
            velocity.y = 0;
        }

        position.y += velocity.y * delta;
        bounds.setPosition(position.x, position.y);
        checkVerticalCollisions();

        position.x += velocity.x * delta;
        bounds.setPosition(position.x, position.y);
        checkHorizontalCollisions();



        visionBox.setPosition(
            position.x - (visionWidth - bounds.width) / 2,
            position.y - (visionHeight - bounds.height) / 2
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

        for (Rectangle rectangle : game.getGrounds()) {
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

        if (state == EnemyState.WALK) {
            if (position.x > patrolRect.x + patrolRect.getWidth() && flipped) {
                turn();
            } else if (position.x < patrolRect.x && !flipped) {
                turn();
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
                    state = previousState;
                    flipped = !flipped;
                }
                break;
            case DAMAGED:
                damagedTimer -= delta;
                velocity.x = MathUtils.lerp(velocity.x, 0, delta * 10f);
                if (damagedTimer <= 0) {
                    previousState = state;
                    state = EnemyState.WALK;
                }
                break;
            case DEATH_AIR:
                velocity.x = MathUtils.lerp(velocity.x, 0, delta * 10f);
                deathAirTimer -= delta;
                if (deathAirTimer <= 0){
                    previousState = state;
                    state = EnemyState.DEATH_LAND;
                    dead = true;
                }
                break;
            case CHASE:
                moveTowardsTarget(target.x, target.y, delta);
                if (position.dst(target) < 1){
                    previousState = state;
                    state = EnemyState.BACK;
                    moveTowardsTarget(startX, startY, delta);
                }
                break;
            case BACK:
                moveTowardsTarget(startX, startY, delta);
                if (position.dst(startX, startY) < 1){
                    previousState = state;
                    state = EnemyState.WALK;
                }
                break;
            case ATTACK:
                attackTimer -= delta;
                if (attackTimer <= 0){
                    previousState = state;
                    state = EnemyState.BACK;
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
        state = EnemyState.CHASE;
        target.x = knightCenter.x;
        target.y = knightCenter.y;
    }

    @Override
    public void attack(Knight knight) {
        state = EnemyState.ATTACK;
    }

    private void moveTowardsTarget(float targetX, float targetY, float deltaTime) {
        this.target.set(targetX, targetY);
        if (targetX > position.x && !flipped){
            turn();
        }
        else if (targetX < position.x && flipped){
            turn();
        }

        position.lerp(target, deltaTime);

    }

    public void setPatrolRect(Rectangle patrolRect) {
        this.patrolRect = patrolRect;
    }

    public EnemyState getState() {
        return state;
    }

    public boolean isFlipped() {
        return flipped;
    }

    @Override
    public void takeDamage() {
        if (dead) return;
        audioListener.onAudioEvent(AudioAction.ENEMY_TAKE_DAMAGE);
        velocity.x = GameController.getActiveSlashView().isFlipped() ? 250 : -250;
        health--;
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
