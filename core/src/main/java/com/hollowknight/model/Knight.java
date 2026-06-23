package com.hollowknight.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.hollowknight.model.enums.GameAction;
import com.hollowknight.model.enums.KnightState;

public class Knight {
    private Game game;
    private int masks;
    private int soul;

    private float width = 225f;
    private float height = 125f;
    private float wOffset = 95f;
    private float hOffsetUp = 40f;
    private float hOffsetDown = 5f;
    private Rectangle bounds;
    private boolean onGround;
    private boolean flipped;
    private KnightState state;
    private float unitScale = 1 / 5f;

    private Vector2 position;
    private Vector2 velocity;

    private final float MOVE_SPEED = 30f;
    private final float JUMP_SPEED = 110f;

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
        inputHandle();

        position.x += velocity.x * delta;
        bounds.setPosition(position.x, position.y);

        for (Rectangle rectangle : game.getGrounds()){
            if (Intersector.overlaps(rectangle, bounds)){
                if (velocity.x > 0){
                    position.x = rectangle.x - bounds.width;
                }
                else if (velocity.x < 0){
                    position.x = rectangle.x + rectangle.width;
                }
                velocity.x = 0;
                bounds.setPosition(position.x, position.y);
            }
        }

        velocity.y += game.getGRAVITY() * delta;
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
}
