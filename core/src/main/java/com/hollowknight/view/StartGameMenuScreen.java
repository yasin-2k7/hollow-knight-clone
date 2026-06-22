package com.hollowknight.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.hollowknight.model.App;
import com.hollowknight.model.GameSave;
import com.hollowknight.model.enums.Texts;
import com.hollowknight.view.game.GameScreen;

public class StartGameMenuScreen extends MenuScreen{
    @Override
    public void show() {
        super.show();

        rootTable.setFillParent(true);
        rootTable.pad(40).top();
        rootTable.defaults().pad(20);


//        Image title_up = new Image(skin.getDrawable("title_up_others"));
//        Label title = new Label(Texts.SELECT_PROFILE.get(App.getCurrentLanguage()), skin, "title");
//        rootTable.add(title).colspan(2).row();
//        rootTable.add(title_up).colspan(2).spaceBottom(60).row();

        rebuildSlots();


        TextButton backBtn = new TextButton(Texts.BACK.get(App.getCurrentLanguage()), skin, "default");
        Table backTable = new Table();
        backTable.setFillParent(true);
        backTable.pad(40);
        backTable.bottom().left().add(backBtn).pad(10);

        rootStack.add(backTable);

        backBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                fadeAndSwitchScreen(new MainMenuScreen());
            }
        });


    }

    private void rebuildSlots(){
        rootTable.clearChildren();

        Image title_up = new Image(skin.getDrawable("title_up_others"));
        Label title = new Label(Texts.SELECT_PROFILE.get(App.getCurrentLanguage()), skin, "title");
        rootTable.add(title).colspan(2).row();
        rootTable.add(title_up).colspan(2).spaceBottom(60).row();

        for (int i = 0; i < App.getSaveSlots().length; i++){
            final int index = i;
            GameSave gameSave = App.getSaveSlots()[index];
            Button newButton;
            if (gameSave == null){
                newButton = new LoadButton(true, 0, index+1, 0);
            }
            else{
                newButton = new LoadButton(false, gameSave.getPlayTime(), index+1, gameSave.getMasks());
            }
            rootTable.add(newButton);
            Button clear = new TextButton(Texts.CLEAR_SAVE.get(App.getCurrentLanguage()), skin);
            rootTable.add(clear).padLeft(200).row();

            newButton.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    fadeAndSwitchScreen(new GameScreen());
                }
            });

            clear.addListener(new ChangeListener(){
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    App.getSaveSlots()[index] = null;
                    rebuildSlots();
                }
            });
        }
    }
}
