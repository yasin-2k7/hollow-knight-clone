package com.hollowknight.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.hollowknight.controller.GameController;
import com.hollowknight.model.enums.AudioAction;
import com.hollowknight.model.enums.GameAction;
import com.hollowknight.model.enums.KnightState;

public class Knight {
    private Game game;
    private int masks;
    private int soul;

    private EntityAudioListener audioListener;

    private float width = 250f;
    private float height = 135f;
    private float wOffset = 120f;
    private float hOffsetUp = 40f;
    private float hOffsetDown = 5f;
    private Rectangle bounds;
    private boolean onGround;
    private boolean onFirstJump;
    private boolean wallInTheRight = false;
    private boolean flipped;
    private boolean canDash = true;
    private boolean canAttack = true;
    private boolean useAltSlash = false;
    private boolean noDamage = false;
    private boolean focusStart = false;

    private float focusGetTime = 0.6f;
    private float focusStartTime = 0.2f;
    private float focusEndTime = 0.2f;
    private float focusTimer = 0f;
    private float focusTime = 0.5f;
    private float damagedTimer = 0f;
    private float damagedTime = 0.3f;
    private float noDamageTimer = 0f;
    private float noDamageTime = 0.9f;
    private float dashCooldownTimer = 0f;
    private float dashCooldown = 1f;
    private float dashTimer = 0f;
    private float dashTime = 0.2f;
    private float attackCooldownTimer = 0;
    private float attackCooldownTime = 0.55f;
    private float usingAltSlashTimer = 0f;
    private float usingAltSlashTime = 0.8f;

    private KnightState state;
    private float unitScale = 1 / 6f;

    private Vector2 position;
    private Vector2 velocity;

    private final float MOVE_SPEED = 50f;
    private final float JUMP_SPEED = 125f;

    public Knight(int masks, int soul, float startX, float startY, Game game) {
        position = new Vector2(startX * unitScale, startY * unitScale);
        velocity = new Vector2(0, 0);
        state = KnightState.IDLE;
        onGround = false;
        this.masks = masks;
        this.soul = soul;
        this.game = game;
        bounds = new Rectangle(position.x, position.y, (width - 2*wOffset) * unitScale , (height - hOffsetDown - hOffsetUp) * unitScale);
    }

