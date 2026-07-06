package com.hollowknight.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.hollowknight.model.*;
import com.hollowknight.model.enemies.Enemy;
import com.hollowknight.model.enemies.Laser;
import com.hollowknight.model.enums.AudioAction;
import com.hollowknight.model.enums.GameState;
import com.hollowknight.model.enums.SlashDirection;
import com.hollowknight.view.AudioManager;
import com.hollowknight.view.GameAssetManager;
import com.hollowknight.view.MainMenuScreen;
import com.hollowknight.view.SettingTable;
import com.hollowknight.view.game.*;
import com.hollowknight.view.game.enemiesView.EnemyView;
import com.hollowknight.view.game.enemiesView.LaserView;

import java.util.ArrayList;

public class GameController {
    private static float unitScale = App.getUnitScale();
    private static GameScreen screen;
    private static ArrayList<EnemyView> enemyViews = new ArrayList<>();
    private static GameState gameState = GameState.RUNNING;
    private static int currentSaveIndex = -1;

    public static void addEnemyView(EnemyView enemyView){
        enemyViews.add(enemyView);
    }

    public static void updateGame(float delta){
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            togglePause();
        }
        if (gameState == GameState.PAUSED) return;
        float cappedDelta = Math.min(delta, 0.016f);

        App.getCurrentGame().update(cappedDelta);
        if (App.getCurrentGame().getActiveSlashEffect() != null) {
            if (App.getCurrentGame().getActiveSlashEffect().isFinished()) {
                App.getCurrentGame().setActiveSlashEffect(null);
                screen.getSlashEffectView().dispose();
                screen.setSlashEffectView(null);
            }
        }

