package com.hollowknight.view.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hollowknight.controller.GameController;
import com.hollowknight.model.App;
import com.hollowknight.model.SlashEffect;
import com.hollowknight.model.enemies.Enemy;
import com.hollowknight.model.enums.KnightState;
import com.hollowknight.model.enums.SlashDirection;

public class SlashEffectView {
    private Texture slashSheet;
    int frameW, frameH;

    private SlashEffect model;

    private Animation<TextureRegion> slashAnimation;

    private float stateTime = 0;
    private boolean flipped;


    public SlashEffectView(int frameH, int frameW, String address, boolean flipped, SlashEffect model) {
        this.model = model;
        this.flipped = flipped;
        this.frameW = frameW;
        this.frameH = frameH;

        slashSheet = new Texture(address);
        TextureRegion[][] frames = TextureRegion.split(slashSheet, frameW, frameH);
        TextureRegion[] slashFrames = new TextureRegion[6];
        System.arraycopy(frames[0], 0, slashFrames, 0, 6);
        slashAnimation = new Animation<>(0.04f, slashFrames);
    }

    public void draw(SpriteBatch batch, float delta){
        stateTime += delta;

        float amountCutX = model.getOriginalWidth() - model.getWidth();
        float amountCutY = model.getOriginalHeight() - model.getHeight();

        int pixelsCutX = (int) (frameW * (amountCutX / model.getOriginalWidth()));
        int pixelsCutY = (int) (frameH * (amountCutY / model.getOriginalHeight()));

        int index = slashAnimation.getKeyFrameIndex(stateTime);

        int srcX = index * frameW + pixelsCutX;
        int srcWidth = frameW - pixelsCutX;

        int srcY = 0;
        int srcHeight = frameH;
        if (model.getType() == SlashDirection.UP) {
            srcY = pixelsCutY;
            srcHeight = frameH - pixelsCutY;
        }


        batch.draw(slashSheet, model.getX(), model.getY(), model.getWidth(), model.getHeight(), srcX, srcY, srcWidth, srcHeight, flipped, false);
    }

    public void dispose(){
        if (slashSheet != null){
            slashSheet.dispose();
        }
    }

    public boolean isFlipped() {
        return flipped;
    }
}
