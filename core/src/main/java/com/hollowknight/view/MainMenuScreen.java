package com.hollowknight.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.hollowknight.model.App;
import com.hollowknight.model.enums.Texts;

public class MainMenuScreen extends MenuScreen {
    @Override
    public void show() {
        super.show();

        App.setCurrentGame(null);
        Table titleTable = new Table();
        Image titleTop = new Image(skin.getDrawable("title_up"));
        Label title = new Label(Texts.GAME_TITLE.get(App.getCurrentLanguage()),skin, "title");
        Image titleBottom = new Image(skin.getDrawable("title_down"));

        titleTable.top().pad(20);
        titleTable.add(titleTop).row();
        titleTable.add(title).space(20).row();
        titleTable.add(titleBottom);



        Table buttonsTable = new Table();
        PointerMenuButton startBtn = new PointerMenuButton(Texts.START_GAME.get(App.getCurrentLanguage()), skin, "default", skin.getAtlas());
        PointerMenuButton settingBtn = new PointerMenuButton(Texts.SETTINGS.get(App.getCurrentLanguage()), skin, "default", skin.getAtlas());
        PointerMenuButton guideBtn = new PointerMenuButton(Texts.GUIDE.get(App.getCurrentLanguage()), skin, "default", skin.getAtlas());
        PointerMenuButton achievementsBtn = new PointerMenuButton(Texts.ACHIEVEMENTS.get(App.getCurrentLanguage()), skin, "default", skin.getAtlas());
        PointerMenuButton exitBtn = new PointerMenuButton(Texts.EXIT.get(App.getCurrentLanguage()), skin, "default", skin.getAtlas());



        buttonsTable.add(startBtn).row();
        buttonsTable.add(settingBtn).row();
        buttonsTable.add(guideBtn).row();
        buttonsTable.add(achievementsBtn).row();
        buttonsTable.add(exitBtn).row();

        buttonsTable.defaults().pad(10);




        rootTable.setFillParent(true);
        rootTable.top().pad(40);

        rootTable.add(titleTable).row();
        rootTable.add(buttonsTable).pad(20);

        settingBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.playClick();
                fadeAndSwitchScreen(new SettingMenuScreen());
            }
        });

        startBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.playClick();
                fadeAndSwitchScreen(new StartGameMenuScreen());
            }
        });

        AudioManager.fadeInMusic(AudioManager.menuMusic);
    }

}
