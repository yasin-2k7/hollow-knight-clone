package com.hollowknight.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.hollowknight.controller.GameController;
import com.hollowknight.model.App;
import com.hollowknight.model.GameSave;
import com.hollowknight.model.Manager;
import com.hollowknight.model.enums.GameState;
import com.hollowknight.model.enums.Texts;
import com.hollowknight.view.game.GameScreen;

public class StartGameMenuScreen extends MenuScreen{
    private Image blackOverlay;

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
                AudioManager.playClick();
            }
        });

        backBtn.addListener(new InputListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                AudioManager.playHover();
            }
        });

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLACK);
        pixmap.fill();
        Texture blackTexture = new Texture(pixmap);
        pixmap.dispose();
        blackOverlay = new Image(blackTexture);
        blackOverlay.setSize(stage.getWidth(), stage.getHeight());
        blackOverlay.getColor().a = 0f;
        blackOverlay.setTouchable(Touchable.disabled);
        stage.addActor(blackOverlay);

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
                newButton = new LoadButton(true, 0, index+1, 0, "");
            }
            else{
                newButton = new LoadButton(false, gameSave.getPlayTime() / 60, index+1, gameSave.getMasks(), gameSave.getLoadButtonBgAddress());
            }
            rootTable.add(newButton);
            Button clear = new TextButton(Texts.CLEAR_SAVE.get(App.getCurrentLanguage()), skin);
            rootTable.add(clear).padLeft(200).row();

            final int finalI = i;
            newButton.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    AudioManager.playClick();
                    AudioManager.fadeOutCurrentMusic();

                    blackOverlay.setTouchable(Touchable.enabled);
                    blackOverlay.addAction(Actions.sequence(
                        Actions.fadeIn(0.5f),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                blackOverlay.remove();
                                GameSave activeSave = (gameSave == null) ? Manager.createNewGame(finalI) : gameSave;
                                TiledMap map = new TmxMapLoader().load(activeSave.getTiledMapAddress());
                                GameController.init(activeSave, map);
                                GameController.setCurrentSaveIndex(finalI);
                                GameController.setGameState(GameState.RUNNING);
                                UiManager.setScreen(GameController.getScreen());
                            }
                        })
                    ));
                }
            });

            clear.addListener(new InputListener(){
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    AudioManager.playHover();
                }
            });

            clear.addListener(new ChangeListener(){
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    App.getSaveSlots()[index] = null;
                    Manager.clearSave(index);
                    rebuildSlots();
                }
            });
        }
    }
}
