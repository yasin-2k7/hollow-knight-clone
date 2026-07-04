package com.hollowknight.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.hollowknight.controller.GameController;
import com.hollowknight.model.enums.GameAction;
import com.hollowknight.model.enums.KnightState;

public class Knight {
    private Game game;
    private int masks;
    private int soul;

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
                }
                velocity.y = 0;
                bounds.setPosition(position.x, position.y);
            }
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
            }
            else if (Gdx.input.isKeyPressed(App.bindings.get(GameAction.MOVE_DOWN))){
                state = KnightState.ATTACK_DOWN;
            }
            else {
                if (useAltSlash){
                    state = KnightState.ATTACK_ALT_SLASH;
                }
                else {
                    state = KnightState.ATTACK_SLASH;
                }
                useAltSlash = !useAltSlash;
            }
            GameController.setSlashEffect(this);
            return;
        }

        if (jumpJustPressed) {
            if (onGround) {
                velocity.y = JUMP_SPEED;
                state = KnightState.JUMP;
                onGround = false;
                onFirstJump = true;
                return;
            } else if (onFirstJump) {
                velocity.y = JUMP_SPEED;
                state = KnightState.DOUBLE_JUMP;
                onFirstJump = false;
                return;
            }
        }

        if (Gdx.input.isKeyPressed(App.bindings.get(GameAction.MOVE_RIGHT))){
            flipped = true;
            velocity.x = MOVE_SPEED;
            if (onGround) { state = KnightState.RUN; }
        }
        else if (Gdx.input.isKeyPressed(App.bindings.get(GameAction.MOVE_LEFT))){
            flipped = false;
            velocity.x = -MOVE_SPEED;
            if (onGround) { state = KnightState.RUN; }
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
}
