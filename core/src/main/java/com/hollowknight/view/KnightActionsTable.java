package com.hollowknight.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.hollowknight.model.App;
import com.hollowknight.model.Manager;
import com.hollowknight.model.enums.Texts;

public class KnightActionsTable extends Stack {
    public KnightActionsTable(Skin skin, Runnable backRunnable) {

        Table backTable = new Table();
        Table rootTable = new Table();

        Image title_up = new Image(skin.getDrawable("title_up_others"));
        Label title = new Label(Texts.KNIGHT_ACTIONS.get(App.getCurrentLanguage()), skin, "title");

        Label moveTitleLabel = new Label(Texts.MOVEMENT.get(App.getCurrentLanguage()), skin);
        Label jumpTitleLabel = new Label(Texts.JUMP.get(App.getCurrentLanguage()), skin);
        Label attackTitleLabel = new Label(Texts.ATTACK.get(App.getCurrentLanguage()), skin);
        Label dashTitleLabel = new Label(Texts.DASH.get(App.getCurrentLanguage()), skin);
        Label healTitleLabel = new Label(Texts.FOCUS.get(App.getCurrentLanguage()), skin);
        Label moveDescLabel = new Label(Texts.MOVE_INSTRUCTIONS.get(App.getCurrentLanguage()), skin, "small");
        Label jumpDescLabel = new Label(Texts.JUMP_INSTRUCTIONS.get(App.getCurrentLanguage()), skin, "small");
        Label attackDescLabel = new Label(Texts.ATTACK_INSTRUCTIONS.get(App.getCurrentLanguage()), skin, "small");
        Label dashDescLabel = new Label(Texts.DASH_INSTRUCTIONS.get(App.getCurrentLanguage()), skin, "small");
        Label healDescLabel = new Label(Texts.HEAL_INSTRUCTIONS.get(App.getCurrentLanguage()), skin, "small");

        TextButton backBtn = new TextButton(Texts.BACK.get(App.getCurrentLanguage()), skin, "default");

        rootTable.setFillParent(true);

        moveDescLabel.setWrap(true);
        jumpDescLabel.setWrap(true);
        attackDescLabel.setWrap(true);
        dashDescLabel.setWrap(true);
        healDescLabel.setWrap(true);

        rootTable.pad(40).top();
        rootTable.defaults().pad(20);
        rootTable.add(title).colspan(2).row();
        rootTable.add(title_up).colspan(2).spaceBottom(40).row();
        rootTable.left().defaults().pad(10).padLeft(300);
        rootTable.add(moveTitleLabel).left().row();
        rootTable.add(moveDescLabel).left().padBottom(20).growX().row();
        rootTable.add(jumpTitleLabel).left().row();
        rootTable.add(jumpDescLabel).left().padBottom(20).growX().row();
        rootTable.add(attackTitleLabel).left().row();
        rootTable.add(attackDescLabel).left().padBottom(20).growX().row();
        rootTable.add(dashTitleLabel).left().row();
        rootTable.add(dashDescLabel).left().padBottom(20).growX().row();
        rootTable.add(healTitleLabel).left().row();
        rootTable.add(healDescLabel).left().padBottom(20).growX().row();

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
