package com.hollowknight.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.hollowknight.model.App;
import com.hollowknight.model.enums.Texts;

public class AudioSettingMenuScreen extends MenuScreen{
    @Override
    public void show() {
        super.show();

        Table backTable = new Table();

        Image title_up = new Image(skin.getDrawable("title_up_others"));
        Label title = new Label(Texts.AUDIO_SETTINGS.get(App.getCurrentLanguage()), skin, "title");
        CheckBox music = new CheckBox(Texts.MUSIC.get(App.getCurrentLanguage()), skin);
        CheckBox sfx = new CheckBox(Texts.SFX.get(App.getCurrentLanguage()), skin);
        Label volume = new Label(Texts.MUSIC_VOLUME.get(App.getCurrentLanguage()), skin);
        Slider musicVolume = new Slider(0, 10, 1, false, skin);
        TextButton reset = new TextButton(Texts.RESET.get(App.getCurrentLanguage()), skin, "default");
        TextButton backBtn = new TextButton(Texts.BACK.get(App.getCurrentLanguage()), skin, "default");

        rootTable.setFillParent(true);

        rootTable.pad(40).top();
        rootTable.defaults().pad(20);
        rootTable.add(title).colspan(2).row();
        rootTable.add(title_up).colspan(2).spaceBottom(60).row();
        rootTable.add(music);
        rootTable.add(sfx).row();
        rootTable.add(volume).center().padBottom(90).padRight(60);
        rootTable.add(musicVolume).size(400, 100).spaceBottom(60).row();
        rootTable.add(reset).bottom().colspan(2).expandY();

        backTable.setFillParent(true);
        backTable.pad(40);
        backTable.bottom().left().add(backBtn).pad(10);

        rootStack.add(backTable);


        backBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                fadeAndSwitchScreen(new SettingMenuScreen());
            }
        });


    }
}
