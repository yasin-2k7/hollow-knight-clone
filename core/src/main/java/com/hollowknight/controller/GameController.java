package com.hollowknight.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Timer;
import com.hollowknight.model.*;
import com.hollowknight.model.enemies.*;
import com.hollowknight.model.enums.*;
import com.hollowknight.view.AudioManager;
import com.hollowknight.view.GameAssetManager;
import com.hollowknight.view.MainMenuScreen;
import com.hollowknight.view.SettingTable;
import com.hollowknight.view.game.*;
import com.hollowknight.view.game.enemiesView.*;

import java.util.ArrayList;

public class GameController {
    private static float unitScale = App.getUnitScale();
    private static GameScreen screen;
    private static ArrayList<EnemyView> enemyViews = new ArrayList<>();
    private static GameState gameState = GameState.RUNNING;
    private static int currentSaveIndex = -1;
    private static Texts zoteCurrentDialogue;
    private static int zoteDialogueNum = 0;
    private static Enemy boss;
    private static Texts[] zoteMainDialogues = {Texts.ZOTE_BOAST_1, Texts.ZOTE_MEDITATION, Texts.ZOTE_BOSS_WARNING };
    private static boolean isTransitioning = false;
    private static ArrayList<BossArenaController> bossArenaControllers = new ArrayList<>();
    private static ArrayList<MovingWallView> movingWallViews = new ArrayList<>();

    public static void addEnemyView(EnemyView enemyView){
        enemyViews.add(enemyView);
    }
    public static void addZoteView(ZoteView zoteView) {screen.setZoteView(zoteView);}

