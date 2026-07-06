package com.hollowknight.view.game.hud;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.hollowknight.model.enums.MaskState;

public class Mask extends Actor {
    private MaskState state = MaskState.FULL;
    private float stateTime;

    private Texture fullSheet;
    private Texture emptyTexture;
    private TextureRegion emptySheet;
    private Texture breakingSheet;
    private Texture fillingSheet;

    private Animation<TextureRegion> fullAnimation;
    private Animation<TextureRegion> breakingAnimation;
    private Animation<TextureRegion> fillingAnimation;

    public Mask(MaskState state) {
        this.state = state;
        int frameW = 126;
        int frameH = 167;

        fullSheet = new Texture("hud/FilledHealthShine.png");
        TextureRegion[][] frames = TextureRegion.split(fullSheet, frameW, frameH);
        TextureRegion[] fullFrames = new TextureRegion[5];
        System.arraycopy(frames[0], 0, fullFrames, 0, 5);
        fullAnimation = new Animation<>(0.1f, fullFrames);

        breakingSheet = new Texture("hud/BreakHealth.png");
        frames = TextureRegion.split(breakingSheet, frameW, frameH);
        TextureRegion[] breakFrames = new TextureRegion[6];
        System.arraycopy(frames[0], 0, breakFrames, 0, 6);
        breakingAnimation = new Animation<>(0.1f, breakFrames);

        fillingSheet = new Texture("hud/HealthRefill.png");
        frames = TextureRegion.split(fillingSheet, frameW, frameH);
        TextureRegion[] fillingFrames = new TextureRegion[5];
        System.arraycopy(frames[0], 0, fillingFrames, 0, 5);
        fillingAnimation = new Animation<>(0.1f, fillingFrames);

        emptyTexture = new Texture("hud/EmptyHealth.png");
        emptySheet = new TextureRegion(emptyTexture);

        fullAnimation.setPlayMode(Animation.PlayMode.LOOP);
    }

    public void breaking() {
        if (state != MaskState.BREAKING && state != MaskState.EMPTY) {
            state = MaskState.BREAKING;
            stateTime = 0f;
        }
    }

    public void fill() {
        if (state != MaskState.FILLING && state != MaskState.FULL) {
            state = MaskState.FILLING;
            stateTime = 0f;
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        stateTime += delta;

        if (state == MaskState.BREAKING && breakingAnimation.isAnimationFinished(stateTime)){
            state = MaskState.EMPTY;
        }
        else if (state == MaskState.FILLING && fillingAnimation.isAnimationFinished(stateTime)){
            stateTime = 0;
            state = MaskState.FULL;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * parentAlpha);
        TextureRegion currentFrame;

        switch (state){
            case FULL -> currentFrame = fullAnimation.getKeyFrame(stateTime);
            case FILLING -> currentFrame = fillingAnimation.getKeyFrame(stateTime);
            case BREAKING -> currentFrame = breakingAnimation.getKeyFrame(stateTime);
            default -> currentFrame = emptySheet;
        }

        batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
    }
}
