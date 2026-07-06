package com.hollowknight.model;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PointMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.hollowknight.controller.GameController;
import com.hollowknight.model.enemies.*;
import com.hollowknight.model.enums.EnemyState;
import com.hollowknight.model.enums.SlashDirection;
import com.hollowknight.view.game.enemiesView.CrawlerView;
import com.hollowknight.view.game.enemiesView.CrystallizedView;
import com.hollowknight.view.game.enemiesView.HuskHornHeadView;
import com.hollowknight.view.game.enemiesView.MosquitoView;

import java.util.ArrayList;

public class Game {
    private float startX, startY;
    private float playTime;
    private String mapAddress;
    private Knight knight;
    private final float GRAVITY = -250f;
    private ArrayList<Rectangle> grounds;
    private ArrayList<Rectangle> turnPositions = new ArrayList<>();
    private ArrayList<Enemy> allEnemies = new ArrayList<>();
    private SlashEffect activeSlashEffect = null;
    private ArrayList<Laser> lasers = new ArrayList<>();


    public float getGRAVITY() {
        return GRAVITY;
    }

    public Game(float startX, float startY, float playTime) {
        this.startX = startX;
        this.startY = startY;
        grounds = new ArrayList<>();
        this.playTime = playTime;
    }

    public void initialize(String mapAddress, TiledMap tiledMap, int masks, int soul){
        knight = new Knight(masks, soul, startX, startY, this);
        this.mapAddress = mapAddress;
        addGrounds(tiledMap);
        addTurnPositions(tiledMap);
        spawnEnemies(tiledMap, App.getUnitScale());
    }

    public void update(float delta){
        playTime += delta;
        knight.update(delta);
        if (activeSlashEffect != null){
            activeSlashEffect.update(delta);
        }

        for (Laser laser : lasers){
            laser.update(delta);
        }

        if (activeSlashEffect != null) {
            for (Enemy enemy : allEnemies) {
                if (Intersector.overlaps(activeSlashEffect.getHitBounds(), enemy.getBounds())) {
                    if (!activeSlashEffect.hasHitAlready(enemy)){
                        if (!enemy.isDead()) {
                            enemy.takeDamage();
                            activeSlashEffect.registerHit(enemy);
                            knight.increaseSoul(11);
                            if (activeSlashEffect.getType() == SlashDirection.DOWN){
                                knight.pogoJump();
                            }
                        }
                    }
                }
            }
        }

        if (!knight.isNoDamage()){
            for (Enemy enemy : allEnemies) {
                if (Intersector.overlaps(knight.getBounds(), enemy.getBounds()) && !enemy.isDead() && enemy.getState() != EnemyState.DEATH_AIR) {
                    if (enemy.getPosition().x > knight.getPosition().x){
                        knight.takeDamage(-1);
                    }
                    else {
                        knight.takeDamage(1);
                    }
                    enemy.knightHit(knight);
                }
            }
        }

        for (Enemy enemy : allEnemies){
            enemy.update(delta);
        }
    }

    private void addGrounds(TiledMap tiledMap){
        MapLayer ground = tiledMap.getLayers().get("ground");

        float unitScale = 1 / 6f;

        for (MapObject object : ground.getObjects()){
            Rectangle tiledRectangle = ((RectangleMapObject) object).getRectangle();
            Rectangle gameRectangle = new Rectangle(tiledRectangle.x * unitScale, tiledRectangle.y * unitScale, tiledRectangle.width * unitScale, tiledRectangle.height * unitScale);
            grounds.add(gameRectangle);
        }
    }

    private void addTurnPositions(TiledMap tiledMap){
        MapLayer ground = tiledMap.getLayers().get("turn positions");

        float unitScale = 1 / 6f;

        for (MapObject object : ground.getObjects()){
            Rectangle tiledRectangle = ((RectangleMapObject) object).getRectangle();
            Rectangle gameRectangle = new Rectangle(tiledRectangle.x * unitScale, tiledRectangle.y * unitScale, tiledRectangle.width * unitScale, tiledRectangle.height * unitScale);
            turnPositions.add(gameRectangle);
        }
    }

    public void spawnEnemies(TiledMap map, float unitScale) {
        MapLayer spawnLayer = map.getLayers().get("enemy spawns");
        if (spawnLayer == null) return;

        for (MapObject object : spawnLayer.getObjects()) {
            if (object instanceof PointMapObject) {
                PointMapObject pointObj = (PointMapObject) object;

                float x = pointObj.getPoint().x * unitScale;
                float y = pointObj.getPoint().y * unitScale;

                String enemyType = pointObj.getProperties().get("enemyType", String.class);

                if ("crawler".equals(enemyType)) {
                    SimpleEnemy simpleEnemy = new SimpleEnemy(this, x, y, 200f, 80f, 60f, 40f, 5f);
                    allEnemies.add(simpleEnemy);
                    GameController.addEnemyView(new CrawlerView(simpleEnemy));
                }
                else if ("husk".equals(enemyType)) {
                    HuskHornHeadEnemy huskHornHeadEnemy = new HuskHornHeadEnemy(this, x, y, 180f, 120f, 60f, 40f, 5f);
                    allEnemies.add(huskHornHeadEnemy);
                    GameController.addEnemyView(new HuskHornHeadView(huskHornHeadEnemy));
                }
                else if ("crystallized".equals(enemyType)) {
                    CrystallizedEnemy crystallizedEnemy = new CrystallizedEnemy(this, x, y, 180f, 120f, 60f, 40f, 5f);
                    allEnemies.add(crystallizedEnemy);
                    GameController.addEnemyView(new CrystallizedView(crystallizedEnemy));
                }
                else if ("mosquito".equals(enemyType)) {
                    FlyerEnemy flyerEnemy = new FlyerEnemy(this, x, y, 180f, 110f, 70f, 40f, 10f);
                    allEnemies.add(flyerEnemy);
                    RectangleMapObject patrolObj = pointObj.getProperties().get("patrol", RectangleMapObject.class);
                    Rectangle tiledRectangle = patrolObj.getRectangle();
                    Rectangle gameRectangle = new Rectangle(tiledRectangle.x * unitScale, tiledRectangle.y * unitScale, tiledRectangle.width * unitScale, tiledRectangle.height * unitScale);
                    flyerEnemy.setPatrolRect(gameRectangle);
                    GameController.addEnemyView(new MosquitoView(flyerEnemy));
                }
            }
        }
    }

    public Knight getKnight() {
        return knight;
    }

    public ArrayList<Rectangle> getGrounds() {
        return grounds;
    }

    public void setActiveSlashEffect(SlashEffect activeSlashEffect) {
        this.activeSlashEffect = activeSlashEffect;
    }

    public SlashEffect getActiveSlashEffect() {
        return activeSlashEffect;
    }

    public ArrayList<Rectangle> getTurnPositions() {
        return turnPositions;
    }

    public ArrayList<Enemy> getAllEnemies() {
        return allEnemies;
    }

    public ArrayList<Laser> getLasers() {
        return lasers;
    }

    public float getPlayTime() {
        return playTime;
    }

    public String getMapAddress() {
        return mapAddress;
    }
}
