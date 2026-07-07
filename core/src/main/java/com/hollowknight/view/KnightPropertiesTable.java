package com.hollowknight.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.hollowknight.model.App;
import com.hollowknight.model.Manager;
import com.hollowknight.model.enums.Texts;

public class KnightPropertiesTable extends Stack {
    public KnightPropertiesTable(Skin skin, Runnable backRunnable) {

        Table backTable = new Table();
        Table rootTable = new Table();

        Image title_up = new Image(skin.getDrawable("title_up_others"));
        Label title = new Label(Texts.KNIGHT_PROPERTIES.get(App.getCurrentLanguage()), skin, "title");
        Label spellLabel = new Label(Texts.SPELLS_INSTRUCTIONS.get(App.getCurrentLanguage()), skin);
        Label soulLabel = new Label(Texts.SOUL_INSTRUCTIONS.get(App.getCurrentLanguage()), skin);
        Label charmsLabel = new Label(Texts.CHARMS_INSTRUCTIONS.get(App.getCurrentLanguage()), skin);
        TextButton backBtn = new TextButton(Texts.BACK.get(App.getCurrentLanguage()), skin, "default");

        rootTable.setFillParent(true);

        spellLabel.setWrap(true);
        soulLabel.setWrap(true);
        charmsLabel.setWrap(true);

        rootTable.pad(40).top();
        rootTable.defaults().pad(20);
        rootTable.add(title).colspan(2).row();
        rootTable.add(title_up).colspan(2).spaceBottom(60).row();
        rootTable.defaults().pad(40);
        rootTable.add(spellLabel).growX().row();
        rootTable.add(soulLabel).growX().row();
        rootTable.add(charmsLabel).growX();


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

        InputListener hoverListener = new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                AudioManager.playHover();
            }
        };

        backBtn.addListener(hoverListener);
    }
}
