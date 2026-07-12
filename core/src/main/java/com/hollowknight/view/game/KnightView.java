package com.hollowknight.view.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hollowknight.controller.GameController;
import com.hollowknight.model.App;
import com.hollowknight.model.Knight;
import com.hollowknight.model.enums.KnightState;

public class KnightView {
    private Texture idleSheet;
    private Texture runSheet;
    private Texture jumpSheet;
    private Texture landSheet;
    private Texture onWallSheet;
    private Texture wallJumpSheet;
    private Texture doubleJumpSheet;
    private Texture dashSheet;
    private Texture slashUpSheet;
    private Texture slashDownSheet;
    private Texture slashSheet;
    private Texture slashAltSheet;
    private Texture focusSheet;
    private Texture focusStartSheet;
    private Texture focusGetSheet;
    private Texture focusEndSheet;
    private Texture fireballCastSheet;
    private Texture screamSheet;
    private Texture shadowDashSheet;
    private Texture deathSheet;

    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> runAnimation;
    private Animation<TextureRegion> jumpAnimation;
    private Animation<TextureRegion> landAnimation;
    private Animation<TextureRegion> onWallAnimation;
    private Animation<TextureRegion> wallJumpAnimation;
    private Animation<TextureRegion> doubleJumpAnimation;
    private Animation<TextureRegion> dashAnimation;
    private Animation<TextureRegion> slashUpAnimation;
    private Animation<TextureRegion> slashDownAnimation;
    private Animation<TextureRegion> slashAnimation;
    private Animation<TextureRegion> slashAltAnimation;
    private Animation<TextureRegion> focusAnimation;
    private Animation<TextureRegion> focusGetAnimation;
    private Animation<TextureRegion> focusStartAnimation;
    private Animation<TextureRegion> focusEndAnimation;
    private Animation<TextureRegion> fireballCastAnimation;
    private Animation<TextureRegion> screamAnimation;
    private Animation<TextureRegion> shadowDashAnimation;
    private Animation<TextureRegion> deathAnimation;

    private float stateTime = 0;
    private float flashTime = 0;
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

        onWallSheet = new Texture("knight/Wall Slide.png");
        frames = TextureRegion.split(onWallSheet, frameW, frameH);
        TextureRegion[] onWallFrames = new TextureRegion[4];
        System.arraycopy(frames[0], 0, onWallFrames, 0, 4);
        onWallAnimation = new Animation<>(0.2f, onWallFrames);

        wallJumpSheet = new Texture("knight/Walljump.png");
        frames = TextureRegion.split(wallJumpSheet, frameW, frameH);
        TextureRegion[] wallJumpFrames = new TextureRegion[9];
        System.arraycopy(frames[0], 0, wallJumpFrames, 0, 9);
        wallJumpAnimation = new Animation<>(0.1f, wallJumpFrames);

        doubleJumpSheet = new Texture("knight/Double Jump.png");
        frames = TextureRegion.split(doubleJumpSheet, frameW, frameH);
        TextureRegion[] doubleJumpFrames = new TextureRegion[8];
        System.arraycopy(frames[0], 0, doubleJumpFrames, 0, 8);
        doubleJumpAnimation = new Animation<>(0.2f, doubleJumpFrames);

        dashSheet = new Texture("knight/Dash.png");
        frames = TextureRegion.split(dashSheet, frameW, frameH);
        TextureRegion[] dashFrames = new TextureRegion[12];
        System.arraycopy(frames[0], 0, dashFrames, 0, 12);
        dashAnimation = new Animation<>(0.018f, dashFrames);

        slashUpSheet = new Texture("knight/UpSlash.png");
        frames = TextureRegion.split(slashUpSheet, frameW, frameH);
        TextureRegion[] slashUpFrames = new TextureRegion[5];
        System.arraycopy(frames[0], 0, slashUpFrames, 0, 5);
        slashUpAnimation = new Animation<>(0.1f, slashUpFrames);

        slashDownSheet = new Texture("knight/DownSlash.png");
        frames = TextureRegion.split(slashDownSheet, frameW, frameH);
        TextureRegion[] slashDownFrames = new TextureRegion[5];
        System.arraycopy(frames[0], 0, slashDownFrames, 0, 5);
        slashDownAnimation = new Animation<>(0.1f, slashDownFrames);

        slashSheet = new Texture("knight/Slash.png");
        frames = TextureRegion.split(slashSheet, frameW, frameH);
        TextureRegion[] slashFrames = new TextureRegion[5];
        System.arraycopy(frames[0], 0, slashFrames, 0, 5);
        slashAnimation = new Animation<>(0.1f, slashFrames);

        slashAltSheet = new Texture("knight/SlashAlt.png");
        frames = TextureRegion.split(slashAltSheet, frameW, frameH);
        TextureRegion[] slashAltFrames = new TextureRegion[5];
        System.arraycopy(frames[0], 0, slashAltFrames, 0, 5);
        slashAltAnimation = new Animation<>(0.1f, slashAltFrames);

        focusSheet = new Texture("knight/Focus.png");
        frames = TextureRegion.split(focusSheet, frameW, frameH);
        TextureRegion[] focusFrames = new TextureRegion[4];
        System.arraycopy(frames[0], 0, focusFrames, 0, 4);
        focusAnimation = new Animation<>(0.1f, focusFrames);

        focusGetSheet = new Texture("knight/Focus Get.png");
        frames = TextureRegion.split(focusGetSheet, frameW, frameH);
        TextureRegion[] focusGetFrames = new TextureRegion[6];
        System.arraycopy(frames[0], 0, focusGetFrames, 0, 6);
        focusGetAnimation = new Animation<>(0.1f, focusGetFrames);

