package com.hollowknight.view.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hollowknight.controller.GameController;
import com.hollowknight.model.App;
import com.hollowknight.model.Game;
import com.hollowknight.model.Knight;
import com.hollowknight.model.enemies.Enemy;
import com.hollowknight.model.enemies.Laser;
import com.hollowknight.model.enums.AudioAction;
import com.hollowknight.model.enums.GameState;
import com.hollowknight.view.AudioManager;
import com.hollowknight.view.GameAssetManager;
import com.hollowknight.view.MenuScreen;
import com.hollowknight.view.game.enemiesView.EnemyView;
import com.hollowknight.view.game.enemiesView.LaserView;
import com.hollowknight.view.game.hud.MasksTable;
import com.hollowknight.view.game.hud.SoulWidget;

import java.util.ArrayList;

public class GameScreen extends MenuScreen {
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer renderer;

    private boolean isStart = true;

    Skin skin = GameAssetManager.skin;

    private PauseTable pauseTable = new PauseTable(skin);
    private CharmsTable charmsTable = new CharmsTable(skin, App.getCurrentGame().getKnight());

    private ShapeRenderer shapeRenderer;

    private OrthographicCamera camera;
    private CameraManager cameraManager;
    private Viewport viewport;
    private SpriteBatch batch;

    private MasksTable masksTable;
    private Table soulsTable;

    private Image fadeOverlay = createDarkOverlay(1f);

    Color topColor = new Color(0.2f, 0.25f, 0.55f, 1f);
    Color bottomColor = new Color(0.01f, 0.01f, 0.05f, 1f);

    private Game game;
    private KnightView knightView;
    private SlashEffectView slashEffectView;
    private SpellEffectView spellEffectView;
    private ArrayList<LaserView> laserViews = new ArrayList<>();


    @Override
    public void show() {
        super.show();

        batch = new SpriteBatch();
        renderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / 6f){
            @Override
            protected void beginRender() {
                if (GameController.getGameState() == GameState.RUNNING) {
                    AnimatedTiledMapTile.updateAnimationBaseTime();
                }
                getBatch().begin();
            }
        };

        camera = new OrthographicCamera();
        viewport = new FitViewport(220f, 110f, camera);

        cameraManager = new CameraManager(camera, App.getUnitScale());
        cameraManager.loadBoundsFromMap(tiledMap);

//        background = new Texture("ui/GameBG3.png");

        shapeRenderer = new ShapeRenderer();

        viewport.apply();

        rootTable.left().top();

        masksTable = new MasksTable(App.getCurrentGame().getKnight(), skin){
            @Override
            public void act(float delta) {
                float hudDelta = GameController.getGameState() == GameState.RUNNING ? delta : 0f;
                super.act(hudDelta);
            }
        };
        soulsTable = new Table(){
            @Override
            public void act(float delta) {
                float hudDelta = GameController.getGameState() == GameState.RUNNING ? delta : 0f;
                super.act(hudDelta);
            }
        };
        soulsTable.setFillParent(true);
        soulsTable.left().top();
        soulsTable.add(new SoulWidget(App.getCurrentGame().getKnight(), skin)).padLeft(10);

        rootStack.add(soulsTable);
        rootStack.add(rootTable);

        rootTable.add(masksTable).padLeft(350).padTop(20);
        AudioManager.fadeInMusic(AudioManager.crossroadsMainMusic);

        fadeOverlay.getColor().a = 0f;
        rootStack.add(fadeOverlay);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        GameController.updateGame(delta);

        float gameDelta = GameController.getGameState() == GameState.RUNNING ? delta : 0f;



        if (isStart){
            isStart = false;
            float centerX = game.getKnight().getPosition().x + 10f;
            float centerY = game.getKnight().getPosition().y + 15f;

            camera.position.set(centerX, centerY, 0);
            camera.update();
        }
        else{
            cameraManager.update(gameDelta, App.getCurrentGame().getKnight().getBounds());
        }

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

        float padding = 25f;

        float viewX = camera.position.x - (viewport.getWorldWidth() / 2) - padding;
        float viewY = camera.position.y - (viewport.getWorldHeight() / 2) - padding;
        float viewWidth = viewport.getWorldWidth() + (padding * 2);
        float viewHeight = viewport.getWorldHeight() + (padding * 2);



        renderer.setView(camera.combined, viewX, viewY, viewWidth, viewHeight);
        renderer.render(new int[]{0,1,2,3,4,5,6});

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (int i = laserViews.size() - 1; i >= 0; i--) {
            LaserView laserView = laserViews.get(i);
            laserView.draw(batch, gameDelta);
            if (laserView.isFinished()) {
                laserView.dispose();
                laserViews.remove(i);
            }
        }
        batch.end();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (EnemyView enemyView : GameController.getEnemyViews()){
            enemyView.draw(batch, gameDelta);
        }
        knightView.draw(batch, game.getKnight(), gameDelta);
        batch.end();

