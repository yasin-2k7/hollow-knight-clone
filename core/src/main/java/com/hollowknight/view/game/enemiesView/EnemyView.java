package com.hollowknight.view.game.enemiesView;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hollowknight.model.enemies.Enemy;

public interface EnemyView {
    void draw(SpriteBatch batch, float delta);
    Enemy getModel();
    void updateRate(float rate);
}