    public void update(float delta){
        boolean wasOnGround = this.onGround;

        if (state == KnightState.FOCUS_START){
            focusTimer -= delta;
            if (focusTimer <= 0){
                state = KnightState.FOCUS;
                audioListener.onAudioEvent(AudioAction.FOCUS_HEALTH_CHARGE);
                focusTimer = focusTime;
            }
            if (velocity.x != 0 || velocity.y != 0 || !Gdx.input.isKeyPressed(App.bindings.get(GameAction.FOCUS_AND_CAST))){
                state = KnightState.IDLE;
                focusStart = false;
            }
        }

        if (state == KnightState.FOCUS){
            focusTimer -= delta;
            if (focusTimer <= 0){
                state = KnightState.FOCUS_GET;

                focusTimer = focusGetTime;
            }
            if (velocity.x != 0 || velocity.y != 0 || !Gdx.input.isKeyPressed(App.bindings.get(GameAction.FOCUS_AND_CAST))){
                state = KnightState.IDLE;
                audioListener.onAudioEvent(AudioAction.FOCUS_NOT_FINISHED);
                focusStart = false;
            }
        }

        if (state == KnightState.FOCUS_GET){
            focusTimer -= delta;
            if (focusTimer <= 0){
                state = KnightState.FOCUS_END;
                focusTimer = focusEndTime;
                audioListener.onAudioEvent(AudioAction.FOCUS_HEALTH_HEAL);
            }
            if (velocity.x != 0 || velocity.y != 0 || !Gdx.input.isKeyPressed(App.bindings.get(GameAction.FOCUS_AND_CAST))){
                state = KnightState.IDLE;
                audioListener.onAudioEvent(AudioAction.FOCUS_NOT_FINISHED);
                focusStart = false;
            }
        }

        if (state == KnightState.FOCUS_END){
            focusTimer -= delta;
            if (focusTimer <= 0){
                System.out.println("yes");
                state = KnightState.IDLE;
                soul -= 33;
                masks++;
                GameController.updateMasks();
                focusStart = false;
            }
            if (velocity.x != 0 || velocity.y != 0 || !Gdx.input.isKeyPressed(App.bindings.get(GameAction.FOCUS_AND_CAST))){
                state = KnightState.IDLE;
                audioListener.onAudioEvent(AudioAction.FOCUS_NOT_FINISHED);
                focusStart = false;
            }
        }

        if (state == KnightState.DASH){
            dashTimer -= delta;
            if (dashTimer <= 0){
                state = onGround ? KnightState.IDLE : KnightState.LANDING;
                velocity.x = 0;
            }
        }

        if (dashCooldownTimer > 0){
            dashCooldownTimer -= delta;
            if (dashCooldownTimer <= 0){
                canDash = true;
            }
        }

        if (attackCooldownTimer > 0){
            attackCooldownTimer -= delta;
            if (attackCooldownTimer <= 0){
                canAttack = true;
                if (state == KnightState.ATTACK_UP || state == KnightState.ATTACK_DOWN || state == KnightState.ATTACK_SLASH || state == KnightState.ATTACK_ALT_SLASH){
                    if (onGround){
                        state = KnightState.IDLE;
                    }
                    else {
                        state = KnightState.LANDING;
                    }
                }
            }
        }

        if (usingAltSlashTimer < 1f){
            usingAltSlashTimer += delta;
            if (usingAltSlashTimer > usingAltSlashTime){
                useAltSlash = false;
            }
        }

        if (state == KnightState.DAMAGED) {
            damagedTimer -= delta;
            velocity.x = MathUtils.lerp(velocity.x, 0, delta * 10f);
            if (damagedTimer <= 0) {
                state = KnightState.IDLE;
                velocity.x = 0;
            }
        }

        if (noDamage){
            noDamageTimer -= delta;
            if (noDamageTimer <= 0){
                noDamage = false;
            }
        }

        inputHandle();

        position.x += velocity.x * delta;
        bounds.setPosition(position.x, position.y);

        for (Rectangle rectangle : game.getGrounds()){
            if (Intersector.overlaps(rectangle, bounds)){
                if (velocity.x > 0){
                    position.x = rectangle.x - bounds.width;
                    wallInTheRight = true;
                }
                else if (velocity.x < 0){
                    position.x = rectangle.x + rectangle.width;
                    wallInTheRight = false;
                }
                if (!onGround && velocity.y <= 0) {
                    state = KnightState.ON_WALL;
                    velocity.y /= 2;
                }
                velocity.x = 0;
                bounds.setPosition(position.x, position.y);
            }
        }

        if (state == KnightState.DASH) {
            velocity.y = 0;
        }
        else{
            velocity.y += game.getGRAVITY() * delta;
        }

        position.y += velocity.y * delta;
        bounds.setPosition(position.x, position.y);

        onGround = false;

        for (Rectangle rectangle : game.getGrounds()){
            if (Intersector.overlaps(rectangle, bounds)){
                if (velocity.y > 0){
                    position.y = rectangle.y - bounds.height;
                }
                else{
                    position.y = rectangle.y + rectangle.height;
                    onGround = true;
                    if (!wasOnGround){
                        audioListener.onAudioEvent(AudioAction.LAND);
                    }
                }
                velocity.y = 0;
                bounds.setPosition(position.x, position.y);
            }
        }

        if (onGround && velocity.x != 0){
            audioListener.onAudioEvent(AudioAction.STONE_FOOTSTEP);
        }

        if (state == KnightState.ON_WALL) {
            audioListener.onAudioEvent(AudioAction.WALL_SLIDE);
        } else {
            audioListener.onAudioEvent(AudioAction.WALL_SLIDE_STOP);
        }

    }

