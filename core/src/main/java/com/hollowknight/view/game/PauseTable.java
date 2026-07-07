package com.hollowknight.view.game;

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
import com.hollowknight.model.enums.Texts;
import com.hollowknight.view.AudioManager;
import com.hollowknight.view.PointerMenuButton;
import com.hollowknight.view.SettingMenuScreen;

public class PauseTable extends Table {
    public PauseTable(Skin skin) {
        this.center().pad(40).defaults().pad(10);
        Label title = new Label(Texts.PAUSED.get(App.getCurrentLanguage()), skin, "title");
        Image title_up = new Image(skin.getDrawable("title_up_others"));
        PointerMenuButton resumeBtn = new PointerMenuButton(Texts.RESUME.get(App.getCurrentLanguage()), skin, "default", skin.getAtlas());
        PointerMenuButton settingBtn = new PointerMenuButton(Texts.SETTINGS.get(App.getCurrentLanguage()), skin, "default", skin.getAtlas());
        PointerMenuButton cheatCodesBtn = new PointerMenuButton(Texts.CHEAT_CODES.get(App.getCurrentLanguage()), skin, "default", skin.getAtlas());
        PointerMenuButton saveAndExitBtn = new PointerMenuButton(Texts.SAVE_AND_EXIT.get(App.getCurrentLanguage()), skin, "default", skin.getAtlas());

        this.add(title).row();
        this.add(title_up).row();
        this.add(resumeBtn).row();
        this.add(settingBtn).row();
        this.add(cheatCodesBtn).row();
        this.add(saveAndExitBtn).row();

        resumeBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.playClick();
                GameController.togglePause();
            }
        });

        settingBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.playClick();
                GameController.settingInPause();
            }
        });

        cheatCodesBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.playClick();
                GameController.showCheatCodes();
            }
        });

        saveAndExitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.playClick();
                GameController.saveGame();
            }
        });


        InputListener hoverListener = new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                AudioManager.playHover();
            }
        };

        resumeBtn.addListener(hoverListener);
        settingBtn.addListener(hoverListener);
        cheatCodesBtn.addListener(hoverListener);
        saveAndExitBtn.addListener(hoverListener);
    }

    private void fadeAndSwitchTable(final Runnable changeTable) {
        this.addAction(Actions.sequence(
            Actions.fadeOut(0.2f),
            Actions.run(new Runnable() {
                @Override
                public void run() {
                    clearChildren();
                    changeTable.run();
                }
            }),
            Actions.fadeIn(0.2f)
        ));
    }
}
