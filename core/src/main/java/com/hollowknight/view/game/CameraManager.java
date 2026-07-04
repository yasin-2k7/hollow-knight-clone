package com.hollowknight.view.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class CameraManager {
    private OrthographicCamera camera;
    private float unitScale;

    private Rectangle bossArena;
    private Rectangle bossTrigger;

    private float cameraMinX = -100;
    private float cameraMaxX = 5000;
    private float cameraMinY = -100;
    private float cameraMaxY = 1000;

    private final float LERP = 4f;
    private boolean BossFightStarted;

    public CameraManager(OrthographicCamera camera, float unitScale) {
        this.camera = camera;
        this.unitScale = unitScale;
    }

    public void loadBoundsFromMap(TiledMap map) {
        MapLayer layer = map.getLayers().get("CameraBounds");
        if (layer == null) return;

        for (MapObject object : layer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                RectangleMapObject rectObj = (RectangleMapObject) object;
                Rectangle rect = rectObj.getRectangle();
                Rectangle scaledRect = new Rectangle(
                    rect.x * unitScale, rect.y * unitScale,
                    rect.width * unitScale, rect.height * unitScale
                );

                if ("boss".equals(rectObj.getName())) {
                    bossArena = scaledRect;
                } else if ("trigger".equals(rectObj.getName())) {
                    bossTrigger = scaledRect;
                } else {
                    cameraMinX = scaledRect.x;
                    cameraMaxX = scaledRect.x + scaledRect.width;
                    cameraMinY = scaledRect.y;
                    cameraMaxY = scaledRect.y + scaledRect.height;
                }
            }
        }
    }

    public void update(float delta, Rectangle knightBounds) {
        if (!BossFightStarted && bossTrigger != null && knightBounds.overlaps(bossTrigger)) {
            BossFightStarted = true;
            cameraMinX = bossArena.x;
            cameraMaxX = bossArena.x + bossArena.width;
            cameraMinY = bossArena.y;
            cameraMaxY = bossArena.y + bossArena.height;
        }

        float targetCamX = knightBounds.x + (knightBounds.width / 2f);
        float targetCamY = knightBounds.y + 2*(knightBounds.height);

        camera.position.x += (targetCamX - camera.position.x) * LERP * delta;
        camera.position.y += (targetCamY - camera.position.y) * LERP * delta;

        float halfCamWidth = (camera.viewportWidth * camera.zoom) / 2f;
        float halfCamHeight = (camera.viewportHeight * camera.zoom) / 2f;

        camera.position.x = MathUtils.clamp(camera.position.x, cameraMinX + halfCamWidth, cameraMaxX - halfCamWidth);
        camera.position.y = MathUtils.clamp(camera.position.y, cameraMinY + halfCamHeight, cameraMaxY - halfCamHeight);

        camera.update();
    }
}
