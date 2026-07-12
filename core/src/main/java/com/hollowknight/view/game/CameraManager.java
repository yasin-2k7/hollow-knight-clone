package com.hollowknight.view.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.hollowknight.controller.BossArenaController;
import com.hollowknight.controller.GameController;

import java.util.ArrayList;

public class CameraManager {
    private static final float BOUNDS_LERP = 0.5f;
    private OrthographicCamera camera;
    private float unitScale;

    private float cameraMinX = -100;
    private float cameraMaxX = 5000;
    private float cameraMinY = -100;
    private float cameraMaxY = 1000;
    private ArrayList<Rectangle> allCameraBounds = new ArrayList<>();

    private boolean firstFrame = true;

    private final float LERP = 7f;

    public CameraManager(OrthographicCamera camera, float unitScale) {
        this.camera = camera;
        this.unitScale = unitScale;
    }

    public void loadBoundsFromMap(TiledMap map) {
        MapLayer layer = map.getLayers().get("CameraBounds");
        if (layer == null) return;
        allCameraBounds.clear();
        firstFrame = true;
        for (MapObject object : layer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                RectangleMapObject rectObj = (RectangleMapObject) object;
                Rectangle rect = rectObj.getRectangle();
                Rectangle scaledRect = new Rectangle(
                    rect.x * unitScale, rect.y * unitScale,
                    rect.width * unitScale, rect.height * unitScale
                );
                allCameraBounds.add(scaledRect);
            }

        }
    }

    public void update(float delta, Rectangle knightBounds) {

        float targetCamX = knightBounds.x + (knightBounds.width / 2f);
        float targetCamY = knightBounds.y + 1.5f*(knightBounds.height);

        float targetMinX = Float.MAX_VALUE;
        float targetMaxX = -Float.MAX_VALUE;
        float targetMinY = Float.MAX_VALUE;
        float targetMaxY = -Float.MAX_VALUE;
        boolean insideAnyRoom = false;


        for (Rectangle rect : allCameraBounds) {
            if (rect.contains(targetCamX, targetCamY)) {
                if (rect.x < targetMinX) targetMinX = rect.x;
                if (rect.x + rect.width > targetMaxX) targetMaxX = rect.x + rect.width;
                if (rect.y < targetMinY) targetMinY = rect.y;
                if (rect.y + rect.height > targetMaxY) targetMaxY = rect.y + rect.height;
                insideAnyRoom = true;
            }
        }

        if (insideAnyRoom) {
            if (firstFrame) {
                cameraMinX = targetMinX;
                cameraMaxX = targetMaxX;
                cameraMinY = targetMinY;
                cameraMaxY = targetMaxY;
                firstFrame = false;
            } else {
                cameraMinX = MathUtils.lerp(cameraMinX, targetMinX, BOUNDS_LERP * delta);
                cameraMaxX = MathUtils.lerp(cameraMaxX, targetMaxX, BOUNDS_LERP * delta);
                cameraMinY = MathUtils.lerp(cameraMinY, targetMinY, BOUNDS_LERP * delta);
                cameraMaxY = MathUtils.lerp(cameraMaxY, targetMaxY, BOUNDS_LERP * delta);
            }
        }

        for (BossArenaController bossArenaController : GameController.getBossArenaControllers()){
            if (bossArenaController.isActive()){
                cameraMinX = bossArenaController.getArenaMinX();
                cameraMaxX = bossArenaController.getArenaMaxX();
            }
        }


        camera.position.x += (targetCamX - camera.position.x) * LERP * delta;
        camera.position.y += (targetCamY - camera.position.y) * LERP * delta;

        float halfCamWidth = (camera.viewportWidth * camera.zoom) / 2f;
        float halfCamHeight = (camera.viewportHeight * camera.zoom) / 2f;

        camera.position.x = MathUtils.clamp(camera.position.x, cameraMinX + halfCamWidth, cameraMaxX - halfCamWidth);
        camera.position.y = MathUtils.clamp(camera.position.y, cameraMinY + halfCamHeight, cameraMaxY - halfCamHeight);

        camera.update();
    }

    public void snapToPosition(Rectangle knightBounds) {
        this.cameraMinX = -999999f;
        this.cameraMaxX = 999999f;
        this.cameraMinY = -999999f;
        this.cameraMaxY = 999999f;
        this.firstFrame = true;

        float targetCamX = knightBounds.x + (knightBounds.width / 2f);
        float targetCamY = knightBounds.y + 2 * (knightBounds.height);

        camera.position.x = targetCamX;
        camera.position.y = targetCamY;

        System.out.println(camera.position.x + " " + camera.position.y);

        camera.update();
    }
}
