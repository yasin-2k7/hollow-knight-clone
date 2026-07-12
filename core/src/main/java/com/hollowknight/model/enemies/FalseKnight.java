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
import com.hollowknight.model.enums.KnightState;

public class FalseKnight extends AbstractEnemy{
    private Rectangle mace;

    private int health = 50;
    private final int TOTAL_HEALTH = 30;

    private EnemyState lastDecision;
    private float jumpAmount;

    private final float DEFENSIVE_JUMP_AMOUNT = 80;

    private float timer;
    private float stunnedTimer;
    private int jumpDirection;
    private boolean hasStunned = false;
    private boolean isActive = false;
    private boolean heavyDamaged = false;

    private float damageInterval = 0;
    private int damageNumInInterval = 0;



    private Vector2[] attackMacePos = new Vector2[]{new Vector2(144, -42), new Vector2(0, 120), new Vector2(-70, 110), new Vector2(-215, -110)};
    private Vector2[] jumpAttackMacePos = new Vector2[]{new Vector2(144, -42), new Vector2(30, 130), new Vector2(-50, 110), new Vector2(-120, 0), new Vector2(-190, -110)};


    float jumpAttackTime;
    private float moveSpeed = 75;
    private float idleTime = 1.5f;
    private final float RUN_TIME = 3f;
    private final float RUN_ANTIC_TIME = 0.3f;
    private final float TURN_TIME = 0.4f;
    private final float DEATH_AIR_TIME = 0.6f;
    private final float DEATH_LAND_TIME = 2.2f;
    private final float BODY_TIME = 10f; //stunned
    private final float DEATH_HIT_TIME = 0.2f;
    private final float STUN_RECOVER_TIME = 0.5f;
    private float attackTime = 0.3f;
    private float attackRecoverTime = 0.5f;
    private float attackAnticipateTime = 0.6f;
    private final float JUMP_ANTICIPATE_TIME = 0.3f;
    private final float LAND_TIME = 0.3f;
    private final float JUMP_ATTACK_RECOVER_TIME = 0.4f;
    private float unitScale = App.getUnitScale();
    private boolean wasLastJumpAttack = false;
    private boolean isHeavyAttack = false;

    public FalseKnight(Game game, float startX, float startY, float width, float height, float wOffset, float hOffsetUp, float hOffsetDown, EnemyType type) {
        super(width, height, wOffset, hOffsetUp, hOffsetDown, game, startX, startY, type);
        mace = new Rectangle(bounds.x, bounds.y, 78f*unitScale, 78f*unitScale);
        reset();
    }

    private void updateMace(){
        int sign = flipped ? -1 : 1;
        float centerX = bounds.x + bounds.width/2;
        float centerY = bounds.y + bounds.height/2;
        switch (state){
            case IDLE:
                mace.setPosition(centerX + sign*144f*unitScale, centerY-42f*unitScale);
                if (flipped) {
                    mace.x -= mace.width;
                }
                break;
            case ATTACK:
                float elapsed = attackTime - timer;
                float progress = Math.min(Math.max(elapsed / attackTime, 0.0f), 1.0f);

                int currentFrameIndex = (int) (progress * 4);

                currentFrameIndex = Math.min(currentFrameIndex, 3);

                Vector2 currentOffset = attackMacePos[currentFrameIndex];

                mace.x = centerX + (currentOffset.x * sign * unitScale);
                if (flipped) {
                    mace.x -= mace.width;
                }
                mace.y = centerY + currentOffset.y*unitScale;
                break;
            case JUMP_ATTACK:
                float jumpTimeElapsed = jumpAttackTime - timer;
                float jumpProgress = Math.min(Math.max(jumpTimeElapsed / attackTime, 0.0f), 1.0f);

                int currentFrameIndexJump = (int) (jumpProgress * 5);

                currentFrameIndexJump = Math.min(currentFrameIndexJump, 4);

                Vector2 currentOffsetJump = jumpAttackMacePos[currentFrameIndexJump];

                mace.x = centerX + (currentOffsetJump.x * sign * unitScale);
                if (flipped) {
                    mace.x -= sign * mace.width;
                }
                mace.y = centerY + currentOffsetJump.y*unitScale;
                break;

            default:
                mace.setPosition(centerX + sign*20f*unitScale, centerY);
                break;
        }
    }

