package com.hollowknight;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hollowknight.controller.GameController;
import com.hollowknight.model.App;
import com.hollowknight.model.Manager;
import com.hollowknight.view.AudioManager;
import com.hollowknight.view.GameAssetManager;
import com.hollowknight.view.MainMenuScreen;
import com.hollowknight.view.UiManager;


/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    private SpriteBatch batch;
    private Texture image;

    @Override
    public void create() {
        batch = new SpriteBatch();

        Manager.loadConfig();

        Manager.loadAllAllSlots();

        GameAssetManager.init();
        UiManager.init(this);
        MainMenuScreen mainMenuScreen = new com.hollowknight.view.MainMenuScreen();
        setScreen(mainMenuScreen);
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    @Override
    public void render() {

        float cappedDelta = Math.min(Gdx.graphics.getDeltaTime(), 0.016f);
        AudioManager.update(cappedDelta);
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
    }
}
