package com.hollowknight.view.game.enemiesView;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.hollowknight.model.App;

public class LaserView {
    private float stateTime = 0f;
    private Rectangle bounds;
    private Texture laserSheet;
    private Animation<TextureRegion> laserAnimation;
    private boolean finished = false;

    public LaserView(Rectangle bounds) {
        this.bounds = bounds;
        laserSheet = new Texture("effects/CrystalLaser.png");
        TextureRegion[][] frames = TextureRegion.split(laserSheet, 117, 117);
        TextureRegion[] laserFrames = new TextureRegion[15];
        System.arraycopy(frames[0], 0, laserFrames, 0, 15);
        laserAnimation = new Animation<>(0.04f, laserFrames);
    }

    public void draw(SpriteBatch batch, float delta){
        stateTime += delta;
        TextureRegion currentFrame = laserAnimation.getKeyFrame(stateTime, false);

        float segmentWidth = currentFrame.getRegionWidth() * App.getUnitScale();
        float segmentHeight = currentFrame.getRegionHeight() * App.getUnitScale();

        int numberOfSegments = MathUtils.ceil(bounds.width / segmentWidth);

        for (int i = 0; i < numberOfSegments; i++) {
            float drawX = bounds.x + (i * segmentWidth);
            batch.draw(currentFrame, drawX, bounds.y-10f, segmentWidth, segmentHeight);
        }
        if (laserAnimation.isAnimationFinished(stateTime)){
            finished = true;
        }
    }

    public boolean isFinished() {
        return finished;
    }

    public void dispose(){
        if (laserSheet != null) laserSheet.dispose();
    }
}
