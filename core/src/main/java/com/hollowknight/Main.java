package com.hollowknight;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
    }
}
