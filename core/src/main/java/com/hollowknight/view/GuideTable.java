package com.hollowknight.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.hollowknight.model.App;
import com.hollowknight.model.enums.Texts;
import com.hollowknight.view.game.CheatCodeTable;

public class GuideTable extends Table {
    Skin skin;
    Runnable runnable;

    public GuideTable(Skin skin, Runnable runnable) {
        this.skin = skin;
        this.runnable = runnable;

        showGuide();
    }

    private void showGuide(){
        this.clearChildren();

        Image title_up = new Image(skin.getDrawable("title_up_others"));
        Label title = new Label(Texts.GUIDE.get(App.getCurrentLanguage()), skin, "title");
        PointerMenuButton actionsBtn = new PointerMenuButton(Texts.KNIGHT_ACTIONS.get(App.getCurrentLanguage()), skin, "default", skin.getAtlas());
        PointerMenuButton propertiesBtn = new PointerMenuButton(Texts.KNIGHT_PROPERTIES.get(App.getCurrentLanguage()), skin, "default", skin.getAtlas());
        PointerMenuButton cheatCodesBtn = new PointerMenuButton(Texts.CHEAT_CODES.get(App.getCurrentLanguage()), skin, "default", skin.getAtlas());
        TextButton backBtn = new TextButton(Texts.BACK.get(App.getCurrentLanguage()), skin, "default");

        this.setFillParent(true);
        this.pad(40).top();
        this.defaults().pad(10);
        this.add(title).row();
        this.add(title_up).spaceBottom(90).row();
        this.add(actionsBtn).row();
        this.add(propertiesBtn).row();
        this.add(cheatCodesBtn).row();
        this.add(backBtn).bottom().left().expand();



        actionsBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.playClick();
                Runnable backAction = () -> fadeAndSwitchTable(() -> {
                    showGuide();
                    GuideTable.this.pad(40).top();
                    GuideTable.this.defaults().pad(10);
                });


                fadeAndSwitchTable(() -> {
                    GuideTable.this.pad(0);
                    GuideTable.this.defaults().pad(0);
                    KnightActionsTable knightActionsTable = new KnightActionsTable(skin, backAction);
                    GuideTable.this.add(knightActionsTable).expand().fill();
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

        propertiesBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.playClick();
                Runnable backAction = () -> fadeAndSwitchTable(() -> {
                    showGuide();
                    GuideTable.this.pad(40).top();
                    GuideTable.this.defaults().pad(10);
                });

                fadeAndSwitchTable(() -> {
                    GuideTable.this.pad(0);
                    GuideTable.this.defaults().pad(0);
                    KnightPropertiesTable propertiesTable = new KnightPropertiesTable(skin, backAction);
                    GuideTable.this.add(propertiesTable).expand().fill();
                });

            }
        });

        cheatCodesBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.playClick();
                Runnable backAction = () -> fadeAndSwitchTable(() -> {
                    showGuide();
                    GuideTable.this.pad(40).top();
                    GuideTable.this.defaults().pad(10);
                });

                fadeAndSwitchTable(() -> {
                    GuideTable.this.pad(0);
                    GuideTable.this.defaults().pad(0);
                    CheatCodeTable cheatCodeTable = new CheatCodeTable(skin, backAction);
                    GuideTable.this.add(cheatCodeTable).expand().fill();
                });
            }
        });

        InputListener hoverListener = new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                AudioManager.playHover();
            }
        };

        actionsBtn.addListener(hoverListener);
        propertiesBtn.addListener(hoverListener);
        cheatCodesBtn.addListener(hoverListener);
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
