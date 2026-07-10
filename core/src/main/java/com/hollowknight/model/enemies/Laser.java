package com.hollowknight.model.enemies;

import com.badlogic.gdx.math.Rectangle;
import com.hollowknight.model.Game;

public class Laser {
    private Rectangle bounds;
    private float originX, originY;
    private final float DEFAULT_LENGTH = 300f;
    private final float HEIGHT = 2.5f;
    private float minLength = DEFAULT_LENGTH;
    private float lifeTime = 0.6f;
    private boolean finished = false;
    private boolean flipped;

    public Laser(float originX, float originY, Game game, boolean flipped) {
        this.originX = originX;
        this.originY = originY;
        bounds = new Rectangle();
        this.flipped = flipped;
        makeBounds(originX, originY, game, flipped);
    }

    private void makeBounds(float originX, float originY, Game game, boolean flipped) {
        minLength = DEFAULT_LENGTH;

        Rectangle laserBeam = new Rectangle();

        for (Rectangle ground : game.getCurrentMap().getGrounds()) {
            if (flipped) {
                laserBeam.set(originX, originY, DEFAULT_LENGTH, HEIGHT);

                if (laserBeam.overlaps(ground)) {
                    if (ground.x >= originX) {
                        float dist = ground.x - originX;
                        if (dist < minLength) {
                            minLength = dist;
                        }
                    }
                }
            } else {
                laserBeam.set(originX - DEFAULT_LENGTH, originY, DEFAULT_LENGTH, HEIGHT);

                if (laserBeam.overlaps(ground)) {
                    float groundRightEdge = ground.x + ground.width;
                    if (groundRightEdge <= originX) {
                        float dist = originX - groundRightEdge;
                        if (dist < minLength) {
                            minLength = dist;
                        }
                    }
                }
            }
        }

        if (flipped) {
            bounds.set(originX, originY, minLength, HEIGHT);
        } else {
            bounds.set(originX - minLength, originY, minLength, HEIGHT);
        }
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void update(float delta){
        lifeTime -= delta;
        if (lifeTime <= 0){
            finished = true;
        }
    }

    public boolean isFlipped() {
        return flipped;
    }

    public boolean isFinished() {
        return finished;
    }
}
