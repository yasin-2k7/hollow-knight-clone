package com.hollowknight.model;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.hollowknight.model.enemies.Enemy;

import java.util.ArrayList;

public class Game {
    private float startX, startY;
    private Knight knight;
    private final float GRAVITY = -250f;
    private ArrayList<Rectangle> grounds;
    private ArrayList<Enemy> allEnemies = new ArrayList<>();
    private SlashEffect activeSlashEffect = null;


    public float getGRAVITY() {
        return GRAVITY;
    }

    public Game(float startX, float startY, TiledMap tiledMap) {
        this.startX = startX;
        this.startY = startY;
        knight = new Knight(0, 0, startX, startY, this);
        grounds = new ArrayList<>();
        AddGrounds(tiledMap);
    }

    public void update(float delta){
        knight.update(delta);
        if (activeSlashEffect != null){
            activeSlashEffect.update(delta);
        }
    }

    private void AddGrounds(TiledMap tiledMap){
        MapLayer ground = tiledMap.getLayers().get("ground");

        float unitScale = 1 / 6f;

        for (MapObject object : ground.getObjects()){
            Rectangle tiledRectangle = ((RectangleMapObject) object).getRectangle();
            Rectangle gameRectangle = new Rectangle(tiledRectangle.x * unitScale, tiledRectangle.y * unitScale, tiledRectangle.width * unitScale, tiledRectangle.height * unitScale);
            grounds.add(gameRectangle);
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
}
