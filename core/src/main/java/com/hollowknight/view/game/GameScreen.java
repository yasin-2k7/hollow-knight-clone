package com.hollowknight.view.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hollowknight.controller.GameController;
import com.hollowknight.model.App;
import com.hollowknight.model.Game;
import com.hollowknight.model.Knight;
import com.hollowknight.model.enemies.Enemy;
import com.hollowknight.model.enemies.Laser;
import com.hollowknight.model.enums.Achievement;
import com.hollowknight.model.enums.AudioAction;
import com.hollowknight.model.enums.GameState;
import com.hollowknight.model.enums.Texts;
import com.hollowknight.view.AudioManager;
import com.hollowknight.view.GameAssetManager;
import com.hollowknight.view.MenuScreen;
import com.hollowknight.view.game.enemiesView.AttackWaveView;
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

    private final Queue<Achievement> toastQueue = new Queue<>();
    private boolean hasNotification = false;

    private ShapeRenderer shapeRenderer;

    private OrthographicCamera camera;
    private CameraManager cameraManager;
    private Viewport viewport;
    private SpriteBatch batch;

    private ParticleEffect dustEffect1;
    private ParticleEffect dustEffect2;

    private MasksTable masksTable;
    private Table soulsTable;

    private Image fadeOverlay = createDarkOverlay(1f);

    Color topColor = new Color(0.2f, 0.25f, 0.55f, 1f);
    Color bottomColor = new Color(0.01f, 0.01f, 0.05f, 1f);

    private Game game;
    private KnightView knightView;
    private ZoteView zoteView;
    private AttackWaveView attackWaveView;
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
        viewport = new FitViewport(240f, 120f, camera);

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
        if (game.getCurrentMap().getMapAddress().equals("map/map2.tmx")){
            AudioManager.fadeInMusic(AudioManager.greenPathMainMusic);
        }
        else {
            AudioManager.fadeInMusic(AudioManager.crossroadsMainMusic);
        }

        fadeOverlay.getColor().a = 0f;
        rootStack.add(fadeOverlay);

        dustEffect1 = new ParticleEffect();
        dustEffect1.load(Gdx.files.internal("effects/p1.p"), Gdx.files.internal("effects/particles/"));

        dustEffect2 = new ParticleEffect();
        dustEffect2.load(Gdx.files.internal("effects/p2.p"), Gdx.files.internal("effects/particles/"));

        dustEffect1.start();
        dustEffect2.start();

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

        if (game.getCurrentMap().getMapAddress().equals("map/map2.tmx")){
            topColor.set(0.023f, 0.043f, 0.047f, 1f);
            bottomColor.set(0.066f, 0.133f, 0.105f, 1f);
        }
        else{
            topColor.set(0.2f, 0.25f, 0.55f, 1f);
            bottomColor.set(0.01f, 0.01f, 0.05f, 1f);
        }


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
        renderer.render(new int[]{0,1,2,3,4,5,6,7});

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
        if (zoteView != null){
            zoteView.act(gameDelta);
            zoteView.draw(batch, 1f);
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

        if (attackWaveView != null) {
            batch.setProjectionMatrix(camera.combined);
            batch.begin();
            attackWaveView.draw(batch, gameDelta);
            batch.end();
        }

        batch.begin();
        for (MovingWallView movingWallView : GameController.getMovingWallViews()){
            movingWallView.draw(batch);
        }
        batch.end();

        renderer.render(new int[]{8,9});





        renderer.render(new int[]{10,11,12});

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);

        shapeRenderer.rect(game.getKnight().getBounds().x, game.getKnight().getBounds().y, game.getKnight().getBounds().width, game.getKnight().getBounds().height);

        shapeRenderer.setColor(Color.CLEAR_WHITE);
        for (Enemy enemy : GameController.getActiveEnemies()){
            shapeRenderer.rect(enemy.getBounds().x, enemy.getBounds().y, enemy.getBounds().width, enemy.getBounds().height);
            if (enemy.getWeapon() != null){
                Rectangle weapon = enemy.getWeapon();
                shapeRenderer.rect(weapon.x, weapon.y, weapon.width, weapon.height);
            }
        }
        for (Laser laser : game.getCurrentMap().getLasers()){
            shapeRenderer.rect(laser.getBounds().x, laser.getBounds().y, laser.getBounds().width, laser.getBounds().height);
        }

        shapeRenderer.setColor(Color.GREEN);
        for (Rectangle rect : game.getCurrentMap().getGrounds()) {
            shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        }

        shapeRenderer.setColor(Color.YELLOW);
        if (game.getActiveSlashEffect() != null){
            shapeRenderer.rect(game.getActiveSlashEffect().getX(), game.getActiveSlashEffect().getY(), game.getActiveSlashEffect().getWidth(), game.getActiveSlashEffect().getHeight());
        }

        shapeRenderer.end();

        batch.begin();
        batch.setProjectionMatrix(camera.combined);

        if (game.getCurrentMap().getMapAddress().equals("map/map2.tmx")){
            dustEffect1.setPosition(camera.position.x, camera.position.y);
            dustEffect1.update(Gdx.graphics.getDeltaTime());
            dustEffect1.draw(batch);
        }
        else{
            dustEffect2.setPosition(camera.position.x, camera.position.y);
            dustEffect2.update(Gdx.graphics.getDeltaTime());
            dustEffect2.draw(batch);
        }



        batch.end();


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

    public void showFinishMenu() {
        modalStack.clearChildren();
        Image darkOverlay = createDarkOverlay(0.5f);
        modalStack.add(darkOverlay);
        switchModalTable(new FinishTable(skin));
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

    public Table createToastNotification(Achievement achievement) {
        Table toast = new Table();

        toast.setBackground(skin.newDrawable("white", new Color(0f, 0f, 0f, 0.8f)));
        toast.pad(15);

        String imagePath = "achievements/" + achievement.name() + ".png";
        Image icon = new Image(new Texture(Gdx.files.internal(imagePath)));
        toast.add(icon).size(64f, 64f).padRight(15);

        Table textTable = new Table();
        textTable.left();

        String unlockedStr = Texts.ACHIEVEMENT_UNLOCKED.get(App.getCurrentLanguage());
        Label unlockedLabel = new Label(unlockedStr, skin, "small");
        unlockedLabel.setColor(Color.GOLD);

        String titleStr = achievement.title.get(App.getCurrentLanguage());
        Label titleLabel = new Label(titleStr, skin, "default");

        textTable.add(unlockedLabel).left().row();
        textTable.add(titleLabel).left().padTop(4);

        toast.add(textTable).expandX().fillX();

        return toast;
    }

    public void addToast(Achievement achievement){
        toastQueue.addLast(achievement);
        if (!hasNotification){
            showNextToast();
        }
    }

    private void showNextToast() {
        if (toastQueue.isEmpty()) {
            hasNotification = false;
            return;
        }


        hasNotification = true;
        Achievement achievement = toastQueue.removeFirst();

        final Table toastTable = createToastNotification(achievement);
        Table wrapper = new Table();
        wrapper.pad(10).right().top();

        toastTable.setColor(toastTable.getColor().r, toastTable.getColor().g, toastTable.getColor().b, 1f);
        wrapper.add(toastTable);
        toastStack.addActor(wrapper);

        toastTable.addAction(Actions.sequence(
            Actions.moveBy(0, 200f),
            Actions.moveBy(0, -200, 0.5f, Interpolation.bounceIn),
            Actions.delay(3.0f),
            Actions.fadeOut(0.4f),
            Actions.removeActor(),
            Actions.run(new Runnable() {
                @Override
                public void run() {
                    System.out.println("notif");
                    showNextToast();

                }
            })
        ));
    }

    public ZoteView getZoteView() {
        return zoteView;
    }

    public void showZoteDialogue(String text){
        toastStack.clearChildren();
        Table wrapper = new Table();
        Table contentTable = new Table();
        contentTable.setBackground(skin.newDrawable("white", new Color(0f, 0f, 0f, 0.8f)));
        contentTable.setColor(0f, 0f, 0f, 0.8f);


        contentTable.center();
        contentTable.pad(20);

        Label dialogueLabel = new Label(text, skin, "default");
        dialogueLabel.setWrap(true);
        dialogueLabel.setAlignment(com.badlogic.gdx.utils.Align.center);

        contentTable.add(dialogueLabel).growX();

        wrapper.pad(40).top();
        wrapper.add(contentTable).growX().top();
        toastStack.add(wrapper);
    }

    public void endTalking(){
        for (Actor actor : toastStack.getChildren()){
            actor.addAction(Actions.fadeOut(0.2f));
        }
        toastStack.clearChildren();
    }

    public void setZoteView(ZoteView zoteView) {
        this.zoteView = zoteView;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    public void updateRendererMap(TiledMap newMap) {
        this.tiledMap = newMap;
        this.renderer.setMap(newMap);
    }

    public AttackWaveView getAttackWaveView() {
        return attackWaveView;
    }

    public void setAttackWaveView(AttackWaveView attackWaveView) {
        this.attackWaveView = attackWaveView;
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
