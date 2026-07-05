package com.hollowknight.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.hollowknight.controller.InputManager;
import com.hollowknight.model.App;
import com.hollowknight.model.enums.GameAction;
import com.hollowknight.model.enums.Texts;

public class KeyboardSettingMenuScreen extends MenuScreen{

    private GameAction actionBeingRebound = null;
    TextButton reset;

    @Override
    public void show() {
        super.show();

        TextButton backBtn = new TextButton(Texts.BACK.get(App.getCurrentLanguage()), skin, "default");

        rootTable.setFillParent(true);
        rootTable.pad(40).center().top();
        rootTable.defaults().pad(20);
        reset = new TextButton(Texts.RESET.get(App.getCurrentLanguage()), skin, "default");


        buildKeyboardMenu();


        Table backTable = new Table();


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

        reset.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                InputManager.resetToDefaults();
                buildKeyboardMenu();
                rootTable.invalidateHierarchy();
            }
        });
    }

    public void buildKeyboardMenu() {
        rootTable.clearChildren();

        Image title_up = new Image(skin.getDrawable("title_up_others"));
        Label title = new Label(Texts.KEYBOARD.get(App.getCurrentLanguage()), skin, "title");

        rootTable.add(title).colspan(4).row();
        rootTable.add(title_up).colspan(4).spaceBottom(20).row();

        // Loop through all game actions to create an organized settings menu grid
        int i = 0;
        for (GameAction action : GameAction.values()) {

            // Grab the current text translated name for the action (e.g., "MOVE LEFT")
            Label actionNameLabel = new Label(Texts.GAME_TITLE.ActionToText(action).get(App.getCurrentLanguage()), skin);

            // Get the active physical key code from your manager
            int currentBoundKey = InputManager.getBindings().get(action);

            // Generate the dynamic graphic icon stack
            Button keyButton = createKeyIcon(action, currentBoundKey);


            if (i++ % 2 == 0){
                rootTable.row().padTop(10);
            }

            if (i < 13){
                rootTable.add(actionNameLabel).center().padRight(150).padLeft(150);
                rootTable.add(keyButton).size(48f, 48f).center();
            }
            else {
                rootTable.add(actionNameLabel).right().padRight(150).colspan(2);
                rootTable.add(keyButton).size(48f, 48f).left().padLeft(150).colspan(2);
            }

        }


        rootTable.row();
        rootTable.add(reset).bottom().colspan(4).expandY();
    }

    public Button createKeyIcon(GameAction action, int keyCode) {
        Button button = new Button(new Button.ButtonStyle());

        if (actionBeingRebound == action) {
            Label promptLabel = new Label("HIT ANY BUTTON", skin);
            promptLabel.setAlignment(Align.center);
            button.add(promptLabel).center();
            return button;
        }

        // 2. Check if the key code matches a direction arrow, otherwise render text
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
                // Convert the integer key code to a clean string string (e.g., 29 -> "A")

                Stack keyStack = new Stack();
                String keyText = Input.Keys.toString(keyCode);

                Image background;
                if (keyText.length() > 2) {
                    // Use your wide rectangle asset for long words (Shift, Ctrl, Space, etc.)
                    background = new Image(skin.getDrawable("button_skin_0003_wide_square_key"));
                } else {
                    // Stick to the regular square for single letters/numbers (A, W, Z, 1...)
                    background = new Image(skin.getDrawable("button_skin_0004_square_key"));
                }

                // Create a centered label to lay over the empty square box
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
