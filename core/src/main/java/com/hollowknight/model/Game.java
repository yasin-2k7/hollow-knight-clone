package com.hollowknight.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PointMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.hollowknight.controller.GameController;
import com.hollowknight.model.enemies.*;
import com.hollowknight.model.enums.*;
import com.hollowknight.view.game.ZoteView;
import com.hollowknight.view.game.enemiesView.CrawlerView;
import com.hollowknight.view.game.enemiesView.CrystallizedView;
import com.hollowknight.view.game.enemiesView.HuskHornHeadView;
import com.hollowknight.view.game.enemiesView.MosquitoView;

import java.util.ArrayList;


public class Game {
    private float startX, startY;
    private float mapStartX, mapStartY;
    private float playTime;
    private int deathNumber;
    private int enemyDeathNumber;
    private Knight knight;
    private int saveIndex;
    private GameMap currentMap;
    private final float GRAVITY = -250f;
    private SlashEffect activeSlashEffect = null;
    private SpellEffect activeSpellEffect = null;
    private AttackWave activeAttackWave = null;
    private EntityAudioListener audioListener;
    private ArrayList<EventListener> eventListeners = new ArrayList<>();


    public float getGRAVITY() {
        return GRAVITY;
    }

    public Game(float mapStartX, float mapStartY, float startX, float startY, float playTime, int masks, int soul, int deathNumber, int enemyDeathNumber, int saveIndex) {
        this.startX = startX;
        this.startY = startY;
        this.mapStartX = mapStartX;
        this.mapStartY = mapStartY;
        this.playTime = playTime;
        this.deathNumber = deathNumber;
        this.enemyDeathNumber = enemyDeathNumber;
        this.saveIndex = saveIndex;
        knight = new Knight(masks, soul, startX, startY, this);
    }

    public void loadMap(String mapAddress, TiledMap tiledMap, float knightSpawnX, float knightSpawnY) {
        this.currentMap = new GameMap(this, mapAddress, tiledMap, 1/6f);
        this.knight.getPosition().set(knightSpawnX, knightSpawnY);
    }

    public void update(float delta){
        playTime += delta;
        knight.update(delta);
        if (activeSlashEffect != null){
            activeSlashEffect.update(delta);
        }
        if (activeSpellEffect != null){
            activeSpellEffect.update(delta);
        }

        for (Laser laser : currentMap.getLasers()){
            laser.update(delta);
        }

        if (activeSlashEffect != null) {
            for (Enemy enemy : currentMap.getAllEnemies()) {
                if (Intersector.overlaps(activeSlashEffect.getHitBounds(), enemy.getBounds())) {
                    if (!activeSlashEffect.hasHitAlready(enemy)){
                        if (!enemy.isDead()) {
                            enemy.takeDamage(1, knight.getPosition().x, knight.getEnemyKnockbackSpeed());
                            activeSlashEffect.registerHit(enemy);
                            knight.increaseSoul(knight.getSoulCatchAmount());
                            if (activeSlashEffect.getType() == SlashDirection.DOWN){
                                knight.pogoJump();
                            }
                        }
                    }
                }
            }
        }

        if (activeSpellEffect != null) {
            for (Enemy enemy : currentMap.getAllEnemies()) {
                if (Intersector.overlaps(activeSpellEffect.getHitBounds(), enemy.getBounds())) {
                    if (!activeSpellEffect.hasHitAlready(enemy)){
                        if (!enemy.isDead()) {
                            enemy.takeDamage(getActiveSpellEffect().getDamage(), knight.getPosition().x, 250f);
                            activeSpellEffect.registerHit(enemy);
                        }
                    }
                }
            }
            if (activeSpellEffect.getType() == SpellType.VENGEFUL_SPIRIT){
                for (Rectangle ground : currentMap.getGrounds()){
                    if (activeSpellEffect.getHitBounds().overlaps(ground)){
                        audioListener.onAudioEvent(AudioAction.STOP_FIREBALL);
                        activeSpellEffect.setFinished(true);
                    }
                }
            }
        }


        if (!knight.isNoDamage() || knight.getState().equals(KnightState.SHADOW_DASH)){
            for (Enemy enemy : currentMap.getAllEnemies()) {
                if (Intersector.overlaps(knight.getBounds(), enemy.getBounds()) && !enemy.isDead() && enemy.getState() != EnemyState.DEATH_AIR) {
                    if (!knight.getState().equals(KnightState.SHADOW_DASH)) {
                        if (enemy.getPosition().x > knight.getPosition().x) {
                            knight.takeDamage(-1);
                        } else {
                            knight.takeDamage(1);
                        }
                        enemy.knightHit(knight);
                    }
                    else{
                        if (!knight.getHitEnemiesByDash().contains(enemy)){
                            enemy.takeDamage(1, knight.getPosition().x, 100f);
                            knight.getHitEnemiesByDash().add(enemy);
                        }

                    }
                }
                if (enemy.getWeapon() != null){
                    if (enemy.getWeapon().overlaps(knight.getBounds())){
                        knight.takeDamage(enemy.getPosition().x > knight.getPosition().x ? -1 : 1);
                    }
                }
            }
        }

         for (Enemy enemy : currentMap.getAllEnemies()) {
             for (Rectangle spike : currentMap.getSpikes()) {
                 if (enemy.getBounds().overlaps(spike)) {
                     float spikeCenterX = spike.x + (spike.width / 2f);
                     enemy.takeSpikeDamage(1, spikeCenterX);
                     break;
                 }
             }
         }

        if (activeAttackWave != null) {
            activeAttackWave.update(delta);
        }

         if (!knight.isNoDamage()) {
             for (Rectangle spike : currentMap.getSpikes()) {
                 if (knight.getBounds().overlaps(spike)) {
                     float spikeCenterX = spike.x + (spike.width / 2f);
                     knight.takeDamage(spikeCenterX < knight.getBounds().x + knight.getBounds().width ? 1 : -1, true);
                     break;
                 }
                 if (activeSlashEffect != null){
                     if (activeSlashEffect.getHitBounds().overlaps(spike) && !activeSlashEffect.getHitSpikes().contains(spike)){
                         if (activeSlashEffect.getType() == SlashDirection.DOWN){
                             activeSlashEffect.getHitSpikes().add(spike);
                             knight.pogoJump();
                         }
                     }
                 }
             }
             for (Laser laser : currentMap.getLasers()){
                 if (knight.getBounds().overlaps(laser.getBounds())){
                     knight.takeDamage(laser.isFlipped() ? 1 : -1, false);
                     break;
                 }
             }
             if (activeAttackWave != null){
                 if (knight.getBounds().overlaps(activeAttackWave.getBounds())){
                     knight.takeDamage((int) activeAttackWave.getDirection());
                 }
             }

         }



         if (currentMap.getZote() != null){
             currentMap.getZote().update(delta);
            if (knight.getBounds().overlaps(currentMap.getZote().getTalkBox()) && !knight.isTalking()){
                if (!knight.CanTalk()){
                    knight.setCanTalk(true);
                    GameController.showInteractKey(true);
                }
                if (knight.getPosition().x > currentMap.getZote().getTalkBox().x + currentMap.getZote().getTalkBox().width/2 && currentMap.getZote().getState() == ZoteState.IDLE && !currentMap.getZote().isFlipped()){
                    currentMap.getZote().turn();
                }
                else if (knight.getPosition().x < currentMap.getZote().getTalkBox().x + currentMap.getZote().getTalkBox().width/2 && currentMap.getZote().getState() == ZoteState.IDLE && currentMap.getZote().isFlipped()){
                    currentMap.getZote().turn();
                }
            }
            else{
                if (knight.CanTalk()){
                    knight.setCanTalk(false);
                    GameController.showInteractKey(false);
                }
            }
         }

        for (Enemy enemy : currentMap.getAllEnemies()){
            enemy.update(delta);
        }

        for (SwitchPoint switchPoint : currentMap.getSwitchPoints()){
            if (switchPoint.getBounds().overlaps(knight.getBounds())){
                switchPoint.switchMap();
            }
        }
    }

