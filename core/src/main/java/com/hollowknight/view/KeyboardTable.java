package com.hollowknight.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.hollowknight.controller.InputManager;
import com.hollowknight.model.App;
import com.hollowknight.model.Manager;
import com.hollowknight.model.enums.GameAction;
import com.hollowknight.model.enums.Texts;

public class KeyboardTable extends Stack {
    private GameAction actionBeingRebound = null;
    private TextButton reset;
    private Table rootTable;
    private Skin skin;

    public KeyboardTable(Skin skin, Runnable backRunnable) {
        rootTable = new Table();
        Table backTable = new Table();
        this.skin = skin;

        TextButton backBtn = new TextButton(Texts.BACK.get(App.getCurrentLanguage()), skin, "default");

        rootTable.pad(40).center().top();
        rootTable.defaults().pad(20);
        reset = new TextButton(Texts.RESET.get(App.getCurrentLanguage()), skin, "default");


        buildKeyboardMenu();

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

        backBtn.addListener(new InputListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                AudioManager.playHover();
            }
        });

        reset.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                InputManager.resetToDefaults();
                Manager.saveConfig();
                buildKeyboardMenu();
                rootTable.invalidateHierarchy();
                AudioManager.playClick();
            }
        });

        reset.addListener(new InputListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                AudioManager.playHover();
            }
        });

    }

    public void buildKeyboardMenu() {
        rootTable.clearChildren();

        Image title_up = new Image(skin.getDrawable("title_up_others"));
        Label title = new Label(Texts.KEYBOARD.get(App.getCurrentLanguage()), skin, "title");

        rootTable.add(title).colspan(4).row();
        rootTable.add(title_up).colspan(4).spaceBottom(20).row();

        int i = 0;
        for (GameAction action : GameAction.values()) {

            Label actionNameLabel = new Label(Texts.GAME_TITLE.ActionToText(action).get(App.getCurrentLanguage()), skin);

            int currentBoundKey = InputManager.getBindings().get(action);

            Button keyButton = createKeyIcon(action, currentBoundKey);


            if (i++ % 2 == 0){
                rootTable.row().padTop(10);
            }

            rootTable.add(actionNameLabel).center().padRight(150).padLeft(150);
            rootTable.add(keyButton).size(48f, 48f).center();
        }


        rootTable.row();
        rootTable.add(reset).pad(10).bottom().colspan(4).expandY();
    }

    public Button createKeyIcon(GameAction action, int keyCode) {
        Button button = new Button(new Button.ButtonStyle());

        if (actionBeingRebound == action) {
            Label promptLabel = new Label("HIT ANY BUTTON", skin);
            promptLabel.setAlignment(Align.center);
            button.add(promptLabel).center();
            return button;
        }

        switch (keyCode) {
            case Input.Keys.UP:
                button.add(new Image(skin.getDrawable("button_skin_0004_square_arrow_u")));
                break;
            case Input.Keys.DOWN:
                button.add(new Image(skin.getDrawable("button_skin_0004_square_arrow_d")));
                break;
            case Input.Keys.LEFT:
                button.add(new Image(skin.getDrawable("button_skin_0004_square_arrow")));
                break;
            case Input.Keys.RIGHT:
                button.add(new Image(skin.getDrawable("button_skin_0004_square_arrow_r")));
                break;
            default:

                Stack keyStack = new Stack();
                String keyText = Input.Keys.toString(keyCode);

                Image background;
                if (keyText.length() > 2) {
                    background = new Image(skin.getDrawable("button_skin_0003_wide_square_key"));
                } else {
                    background = new Image(skin.getDrawable("button_skin_0004_square_key"));
                }

                Label keyLabel = new Label(keyText, skin);
                keyLabel.setAlignment(Align.center);


                keyStack.add(background);
                keyStack.add(keyLabel);
                button.add(keyStack);
                break;
        }

        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                actionBeingRebound = action;
                listenForKeypress();
                buildKeyboardMenu();
            }
        });

        return button;
    }

    private void listenForKeypress() {
        final Stage stage = this.getStage();
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    actionBeingRebound = null;
                    Gdx.input.setInputProcessor(stage);
                    buildKeyboardMenu();
                    return true;
                }

                int previousKeyCode = InputManager.getBindings().get(actionBeingRebound);

                for (GameAction action : InputManager.getBindings().keys()){
                    int a = InputManager.getBindings().get(action);
                    if (action != actionBeingRebound && InputManager.getBindings().get(action).equals(keycode)){
                        InputManager.rebindKey(action, previousKeyCode);
                        break;
                    }
                }

                InputManager.rebindKey(actionBeingRebound, keycode);

                actionBeingRebound = null;
                Gdx.input.setInputProcessor(stage);
                buildKeyboardMenu();
                return true;
            }
        });
    }
}
