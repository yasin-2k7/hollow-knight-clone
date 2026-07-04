package com.hollowknight.view.game.enemiesView;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hollowknight.model.enemies.CrystallizedEnemy;
import com.hollowknight.model.enemies.HuskHornHeadEnemy;
import com.hollowknight.model.enums.EnemyState;

public class CrystallizedView implements EnemyView{
    private Texture idleSheet;
    private Texture walkSheet;
    private Texture turnSheet;
    private Texture deathAirSheet;
    private Texture deathLandSheet;
    private Texture evadeSheet;
    private Texture shootSheet;

    private CrystallizedEnemy model;


    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> turnAnimation;
    private Animation<TextureRegion> walkAnimation;
    private Animation<TextureRegion> deathAirAnimation;
    private Animation<TextureRegion> deathLandAnimation;
    private Animation<TextureRegion> evadeAnimation;
    private Animation<TextureRegion> shootAnimation;

    private float stateTime = 0;
    private EnemyState previousState = EnemyState.IDLE;

    public CrystallizedView(CrystallizedEnemy model){
        this.model = model;

        int frameW = 285;
        int frameH = 189;

        idleSheet = new Texture("enemies/crystallized/Idle.png");
        TextureRegion[][] frames = TextureRegion.split(idleSheet, frameW, frameH);
        TextureRegion[] idleFrames = new TextureRegion[5];
        System.arraycopy(frames[0], 0, idleFrames, 0, 5);
        idleAnimation = new Animation<>(0.2f, idleFrames);

        turnSheet = new Texture("enemies/crystallized/Turn.png");
        frames = TextureRegion.split(turnSheet, frameW, frameH);
        TextureRegion[] turnFrames = new TextureRegion[3];
        System.arraycopy(frames[0], 0, turnFrames, 0, 3);
        turnAnimation = new Animation<>(0.2f, turnFrames);

        deathAirSheet = new Texture("enemies/crystallized/Death Air.png");
        frames = TextureRegion.split(deathAirSheet, frameW, frameH);
        TextureRegion[] deathAirFrames = new TextureRegion[3];
        System.arraycopy(frames[0], 0, deathAirFrames, 0, 3);
        deathAirAnimation = new Animation<>(0.2f, deathAirFrames);

        deathLandSheet = new Texture("enemies/crystallized/Death Land.png");
        frames = TextureRegion.split(deathLandSheet, frameW, frameH);
        TextureRegion[] deathLandFrames = new TextureRegion[3];
        System.arraycopy(frames[0], 0, deathLandFrames, 0, 3);
        deathLandAnimation = new Animation<>(0.2f, deathLandFrames);

        evadeSheet = new Texture("enemies/crystallized/Evade.png");
        frames = TextureRegion.split(evadeSheet, frameW, frameH);
        TextureRegion[] evadeFrames = new TextureRegion[7];
        System.arraycopy(frames[0], 0, evadeFrames, 0, 7);
        evadeAnimation = new Animation<>(0.2f, evadeFrames);

        shootSheet = new Texture("enemies/crystallized/Shoot.png");
        frames = TextureRegion.split(shootSheet, frameW, frameH);
        TextureRegion[] shootFrames = new TextureRegion[7];
        System.arraycopy(frames[0], 0, shootFrames, 0, 7);
        shootAnimation = new Animation<>(0.2f, shootFrames);

        walkSheet = new Texture("enemies/crystallized/Run.png");
        frames = TextureRegion.split(walkSheet, frameW, frameH);
        TextureRegion[] runFrames = new TextureRegion[6];
        System.arraycopy(frames[0], 0, runFrames, 0, 6);
        walkAnimation = new Animation<>(0.2f, runFrames);

    }

    public void draw(SpriteBatch batch, float delta){
        if (model.getState() != previousState) {
            stateTime = 0;
            previousState = model.getState();
        }

        if (model.getState() != EnemyState.DAMAGED){
            stateTime += delta;
        }

        TextureRegion currentFrame = switch (model.getState()) {
            case TURN -> turnAnimation.getKeyFrame(stateTime, false);
            case DEATH_AIR -> deathAirAnimation.getKeyFrame(stateTime, false);
            case DEATH_LAND -> deathLandAnimation.getKeyFrame(stateTime, false);
            case SHOOT -> shootAnimation.getKeyFrame(stateTime, false);
            case EVADE -> evadeAnimation.getKeyFrame(stateTime, true);
            case WALK -> walkAnimation.getKeyFrame(stateTime, true);
            default -> idleAnimation.getKeyFrame(stateTime, true);
        };

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
        if (shootSheet != null) shootSheet.dispose();
        if (turnSheet != null) turnSheet.dispose();
        if (deathAirSheet != null) deathAirSheet.dispose();
        if (deathLandSheet != null) deathLandSheet.dispose();
        if (evadeAnimation != null) evadeSheet.dispose();
        if (walkSheet != null) walkSheet.dispose();

    }
}