    public Knight getKnight() {
        return knight;
    }


    public void setActiveSlashEffect(SlashEffect activeSlashEffect) {
        this.activeSlashEffect = activeSlashEffect;
    }

    public SlashEffect getActiveSlashEffect() {
        return activeSlashEffect;
    }

    public void setActiveSpellEffect(SpellEffect activeSpellEffect) {
        this.activeSpellEffect = activeSpellEffect;
    }

    public SpellEffect getActiveSpellEffect() {
        return activeSpellEffect;
    }

    public float getPlayTime() {
        return playTime;
    }

    public float getStartX() {
        return startX;
    }

    public float getStartY() {
        return startY;
    }

    public float getMapStartX() {
        return mapStartX;
    }

    public float getMapStartY() {
        return mapStartY;
    }

    public void setAudioListener(EntityAudioListener audioListener) {
        this.audioListener = audioListener;
    }

    public void addEventListener(EventListener eventListener){
        eventListeners.add(eventListener);
    }

    public void informListeners(Achievement achievement){
        if (App.achievements.get(achievement)) return;
        for (EventListener eventListener : eventListeners){
            eventListener.onAchievementUnlocked(achievement);
        }
    }

    public void zoteTalked(){
        informListeners(Achievement.ZOTE);
    }

    public GameMap getCurrentMap() {
        return currentMap;
    }

    public void setCurrentMap(GameMap currentMap) {
        this.currentMap = currentMap;
    }

    public void setNewMapStartPoint(float x, float y){
        this.mapStartX = x;
        this.mapStartY = y;
    }

    public void setActiveAttackWave(AttackWave activeAttackWave) {
        this.activeAttackWave = activeAttackWave;
    }

    public AttackWave getActiveAttackWave() {
        return activeAttackWave;
    }

    public int getDeathNumber() {
        return deathNumber;
    }

    public void setDeathNumber(int deathNumber) {
        this.deathNumber = deathNumber;
    }

    public void setEnemyDeathNumber(int enemyDeathNumber) {
        this.enemyDeathNumber = enemyDeathNumber;
    }

    public int getSaveIndex() {
        return saveIndex;
    }

    public int getEnemyDeathNumber() {
        return enemyDeathNumber;
    }
}
