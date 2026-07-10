package com.hollowknight.view.game.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.hollowknight.model.Knight;

public class SoulWidget extends Actor {
    private Knight knight;

    private boolean started = true;

    private float orbX = -25f;
    private float orbY = 0f;
    private float orbWidth = 1950f;
    private float orbHeight = 1000f;

    private Animation<TextureRegion> soulAnimation;
    private float stateTime = 0f;

    private TextureRegion maskCircle;
    private TextureRegion vesselFrame;
    private Texture soulEyesTexture;

    private float displayedSoul = 0f;
    private final float MAX_SOUL = 99f;

    private FrameBuffer fbo;
    private TextureRegion fboRegion;

    private Animation<TextureRegion> introAnimation;
    private float introStateTime = 2f;
    private boolean isIntroPlaying = false;


    public SoulWidget(Knight knight, Skin skin) {
        this.knight = knight;
        this.displayedSoul = knight.getSoul();

        soulEyesTexture = new Texture(Gdx.files.internal("hud/SoulOrb_Eye.png"));

        Texture backgroundCircle = new Texture(Gdx.files.internal("hud/SoulOrb_Full.png"));
        this.maskCircle = new TextureRegion(backgroundCircle);

        Texture sheet = new Texture(Gdx.files.internal("hud/Soulorb.png"));
        TextureRegion[][] tmp = TextureRegion.split(sheet, sheet.getWidth() / 10, sheet.getHeight() / 2);
        TextureRegion[] frames = new TextureRegion[5];

        Texture introSheet = new Texture(Gdx.files.internal("hud/HealthBar.png"));
        TextureRegion[][] tmpIntro = TextureRegion.split(introSheet, introSheet.getWidth() / 6, introSheet.getHeight());
        TextureRegion[] introFrames = new TextureRegion[6];
        System.arraycopy(tmpIntro[0], 0, introFrames, 0, 6);

        introAnimation = new Animation<>(0.08f, introFrames);
        introAnimation.setPlayMode(Animation.PlayMode.NORMAL);
        vesselFrame = introFrames[5];

        int index = 0;
        for (int i = 4; i < 9; i++) {
            frames[index++] = tmp[1][i];
        }

        soulAnimation = new Animation<>(0.18f, frames);
        soulAnimation.setPlayMode(Animation.PlayMode.LOOP);

        setSize(360, 200);

        initFBO();
    }

    private void initFBO() {
        if (fbo != null) fbo.dispose();

        fbo = new FrameBuffer(Pixmap.Format.RGBA8888, (int)getWidth(), (int)getHeight(), false);
        fboRegion = new TextureRegion(fbo.getColorBufferTexture());
        fboRegion.flip(false, true);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (isIntroPlaying || started) {
            if (!isIntroPlaying){
                introStateTime -= delta;
                if (introStateTime <= 0){
                    isIntroPlaying = true;
                    introStateTime = 0f;
                    started = false;
                }
            }
            else{
                introStateTime += delta;

                if (introAnimation.isAnimationFinished(introStateTime)) {
                    isIntroPlaying = false;
                    stateTime = 0f;
                }
            }
        } else {
            stateTime += delta;
            displayedSoul = MathUtils.lerp(displayedSoul, knight.getSoul(), delta * 6f);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * parentAlpha);
        if (started || isIntroPlaying) {
            if (!isIntroPlaying){
                return;
            }
            TextureRegion currentIntroFrame = introAnimation.getKeyFrame(introStateTime);
            batch.draw(currentIntroFrame, getX(), getY(), getWidth(), getHeight());
            return;
        }
        TextureRegion currentFluidFrame = soulAnimation.getKeyFrame(stateTime);

        float soulPercentage = displayedSoul / MAX_SOUL;

        float fluidY = -orbHeight + (orbHeight * soulPercentage);

        batch.end();

        fbo.begin();
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.draw(currentFluidFrame, orbX-20f, orbY + fluidY, orbWidth, orbHeight);

        batch.setBlendFunction(GL20.GL_ZERO, GL20.GL_SRC_ALPHA);
        batch.draw(maskCircle, orbX, orbY, orbWidth, orbHeight);



        batch.end();
        fbo.end();

        batch.begin();
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        if (vesselFrame != null) {
            batch.draw(vesselFrame, getX(), getY(), getWidth(), getHeight());
        }
        batch.draw(fboRegion, getX(), getY(), getWidth(), getHeight());

        float eyesX = 50f;
        float eyesY = 30f;
        float eyesWidth = 110f;
        float eyesHeight = 40f;

        batch.draw(soulEyesTexture, getX() + eyesX, getY() + eyesY, eyesWidth, eyesHeight);

    }
}
