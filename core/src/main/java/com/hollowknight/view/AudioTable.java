package com.hollowknight.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.hollowknight.model.App;
import com.hollowknight.model.Manager;
import com.hollowknight.model.enums.Texts;

public class AudioTable extends Stack {
    public AudioTable(Skin skin, Runnable backRunnable) {

        Table backTable = new Table();
        Table rootTable = new Table();

        Image title_up = new Image(skin.getDrawable("title_up_others"));
        Label title = new Label(Texts.AUDIO_SETTINGS.get(App.getCurrentLanguage()), skin, "title");
        CheckBox music = new CheckBox(Texts.MUSIC.get(App.getCurrentLanguage()), skin);
        CheckBox sfx = new CheckBox(Texts.SFX.get(App.getCurrentLanguage()), skin);
        Label volume = new Label(Texts.MUSIC_VOLUME.get(App.getCurrentLanguage()), skin);
        Slider musicVolume = new Slider(0, 10, 1f, false, skin);
        TextButton reset = new TextButton(Texts.RESET.get(App.getCurrentLanguage()), skin, "default");
        TextButton backBtn = new TextButton(Texts.BACK.get(App.getCurrentLanguage()), skin, "default");

        rootTable.setFillParent(true);
        music.setChecked(App.isMusicEnabled());
        sfx.setChecked(App.isSfxEnabled());
        musicVolume.setValue(App.getMusicVolume()*10);

        rootTable.pad(40).top();
        rootTable.defaults().pad(20);
        rootTable.add(title).colspan(2).row();
        rootTable.add(title_up).colspan(2).spaceBottom(60).row();
        rootTable.add(music);
        rootTable.add(sfx).row();
        rootTable.add(volume).center().padBottom(90).padRight(60);
        rootTable.add(musicVolume).size(400, 100).spaceBottom(60).row();
        rootTable.add(reset).pad(10).bottom().colspan(2).expandY();

        backTable.pad(40);
        backTable.bottom().left().add(backBtn).pad(10);

        this.add(rootTable);
        this.add(backTable);


        backBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                backRunnable.run();
                AudioManager.playClick();
            }
        });

        reset.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                App.setSfxEnabled(true);
                App.setMusicEnabled(true);
                App.setMusicVolume(0.8f);
                music.setChecked(App.isMusicEnabled());
                sfx.setChecked(App.isSfxEnabled());
                musicVolume.setValue(App.getMusicVolume()*10);
                Manager.saveConfig();
                AudioManager.playClick();
            }
        });

        musicVolume.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float sliderValue = musicVolume.getValue();
                App.setMusicVolume(sliderValue / 10f);
                AudioManager.updateMusicVolume();
                Manager.saveConfig();
            }
        });

        music.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                App.setMusicEnabled(music.isChecked());
                AudioManager.updateMusicBoolean();
                Manager.saveConfig();
            }
        });

        sfx.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                App.setSfxEnabled(sfx.isChecked());
                Manager.saveConfig();
            }
        });

        InputListener hoverListener = new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                AudioManager.playHover();
            }
        };

        backBtn.addListener(hoverListener);
        reset.addListener(hoverListener);

    }
}
