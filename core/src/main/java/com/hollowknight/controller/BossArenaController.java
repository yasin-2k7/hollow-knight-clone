package com.hollowknight.controller;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.hollowknight.model.App;
import com.hollowknight.model.EntityAudioListener;
import com.hollowknight.model.MovingWall;
import com.hollowknight.model.enemies.Enemy;
import com.hollowknight.model.enums.AudioAction;
import com.hollowknight.view.AudioManager;

public class BossArenaController {
    private MovingWall firstWall, secondWall;
    private Rectangle triggerBounds;
    private boolean active = false;
    private float arenaMinX;
    private float arenaMaxX;

    public BossArenaController(Vector2 firstWallPos, Vector2 secondWallPos, Rectangle triggerBounds) {
        this.firstWall = new MovingWall(firstWallPos);
        this.secondWall = new MovingWall(secondWallPos);
        this.triggerBounds = triggerBounds;
        arenaMinX = firstWallPos.x -10f;
        arenaMaxX = secondWallPos.x + 10f;
    }

    public void update(float delta, Enemy boss){
        firstWall.update(delta);
        secondWall.update(delta);
        if (!active) {
            if (boss.isDead())  return;
            if (App.getCurrentGame().getKnight().getBounds().overlaps(triggerBounds)){
                active = true;
                AudioManager.fadeOutCurrentMusic();
                AudioManager.fadeInMusic(AudioManager.crossroadsActionMusic);
                firstWall.moveDown();
                secondWall.moveDown();
                boss.setActive();
            }
            return;
        }
        if (boss.isDead()){
            active = false;
            AudioManager.fadeOutCurrentMusic();
            firstWall.moveUp();
            secondWall.moveUp();
            GameController.finishGame();
        }
    }


    public float getArenaMinX() {
        return arenaMinX;
    }

    public float getArenaMaxX() {
        return arenaMaxX;
    }



    public MovingWall getFirstWall() {
        return firstWall;
    }

    public MovingWall getSecondWall() {
        return secondWall;
    }

    public void reset() {
        firstWall.reset();
        secondWall.reset();
        AudioManager.fadeOutCurrentMusic();
        AudioManager.fadeInMusic(AudioManager.crossroadsMainMusic);
        active = false;
        GameController.getBoss().reset();
    }

    public boolean isActive() {
        return active;
    }
}
