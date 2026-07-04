package com.hollowknight.view.game.enemiesView;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hollowknight.model.enemies.SimpleEnemy;
import com.hollowknight.model.enums.EnemyState;

public class CrawlerView implements EnemyView{
    private Texture walkSheet;
    private Texture turnSheet;
    private Texture deathAirSheet;
    private Texture deathLandSheet;

    private SimpleEnemy model;


    private Animation<TextureRegion> walkAnimation;
    private Animation<TextureRegion> turnAnimation;
    private Animation<TextureRegion> deathAirAnimation;
    private Animation<TextureRegion> deathLandAnimation;

    private float stateTime = 0;
    private EnemyState previousState = EnemyState.WALK;

    public CrawlerView(SimpleEnemy model){
        this.model = model;

        int frameW = 301;
        int frameH = 149;
        int deathFrameW = 303;
        int deathFrameH = 177;

        walkSheet = new Texture("enemies/crawler/Walk.png");
        TextureRegion[][] frames = TextureRegion.split(walkSheet, frameW, frameH);
        TextureRegion[] walkFrames = new TextureRegion[4];
        System.arraycopy(frames[0], 0, walkFrames, 0, 4);
        walkAnimation = new Animation<>(0.2f, walkFrames);

        turnSheet = new Texture("enemies/crawler/Turn.png");
        frames = TextureRegion.split(turnSheet, frameW, frameH);
        TextureRegion[] turnFrames = new TextureRegion[2];
        System.arraycopy(frames[0], 0, turnFrames, 0, 2);
        turnAnimation = new Animation<>(0.2f, turnFrames);

        deathAirSheet = new Texture("enemies/crawler/Death Air.png");
        frames = TextureRegion.split(deathAirSheet, deathFrameW, deathFrameH);
        TextureRegion[] deathAirFrames = new TextureRegion[3];
        System.arraycopy(frames[0], 0, deathAirFrames, 0, 3);
        deathAirAnimation = new Animation<>(0.2f, deathAirFrames);

        deathLandSheet = new Texture("enemies/crawler/Death Land.png");
        frames = TextureRegion.split(deathLandSheet, deathFrameW, deathFrameH);
        TextureRegion[] deathLandFrames = new TextureRegion[2];
        System.arraycopy(frames[0], 0, deathLandFrames, 0, 2);
        deathLandAnimation = new Animation<>(0.2f, deathLandFrames);

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
            case WALK:
                currentFrame = walkAnimation.getKeyFrame(stateTime, true);
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
            default:
                currentFrame = walkAnimation.getKeyFrame(stateTime, true);
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
        if (walkSheet != null) walkSheet.dispose();
        if (turnSheet != null) turnSheet.dispose();
        if (deathAirSheet != null) deathAirSheet.dispose();
        if (deathLandSheet != null) deathLandSheet.dispose();

    }
}
