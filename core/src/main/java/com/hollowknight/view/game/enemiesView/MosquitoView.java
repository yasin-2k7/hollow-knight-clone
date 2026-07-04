package com.hollowknight.view.game.enemiesView;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hollowknight.model.enemies.FlyerEnemy;
import com.hollowknight.model.enemies.SimpleEnemy;
import com.hollowknight.model.enums.EnemyState;

public class MosquitoView implements EnemyView{
    private Texture idleSheet;
    private Texture turnSheet;
    private Texture deathAirSheet;
    private Texture deathLandSheet;
    private Texture attackSheet;
    private Texture attackAnticipateSheet;

    private FlyerEnemy model;


    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> turnAnimation;
    private Animation<TextureRegion> deathAirAnimation;
    private Animation<TextureRegion> deathLandAnimation;
    private Animation<TextureRegion> attackAnimation;
    private Animation<TextureRegion> attackAnticipateAnimation;

    private float stateTime = 0;
    private EnemyState previousState = EnemyState.WALK;

    public MosquitoView(FlyerEnemy model){
        this.model = model;

        int frameW = 220;
        int frameH = 155;

        idleSheet = new Texture("enemies/mosquito/Idle.png");
        TextureRegion[][] frames = TextureRegion.split(idleSheet, frameW, frameH);
        TextureRegion[] idleFrames = new TextureRegion[8];
        System.arraycopy(frames[0], 0, idleFrames, 0, 8);
        idleAnimation = new Animation<>(0.1f, idleFrames);

        turnSheet = new Texture("enemies/mosquito/Turn.png");
        frames = TextureRegion.split(turnSheet, frameW, frameH);
        TextureRegion[] turnFrames = new TextureRegion[2];
        System.arraycopy(frames[0], 0, turnFrames, 0, 2);
        turnAnimation = new Animation<>(0.2f, turnFrames);

        deathAirSheet = new Texture("enemies/mosquito/Death Air.png");
        frames = TextureRegion.split(deathAirSheet, frameW, frameH);
        TextureRegion[] deathAirFrames = new TextureRegion[3];
        System.arraycopy(frames[0], 0, deathAirFrames, 0, 3);
        deathAirAnimation = new Animation<>(0.2f, deathAirFrames);

        deathLandSheet = new Texture("enemies/mosquito/Death Land.png");
        frames = TextureRegion.split(deathLandSheet, frameW, frameH);
        TextureRegion[] deathLandFrames = new TextureRegion[2];
        System.arraycopy(frames[0], 0, deathLandFrames, 0, 2);
        deathLandAnimation = new Animation<>(0.2f, deathLandFrames);

        attackSheet = new Texture("enemies/mosquito/Attack.png");
        frames = TextureRegion.split(attackSheet, frameW, frameH);
        TextureRegion[] attackFrames = new TextureRegion[3];
        System.arraycopy(frames[0], 0, attackFrames, 0, 3);
        attackAnimation = new Animation<>(0.15f, attackFrames);

        attackAnticipateSheet = new Texture("enemies/mosquito/Attack Anticipate.png");
        frames = TextureRegion.split(attackAnticipateSheet, frameW, frameH);
        TextureRegion[] attackAnticipateFrames = new TextureRegion[6];
        System.arraycopy(frames[0], 0, attackAnticipateFrames, 0, 6);
        attackAnticipateAnimation = new Animation<>(0.1f, attackAnticipateFrames);

    }

    public void draw(SpriteBatch batch, float delta){
        if (model.getState() != previousState) {
            stateTime = 0;
            previousState = model.getState();
        }

        if (model.getState() != EnemyState.DAMAGED){
            stateTime += delta;
        }

        TextureRegion currentFrame;
        switch (model.getState()) {
            case WALK, BACK:
                currentFrame = idleAnimation.getKeyFrame(stateTime, true);
                break;
            case TURN:
                currentFrame = turnAnimation.getKeyFrame(stateTime, false);
                break;
            case DEATH_AIR:
                currentFrame = deathAirAnimation.getKeyFrame(stateTime, false);
                break;
            case DEATH_LAND:
                currentFrame = deathLandAnimation.getKeyFrame(stateTime, false);
                break;
            case CHASE:
                currentFrame = attackAnticipateAnimation.getKeyFrame(stateTime, true);
                break;
            case ATTACK:
                currentFrame = attackAnimation.getKeyFrame(stateTime, false);
                break;
            default:
                currentFrame = idleAnimation.getKeyFrame(stateTime, true);
        }

        if (model.isFlipped() && !currentFrame.isFlipX()){
            currentFrame.flip(true, false);
        }
        else if (!model.isFlipped() && currentFrame.isFlipX()){
            currentFrame.flip(true, false);
        }

        float renderX = model.getPosition().x - model.getwOffset();
        float renderY = model.getPosition().y - model.gethOffsetDown();

        float renderWidth = model.getWidth();
        float renderHeight = model.getHeight();

        batch.draw(currentFrame, renderX, renderY, renderWidth, renderHeight);
    }

    public void dispose() {
        if (idleSheet != null) idleSheet.dispose();
        if (attackSheet != null) attackSheet.dispose();
        if (attackAnticipateSheet != null) attackAnticipateSheet.dispose();
        if (turnSheet != null) turnSheet.dispose();
        if (deathAirSheet != null) deathAirSheet.dispose();
        if (deathLandSheet != null) deathLandSheet.dispose();

    }
}
