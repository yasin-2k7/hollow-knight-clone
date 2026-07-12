package com.hollowknight.model;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PointMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.hollowknight.controller.BossArenaController;
import com.hollowknight.controller.GameController;
import com.hollowknight.model.enemies.*;
import com.hollowknight.model.enums.EnemyType;

import java.util.ArrayList;

public class GameMap {
    private String mapAddress;
    private ArrayList<Rectangle> grounds = new ArrayList<>();
    private ArrayList<SwitchPoint> switchPoints = new ArrayList<>();
    private ArrayList<Rectangle> spikes = new ArrayList<>();
    private ArrayList<Rectangle> turnPositions = new ArrayList<>();
    private ArrayList<Enemy> allEnemies = new ArrayList<>();
    private ArrayList<Laser> lasers = new ArrayList<>();
    private Zote zote;
    private Game game;
    private String loadBgAddress;


    public GameMap(Game game, String mapAddress, TiledMap tiledMap, float unitScale) {
        this.game = game;
        this.mapAddress = mapAddress;
        loadBgAddress = mapAddress.equals("map/map2.tmx") ? "Area_Green_Path" : "Area_Forgotten Crossroads";

        loadGrounds(tiledMap, unitScale);
        loadSpikes(tiledMap, unitScale);
        loadTurnPositions(tiledMap, unitScale);
        loadMovingWalls(tiledMap, unitScale);
        spawnEnemies(tiledMap, unitScale);
        loadSwitchPoints(tiledMap, unitScale);

    }

    private void loadMovingWalls(TiledMap tiledMap, float unitScale) {
        MapLayer layer = tiledMap.getLayers().get("boss trigger");
        if (layer == null) return;
        for (MapObject obj : layer.getObjects()) {
            if (obj instanceof RectangleMapObject && "trigger".equals(obj.getName())) {
                RectangleMapObject rectObj = (RectangleMapObject) obj;
                Rectangle rect = rectObj.getRectangle();
                Rectangle trigger = new Rectangle(
                    rect.x * unitScale,
                    rect.y * unitScale,
                    rect.width * unitScale,
                    rect.height * unitScale
                );

                PointMapObject firstWallPoint = (PointMapObject) obj.getProperties().get("firstWallPos");
                PointMapObject secondWallPoint = (PointMapObject) obj.getProperties().get("secondWallPos");
                if (firstWallPoint != null && secondWallPoint != null) {

                    float w1X = firstWallPoint.getProperties().get("x", Float.class);
                    float w1Y = firstWallPoint.getProperties().get("y", Float.class);
                    Vector2 firstWall = new Vector2(w1X, w1Y).scl(unitScale);

                    float w2X = secondWallPoint.getProperties().get("x", Float.class);
                    float w2Y = secondWallPoint.getProperties().get("y", Float.class);
                    Vector2 secondWall = new Vector2(w2X, w2Y).scl(unitScale);

                    GameController.addBossArena(new BossArenaController(firstWall, secondWall, trigger));
                }
            }
        }
    }

