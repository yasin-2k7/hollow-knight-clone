package com.hollowknight.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.hollowknight.model.App;
import com.hollowknight.model.enums.Texts;

public class SettingMenuScreen extends MenuScreen{
    @Override
    public void show() {
        super.show();

        Image title_up = new Image(skin.getDrawable("title_up_others"));
        Label title = new Label(Texts.SETTINGS.get(App.getCurrentLanguage()), skin, "title");
        PointerMenuButton audioBtn = new PointerMenuButton(Texts.AUDIO.get(App.getCurrentLanguage()), skin, "default", skin.getAtlas());
        PointerMenuButton keyboardBtn = new PointerMenuButton(Texts.KEYBOARD.get(App.getCurrentLanguage()), skin, "default", skin.getAtlas());
        PointerMenuButton languageBtn = new PointerMenuButton(Texts.LANGUAGE.get(App.getCurrentLanguage()), skin, "default", skin.getAtlas());
        TextButton backBtn = new TextButton(Texts.BACK.get(App.getCurrentLanguage()), skin, "default");

        rootTable.setFillParent(true);
        rootTable.pad(60).top();
        rootTable.defaults().pad(10);
        rootTable.add(title).row();
        rootTable.add(title_up).spaceBottom(90).row();
        rootTable.add(audioBtn).row();
        rootTable.add(keyboardBtn).row();
        rootTable.add(languageBtn).row();
        rootTable.add(backBtn).bottom().left().expand();



        audioBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                fadeAndSwitchScreen(new AudioSettingMenuScreen());
            }
        });


        backBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                fadeAndSwitchScreen(new MainMenuScreen());
            }
        });

        languageBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                fadeAndSwitchScreen(new LanguageSettingMenuScreen());
            }
        });

        keyboardBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                fadeAndSwitchScreen(new KeyboardSettingMenuScreen());
            }
        });
    }

}