        if (slashEffectView != null) {
            batch.setProjectionMatrix(camera.combined);
            batch.begin();
            slashEffectView.draw(batch, gameDelta);
            batch.end();
        }

        if (spellEffectView != null) {
            batch.setProjectionMatrix(camera.combined);
            batch.begin();
            spellEffectView.draw(batch, gameDelta);
            batch.end();
        }

        renderer.render(new int[]{7,8});





        renderer.render(new int[]{9,10,11});

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);

        shapeRenderer.rect(game.getKnight().getBounds().x, game.getKnight().getBounds().y, game.getKnight().getBounds().width, game.getKnight().getBounds().height);

        shapeRenderer.setColor(Color.CLEAR_WHITE);
        for (Enemy enemy : GameController.getActiveEnemies()){
            shapeRenderer.rect(enemy.getBounds().x, enemy.getBounds().y, enemy.getBounds().width, enemy.getBounds().height);
        }
        for (Laser laser : game.getLasers()){
            shapeRenderer.rect(laser.getBounds().x, laser.getBounds().y, laser.getBounds().width, laser.getBounds().height);
        }

        shapeRenderer.setColor(Color.GREEN);
        for (Rectangle rect : game.getGrounds()) {
            shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        }

        shapeRenderer.setColor(Color.YELLOW);
        if (game.getActiveSlashEffect() != null){
            shapeRenderer.rect(game.getActiveSlashEffect().getX(), game.getActiveSlashEffect().getY(), game.getActiveSlashEffect().getWidth(), game.getActiveSlashEffect().getHeight());
        }

        shapeRenderer.end();


        stage.getViewport().apply();
        stage.act(delta);
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    public void setSlashEffectView(SlashEffectView slashEffectView) {
        this.slashEffectView = slashEffectView;
    }

    public SlashEffectView getSlashEffectView() {
        return slashEffectView;
    }

    public SpellEffectView getSpellEffectView() {
        return spellEffectView;
    }

    public void setSpellEffectView(SpellEffectView spellEffectView) {
        this.spellEffectView = spellEffectView;
    }

    public MasksTable getMasksTable() {
        return masksTable;
    }

    public ArrayList<LaserView> getLaserViews() {
        return laserViews;
    }

    public void setTiledMap(TiledMap tiledMap) {
        this.tiledMap = tiledMap;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setKnightView(KnightView knightView) {
        this.knightView = knightView;
    }

    private Image createDarkOverlay(float alpha) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, alpha);
        pixmap.fill();

        Texture texture = new Texture(pixmap);
        pixmap.dispose();

        return new Image(texture);
    }

    public void switchModalTable(final Table newTable) {
        Actor currentMenu = null;
        if (modalStack.getChildren().size > 1) {
            currentMenu = modalStack.getChildren().get(1);
        }

        if (currentMenu != null) {
            currentMenu.addAction(Actions.sequence(
                Actions.fadeOut(0.15f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        modalStack.removeActor(modalStack.getChildren().get(1));
                        fadeInNewTable(newTable);
                    }
                })
            ));
        } else {
            fadeInNewTable(newTable);
        }
    }

    private void fadeInNewTable(Table newTable) {
        newTable.getColor().a = 0;
        modalStack.add(newTable);
        newTable.addAction(Actions.fadeIn(0.15f));
    }

    public void showPauseMenu() {
        modalStack.clearChildren();
        Image darkOverlay = createDarkOverlay(0.5f);
        modalStack.add(darkOverlay);
        switchModalTable(pauseTable);
        modalStack.setVisible(true);
    }

    public void showInventoryMenu() {
        modalStack.clearChildren();
        Image darkOverlay = createDarkOverlay(0.5f);
        modalStack.add(darkOverlay);
        switchModalTable(charmsTable);
        modalStack.setVisible(true);
    }

    public void backToGame(){
        modalStack.setVisible(false);
    }

    public void triggerScreenFade(final Runnable actionOnBlack, float blackTime) {
        fadeOverlay.clearActions();

        fadeOverlay.addAction(Actions.sequence(
            Actions.fadeIn(0.05f),
            Actions.run(actionOnBlack),
            Actions.delay(blackTime),
            Actions.fadeOut(0.15f)
        ));
    }

    @Override
    public void dispose() {
        if (stage != null) stage.dispose();
        if (tiledMap != null) tiledMap.dispose();
        if (renderer != null) renderer.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();
        if (batch != null) batch.dispose();
        if (laserViews != null) laserViews.clear();
        if (knightView != null) knightView.dispose();
        if (slashEffectView != null) slashEffectView.dispose();
        if (spellEffectView != null) spellEffectView.dispose();
        GameController.getEnemyViews().clear();

    }
}
