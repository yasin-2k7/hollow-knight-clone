package com.hollowknight.model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.hollowknight.controller.GameController;
import com.hollowknight.model.enums.AudioAction;

import com.hollowknight.model.enums.ZoteState;

public class Zote {
    private Game game;

    private EntityAudioListener audioListener;

    private Vector2 position;
    private float width,height,wOffset,hOffsetUp,hOffsetDown;
    private Rectangle talkBox;
    private float speakTime = 1.5f;
    private float speakTimer = 0f;
    private ZoteState state = ZoteState.IDLE;
    private boolean flipped;
    private final float turnTime = 0.5f;
    private float turnTimer = 0f;

    public Zote(float startX, float startY, float width, float height, float wOffset, float hOffsetUp, float hOffsetDown) {
        float scale = App.getUnitScale();
        this.width = width*scale;
        this.height = height*scale;
        position = new Vector2(startX, startY);

        this.wOffset = wOffset*scale;
        this.hOffsetUp = hOffsetUp*scale;
        this.hOffsetDown = hOffsetDown*scale;

        float talkBoxWidth = 50;
        float talkBoxHeight = 30;
        float newWidth = (this.width - 2*this.wOffset);
        float newHeight = (this.height - this.hOffsetDown - this.hOffsetUp);
        float newX = startX;
        float newY = startY + this.hOffsetDown;
        talkBox = new Rectangle(newX - (talkBoxWidth-newWidth)/2, newY - (talkBoxHeight-newHeight)/2, talkBoxWidth, talkBoxHeight);
    }

    public void speak(){
        speakTimer = speakTime;
        state = ZoteState.SPEAK;
        audioListener.onAudioEvent(AudioAction.ZOTE_SPEAK);
    }

    public void update(float delta){
        if (state == ZoteState.SPEAK){
            speakTimer -= delta;
            if (speakTimer <= 0){
                state = ZoteState.IDLE;
            }
        }
        if (state == ZoteState.TURN){
            turnTimer -= delta;
            if (turnTimer <= 0) {
                state = ZoteState.IDLE;
                flipped = !flipped;
            }
        }
    }

    public void turn(){
        state = ZoteState.TURN;
        turnTimer = turnTime;
    }

    public void setAudioListener(EntityAudioListener audioListener) {
        this.audioListener = audioListener;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Rectangle getTalkBox() {
        return talkBox;
    }

    public ZoteState getState() {
        return state;
    }

    public boolean isFlipped() {
        return flipped;
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

    public float gethOffsetUp() {
        return hOffsetUp;
    }

    public float gethOffsetDown() {
        return hOffsetDown;
    }
}
