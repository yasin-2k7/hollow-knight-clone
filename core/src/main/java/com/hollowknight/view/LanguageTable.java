package com.hollowknight.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.hollowknight.model.App;
import com.hollowknight.model.enums.Language;
import com.hollowknight.model.enums.Texts;

public class LanguageTable extends Stack {
    private TextButton english;
    private TextButton french;
    private Label title;
    private TextButton backBtn;
    private Table rootTable;

    public LanguageTable(Skin skin, Runnable backRunnable) {
        rootTable = new Table();
        Image title_up = new Image(skin.getDrawable("title_up_others"));
        title = new Label(Texts.LANGUAGE.get(App.getCurrentLanguage()), skin, "title");
        english = new TextButton("ENGLISH", skin, "toggle");
        french = new TextButton("FRENCH", skin, "toggle");
        backBtn = new TextButton(Texts.BACK.get(App.getCurrentLanguage()), skin, "default");


        rootTable.setFillParent(true);

        rootTable.pad(40).top();
        rootTable.defaults().pad(20);
        rootTable.add(title).row();
        rootTable.add(title_up).spaceBottom(100).row();
        rootTable.add(english).row();
        rootTable.add(french).row();

        rootTable.add(backBtn).pad(10).bottom().left().expand();

        setCheckedLanguage();

        ButtonGroup<TextButton> languageGroup = new ButtonGroup<>(english, french);
        languageGroup.setMaxCheckCount(1);
        languageGroup.setMinCheckCount(1);

        this.add(rootTable);

        backBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                backRunnable.run();
                AudioManager.playClick();
            }
        });

        backBtn.addListener(new InputListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                AudioManager.playHover();
            }
        });

        english.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (english.isChecked()) {
                    App.setCurrentLanguage(Language.ENGLISH);
                    updateUIText();
                }
            }
        });

        french.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (french.isChecked()) {
                    App.setCurrentLanguage(Language.FRENCH);
                    updateUIText();
                }
            }
        });

    }

    private void setCheckedLanguage() {
        switch (App.getCurrentLanguage()){
            case ENGLISH -> english.setChecked(true);
            case FRENCH -> french.setChecked(true);
        }
    }

    public void updateUIText() {
        Language currentLang = App.getCurrentLanguage();
        title.setText(Texts.LANGUAGE.get(currentLang));
        backBtn.setText(Texts.BACK.get(currentLang));

        rootTable.invalidateHierarchy();
    }
}
