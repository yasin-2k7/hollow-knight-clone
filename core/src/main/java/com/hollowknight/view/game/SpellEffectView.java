package com.hollowknight.view.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hollowknight.model.SpellEffect;

public class SpellEffectView {
    private Texture spellSheet;
    private Animation<TextureRegion> spellAnimation;
    private SpellEffect model;
    private float stateTime = 0;
    private boolean flipped;

    public SpellEffectView(int frameW, int frameH, String address, int totalFrames, float frameDuration, boolean flipped, SpellEffect model) {
        this.model = model;
        this.flipped = flipped;

        spellSheet = new Texture(address);
        TextureRegion[][] frames = TextureRegion.split(spellSheet, frameW, frameH);

        TextureRegion[] spellFrames = new TextureRegion[totalFrames];
        System.arraycopy(frames[0], 0, spellFrames, 0, totalFrames);

        spellAnimation = new Animation<>(frameDuration, spellFrames);
    }

    public void draw(SpriteBatch batch, float delta) {
        stateTime += delta;

        TextureRegion currentFrame = spellAnimation.getKeyFrame(stateTime, false);

        if (flipped && currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        } else if (!flipped && !currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        }

        batch.draw(currentFrame, model.getX(), model.getY(), model.getWidth(), model.getHeight());
    }

    public void dispose() {
        if (spellSheet != null) {
            spellSheet.dispose();
        }
    }
}
