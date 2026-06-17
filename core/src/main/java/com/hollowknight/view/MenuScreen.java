package com.hollowknight.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hollowknight.Main;
import com.hollowknight.model.enums.Backgrounds;

public abstract class MenuScreen implements Screen {
    protected Stage stage;
    protected Skin skin;

    protected Table rootTable;
    protected Stack rootStack;
    private Stack modalStack;
    private Stack toastStack;
    private Stack mainStack;

    private Viewport backgroundViewport;
    private Texture backgroundTexture;
    private SpriteBatch backgroundBatch;

    @Override
    public void show() {
        ScreenViewport viewport = new ScreenViewport();
        stage = new Stage(viewport);
        skin = GameAssetManager.skin;

        backgroundViewport = new ScreenViewport();
        backgroundTexture = new Texture(Gdx.files.internal("ui/bg/controller_prompt_bg.png"));
        backgroundBatch = new SpriteBatch();


        mainStack = new Stack();
        modalStack = new Stack();
        toastStack = new Stack();
        rootStack = new Stack();
        rootTable = new Table();


        rootStack.add(rootTable);

        mainStack.setFillParent(true);


        stage.addActor(mainStack);
        mainStack.add(rootStack);
        mainStack.add(modalStack);
        mainStack.add(toastStack);

        stage.getRoot().getColor().a = 0;
        stage.getRoot().addAction(Actions.fadeIn(0.4f));


        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(0, 0, 0, 1);

        backgroundViewport.apply();

        backgroundBatch.setProjectionMatrix(backgroundViewport.getCamera().combined);

        backgroundBatch.begin();

        backgroundBatch.setColor(1, 1, 1, 1f);

        backgroundBatch.enableBlending();
        backgroundBatch.draw(
            backgroundTexture,
            0,
            0,
            backgroundViewport.getWorldWidth(),
            backgroundViewport.getWorldHeight()
        );
        backgroundBatch.end();


        stage.getViewport().apply();
        stage.act(v);
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        backgroundViewport.update(width, height, true);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        if (stage != null) stage.dispose();
        if (backgroundTexture != null) backgroundTexture.dispose();
        if (backgroundBatch != null) backgroundBatch.dispose();
    }

    public Stack getModalStack() {
        return modalStack;
    }

    public void fadeAndSwitchScreen(final Screen targetScreen) {
        Gdx.input.setInputProcessor(null);
        stage.getRoot().addAction(Actions.fadeOut(0.4f));
        UiManager.setScreen(targetScreen);
    }
}
