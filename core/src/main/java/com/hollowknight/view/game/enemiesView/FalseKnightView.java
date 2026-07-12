package com.hollowknight.view.game.enemiesView;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hollowknight.model.enemies.Enemy;
import com.hollowknight.model.enemies.FalseKnight;
import com.hollowknight.model.enums.EnemyState;

public class FalseKnightView implements EnemyView{
    private Texture idleSheet;
    private Texture runSheet;
    private Texture runAnticSheet;
    private Texture turnSheet;
    private Texture deathAirSheet;
    private Texture deathLandSheet;
    private Texture bodySheet;
    private Texture deathHitSheet;
    private Texture stunRecoverSheet;
    private Texture attackSheet;
    private Texture attackRecoverSheet;
    private Texture attackAnticipateSheet;
    private Texture jumpSheet;
    private Texture jumpAnticipateSheet;
    private Texture jumpAttackSheet;
    private Texture landSheet;
    private Texture jumpAttackRecoverSheet;

    private FalseKnight model;

    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> turnAnimation;
    private Animation<TextureRegion> runAnimation;
    private Animation<TextureRegion> runAnticAnimation;
    private Animation<TextureRegion> deathAirAnimation;
    private Animation<TextureRegion> deathLandAnimation;
    private Animation<TextureRegion> bodyAnimation;
    private Animation<TextureRegion> deathHitAnimation;
    private Animation<TextureRegion> stunRecoverAnimation;
    private Animation<TextureRegion> attackAnimation;
    private Animation<TextureRegion> attackRecoverAnimation;
    private Animation<TextureRegion> attackAnticipateAnimation;
    private Animation<TextureRegion> jumpAnimation;
    private Animation<TextureRegion> jumpAnticipateAnimation;
    private Animation<TextureRegion> jumpAttackAnimation;
    private Animation<TextureRegion> landAnimation;
    private Animation<TextureRegion> jumpAttackRecoverAnimation;

    private float stateTime = 0;
    private EnemyState previousState = EnemyState.WALK;

    public FalseKnightView(FalseKnight model){
        this.model = model;

        int frameW = 1095;
        int frameH = 636;

        idleSheet = new Texture("enemies/false knight/Idle.png");
        TextureRegion[][] frames = TextureRegion.split(idleSheet, frameW, frameH);
        TextureRegion[] idleFrames = new TextureRegion[5];
        System.arraycopy(frames[0], 0, idleFrames, 0, 5);
        idleAnimation = new Animation<>(0.2f, idleFrames);

        turnSheet = new Texture("enemies/false knight/Turn.png");
        frames = TextureRegion.split(turnSheet, frameW, frameH);
        TextureRegion[] turnFrames = new TextureRegion[2];
        System.arraycopy(frames[0], 0, turnFrames, 0, 2);
        turnAnimation = new Animation<>(0.2f, turnFrames);

        deathAirSheet = new Texture("enemies/false knight/DeathFall.png");
        frames = TextureRegion.split(deathAirSheet, frameW, frameH);
        TextureRegion[] deathAirFrames = new TextureRegion[3];
        System.arraycopy(frames[0], 0, deathAirFrames, 0, 3);
        deathAirAnimation = new Animation<>(0.2f, deathAirFrames);


        deathLandSheet = new Texture("enemies/false knight/DeathLand.png");
        frames = TextureRegion.split(deathLandSheet, frameW, frameH);
        TextureRegion[] deathLandFrames = new TextureRegion[11];
        System.arraycopy(frames[0], 0, deathLandFrames, 0, 11);
        deathLandAnimation = new Animation<>(0.2f, deathLandFrames);

        attackSheet = new Texture("enemies/false knight/Attack.png");
        frames = TextureRegion.split(attackSheet, frameW, frameH);
        TextureRegion[] attackFrames = new TextureRegion[3];
        System.arraycopy(frames[0], 0, attackFrames, 0, 3);
        attackAnimation = new Animation<>(0.1f, attackFrames);

        attackAnticipateSheet = new Texture("enemies/false knight/Attack Antic.png");
        frames = TextureRegion.split(attackAnticipateSheet, frameW, frameH);
        TextureRegion[] attackAnticipateFrames = new TextureRegion[6];
        System.arraycopy(frames[0], 0, attackAnticipateFrames, 0, 6);
        attackAnticipateAnimation = new Animation<>(0.1f, attackAnticipateFrames);

        attackRecoverSheet = new Texture("enemies/false knight/Attack Recover.png");
        frames = TextureRegion.split(attackRecoverSheet, frameW, frameH);
        TextureRegion[] attackRecoverFrames = new TextureRegion[5];
        System.arraycopy(frames[0], 0, attackRecoverFrames, 0, 5);
        attackRecoverAnimation = new Animation<>(0.1f, attackRecoverFrames);

        bodySheet = new Texture("enemies/false knight/Body.png");
        frames = TextureRegion.split(bodySheet, frameW, frameH);
        TextureRegion[] bodyFrames = new TextureRegion[5];
        System.arraycopy(frames[0], 0, bodyFrames, 0, 5);
        bodyAnimation = new Animation<>(0.1f, bodyFrames);

        deathHitSheet = new Texture("enemies/false knight/DeathHit.png");
        frames = TextureRegion.split(deathHitSheet, frameW, frameH);
        TextureRegion[] deathHitFrames = new TextureRegion[3];
        System.arraycopy(frames[0], 0, deathHitFrames, 0, 3);
        deathHitAnimation = new Animation<>(0.1f, deathHitFrames);

        runSheet = new Texture("enemies/false knight/Run.png");
        frames = TextureRegion.split(runSheet, frameW, frameH);
        TextureRegion[] runFrames = new TextureRegion[5];
        System.arraycopy(frames[0], 0, runFrames, 0, 5);
        runAnimation = new Animation<>(0.1f, runFrames);

        runAnticSheet = new Texture("enemies/false knight/Run Antic.png");
        frames = TextureRegion.split(runAnticSheet, frameW, frameH);
        TextureRegion[] runAnticFrames = new TextureRegion[2];
        System.arraycopy(frames[0], 0, runAnticFrames, 0, 2);
        runAnticAnimation = new Animation<>(0.1f, runAnticFrames);

        stunRecoverSheet = new Texture("enemies/false knight/Stun Recover.png");
        frames = TextureRegion.split(stunRecoverSheet, frameW, frameH);
        TextureRegion[] stunRecoverFrames = new TextureRegion[6];
        System.arraycopy(frames[0], 0, stunRecoverFrames, 0, 6);
        stunRecoverAnimation = new Animation<>(0.1f, stunRecoverFrames);

        jumpAnticipateSheet = new Texture("enemies/false knight/Jump Antic.png");
        frames = TextureRegion.split(jumpAnticipateSheet, frameW, frameH);
        TextureRegion[] jumpAnticipateFrames = new TextureRegion[3];
        System.arraycopy(frames[0], 0, jumpAnticipateFrames, 0, 3);
        jumpAnticipateAnimation = new Animation<>(0.1f, jumpAnticipateFrames);

        jumpSheet = new Texture("enemies/false knight/Jump.png");
        frames = TextureRegion.split(jumpSheet, frameW, frameH);
        TextureRegion[] jumpFrames = new TextureRegion[4];
        System.arraycopy(frames[0], 0, jumpFrames, 0, 4);
        jumpAnimation = new Animation<>(0.1f, jumpFrames);

        landSheet = new Texture("enemies/false knight/Land.png");
        frames = TextureRegion.split(landSheet, frameW, frameH);
        TextureRegion[] landFrames = new TextureRegion[5];
        System.arraycopy(frames[0], 0, landFrames, 0, 5);
        landAnimation = new Animation<>(0.1f, landFrames);

        jumpAttackSheet = new Texture("enemies/false knight/Jump Attack.png");
        frames = TextureRegion.split(jumpAttackSheet, frameW, frameH);
        TextureRegion[] jumpAttackFrames = new TextureRegion[8];
        System.arraycopy(frames[0], 0, jumpAttackFrames, 0, 8);
        jumpAttackAnimation = new Animation<>(0.22f, jumpAttackFrames);

        jumpAttackRecoverSheet = new Texture("enemies/false knight/Jump Attack Recover.png");
        frames = TextureRegion.split(jumpAttackRecoverSheet, frameW, frameH);
        TextureRegion[] jumpAttackRecoverFrames = new TextureRegion[2];
        System.arraycopy(frames[0], 0, jumpAttackRecoverFrames, 0, 2);
        jumpAttackRecoverAnimation = new Animation<>(0.2f, jumpAttackRecoverFrames);

    }

