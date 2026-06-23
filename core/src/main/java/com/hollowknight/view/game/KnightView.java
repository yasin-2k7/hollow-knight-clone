package com.hollowknight.view.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hollowknight.model.Knight;
import com.hollowknight.model.enums.KnightState;

public class KnightView {
    private Texture idleSheet;
    private Texture runSheet;
    private Texture jumpSheet;
    private Texture landSheet;

    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> runAnimation;
    private Animation<TextureRegion> jumpAnimation;
    private Animation<TextureRegion> landAnimation;

    private float stateTime = 0;
    private KnightState previousState = KnightState.IDLE;

    public KnightView(){
        int frameW = 349;
        int frameH = 186;

        idleSheet = new Texture("knight/Idle.png");
        TextureRegion[][] frames = TextureRegion.split(idleSheet, frameW, frameH);
        TextureRegion[] idleFrames = new TextureRegion[9];
        System.arraycopy(frames[0], 0, idleFrames, 0, 9);
        idleAnimation = new Animation<>(0.2f, idleFrames);

        runSheet = new Texture("knight/Run.png");
        frames = TextureRegion.split(runSheet, frameW, frameH);
        TextureRegion[] runFrames = new TextureRegion[13];
        System.arraycopy(frames[0], 0, runFrames, 0, 13);
        runAnimation = new Animation<>(0.2f, runFrames);

        jumpSheet = new Texture("knight/Airborne.png");
        frames = TextureRegion.split(jumpSheet, frameW, frameH);
        TextureRegion[] jumpFrames = new TextureRegion[12];
        System.arraycopy(frames[0], 0, jumpFrames, 0, 12);
        jumpAnimation = new Animation<>(0.2f, jumpFrames);

        landSheet = new Texture("knight/Landing.png");
        frames = TextureRegion.split(landSheet, frameW, frameH);
        TextureRegion[] landFrames = new TextureRegion[4];
        System.arraycopy(frames[0], 0, landFrames, 0, 4);
        landAnimation = new Animation<>(0.2f, landFrames);
    }

    public void draw(SpriteBatch batch, Knight knight, float delta){
        if (knight.getState() != previousState) {
            stateTime = 0;
            previousState = knight.getState();
        }
        stateTime += delta;

        TextureRegion currentFrame;
        switch (knight.getState()) {
            case RUN:
                currentFrame = runAnimation.getKeyFrame(stateTime, true);
                break;
            case JUMP:
                currentFrame = jumpAnimation.getKeyFrame(stateTime, false);
                break;
            case LANDING:
                currentFrame = landAnimation.getKeyFrame(stateTime, false);
                break;
            case IDLE:
            default:
                currentFrame = idleAnimation.getKeyFrame(stateTime, true);
                break;
        }

        if (knight.isFlipped() && !currentFrame.isFlipX()){
            currentFrame.flip(true, false);
        }
        else if (!knight.isFlipped() && currentFrame.isFlipX()){
            currentFrame.flip(true, false);
        }

        batch.draw(currentFrame, knight.getPosition().x, knight.getPosition().y, 45f, 25f);
    }

    public void dispose() {
        if (idleSheet != null) idleSheet.dispose();
        if (runSheet != null) runSheet.dispose();
        if (jumpSheet != null) jumpSheet.dispose();
        if (landSheet != null) landSheet.dispose();

    }
}
