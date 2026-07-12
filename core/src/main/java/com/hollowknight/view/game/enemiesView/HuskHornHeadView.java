package com.hollowknight.view.game.enemiesView;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hollowknight.model.enemies.Enemy;
import com.hollowknight.model.enemies.FlyerEnemy;
import com.hollowknight.model.enemies.HuskHornHeadEnemy;
import com.hollowknight.model.enums.EnemyState;

public class HuskHornHeadView implements EnemyView{
    private Texture idleSheet;
    private Texture walkSheet;
    private Texture turnSheet;
    private Texture deathAirSheet;
    private Texture deathLandSheet;
    private Texture attackSheet;
    private Texture attackCooldownSheet;
    private Texture attackAnticipateSheet;

    private HuskHornHeadEnemy model;


    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> turnAnimation;
    private Animation<TextureRegion> walkAnimation;
    private TextureRegion deathAirAnimation;
    private Animation<TextureRegion> deathLandAnimation;
    private Animation<TextureRegion> attackAnimation;
    private TextureRegion attackCooldownAnimation;
    private Animation<TextureRegion> attackAnticipateAnimation;

    private float stateTime = 0;
    private EnemyState previousState = EnemyState.WALK;

    public HuskHornHeadView(HuskHornHeadEnemy model){
        this.model = model;

        int frameW = 239;
        int frameH = 219;

        idleSheet = new Texture("enemies/husk_hornhead/Idle.png");
        TextureRegion[][] frames = TextureRegion.split(idleSheet, frameW, frameH);
        TextureRegion[] idleFrames = new TextureRegion[6];
        System.arraycopy(frames[0], 0, idleFrames, 0, 6);
        idleAnimation = new Animation<>(0.2f, idleFrames);

        turnSheet = new Texture("enemies/husk_hornhead/Turn.png");
        frames = TextureRegion.split(turnSheet, frameW, frameH);
        TextureRegion[] turnFrames = new TextureRegion[2];
        System.arraycopy(frames[0], 0, turnFrames, 0, 2);
        turnAnimation = new Animation<>(0.2f, turnFrames);

        deathAirSheet = new Texture("enemies/husk_hornhead/Death Air.png");
        deathAirAnimation = new TextureRegion(deathAirSheet);

        attackCooldownSheet = new Texture("enemies/husk_hornhead/Attack Cooldown.png");
        attackCooldownAnimation = new TextureRegion(attackCooldownSheet);

        deathLandSheet = new Texture("enemies/husk_hornhead/Death Land.png");
        frames = TextureRegion.split(deathLandSheet, frameW, frameH);
        TextureRegion[] deathLandFrames = new TextureRegion[2];
        System.arraycopy(frames[0], 0, deathLandFrames, 0, 2);
        deathLandAnimation = new Animation<>(0.2f, deathLandFrames);

        attackSheet = new Texture("enemies/husk_hornhead/Attack Lunge.png");
        frames = TextureRegion.split(attackSheet, frameW, frameH);
        TextureRegion[] attackFrames = new TextureRegion[12];
        System.arraycopy(frames[0], 0, attackFrames, 0, 12);
        attackAnimation = new Animation<>(0.1f, attackFrames);

        attackAnticipateSheet = new Texture("enemies/husk_hornhead/Attack Anticipate.png");
        frames = TextureRegion.split(attackAnticipateSheet, frameW, frameH);
        TextureRegion[] attackAnticipateFrames = new TextureRegion[5];
        System.arraycopy(frames[0], 0, attackAnticipateFrames, 0, 5);
        attackAnticipateAnimation = new Animation<>(0.1f, attackAnticipateFrames);

        walkSheet = new Texture("enemies/husk_hornhead/Walk.png");
        frames = TextureRegion.split(walkSheet, frameW, frameH);
        TextureRegion[] walkFrames = new TextureRegion[7];
        System.arraycopy(frames[0], 0, walkFrames, 0, 7);
        walkAnimation = new Animation<>(0.2f, walkFrames);

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
            case DEATH_AIR -> deathAirAnimation;
            case DEATH_LAND -> deathLandAnimation.getKeyFrame(stateTime, false);
            case ATTACK_ANTICIPATE -> attackAnticipateAnimation.getKeyFrame(stateTime, false);
            case ATTACK -> attackAnimation.getKeyFrame(stateTime, true);
            case IDLE -> idleAnimation.getKeyFrame(stateTime, true);
            case ATTACK_COOLDOWN -> attackCooldownAnimation;
            default -> walkAnimation.getKeyFrame(stateTime, true);
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

    @Override
    public Enemy getModel() {
        return null;
    }

    @Override
    public void updateRate(float rate) {

    }

    public void dispose() {
        if (idleSheet != null) idleSheet.dispose();
        if (attackSheet != null) attackSheet.dispose();
        if (attackAnticipateSheet != null) attackAnticipateSheet.dispose();
        if (turnSheet != null) turnSheet.dispose();
        if (deathAirSheet != null) deathAirSheet.dispose();
        if (deathLandSheet != null) deathLandSheet.dispose();
        if (attackCooldownSheet != null) attackCooldownSheet.dispose();
        if (walkSheet != null) walkSheet.dispose();

    }
}