    public void updateSpeed(float rate){

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
            case ATTACK_ANTICIPATE -> attackAnticipateAnimation.getKeyFrame(stateTime, false);
            case ATTACK -> attackAnimation.getKeyFrame(stateTime, false);
            case ATTACK_RECOVER -> attackRecoverAnimation.getKeyFrame(stateTime, false);
            case BODY -> bodyAnimation.getKeyFrame(stateTime, true);
            case DEATH_HIT -> deathHitAnimation.getKeyFrame(stateTime, false);
            case JUMP -> jumpAnimation.getKeyFrame(stateTime, false);
            case JUMP_ANTIC -> jumpAnticipateAnimation.getKeyFrame(stateTime, false);
            case JUMP_ATTACK -> jumpAttackAnimation.getKeyFrame(stateTime, false);
            case JUMP_ATTACK_RECOVER -> jumpAttackRecoverAnimation.getKeyFrame(stateTime, false);
            case LAND -> landAnimation.getKeyFrame(stateTime, false);
            case RUN_ANTIC -> runAnticAnimation.getKeyFrame(stateTime, false);
            case RUN -> runAnimation.getKeyFrame(stateTime, true);
            case STUN_RECOVER -> stunRecoverAnimation.getKeyFrame(stateTime, false);
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

    @Override
    public Enemy getModel() {
        return model;
    }

    @Override
    public void updateRate(float rate) {
        attackRecoverAnimation.setFrameDuration(attackRecoverAnimation.getFrameDuration()*rate);
        attackAnimation.setFrameDuration(attackAnimation.getFrameDuration()*rate);
        attackAnticipateAnimation.setFrameDuration(attackAnticipateAnimation.getFrameDuration()*rate);
    }

    public void dispose() {
        if (idleSheet != null) idleSheet.dispose();
        if (attackSheet != null) attackSheet.dispose();
        if (attackAnticipateSheet != null) attackAnticipateSheet.dispose();
        if (turnSheet != null) turnSheet.dispose();;
        if (deathAirSheet != null) deathAirSheet.dispose();
        if (deathLandSheet != null) deathLandSheet.dispose();

    }
}