        App.getCurrentGame().getLasers().removeIf(Laser::isFinished);
    }

    public static void setSlashEffect(Knight knight){
        float x,y,width,height;
        int wFrame,hFrame;
        String address;
        switch (knight.getState()){
            case ATTACK_UP -> {
                wFrame = 169;
                hFrame = 192;
                width = 100f * unitScale;
                float maxHeight = 170f * unitScale;
                height = maxHeight;
                x = knight.getPosition().x + unitScale*(knight.getWidth()-2*knight.getwOffset())/2 - width/2;
                y = knight.getPosition().y + unitScale*(knight.getHeight() - knight.gethOffsetUp());
                address = "effects/UpSlashEffect.png";
                for (Rectangle ground : App.getCurrentGame().getGrounds()) {
                    Rectangle fakeBounds = new Rectangle(x, y, width, height);
                    if (Intersector.overlaps(ground, fakeBounds)) {
                        float allowedHeight = ground.y - y;
                        if (allowedHeight < height && allowedHeight > 0) {
                            height = allowedHeight;
                        }
                    }
                }
                SlashEffect slashEffect = new SlashEffect(SlashDirection.UP, x, y, width, height);
                slashEffect.setOriginalSize(width, maxHeight);
                App.getCurrentGame().setActiveSlashEffect(slashEffect);
                screen.setSlashEffectView(new SlashEffectView(hFrame, wFrame, address, knight.isFlipped(), slashEffect));
            }
            case ATTACK_DOWN -> {
                wFrame = 182;
                hFrame = 209;
                width = 100f * unitScale;
                height = 170f * unitScale;
                float maxHeight = height;
                x = knight.getPosition().x + unitScale*(knight.getWidth()-2*knight.getwOffset())/2 - width/2;
                y = knight.getPosition().y - height + unitScale* knight.gethOffsetDown();
                float startY = y;
                address = "effects/DownSlashEffect.png";
                for (Rectangle ground : App.getCurrentGame().getGrounds()) {
                    Rectangle fakeBounds = new Rectangle(x, y, width, height);
                    if (Intersector.overlaps(ground, fakeBounds)) {
                        float groundTopEdge = ground.y + ground.height;
                        if (groundTopEdge > y && groundTopEdge < knight.getPosition().y) {
                            y = groundTopEdge;
                            height = knight.getPosition().y - y;
                        }
                    }
                }

                SlashEffect slashEffect = new SlashEffect(SlashDirection.DOWN, x, y, width, height);
                slashEffect.setOriginalSize(width, maxHeight);
                App.getCurrentGame().setActiveSlashEffect(slashEffect);
                screen.setSlashEffectView(new SlashEffectView(hFrame, wFrame, address, knight.isFlipped(), slashEffect));
           }
            case ATTACK_SLASH -> {
                wFrame = 349;
                hFrame = 186;
                float maxWidth = 170f * unitScale;
                height = 80f * unitScale;
                y = knight.getPosition().y;
                address = "effects/SlashEffect.png";

                if (knight.isFlipped()) {
                    x = knight.getPosition().x;
                    width = maxWidth;
                    for (Rectangle ground : App.getCurrentGame().getGrounds()) {
                        Rectangle fakeBounds = new Rectangle(x, y, width, height);
                        if (Intersector.overlaps(ground, fakeBounds)) {
                            float allowedWidth = ground.x - x;
                            if (allowedWidth < width && allowedWidth > 0) width = allowedWidth;
                        }
                    }
                } else {
                    x = knight.getPosition().x + knight.getBounds().width - maxWidth;
                    width = maxWidth;
                    for (Rectangle ground : App.getCurrentGame().getGrounds()) {
                        Rectangle fakeBounds = new Rectangle(x, y, width, height);
                        if (Intersector.overlaps(ground, fakeBounds)) {
                            float groundRightEdge = ground.x + ground.width;
                            if (groundRightEdge > x && groundRightEdge < knight.getPosition().x) {
                                x = groundRightEdge;
                                width = (knight.getPosition().x) - x;
                            }
                        }
                    }
                }
                SlashEffect slashEffect = new SlashEffect(SlashDirection.NORMAL, x, y, width, height);
                slashEffect.setOriginalSize(maxWidth, height);
                App.getCurrentGame().setActiveSlashEffect(slashEffect);
                screen.setSlashEffectView(new SlashEffectView(hFrame, wFrame, address, knight.isFlipped(), slashEffect));
            }
            case ATTACK_ALT_SLASH -> {
                wFrame = 349;
                hFrame = 186;
                float maxWidth = 170f * unitScale;
                height = 80f * unitScale;
                y = knight.getPosition().y;
                address = "effects/SlashEffectAlt.png";
                if (knight.isFlipped()) {
                    x = knight.getPosition().x;
                    width = maxWidth;
                    for (Rectangle ground : App.getCurrentGame().getGrounds()) {
                        Rectangle fakeBounds = new Rectangle(x, y, width, height);
                        if (Intersector.overlaps(ground, fakeBounds)) {
                            float allowedWidth = ground.x - x;
                            if (allowedWidth < width && allowedWidth > 0) width = allowedWidth;
                        }
                    }
                } else {
                    x = knight.getPosition().x + knight.getBounds().width - maxWidth;
                    width = maxWidth;
                    for (Rectangle ground : App.getCurrentGame().getGrounds()) {
                        Rectangle fakeBounds = new Rectangle(x, y, width, height);
                        if (Intersector.overlaps(ground, fakeBounds)) {
                            float groundRightEdge = ground.x + ground.width;
                            if (groundRightEdge > x && groundRightEdge < knight.getPosition().x) {
                                x = groundRightEdge;
                                width = (knight.getPosition().x) - x;
                            }
                        }
                    }
                }
                SlashEffect slashEffect = new SlashEffect(SlashDirection.ALT, x, y, width, height);
                slashEffect.setOriginalSize(maxWidth, height);
                App.getCurrentGame().setActiveSlashEffect(slashEffect);
                screen.setSlashEffectView(new SlashEffectView(hFrame, wFrame, address, knight.isFlipped(), slashEffect));
            }
        }
    }

    public static void init(String mapAddress, TiledMap tiledMap, float startX, float startY, int masks, int soul, float playTime){
        Game game = new Game(startX, startY, playTime);
        App.setCurrentGame(game);
        game.initialize(mapAddress, tiledMap, masks, soul);

        screen = new GameScreen();
        screen.setTiledMap(tiledMap);
        screen.setGame(game);

        KnightView knightView = new KnightView();
        screen.setKnightView(knightView);

        game.getKnight().setAudioListener(new EntityAudioListener() {
            @Override
            public void onAudioEvent(AudioAction action) {
                handleAudioEvent(action);
            }
        });

        for (Enemy enemy : game.getAllEnemies()){
            enemy.setAudioListener(new EntityAudioListener() {
                @Override
                public void onAudioEvent(AudioAction action) {
                    handleAudioEvent(action);
                }
            });
        }
    }

    public static void createLaser(float originX, float originY, boolean flipped){
        Laser laser = new Laser(originX, originY, App.getCurrentGame(), flipped);
        App.getCurrentGame().getLasers().add(laser);
        LaserView laserView = new LaserView(laser.getBounds());
        screen.getLaserViews().add(laserView);
    }

    private static void handleAudioEvent(AudioAction event) {
        switch (event) {
            case KNIGHT_ATTACK -> AudioManager.playSwordA();
            case KNIGHT_ATTACK_ALT -> AudioManager.playSwordB();
            case KNIGHT_TAKE_DAMAGE -> AudioManager.playHeroDamage();
            case KNIGHT_DASH -> AudioManager.playHeroDash();
            case KNIGHT_DEATH -> AudioManager.playHeroDeath();
            case KNIGHT_JUMP -> AudioManager.playHeroJump();
            case LAND -> AudioManager.playHeroLand();
            case MANTIS_CLAW -> AudioManager.playHeroMantisClaw();
            case WALL_JUMP -> AudioManager.playHeroWallJump();
            case STONE_FOOTSTEP -> AudioManager.playStoneFootstep();
            case WALL_SLIDE -> AudioManager.playWallSlide();
            case WALL_SLIDE_STOP -> AudioManager.stopWallSlide();
            case ENEMY_TAKE_DAMAGE -> AudioManager.playEnemyDamage();
            case KNIGHT_GAIN_SOUL -> AudioManager.playKnightGainSoul();
            case FOCUS_READY -> AudioManager.playFocusReady();
            case FOCUS_HEALTH_CHARGE -> AudioManager.playFocusHealthCharge();
            case FOCUS_HEALTH_HEAL -> AudioManager.playFocusHealthHeal();
            case FOCUS_NOT_FINISHED -> AudioManager.stopFocus();
            default -> {
                return;
            }
        }
    }

    public static void pauseGame(){
        gameState = GameState.PAUSED;
        screen.showPauseMenu();

    }

    public static void togglePause(){
        if (gameState == GameState.RUNNING){
            pauseGame();
        }
        else {
            resumeGame();
        }
    }

    public static void resumeGame(){
        gameState = GameState.RUNNING;
        screen.backToGame();
    }

    public static void settingInPause(){
        screen.switchModalTable(new SettingTable(GameAssetManager.skin, () -> {
            screen.switchModalTable(new PauseTable(GameAssetManager.skin));
        }));
    }

    public static void showCheatCodes(){
        screen.switchModalTable(new CheatCodeTable(GameAssetManager.skin, () -> {
            screen.switchModalTable(new PauseTable(GameAssetManager.skin));
        }));
    }

    public static void saveGame(){
        Knight knight = App.getCurrentGame().getKnight();
        GameSave currentGame = new GameSave(knight.getPosition().x / unitScale, knight.getPosition().y / unitScale, knight.getSoul(), knight.getMasks(), App.getCurrentGame().getPlayTime(), App.getCurrentGame().getMapAddress());
        Manager.saveGame(currentSaveIndex, currentGame);
        currentSaveIndex = -1;
        screen.fadeAndSwitchScreen(new MainMenuScreen());
        AudioManager.fadeOutCurrentMusic();
    }

    public static ArrayList<EnemyView> getEnemyViews() {
        return enemyViews;
    }

    public static SlashEffectView getActiveSlashView(){
        return screen.getSlashEffectView();
    }

    public static ArrayList<Enemy> getActiveEnemies(){
        return App.getCurrentGame().getAllEnemies();
    }

    public static void updateMasks(){
        screen.getMasksTable().updateHealth(App.getCurrentGame().getKnight().getMasks());
    }

    public static GameScreen getScreen() {
        return screen;
    }

    public static void setScreen(GameScreen screen) {
        GameController.screen = screen;
    }

    public static GameState getGameState() {
        return gameState;
    }

    public static void setCurrentSaveIndex(int currentSaveIndex) {
        GameController.currentSaveIndex = currentSaveIndex;
    }

    public static void setGameState(GameState gameState) {
        GameController.gameState = gameState;
    }
}