    private void loadGrounds(TiledMap tiledMap, float unitScale) {
        MapLayer layer = tiledMap.getLayers().get("ground");
        if (layer == null) return;
        for (MapObject object : layer.getObjects()) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            grounds.add(new Rectangle(rect.x * unitScale, rect.y * unitScale, rect.width * unitScale, rect.height * unitScale));
        }
    }

    private void loadSwitchPoints(TiledMap tiledMap, float unitScale) {
        MapLayer layer = tiledMap.getLayers().get("switch map points");
        if (layer == null) return;
        for (MapObject object : layer.getObjects()) {
            String nextMapAddress = object.getProperties().get("address").toString();
            float nextMapSpawnX = (float) object.getProperties().get("spawnX");
            float nextMapSpawnY = (float) object.getProperties().get("spawnY");
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            Rectangle inGameRect = new Rectangle(rect.x * unitScale, rect.y * unitScale, rect.width * unitScale, rect.height * unitScale);
            switchPoints.add(new SwitchPoint(inGameRect, nextMapSpawnX*unitScale, nextMapSpawnY*unitScale, nextMapAddress));
        }
    }

    private void loadSpikes(TiledMap tiledMap, float unitScale) {
        MapLayer layer = tiledMap.getLayers().get("spikes");
        if (layer == null) return;
        for (MapObject object : layer.getObjects()) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            spikes.add(new Rectangle(rect.x * unitScale, rect.y * unitScale, rect.width * unitScale, rect.height * unitScale));
        }
    }

    private void loadTurnPositions(TiledMap tiledMap, float unitScale) {
        MapLayer layer = tiledMap.getLayers().get("turn positions");
        if (layer == null) return;
        for (MapObject object : layer.getObjects()) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            turnPositions.add(new Rectangle(rect.x * unitScale, rect.y * unitScale, rect.width * unitScale, rect.height * unitScale));
        }
    }

    private void spawnEnemies(TiledMap map, float unitScale) {
        MapLayer spawnLayer = map.getLayers().get("enemy spawns");
        if (spawnLayer == null) return;

        for (MapObject object : spawnLayer.getObjects()) {
            if (object instanceof PointMapObject) {
                PointMapObject pointObj = (PointMapObject) object;
                float x = pointObj.getPoint().x * unitScale;
                float y = pointObj.getPoint().y * unitScale;
                String enemyType = pointObj.getProperties().get("enemyType", String.class);

                Enemy enemy = null;
                if ("crawler".equals(enemyType)) {
                    enemy = new SimpleEnemy(game, x, y, 200f, 80f, 60f, 40f, 5f, EnemyType.CRAWLER);
                    allEnemies.add(enemy);
                }else if ("mosscreep".equals(enemyType)) {
                        enemy = new SimpleEnemy(game, x, y, 180f, 100f, 40f, 15f, 10f, EnemyType.MOSSCREEP);
                        allEnemies.add(enemy);
                } else if ("husk".equals(enemyType)) {
                    enemy = new HuskHornHeadEnemy(game, x, y, 180f, 120f, 60f, 40f, 5f, EnemyType.HUSK);
                    allEnemies.add(enemy);
                } else if ("crystallized".equals(enemyType)) {
                    enemy = new CrystallizedEnemy(game, x, y, 180f, 120f, 60f, 40f, 5f, EnemyType.CRYSTALLIZED);
                    allEnemies.add(enemy);
                } else if ("falseknight".equals(enemyType)) {
                    enemy = new FalseKnight(game, x, y, 700f, 400f, 280f, 150f, 30f, EnemyType.FALSEKNIGHT);
                    allEnemies.add(enemy);
                    System.out.println("created model");
                    GameController.setBoss(enemy);
                } else if ("zote".equals(enemyType)) {
                    this.zote = new Zote(x, y, 180f, 120f, 60f, 40f, 5f);
                } else if ("mosquito".equals(enemyType)) {
                    enemy = new FlyerEnemy(game, x, y, 180f, 110f, 70f, 40f, 10f, EnemyType.MOSQUITO);
                    allEnemies.add(enemy);
                    RectangleMapObject patrolObj = pointObj.getProperties().get("patrol", RectangleMapObject.class);
                    Rectangle rect = patrolObj.getRectangle();
                    enemy.setPatrolRect(new Rectangle(rect.x * unitScale, rect.y * unitScale, rect.width * unitScale, rect.height * unitScale));
                }
                if (enemy != null){
                    System.out.println("creating view");
                    GameController.makeEnemyView(enemy, enemyType);
                }

            }
        }
    }

    public String getMapAddress() {
        return mapAddress;
    }

    public ArrayList<Rectangle> getGrounds() {
        return grounds;
    }

    public ArrayList<Rectangle> getSpikes() {
        return spikes;
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

    public Zote getZote() {
        return zote;
    }

    public String getLoadBgAddress() {
        return loadBgAddress;
    }

    public ArrayList<SwitchPoint> getSwitchPoints() {
        return switchPoints;
    }
}