    public static void updateGame(float delta){
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            togglePause();
        }
        if (Gdx.input.isKeyJustPressed(App.bindings.get(GameAction.INVENTORY))){
            toggleInventory();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) && Gdx.input.isKeyJustPressed(Input.Keys.B)){
            if (isTransitioning) return;
            isTransitioning = true;

            boss = null;
            bossArenaControllers.clear();
            AudioManager.fadeOutCurrentMusic();
            screen.getLaserViews().clear();
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    fadeScreen(() -> {
                        TiledMap newMap = new TmxMapLoader().load("map/map3.tmx");
                        App.getCurrentGame().loadMap("map/map3.tmx", newMap, 75, 142);
                        App.getCurrentGame().setNewMapStartPoint(75, 142);
                        App.getCurrentGame().getKnight().getBounds().setPosition(75, 142);

                        GameMap currentMap = App.getCurrentGame().getCurrentMap();

                        if (currentMap.getZote() != null) {
                            addZoteView(new ZoteView(currentMap.getZote()));
                            currentMap.getZote().setAudioListener(GameController::handleAudioEvent);
                        } else {
                            screen.setZoteView(null);
                        }
                        clearAllEnemyViews();
                        for (Enemy enemy : currentMap.getAllEnemies()) {
                            enemy.setAudioListener(action -> handleAudioEvent(action));

                            makeEnemyView(enemy, enemy.getType().name().toLowerCase());
                        }

                        screen.getCameraManager().loadBoundsFromMap(newMap);

                        screen.updateRendererMap(newMap);

                        screen.getCameraManager().snapToPosition(App.getCurrentGame().getKnight().getBounds());

                        if (App.getCurrentGame().getCurrentMap().getMapAddress().equals("map/map2.tmx")){
                            AudioManager.fadeInMusic(AudioManager.greenPathMainMusic);
                        }
                        else {
                            AudioManager.fadeInMusic(AudioManager.crossroadsMainMusic);
                        }

                        isTransitioning = false;
                    }, 0.7f);
                }
            }, 0.3f);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) && Gdx.input.isKeyJustPressed(Input.Keys.E)){
            if (App.getCurrentGame().getKnight().getMasks() < 5){
                App.getCurrentGame().getKnight().increaseMask();
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) && Gdx.input.isKeyJustPressed(Input.Keys.R)){
            App.getCurrentGame().getKnight().setSoul(99);
            AudioManager.playKnightGainSoul();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) && Gdx.input.isKeyJustPressed(Input.Keys.G)){
            App.getCurrentGame().getKnight().toggleNoDamage();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) && Gdx.input.isKeyJustPressed(Input.Keys.K)){
            for (Enemy enemy : App.getCurrentGame().getCurrentMap().getAllEnemies()){
                if (enemy.getPosition().dst(App.getCurrentGame().getKnight().getPosition()) < 120){
                    enemy.takeDamage(100, App.getCurrentGame().getKnight().getPosition().x, 50f);
                }
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) && Gdx.input.isKeyJustPressed(Input.Keys.S)){
            App.getCurrentGame().getKnight().setSpectatorMode(!App.getCurrentGame().getKnight().isSpectatorMode());
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

        for (BossArenaController bossArenaController : bossArenaControllers){
            bossArenaController.update(cappedDelta, boss);
        }

        if (App.getCurrentGame().getActiveSpellEffect() != null) {
            if (App.getCurrentGame().getActiveSpellEffect().isFinished()) {
                App.getCurrentGame().setActiveSpellEffect(null);
                screen.getSpellEffectView().dispose();
                screen.setSpellEffectView(null);
            }
        }

        if (App.getCurrentGame().getActiveAttackWave() != null) {
            if (App.getCurrentGame().getActiveAttackWave().isDead()) {
                App.getCurrentGame().setActiveAttackWave(null);
                screen.getAttackWaveView().dispose();
                screen.setAttackWaveView(null);
            }
        }

        App.getCurrentGame().getCurrentMap().getLasers().removeIf(Laser::isFinished);
    }



    public static void setSpellEffect(boolean isUpgraded, SpellType type, float x, float y, float width, float height, int damage, boolean isFlipped){
        String address;
        int wFrame;
        int hFrame;
        int totalFrame;
        float frameDuration;
        if (type.equals(SpellType.VENGEFUL_SPIRIT)){
            address = isUpgraded ? "effects/ShadowBall.png" : "effects/SoulBall.png";
            wFrame = isUpgraded ? 504 : 317;
            hFrame = isUpgraded ? 157 : 143;
            totalFrame = isUpgraded ? 6 : 4;
            frameDuration = 0.15f;
        }
        else{
            address = isUpgraded ? "effects/ShadowScream.png" : "effects/SoulScream.png";
            wFrame = isUpgraded ? 357 : 332;
            hFrame = isUpgraded ? 292 : 306;
            totalFrame = 13;
            frameDuration = 0.15f;
        }
        SpellEffect spellEffect = new SpellEffect(type, x, y, width, height, damage, isFlipped);
        App.getCurrentGame().setActiveSpellEffect(spellEffect);
        screen.setSpellEffectView(new SpellEffectView(wFrame, hFrame, address, totalFrame, frameDuration, isFlipped, spellEffect));
    }

    public static void setAttackWave(float x, float y, float direction){
        AttackWave attackWave = new AttackWave(150, 70, direction, x, y);
        App.getCurrentGame().setActiveAttackWave(attackWave);
        screen.setAttackWaveView(new AttackWaveView(attackWave));
    }

    public static void setSlashEffect(Knight knight, float attackTime){
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
                for (Rectangle ground : App.getCurrentGame().getCurrentMap().getGrounds()) {
                    Rectangle fakeBounds = new Rectangle(x, y, width, height);
                    if (Intersector.overlaps(ground, fakeBounds)) {
                        float allowedHeight = ground.y - y;
                        if (allowedHeight < height && allowedHeight > 0) {
                            height = allowedHeight;
                        }
                    }
                }
                SlashEffect slashEffect = new SlashEffect(SlashDirection.UP, x, y, width, height, knight.getSlashDamage(), attackTime);
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
                for (Rectangle ground : App.getCurrentGame().getCurrentMap().getGrounds()) {
                    Rectangle fakeBounds = new Rectangle(x, y, width, height);
                    if (Intersector.overlaps(ground, fakeBounds)) {
                        float groundTopEdge = ground.y + ground.height;
                        if (groundTopEdge > y && groundTopEdge < knight.getPosition().y) {
                            y = groundTopEdge;
                            height = knight.getPosition().y - y;
                        }
                    }
                }

                SlashEffect slashEffect = new SlashEffect(SlashDirection.DOWN, x, y, width, height, knight.getSlashDamage(), attackTime);
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
                    for (Rectangle ground : App.getCurrentGame().getCurrentMap().getGrounds()) {
                        Rectangle fakeBounds = new Rectangle(x, y, width, height);
                        if (Intersector.overlaps(ground, fakeBounds)) {
                            float allowedWidth = ground.x - x;
                            if (allowedWidth < width && allowedWidth > 0) width = allowedWidth;
                        }
                    }
                } else {
                    x = knight.getPosition().x + knight.getBounds().width - maxWidth;
                    width = maxWidth;
                    for (Rectangle ground : App.getCurrentGame().getCurrentMap().getGrounds()) {
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
                SlashEffect slashEffect = new SlashEffect(SlashDirection.NORMAL, x, y, width, height, knight.getSlashDamage(), attackTime);
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
                    for (Rectangle ground : App.getCurrentGame().getCurrentMap().getGrounds()) {
                        Rectangle fakeBounds = new Rectangle(x, y, width, height);
                        if (Intersector.overlaps(ground, fakeBounds)) {
                            float allowedWidth = ground.x - x;
                            if (allowedWidth < width && allowedWidth > 0) width = allowedWidth;
                        }
                    }
                } else {
                    x = knight.getPosition().x + knight.getBounds().width - maxWidth;
                    width = maxWidth;
                    for (Rectangle ground : App.getCurrentGame().getCurrentMap().getGrounds()) {
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
                SlashEffect slashEffect = new SlashEffect(SlashDirection.ALT, x, y, width, height, knight.getSlashDamage(), attackTime);
                slashEffect.setOriginalSize(maxWidth, height);
                App.getCurrentGame().setActiveSlashEffect(slashEffect);
                screen.setSlashEffectView(new SlashEffectView(hFrame, wFrame, address, knight.isFlipped(), slashEffect));
            }
        }
    }

    public static void init(GameSave gameSave,TiledMap tiledMap){
        Game game = new Game(gameSave.getMapStartX()*unitScale, gameSave.getMapStartY()*unitScale, gameSave.getStartX()*unitScale, gameSave.getStartY()*unitScale, gameSave.getPlayTime(), gameSave.getMasks(), gameSave.getSoul(), gameSave.getDeathNumber(), gameSave.getEnemyDeathNumber(), gameSave.getSaveIndex());
        App.setCurrentGame(game);
        GameMap map = new GameMap(game, gameSave.getTiledMapAddress(), tiledMap, 1/6f);
        game.setCurrentMap(map);
        game.addEventListener(new EventListener() {
            @Override
            public void onAchievementUnlocked(Achievement achievement) {
                achievementNotif(achievement);
            }
        });

        game.addEventListener(new EventListener() {
            @Override
            public void onAchievementUnlocked(Achievement achievement) {
                App.updateAchievements(achievement);
            }
        });


        screen = new GameScreen();
        screen.setTiledMap(tiledMap);
        screen.setGame(game);

        KnightView knightView = new KnightView();
        screen.setKnightView(knightView);
        if (game.getCurrentMap().getZote() != null){
            addZoteView(new ZoteView(game.getCurrentMap().getZote()));
        }

        game.setAudioListener(new EntityAudioListener() {
            @Override
            public void onAudioEvent(AudioAction action) {
                handleAudioEvent(action);
            }
        });

        game.getKnight().setAudioListener(new EntityAudioListener() {
            @Override
            public void onAudioEvent(AudioAction action) {
                handleAudioEvent(action);
            }
        });

        if (game.getCurrentMap().getZote() != null){
            game.getCurrentMap().getZote().setAudioListener(new EntityAudioListener() {
                @Override
                public void onAudioEvent(AudioAction action) {
                    handleAudioEvent(action);
                }
            });
        }

        for (Enemy enemy : game.getCurrentMap().getAllEnemies()){
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
        App.getCurrentGame().getCurrentMap().getLasers().add(laser);
        LaserView laserView = new LaserView(laser.getBounds());
        screen.getLaserViews().add(laserView);
    }

    public static void switchMap(SwitchPoint switchPoint){
        if (isTransitioning) return;
        isTransitioning = true;

        boss = null;
        bossArenaControllers.clear();
        AudioManager.fadeOutCurrentMusic();
        screen.getLaserViews().clear();
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                fadeScreen(() -> {
                    TiledMap newMap = new TmxMapLoader().load(switchPoint.getAddress());
                    App.getCurrentGame().loadMap(switchPoint.getAddress(), newMap, switchPoint.getX(), switchPoint.getY());
                    App.getCurrentGame().setNewMapStartPoint(switchPoint.getX(), switchPoint.getY());
                    App.getCurrentGame().getKnight().getBounds().setPosition(switchPoint.getX(), switchPoint.getY());

                    GameMap currentMap = App.getCurrentGame().getCurrentMap();

                    if (currentMap.getZote() != null) {
                        addZoteView(new ZoteView(currentMap.getZote()));
                        currentMap.getZote().setAudioListener(GameController::handleAudioEvent);
                    } else {
                        screen.setZoteView(null);
                    }
                    clearAllEnemyViews();
                    for (Enemy enemy : currentMap.getAllEnemies()) {
                        enemy.setAudioListener(action -> handleAudioEvent(action));

                        makeEnemyView(enemy, enemy.getType().name().toLowerCase());
                    }

                    screen.getCameraManager().loadBoundsFromMap(newMap);

                    screen.updateRendererMap(newMap);

                    screen.getCameraManager().snapToPosition(App.getCurrentGame().getKnight().getBounds());

                    if (App.getCurrentGame().getCurrentMap().getMapAddress().equals("map/map2.tmx")){
                        AudioManager.fadeInMusic(AudioManager.greenPathMainMusic);
                    }
                    else {
                        AudioManager.fadeInMusic(AudioManager.crossroadsMainMusic);
                    }

                    isTransitioning = false;
                }, 0.7f);
            }
        }, 0.3f);


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
            case FIREBALL -> AudioManager.playFireball();
            case SCREAM -> AudioManager.playScream();
            case STOP_FIREBALL -> AudioManager.stopFireball();
            case ZOTE_SPEAK -> AudioManager.playZoteSound();
            case WALL_MOVE -> AudioManager.playWallMove();
            case WALL_MOVE_IMPACT -> AudioManager.playWallMoveImpact();
            default -> {
                return;
            }
        }
    }

    public static void togglePause(){
        if (gameState == GameState.RUNNING){
            gameState = GameState.PAUSED;
            screen.showPauseMenu();
        }
        else {
            gameState = GameState.RUNNING;
            screen.backToGame();
        }
    }

    public static void toggleInventory(){
        if (gameState == GameState.RUNNING){
            gameState = GameState.PAUSED;
            screen.showInventoryMenu();
        }
        else {
            gameState = GameState.RUNNING;
            screen.backToGame();
        }
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
        GameSave currentGame = new GameSave(knight.getPosition().x / unitScale, knight.getPosition().y / unitScale, knight.getSoul(), knight.getMasks(), App.getCurrentGame().getPlayTime(), App.getCurrentGame().getCurrentMap().getMapAddress(), App.getCurrentGame().getMapStartX() / unitScale, App.getCurrentGame().getMapStartY() / unitScale, App.getCurrentGame().getCurrentMap().getLoadBgAddress(), App.getCurrentGame().getSaveIndex(), App.getCurrentGame().getDeathNumber(), App.getCurrentGame().getEnemyDeathNumber());
        Manager.saveGame(currentSaveIndex, currentGame);
        Manager.saveConfig();
        currentSaveIndex = -1;
        screen.fadeAndSwitchScreen(new MainMenuScreen());
    }

    public static void fadeScreen (Runnable runnable, float blackTime){
        screen.triggerScreenFade(runnable, blackTime);
    }

    public static void snapCamera(){
        screen.getCameraManager().snapToPosition(App.getCurrentGame().getKnight().getBounds());
    }

    public static void zoteNextDialogue(){
        if (zoteCurrentDialogue == null){
            if (zoteDialogueNum == 3){
                zoteCurrentDialogue = Texts.ZOTE_DISMISS;
                screen.showZoteDialogue(zoteCurrentDialogue.get(App.getCurrentLanguage()));
                App.getCurrentGame().getCurrentMap().getZote().speak();
            }
            else {
                zoteCurrentDialogue = zoteMainDialogues[zoteDialogueNum++];
                screen.showZoteDialogue(zoteCurrentDialogue.get(App.getCurrentLanguage()));
                App.getCurrentGame().getCurrentMap().getZote().speak();
            }
        }
        else{
            if (zoteDialogueNum == 3){
                App.getCurrentGame().getKnight().setTalking(false);
                screen.endTalking();
                if (zoteCurrentDialogue == Texts.ZOTE_BOSS_WARNING) App.getCurrentGame().zoteTalked();
                zoteCurrentDialogue = null;
            }
            else{
                zoteCurrentDialogue = zoteMainDialogues[zoteDialogueNum++];
                screen.showZoteDialogue(zoteCurrentDialogue.get(App.getCurrentLanguage()));
                App.getCurrentGame().getCurrentMap().getZote().speak();
            }
        }
    }

    public static ArrayList<EnemyView> getEnemyViews() {
        return enemyViews;
    }

    public static SlashEffectView getActiveSlashView(){
        return screen.getSlashEffectView();
    }

    public static ArrayList<Enemy> getActiveEnemies(){
        return App.getCurrentGame().getCurrentMap().getAllEnemies();
    }

    public static void updateMasks(){
        screen.getMasksTable().updateHealth(App.getCurrentGame().getKnight().getMasks());
    }

    public static void showInteractKey(boolean show){
        screen.getZoteView().toggleInteractionPrompt(show);
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


    public static void achievementNotif(Achievement achievement) {
        screen.addToast(achievement);
    }

    public static void clearAllEnemyViews() {
        enemyViews.clear();
    }

    public static void makeEnemyView(Enemy enemy, String enemyType) {
        switch (enemyType) {
            case "crawler":
                GameController.addEnemyView(new CrawlerView((SimpleEnemy) enemy));
                break;
            case "mosscreep":
                GameController.addEnemyView(new MosscreepView((SimpleEnemy) enemy));
                break;
            case "husk":
                GameController.addEnemyView(new HuskHornHeadView((HuskHornHeadEnemy) enemy));
                break;
            case "crystallized":
                GameController.addEnemyView(new CrystallizedView((CrystallizedEnemy) enemy));
                break;
            case "mosquito":
                GameController.addEnemyView(new MosquitoView((FlyerEnemy) enemy));
                break;
            case "falseknight":
                GameController.addEnemyView(new FalseKnightView((FalseKnight) enemy));
                break;
        }
    }

    public static void addBossArena(BossArenaController bossArenaController){
        bossArenaControllers.add(bossArenaController);
        bossArenaController.getFirstWall().setAudioListener(new EntityAudioListener() {
            @Override
            public void onAudioEvent(AudioAction action) {
                handleAudioEvent(action);
            }
        });
        bossArenaController.getSecondWall().setAudioListener(new EntityAudioListener() {
            @Override
            public void onAudioEvent(AudioAction action) {
                handleAudioEvent(action);
            }
        });
        movingWallViews.add(new MovingWallView(bossArenaController.getFirstWall()));
        movingWallViews.add(new MovingWallView(bossArenaController.getSecondWall()));
    }

    public static ArrayList<BossArenaController> getBossArenaControllers() {
        return bossArenaControllers;
    }

    public static ArrayList<MovingWallView> getMovingWallViews() {
        return movingWallViews;
    }

    public static void finishGame() {
        AudioManager.fadeInMusic(AudioManager.victory);
        App.getCurrentGame().informListeners(Achievement.DEFEAT_FALSE_KNIGHT);
        App.getCurrentGame().informListeners(Achievement.COMPLETION);
        if (App.getCurrentGame().getPlayTime() < 900f){
            App.getCurrentGame().informListeners(Achievement.SPEEDRUN);
        }

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                gameState = GameState.PAUSED;
                screen.showFinishMenu();
            }
        }, 10f);


    }

    public static void upgradeBoss(Enemy boss, float rate){
        for (EnemyView enemyView : enemyViews){
            if (enemyView.getModel() == boss){
                enemyView.updateRate(rate);
            }
        }
    }

    public static Enemy getBoss() {
        return boss;
    }

    public static void setBoss(Enemy boss) {
        GameController.boss = boss;
    }

    public static void resetBossArenas() {
        for (BossArenaController bossArenaController : bossArenaControllers){
            bossArenaController.reset();
        }
    }
}
