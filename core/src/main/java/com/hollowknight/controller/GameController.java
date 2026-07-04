package com.hollowknight.controller;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.hollowknight.model.App;
import com.hollowknight.model.Knight;
import com.hollowknight.model.SlashEffect;
import com.hollowknight.model.enemies.Enemy;
import com.hollowknight.model.enemies.Laser;
import com.hollowknight.model.enums.KnightState;
import com.hollowknight.model.enums.SlashDirection;
import com.hollowknight.view.UiManager;
import com.hollowknight.view.game.GameScreen;
import com.hollowknight.view.game.SlashEffectView;
import com.hollowknight.view.game.enemiesView.CrawlerView;
import com.hollowknight.view.game.enemiesView.EnemyView;
import com.hollowknight.view.game.enemiesView.LaserView;

import java.util.ArrayList;

public class GameController {
    private static float unitScale = App.getUnitScale();
    private static GameScreen screen;
    private static ArrayList<EnemyView> enemyViews = new ArrayList<>();

    public static void addEnemyView(EnemyView enemyView){
        enemyViews.add(enemyView);
    }

    public static void updateGame(float delta){
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
                float maxHeight = 200f * unitScale;
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
                height = 200f * unitScale;
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
                float maxWidth = 250f * unitScale;
                height = 125f * unitScale;
                y = knight.getPosition().y;
                address = "effects/SlashEffect.png";

                if (knight.isFlipped()) {
                    x = knight.getPosition().x + unitScale * (knight.getWidth() - 2 * knight.getwOffset()) - 15f;
                    width = maxWidth;
                    for (Rectangle ground : App.getCurrentGame().getGrounds()) {
                        Rectangle fakeBounds = new Rectangle(x, y, width, height);
                        if (Intersector.overlaps(ground, fakeBounds)) {
                            float allowedWidth = ground.x - x;
                            if (allowedWidth < width && allowedWidth > 0) width = allowedWidth;
                        }
                    }
                } else {
                    x = knight.getPosition().x - maxWidth + 15f;
                    width = maxWidth;
                    for (Rectangle ground : App.getCurrentGame().getGrounds()) {
                        Rectangle fakeBounds = new Rectangle(x, y, width, height);
                        if (Intersector.overlaps(ground, fakeBounds)) {
                            float groundRightEdge = ground.x + ground.width;
                            if (groundRightEdge > x && groundRightEdge < knight.getPosition().x) {
                                x = groundRightEdge;
                                width = (knight.getPosition().x + 15f) - x;
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
                float maxWidth = 250f * unitScale;
                height = 125f * unitScale;
                y = knight.getPosition().y;
                address = "effects/SlashEffectAlt.png";
                if (knight.isFlipped()) {
                    x = knight.getPosition().x + unitScale * (knight.getWidth() - 2 * knight.getwOffset()) - 15f;
                    width = maxWidth;
                    for (Rectangle ground : App.getCurrentGame().getGrounds()) {
                        Rectangle fakeBounds = new Rectangle(x, y, width, height);
                        if (Intersector.overlaps(ground, fakeBounds)) {
                            float allowedWidth = ground.x - x;
                            if (allowedWidth < width && allowedWidth > 0) width = allowedWidth;
                        }
                    }
                } else {
                    x = knight.getPosition().x - maxWidth + 15f;
                    width = maxWidth;
                    for (Rectangle ground : App.getCurrentGame().getGrounds()) {
                        Rectangle fakeBounds = new Rectangle(x, y, width, height);
                        if (Intersector.overlaps(ground, fakeBounds)) {
                            float groundRightEdge = ground.x + ground.width;
                            if (groundRightEdge > x && groundRightEdge < knight.getPosition().x) {
                                x = groundRightEdge;
                                width = (knight.getPosition().x+15f) - x;
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

    public static void createLaser(float originX, float originY, boolean flipped){
        Laser laser = new Laser(originX, originY, App.getCurrentGame(), flipped);
        App.getCurrentGame().getLasers().add(laser);
        LaserView laserView = new LaserView(laser.getBounds());
        screen.getLaserViews().add(laserView);
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

    public static void setScreen(GameScreen screen) {
        GameController.screen = screen;
    }
}