        focusStartSheet = new Texture("knight/Focus Start.png");
        frames = TextureRegion.split(focusStartSheet, frameW, frameH);
        TextureRegion[] focusStartFrames = new TextureRegion[3];
        System.arraycopy(frames[0], 0, focusStartFrames, 0, 3);
        focusStartAnimation = new Animation<>(0.07f, focusStartFrames);

        focusEndSheet = new Texture("knight/Focus End.png");
        frames = TextureRegion.split(focusEndSheet, frameW, frameH);
        TextureRegion[] focusEndFrames = new TextureRegion[3];
        System.arraycopy(frames[0], 0, focusEndFrames, 0, 3);
        focusEndAnimation = new Animation<>(0.07f, focusEndFrames);

        fireballCastSheet = new Texture("knight/Fireball Cast.png");
        frames = TextureRegion.split(fireballCastSheet, frameW, frameH);
        TextureRegion[] fireballCastFrames = new TextureRegion[9];
        System.arraycopy(frames[0], 0, fireballCastFrames, 0, 9);
        fireballCastAnimation = new Animation<>(0.1f, fireballCastFrames);

        screamSheet = new Texture("knight/Scream.png");
        frames = TextureRegion.split(screamSheet, frameW, frameH);
        TextureRegion[] screamFrames = new TextureRegion[7];
        System.arraycopy(frames[0], 0, screamFrames, 0, 7);
        screamAnimation = new Animation<>(0.1f, screamFrames);

        shadowDashSheet = new Texture("knight/Shadow Dash.png");
        frames = TextureRegion.split(shadowDashSheet, frameW, frameH);
        TextureRegion[] shadowDashFrames = new TextureRegion[11];
        System.arraycopy(frames[0], 0, shadowDashFrames, 0, 11);
        shadowDashAnimation = new Animation<>(0.02f, shadowDashFrames);

        deathSheet = new Texture("knight/Death.png");
        frames = TextureRegion.split(deathSheet, frameW, frameH);
        TextureRegion[] deathFrames = new TextureRegion[18];
        System.arraycopy(frames[0], 0, deathFrames, 0, 18);
        deathAnimation = new Animation<>(0.1f, deathFrames);

    }

    public void draw(SpriteBatch batch, Knight knight, float delta){
        if (knight.getState() != previousState) {
            stateTime = 0;
            previousState = knight.getState();
        }

        flashTime += delta;

        if (knight.getState() != KnightState.DAMAGED && !knight.isSpectatorMode()){
            stateTime += delta;
        }

        if (knight.isNoDamage()) {
            if ((int)(flashTime * 20) % 2 == 0) {
                batch.setColor(1f, 1f, 1f, 0.2f);
            } else {
                batch.setColor(1f, 1f, 1f, 0.8f);
            }
        }
        else {
            batch.setColor(1f, 1f, 1f, 1f);
        }


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
            case ON_WALL:
                currentFrame = onWallAnimation.getKeyFrame(stateTime, true);
                break;
            case WALL_JUMP:
                currentFrame = wallJumpAnimation.getKeyFrame(stateTime, false);
                break;
            case DOUBLE_JUMP:
                currentFrame = doubleJumpAnimation.getKeyFrame(stateTime, false);
                break;
            case DASH:
                currentFrame = dashAnimation.getKeyFrame(stateTime, false);
                break;
            case ATTACK_SLASH:
                currentFrame = slashAnimation.getKeyFrame(stateTime, false);
                break;
            case ATTACK_ALT_SLASH:
                currentFrame = slashAltAnimation.getKeyFrame(stateTime, false);
                break;
            case ATTACK_UP:
                currentFrame = slashUpAnimation.getKeyFrame(stateTime, false);
                break;
            case ATTACK_DOWN:
                currentFrame = slashDownAnimation.getKeyFrame(stateTime, false);
                break;
            case FOCUS_START:
                currentFrame = focusStartAnimation.getKeyFrame(stateTime, false);
                break;
            case FOCUS:
                currentFrame = focusAnimation.getKeyFrame(stateTime, true);
                break;
            case FOCUS_GET:
                currentFrame = focusGetAnimation.getKeyFrame(stateTime, false);
                break;
            case FOCUS_END:
                currentFrame = focusEndAnimation.getKeyFrame(stateTime, false);
                break;
            case FIREBALL_CAST:
                currentFrame = fireballCastAnimation.getKeyFrame(stateTime, false);
                break;
            case SCREAM:
                currentFrame = screamAnimation.getKeyFrame(stateTime, false);
                break;
            case SHADOW_DASH:
                currentFrame = shadowDashAnimation.getKeyFrame(stateTime, false);
                break;
            case DEATH:
                currentFrame = deathAnimation.getKeyFrame(stateTime, false);
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

        float scale = 1 / 6f;
        float renderX = knight.getPosition().x - (knight.getwOffset() * scale);
        float renderY = knight.getPosition().y - (knight.gethOffsetDown() * scale);

        float renderWidth = knight.getWidth() * scale;
        float renderHeight = knight.getHeight() * scale;

        batch.draw(currentFrame, renderX, renderY, renderWidth, renderHeight);
        batch.setColor(1f, 1f, 1f, 1f);
    }

    public void dispose() {
        Texture[] sheets = {
            idleSheet, runSheet, jumpSheet, landSheet, onWallSheet,
            wallJumpSheet, doubleJumpSheet, dashSheet, slashUpSheet,
            slashDownSheet, slashSheet, slashAltSheet, focusSheet,
            focusStartSheet, focusGetSheet, focusEndSheet, fireballCastSheet, screamSheet, shadowDashSheet
        };
        for (Texture sheet : sheets) {
            if (sheet != null) {
                sheet.dispose();
            }
        }
    }
}