    public void inputHandle(){
        boolean jumpJustPressed = Gdx.input.isKeyJustPressed(App.bindings.get(GameAction.JUMP));
        boolean jumpPressed = Gdx.input.isKeyPressed(App.bindings.get(GameAction.JUMP));

        boolean leftJustPressed = Gdx.input.isKeyJustPressed(App.bindings.get(GameAction.MOVE_LEFT));
        boolean rightJustPressed = Gdx.input.isKeyJustPressed(App.bindings.get(GameAction.MOVE_RIGHT));

        boolean leftPressed = Gdx.input.isKeyPressed(App.bindings.get(GameAction.MOVE_LEFT));
        boolean rightPressed = Gdx.input.isKeyPressed(App.bindings.get(GameAction.MOVE_RIGHT));

        if (state == KnightState.DASH || state == KnightState.ATTACK_UP || state == KnightState.ATTACK_DOWN || state == KnightState.ATTACK_SLASH || state == KnightState.ATTACK_ALT_SLASH || state == KnightState.DAMAGED){
            return;
        }

        if (masks < 5 && soul >= 33 && Gdx.input.isKeyPressed(App.bindings.get(GameAction.FOCUS_AND_CAST)) && velocity.x == 0 && velocity.y == 0){
            if (!focusStart){
                focusStart = true;
                focusTimer = focusTime;
                audioListener.onAudioEvent(AudioAction.FOCUS_READY);
                state = KnightState.FOCUS_START;
            }
            return;
        }

        if (state == KnightState.ON_WALL){
            boolean wallJump = false;
            onFirstJump = false;
            if (jumpJustPressed && ((wallInTheRight && leftPressed) || (!wallInTheRight && rightPressed))) {
                wallJump = true;
            }
            else if (jumpPressed && ((wallInTheRight && leftJustPressed) || (!wallInTheRight && rightJustPressed))) {
                wallJump = true;
            }

            if (wallJump){
                audioListener.onAudioEvent(AudioAction.WALL_JUMP);
                velocity.y = JUMP_SPEED;
                state = KnightState.WALL_JUMP;
                onFirstJump = true;
                if (wallInTheRight){
                    flipped = false;
                    velocity.x = - 1.5f * MOVE_SPEED;
                }
                else{
                    flipped = true;
                    velocity.x = 1.5f * MOVE_SPEED;
                }
                return;
            }
        }

        if (Gdx.input.isKeyPressed(App.bindings.get(GameAction.DASH))){
            if (canDash){
                audioListener.onAudioEvent(AudioAction.KNIGHT_DASH);
                canDash = false;
                dashCooldownTimer = dashCooldown;
                dashTimer = dashTime;
                state = KnightState.DASH;
                velocity.x = (flipped ? 1 : -1 ) * MOVE_SPEED * 3;
                return;
            }
        }

        if (Gdx.input.isKeyJustPressed(App.bindings.get(GameAction.ATTACK)) && canAttack){
            canAttack = false;
            attackCooldownTimer = attackCooldownTime;

            if (Gdx.input.isKeyPressed(App.bindings.get(GameAction.MOVE_UP))){
                state = KnightState.ATTACK_UP;
                audioListener.onAudioEvent(AudioAction.KNIGHT_ATTACK);
            }
            else if (Gdx.input.isKeyPressed(App.bindings.get(GameAction.MOVE_DOWN))){
                state = KnightState.ATTACK_DOWN;
                audioListener.onAudioEvent(AudioAction.KNIGHT_ATTACK);
            }
            else {
                if (useAltSlash){
                    state = KnightState.ATTACK_ALT_SLASH;
                    audioListener.onAudioEvent(AudioAction.KNIGHT_ATTACK_ALT);
                }
                else {
                    state = KnightState.ATTACK_SLASH;
                    audioListener.onAudioEvent(AudioAction.KNIGHT_ATTACK);
                }
                useAltSlash = !useAltSlash;
            }
            GameController.setSlashEffect(this);
            return;
        }

        if (jumpJustPressed) {
            if (onGround) {
                audioListener.onAudioEvent(AudioAction.KNIGHT_JUMP);
                velocity.y = JUMP_SPEED;
                state = KnightState.JUMP;
                onGround = false;
                onFirstJump = true;
                return;
            } else if (onFirstJump) {
                audioListener.onAudioEvent(AudioAction.KNIGHT_JUMP);
                velocity.y = JUMP_SPEED;
                state = KnightState.DOUBLE_JUMP;
                onFirstJump = false;
                return;
            }
        }

        if (Gdx.input.isKeyPressed(App.bindings.get(GameAction.MOVE_RIGHT))){
            flipped = true;
            velocity.x = MOVE_SPEED;
            if (onGround) {
                state = KnightState.RUN;
            }
        }
        else if (Gdx.input.isKeyPressed(App.bindings.get(GameAction.MOVE_LEFT))){
            flipped = false;
            velocity.x = -MOVE_SPEED;
            if (onGround) {
                state = KnightState.RUN;
            }
        }
        else {
            velocity.x = 0;
            if (onGround) {state = KnightState.IDLE;}
        }

        if (!Gdx.input.isKeyPressed(App.bindings.get(GameAction.JUMP)) && velocity.y > 0){
            velocity.y = velocity.y * 0.7f;
        }
    }

    public void takeDamage(int sign){
        audioListener.onAudioEvent(AudioAction.KNIGHT_TAKE_DAMAGE);
        velocity.x = 250f * sign;
        masks--;
        GameController.updateMasks();
        if (masks == 0){
            velocity.y = 60f;
            return;
        }
        noDamage = true;
        noDamageTimer = noDamageTime;
        state = KnightState.DAMAGED;
        damagedTimer = damagedTime;
        velocity.y = 20f;
    }

    public int getMasks() {
        return masks;
    }

    public int getSoul() {
        return soul;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public boolean isFlipped() {
        return flipped;
    }

    public KnightState getState() {
        return state;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Rectangle getBounds() {
        return bounds;
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

    public float gethOffsetDown() {
        return hOffsetDown;
    }

    public float gethOffsetUp() {
        return hOffsetUp;
    }

    public boolean isNoDamage() {
        return noDamage;
    }

    public void setAudioListener(EntityAudioListener audioListener) {
        this.audioListener = audioListener;
    }

    public void increaseSoul(int amount){
        if (soul < 99){
            float delayInSeconds = 0.5f;
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    audioListener.onAudioEvent(AudioAction.KNIGHT_GAIN_SOUL);
                    soul = Math.min(99, soul+amount);
                }
            }, delayInSeconds);
        }
    }
}
