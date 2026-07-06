package com.hollowknight.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.hollowknight.model.App;
import com.hollowknight.model.enums.Texts;

public class SettingTable extends Table {
    Skin skin;
    Runnable runnable;

    public SettingTable(Skin skin, Runnable runnable) {
        this.skin = skin;
        this.runnable = runnable;

        showSetting();
    }

    private void showSetting(){
        this.clearChildren();

        Image title_up = new Image(skin.getDrawable("title_up_others"));
        Label title = new Label(Texts.SETTINGS.get(App.getCurrentLanguage()), skin, "title");
        PointerMenuButton audioBtn = new PointerMenuButton(Texts.AUDIO.get(App.getCurrentLanguage()), skin, "default", skin.getAtlas());
        PointerMenuButton keyboardBtn = new PointerMenuButton(Texts.KEYBOARD.get(App.getCurrentLanguage()), skin, "default", skin.getAtlas());
        PointerMenuButton languageBtn = new PointerMenuButton(Texts.LANGUAGE.get(App.getCurrentLanguage()), skin, "default", skin.getAtlas());
        TextButton backBtn = new TextButton(Texts.BACK.get(App.getCurrentLanguage()), skin, "default");

        this.setFillParent(true);
        this.pad(40).top();
        this.defaults().pad(10);
        this.add(title).row();
        this.add(title_up).spaceBottom(90).row();
        this.add(audioBtn).row();
        this.add(keyboardBtn).row();
        this.add(languageBtn).row();
        this.add(backBtn).bottom().left().expand();



        audioBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.playClick();
                Runnable backAction = () -> fadeAndSwitchTable(() -> {
                        showSetting();
                        SettingTable.this.pad(40).top();
                        SettingTable.this.defaults().pad(10);
                    });


                fadeAndSwitchTable(() -> {
                    SettingTable.this.pad(0);
                    SettingTable.this.defaults().pad(0);
                    AudioTable audioTable = new AudioTable(skin, backAction);
                    SettingTable.this.add(audioTable).expand().fill();
                });
            }
        });

        backBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                runnable.run();
                AudioManager.playClick();
            }
        });

        languageBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.playClick();
                Runnable backAction = () -> fadeAndSwitchTable(() -> {
                    showSetting();
                    SettingTable.this.pad(40).top();
                    SettingTable.this.defaults().pad(10);
                });

                fadeAndSwitchTable(() -> {
                    SettingTable.this.pad(0);
                    SettingTable.this.defaults().pad(0);
                    LanguageTable languageTable = new LanguageTable(skin, backAction);
                    SettingTable.this.add(languageTable).expand().fill();
                });

            }
        });

        keyboardBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.playClick();
                Runnable backAction = () -> fadeAndSwitchTable(() -> {
                    showSetting();
                    SettingTable.this.pad(40).top();
                    SettingTable.this.defaults().pad(10);
                });

                fadeAndSwitchTable(() -> {
                    SettingTable.this.pad(0);
                    SettingTable.this.defaults().pad(0);
                    KeyboardTable keyboardTable = new KeyboardTable(skin, backAction);
                    SettingTable.this.add(keyboardTable).expand().fill();
                });
            }
        });

        InputListener hoverListener = new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                AudioManager.playHover();
            }
        };

        audioBtn.addListener(hoverListener);
        keyboardBtn.addListener(hoverListener);
        languageBtn.addListener(hoverListener);
        backBtn.addListener(hoverListener);


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
