package com.hollowknight.view.game;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.hollowknight.model.App;
import com.hollowknight.model.enums.Texts;
import com.hollowknight.view.*;

public class CheatCodeTable extends Table {
    Skin skin;
    Runnable runnable;

    public CheatCodeTable(Skin skin, Runnable runnable) {
        this.skin = skin;
        this.runnable = runnable;

        show();
    }

    private void show(){
        this.clearChildren();

        Image title_up = new Image(skin.getDrawable("title_up_others"));
        Label title = new Label(Texts.CHEAT_CODES.get(App.getCurrentLanguage()), skin, "title");
        Label bossArenaTeleport = new Label(Texts.BOSS_ARENA_TELEPORT.get(App.getCurrentLanguage()) + " : " + Texts.SHIFT.get(App.getCurrentLanguage()) + " + B", skin, "default");
        Label spectatorMode = new Label(Texts.SPECTATOR_MODE.get(App.getCurrentLanguage()) + " : " + Texts.SHIFT.get(App.getCurrentLanguage()) + " + S", skin, "default");
        Label emergencyHeal = new Label(Texts.EMERGENCY_HEAL.get(App.getCurrentLanguage()) + " : " + Texts.SHIFT.get(App.getCurrentLanguage()) + " + E", skin, "default");
        Label refillSoulVessel = new Label(Texts.REFILL_SOUL_VESSEL.get(App.getCurrentLanguage()) + " : " + Texts.SHIFT.get(App.getCurrentLanguage()) + " + R", skin, "default");
        Label godMode = new Label(Texts.GOD_MODE.get(App.getCurrentLanguage()) + " : " + Texts.SHIFT.get(App.getCurrentLanguage()) + " + G", skin, "default");
        Label killNearbyEnemies = new Label(Texts.KILL_NEARBY_ENEMIES.get(App.getCurrentLanguage()) + " : " + Texts.SHIFT.get(App.getCurrentLanguage()) + " + K", skin, "default");
        TextButton backBtn = new TextButton(Texts.BACK.get(App.getCurrentLanguage()), skin, "default");

        this.setFillParent(true);
        this.pad(40).top();
        this.defaults().pad(10);
        this.add(title).row();
        this.add(title_up).spaceBottom(90).row();
        this.add(bossArenaTeleport).row();
        this.add(spectatorMode).row();
        this.add(emergencyHeal).row();
        this.add(refillSoulVessel).row();
        this.add(godMode).row();
        this.add(killNearbyEnemies).row();
        this.add(backBtn).bottom().left().expand();

        backBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                runnable.run();
                AudioManager.playClick();
            }
        });

        InputListener hoverListener = new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                AudioManager.playHover();
            }
        };

        backBtn.addListener(hoverListener);
    }
}
