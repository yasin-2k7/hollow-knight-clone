package com.hollowknight.view.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hollowknight.model.Game;
import com.hollowknight.model.Knight;
import com.hollowknight.view.GameAssetManager;

public class GameScreen implements Screen {
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer renderer;

    Skin skin = GameAssetManager.skin;

    private ShapeRenderer shapeRenderer;

    private OrthographicCamera camera;
    private Viewport viewport;
    private SpriteBatch batch;

    Color topColor = new Color(0.15f, 0.15f, 0.45f, 1f);
    // مشکی برای پایین (عمق تاریکی)
    Color bottomColor = new Color(0.01f, 0.01f, 0.05f, 1f);

    private Texture background;

    private Game game;
    private Knight knight;

    @Override
    public void show() {


        batch = new SpriteBatch();

        tiledMap = new TmxMapLoader().load("map/Map.tmx");
        renderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / 5f);

        camera = new OrthographicCamera();
        viewport = new FitViewport(300f, 150f, camera);

//        background = new Texture("ui/GameBG3.png");

        shapeRenderer = new ShapeRenderer();

        viewport.apply();

        camera.position.set(400, 150, 0);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyPressed(Input.Keys.D)){
            camera.translate(1, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)){
            camera.translate(-1, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)){
            camera.translate(0, 1, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)){
            camera.translate(0, -1, 0);
        }

        camera.update();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(0.05f, 0.05f, 0.15f, 1f);

        shapeRenderer.rect(
            camera.position.x - viewport.getWorldWidth() / 2,
            camera.position.y - viewport.getWorldHeight() / 2,
            viewport.getWorldWidth(),
            viewport.getWorldHeight(),
            bottomColor,
            bottomColor,
            topColor,
            topColor
        );

        shapeRenderer.end();


//        batch.begin();
//        // ترفند: اگر می‌خواهید تصویر به دوربین بچسبد و با حرکت بازیکن جا نماند،
//        // پوزیشن آن را بر اساس مختصات فعلی دوربین تنظیم می‌کنیم:
//        float bgX = camera.position.x - viewport.getWorldWidth() / 2;
//        float bgY = camera.position.y - viewport.getWorldHeight() / 2;
//
//        batch.draw(background, bgX, bgY, viewport.getWorldWidth(), viewport.getWorldHeight());
//        batch.end();

        float padding = 25f;

        float viewX = camera.position.x - (viewport.getWorldWidth() / 2) - padding;
        float viewY = camera.position.y - (viewport.getWorldHeight() / 2) - padding;
        float viewWidth = viewport.getWorldWidth() + (padding * 2);
        float viewHeight = viewport.getWorldHeight() + (padding * 2);

        renderer.setView(camera.combined, viewX, viewY, viewWidth, viewHeight);
        renderer.render();

        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        batch.end();

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