    @Override
    public void update(float delta) {
        damageInterval += delta;
        if (damageInterval >= 3){
            damageNumInInterval = 0;
            damageInterval = 0;
        }
        updateMace();
        if (dead){
            if (!onGround){
                velocity.y += game.getGRAVITY() * delta;
                position.y += velocity.y * delta;
                bounds.setPosition(position.x, position.y);
                checkVerticalCollisions();
            }
            if (App.getCurrentGame().getKnight().getPosition().dst(position) > 800f){
                reset();
            }
            return;
        }

        handleStates(delta);

        velocity.y += game.getGRAVITY() * delta;
        position.y += velocity.y * delta;
        bounds.setPosition(position.x, position.y);
        checkVerticalCollisions();

        position.x += velocity.x * delta;
        bounds.setPosition(position.x, position.y);
        checkHorizontalCollisions();
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
                        turn();

                    }
                }
                bounds.setPosition(position.x, position.y);
            }
        }
    }

    private void handleStates(float delta) {
        if (!isActive) return;
        if (App.getCurrentGame().getKnight().getState().equals(KnightState.DEATH)){
            state = EnemyState.IDLE;
            return;
        }
        switch (state){
            case TURN:
                velocity.x = 0;
                timer -= delta;
                if (timer <= 0) {
                    flipped = !flipped;
                    state = EnemyState.IDLE;
                    timer = idleTime;
                }
                break;
            case IDLE:
                velocity.x = 0;
                velocity.y = 0;
                if (App.getCurrentGame().getKnight().getPosition().x > position.x && !flipped){
                    turn();
                    return;
                }
                else if (App.getCurrentGame().getKnight().getPosition().x < position.x && flipped){
                    turn();
                    return;
                }
                timer -= delta;
                if (timer <= 0){
                    checkVision(App.getCurrentGame().getKnight());
                }
                break;
            case ATTACK_ANTICIPATE:
                timer -= delta;
                if (timer <= 0){
                    state = EnemyState.ATTACK;
                    timer = attackTime;
                }
                break;
            case ATTACK:
                timer -= delta;
                if (timer <= 0){
                    state = EnemyState.ATTACK_RECOVER;
                    timer = attackRecoverTime;
                }
                break;
            case ATTACK_RECOVER:
                timer -= delta;
                if (timer <= 0){
                    state = EnemyState.IDLE;
                    timer = idleTime;
                }
                break;
            case BODY:
                stunnedTimer -= delta;
                if (stunnedTimer <= 0){
                    state = EnemyState.STUN_RECOVER;
                    timer = STUN_RECOVER_TIME;
                }
                break;
            case DEATH_HIT:
                stunnedTimer -= delta;
                timer -= delta;
                if (timer <= 0){
                    state = EnemyState.BODY;
                }
                if (stunnedTimer <= 0){
                    state = EnemyState.STUN_RECOVER;
                    timer = STUN_RECOVER_TIME;
                }
                break;
            case STUN_RECOVER:
                timer -= delta;
                if (timer <= 0){
                    idleTime *= 1/2f;
                    moveSpeed *= 1.3f;
                    GameController.upgradeBoss(this, 2/3f);
                    state = EnemyState.IDLE;
                    timer = idleTime;
                }
                break;
            case DEATH_AIR:
                timer -= delta;
                if (timer <= 0){
                    state = EnemyState.DEATH_LAND;
                    timer = DEATH_LAND_TIME;
                    dead = true;
                }
                break;
            case JUMP_ANTIC:
                velocity.x = 0;
                timer -= delta;
                if (timer <= 0) {
                    if (isHeavyAttack){
                        state = EnemyState.JUMP_ATTACK;
                        float jumpForce = 150f;
                        velocity.y = jumpForce;
                        float airTime = (2 * jumpForce) / Math.abs(game.getGRAVITY());
                        velocity.x = (jumpAmount / airTime) * jumpDirection;
                        jumpAttackTime = airTime;
                        timer = airTime;

                    }
                    else {
                        state = EnemyState.JUMP;
                        float jumpForce = 150f;
                        velocity.y = jumpForce;
                        float airTime = (2 * jumpForce) / Math.abs(game.getGRAVITY());
                        velocity.x = (jumpAmount / airTime) * jumpDirection;
                    }
                }
                break;
            case JUMP_ATTACK:
                timer -= delta;
                isHeavyAttack = false;
                if (onGround && velocity.y <= 0) {
                    generateWave();
                    state = EnemyState.JUMP_ATTACK_RECOVER;
                    timer = JUMP_ATTACK_RECOVER_TIME;
                    velocity.x = 0;
                }
                break;
            case JUMP_ATTACK_RECOVER:
                velocity.x = 0;
                timer -= delta;
                if (timer <= 0) {
                    state = EnemyState.IDLE;
                    timer = idleTime;
                }
                break;

            case JUMP:
                if (onGround && velocity.y <= 0) {
                    state = EnemyState.LAND;
                    timer = LAND_TIME;
                    velocity.x = 0;
                }
                break;

            case LAND:
                velocity.x = 0;
                timer -= delta;
                if (timer <= 0) {
                    state = EnemyState.IDLE;
                    timer = idleTime;
                }
                break;
            case RUN_ANTIC:
                velocity.x = 0;
                timer -= delta;
                if (timer <= 0) {
                    state = EnemyState.RUN;
                    timer = RUN_TIME;

                    int sign = flipped ? 1 : -1;
                    velocity.x = moveSpeed * sign;
                }
                break;

            case RUN:
                timer -= delta;
                if (timer <= 0) {
                    state = EnemyState.IDLE;
                    timer = idleTime;
                    velocity.x = 0;
                }
                break;
        }
    }

    @Override
    public void checkVision(Knight knight) {
        int directionToKnight = (knight.getPosition().x > this.position.x) ? 1 : -1;
        float distance = knight.getPosition().dst(position);
        int[] weights;
        if (hasStunned){
            if (distance < 90){
                weights = new int[]{50,5,10,5,30};
            }
            else if (distance < 130){
                weights = new int[]{10,20,40,10,20};
            }
            else{
                weights = new int[]{10,60,30,0};
            }
        }
        else{
            if (distance < 90){
                weights = new int[]{60,10,20,10};
            }
            else if (distance < 130){
                weights = new int[]{10,30,50,10};
            }
            else{
                weights = new int[]{10,60,30,0};
            }
        }


        int decision = chooseNextAction(weights);
        if (heavyDamaged){
            heavyDamaged = false;
            state = EnemyState.JUMP_ANTIC;
            timer = JUMP_ANTICIPATE_TIME;
            jumpAmount = DEFENSIVE_JUMP_AMOUNT;
            jumpDirection = -directionToKnight;
            wasLastJumpAttack = false;
        }
        else if (decision ==0){
            if (EnemyState.ATTACK_ANTICIPATE.equals(lastDecision)){
                checkVision(knight);
                return;
            }
            attack(knight);
        }
        else if(decision == 1){
            if (EnemyState.JUMP_ANTIC.equals(lastDecision) && wasLastJumpAttack){
                checkVision(knight);
                return;
            }
            state = EnemyState.JUMP_ANTIC;
            timer = JUMP_ANTICIPATE_TIME;
            jumpAmount = Math.abs(knight.getPosition().x - position.x + 20);
            jumpDirection = directionToKnight;
            wasLastJumpAttack = true;
        }
        else if (decision == 2){
            if (EnemyState.RUN_ANTIC.equals(lastDecision)){
                checkVision(knight);
                return;
            }
            state = EnemyState.RUN_ANTIC;
        }
        else if (decision == 3){
            if (EnemyState.JUMP_ANTIC.equals(lastDecision) && !wasLastJumpAttack){
                checkVision(knight);
                return;
            }
            state = EnemyState.JUMP_ANTIC;
            timer = JUMP_ANTICIPATE_TIME;
            jumpAmount = DEFENSIVE_JUMP_AMOUNT;
            jumpDirection = -directionToKnight;
            wasLastJumpAttack = false;
        }
        else if (decision == 4){
            if (EnemyState.JUMP_ANTIC.equals(lastDecision) && wasLastJumpAttack){
                checkVision(knight);
                return;
            }
            state = EnemyState.JUMP_ANTIC;
            timer = JUMP_ANTICIPATE_TIME;
            jumpAmount = DEFENSIVE_JUMP_AMOUNT;
            jumpDirection = directionToKnight;
            wasLastJumpAttack = true;
            isHeavyAttack = true;
        }
        lastDecision = state;
    }

    public int chooseNextAction(int[] weights) {
        int totalWeight = 0;
        for (int w : weights) {
            totalWeight += w;
        }
        int randomValue = MathUtils.random(0, totalWeight - 1);
        for (int i = 0; i < weights.length; i++) {
            if (randomValue < weights[i]) {
                return i;
            }
            randomValue -= weights[i];
        }

        return 0;
    }

    @Override
    public void attack(Knight knight) {
        state = EnemyState.ATTACK_ANTICIPATE;
        timer = attackAnticipateTime;
    }



    public EnemyState getState() {
        return state;
    }

    public boolean isFlipped() {
        return flipped;
    }

    @Override
    public Rectangle getWeapon() {
        return mace;
    }

    @Override
    public void takeDamage(int amount, float sourceX, float knockbackSpeed) {
        if (dead) return;
        audioListener.onAudioEvent(AudioAction.ENEMY_TAKE_DAMAGE);
        health -= amount;
        damageNumInInterval += amount;
        if (damageNumInInterval >= 4){
            heavyDamaged = true;
        }
        if (state == EnemyState.BODY){
            state = EnemyState.DEATH_HIT;
            timer = DEATH_HIT_TIME;
        }
        if (health <= 0){
            App.getCurrentGame().setEnemyDeathNumber(App.getCurrentGame().getEnemyDeathNumber() + 1);
            App.enemyList.put(type, true);
            state = EnemyState.DEATH_AIR;
            velocity.y = 10f;
            timer = DEATH_AIR_TIME;
        }
        else if (health <= TOTAL_HEALTH/2 && !hasStunned){
            hasStunned = true;
            state = EnemyState.BODY;
            stunnedTimer = BODY_TIME;
        }


    }

    private void generateWave(){
        GameController.setAttackWave(mace.x, mace.y+0.2f, flipped? 1 : -1);
    }

    @Override
    public void reset(){
        this.position.x = this.startX;
        this.position.y = this.startY;
        this.velocity.y = 0;
        this.velocity.x = 0;
        heavyDamaged = false;
        this.flipped = false;
        this.state = EnemyState.IDLE;
        this.health = TOTAL_HEALTH;
        this.dead = false;
        this.isActive = false;
        hasStunned = false;
    }

    public void turn(){
        state = EnemyState.TURN;
        timer = TURN_TIME;
    }

    @Override
    public void setActive() {
        isActive = true;
    }

    @Override
    public void knightHit(Knight knight) {

    }
}
