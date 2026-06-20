package com.hollowknight;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hollowknight.model.App;
import com.hollowknight.model.GameSave;
import com.hollowknight.view.GameAssetManager;
import com.hollowknight.view.InputManager;
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
        InputManager.resetToDefaults();
        UiManager.init(this);
        App.getSaveSlots()[2] = new GameSave(5, 10, 22, 5, 8);
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
