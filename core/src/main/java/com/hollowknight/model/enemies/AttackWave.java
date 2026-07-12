package com.hollowknight.model.enemies;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.hollowknight.model.App;

public class AttackWave {
    private Rectangle bounds;
    private float speed = 0f;
    private float acc = 15f;
    private boolean dead = false;
    private float lifeTime = 1f;
    private float direction;
    private Vector2 position;


    public AttackWave(float width, float height, float direction, float x, float y) {
        this.bounds = new Rectangle(x, y, width* App.getUnitScale(),height*App.getUnitScale());
        this.direction = direction;
        position = new Vector2(x, y);
    }

    public void update(float delta){
        for (Rectangle rectangle : App.getCurrentGame().getCurrentMap().getGrounds()){
            if (rectangle.overlaps(bounds)){
                dead = true;
                return;
            }
        }
        lifeTime -= delta;
        if (lifeTime <= 0){
            dead = true;
            return;
        }
        speed += acc * delta;
        position.x += direction * speed;
        bounds.x = position.x;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public float getDirection() {
        return direction;
    }

    public boolean isDead() {
        return dead;
    }
}
