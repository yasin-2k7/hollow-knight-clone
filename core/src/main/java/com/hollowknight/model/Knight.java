package com.hollowknight.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.hollowknight.model.enums.GameAction;
import com.hollowknight.model.enums.KnightState;

public class Knight {
    private int masks;
    private int soul;

    private boolean onGround;
    private boolean flipped;
    private KnightState state;

    private Vector2 position;
    private Vector2 velocity;

    private final float MOVE_SPEED = 30f;
    private final float JUMP_SPEED = 110f;

    public Knight(int masks, int soul, float startX, float startY) {
        position = new Vector2(startX, startY);
        velocity = new Vector2(0, 0);
        state = KnightState.IDLE;
        onGround = false;
        this.masks = masks;
        this.soul = soul;
    }

    public void update(float delta){
        inputHandle();

        if (!onGround){
            velocity.y += App.getCurrentGame().getGRAVITY() * delta;
            if (velocity.y < 0){
                state = KnightState.LANDING;
            }
        }
        else {
            velocity.y = 0;
        }

        position.add(velocity.x * delta, velocity.y * delta);

        if (position.y <= 62f) {
            position.y = 62f;
            onGround = true;
        }

    }

    public void inputHandle(){
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
        if (Gdx.input.isKeyJustPressed(App.bindings.get(GameAction.JUMP)) && onGround){
            velocity.y = JUMP_SPEED;
            state = KnightState.JUMP;
            onGround = false;
        }
        if (!Gdx.input.isKeyPressed(App.bindings.get(GameAction.JUMP)) && velocity.y > 0){
            velocity.y = velocity.y * 0.5f;
        }
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
}
