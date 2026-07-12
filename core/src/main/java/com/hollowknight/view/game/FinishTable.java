package com.hollowknight.view.game;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.hollowknight.controller.GameController;
import com.hollowknight.model.App;
import com.hollowknight.model.GameSave;
import com.hollowknight.model.Manager;
import com.hollowknight.model.enums.GameState;
import com.hollowknight.model.enums.Texts;
import com.hollowknight.view.AudioManager;
import com.hollowknight.view.MainMenuScreen;
import com.hollowknight.view.PointerMenuButton;
import com.hollowknight.view.UiManager;

public class FinishTable extends Table {
    public FinishTable(Skin skin) {
        this.center().pad(40).defaults().pad(10);
        Label title = new Label(Texts.THE_END.get(App.getCurrentLanguage()), skin, "title");
        Image title_up = new Image(skin.getDrawable("title_up_others"));
        Label death = new Label(Texts.DEATH_NUMBER.get(App.getCurrentLanguage()) + ": " + App.getCurrentGame().getDeathNumber(), skin, "default");
        Label enemyDeath = new Label(Texts.ENEMY_DEATH_NUMBER.get(App.getCurrentLanguage()) + ": " + App.getCurrentGame().getEnemyDeathNumber(), skin, "default");
        Label playTime = new Label(Texts.PLAY_TIME.get(App.getCurrentLanguage()) + ": " + ((int) App.getCurrentGame().getPlayTime()/60f) + " Minutes", skin, "default");
        PointerMenuButton restartBtn = new PointerMenuButton(Texts.RESTART.get(App.getCurrentLanguage()), skin, "default", skin.getAtlas());
        PointerMenuButton exitBtn = new PointerMenuButton(Texts.SAVE_AND_EXIT.get(App.getCurrentLanguage()), skin, "default", skin.getAtlas());

        this.add(title).row();
        this.add(title_up).row();
        this.add(death).row();
        this.add(enemyDeath).row();
        this.add(playTime).row();
        this.add(restartBtn).row();
        this.add(exitBtn).row();

        restartBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.playClick();

                GameController.setBoss(null);
                GameController.getBossArenaControllers().clear();
                GameSave activeSave = Manager.createNewGame(App.getCurrentGame().getSaveIndex());
                TiledMap map = new TmxMapLoader().load(activeSave.getTiledMapAddress());
                GameController.init(activeSave, map);
                GameController.setCurrentSaveIndex(App.getCurrentGame().getSaveIndex());
                GameController.setGameState(GameState.RUNNING);
                UiManager.setScreen(GameController.getScreen());
            }
        });

        exitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.playClick();
                GameController.getScreen().fadeAndSwitchScreen(new MainMenuScreen());
            }
        });

        InputListener hoverListener = new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                AudioManager.playHover();
            }
        };

        restartBtn.addListener(hoverListener);
        exitBtn.addListener(hoverListener);

    }
}
