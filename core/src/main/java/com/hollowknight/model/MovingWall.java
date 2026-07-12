package com.hollowknight.model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.hollowknight.model.enums.AudioAction;

public class MovingWall {
    private Rectangle bounds;
    private Vector2 position;
    private float width = 60f;
    private float height = 210f;
    private EntityAudioListener audioListener;
    private float startY, finishY;
    private final float moveSpeed = 50f;
    private float speedY = 0;

    public MovingWall(Vector2 position) {
        this.position = position;
        bounds = new Rectangle(position.x, position.y, width*App.getUnitScale(), height*App.getUnitScale());
        startY = position.y;
        finishY = position.y - 35f;
    }

    public void update(float delta){
        if (speedY == 0) return;
        position.y += speedY * delta;

        if (speedY < 0 && position.y <= finishY){
            position.y = finishY;
            audioListener.onAudioEvent(AudioAction.WALL_MOVE_IMPACT);
            speedY = 0;
        }
        else if (speedY > 0 && position.y >= startY){
            position.y = startY;
            audioListener.onAudioEvent(AudioAction.WALL_MOVE_IMPACT);
            speedY = 0;
        }
        bounds.y = position.y;
    }

    public void moveUp(){
        audioListener.onAudioEvent(AudioAction.WALL_MOVE);
        speedY = moveSpeed;
    }

    public void moveDown(){
        App.getCurrentGame().getCurrentMap().getGrounds().add(getBounds());
        App.getCurrentGame().getCurrentMap().getGrounds().add(getBounds());
        audioListener.onAudioEvent(AudioAction.WALL_MOVE);
        speedY = -moveSpeed;
    }

    public void reset(){
        position.y = startY;
        bounds.y = startY;
        speedY = 0;

    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setAudioListener(EntityAudioListener audioListener) {
        this.audioListener = audioListener;
    }
}
