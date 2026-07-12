package com.hollowknight.view.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hollowknight.model.MovingWall;

public class MovingWallView {
    private TextureRegion picture;
    private MovingWall model;

    public MovingWallView(MovingWall model) {
        this.model = model;
        picture = new TextureRegion(new Texture("game/movingWall.png"));
    }

    public void draw(SpriteBatch batch){
        batch.draw(picture, model.getBounds().x, model.getBounds().y, model.getBounds().width, model.getBounds().height);
    }
}
