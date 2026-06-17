package com.hollowknight.view;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.hollowknight.model.enums.GameAction;

public class KeyboardSettingMenuScreen extends MenuScreen{
    @Override
    public void show() {
        super.show();

        InputManager.resetToDefaults();
        buildKeyboardMenu();

    }

    public void buildKeyboardMenu() {
        rootTable.clearChildren();

        // Loop through all game actions to create an organized settings menu grid
        for (GameAction action : GameAction.values()) {

            // Grab the current text translated name for the action (e.g., "MOVE LEFT")
            Label actionNameLabel = new Label(action.name().replace("_", " "), skin);

            // Get the active physical key code from your manager
            int currentBoundKey = InputManager.getBindings().get(action);

            // Generate the dynamic graphic icon stack
            Stack keyVisualIcon = createKeyIcon(currentBoundKey);

            // Add them to your menu grid row
            rootTable.row().padTop(15);
            rootTable.add(actionNameLabel).left().padRight(50);

            // Explicitly size the stack cell so your square asset doesn't stretch awkwardly!
            rootTable.add(keyVisualIcon).size(48f, 48f).center();
        }
    }

    public Stack createKeyIcon(int keyCode) {
        Stack keyStack = new Stack();

        // 1. Every key gets the background empty square
        Image backgroundSquare = new Image(skin.getDrawable("button_skin_0004_square_key"));
        keyStack.add(backgroundSquare);

        // 2. Check if the key code matches a direction arrow, otherwise render text
        switch (keyCode) {
            case Input.Keys.UP:
                keyStack.add(new Image(skin.getDrawable("button_skin_0004_square_arrow_u")));
                break;
            case Input.Keys.DOWN:
                keyStack.add(new Image(skin.getDrawable("button_skin_0004_square_arrow_d")));
                break;
            case Input.Keys.LEFT:
                keyStack.add(new Image(skin.getDrawable("button_skin_0004_square_arrow")));
                break;
            case Input.Keys.RIGHT:
                keyStack.add(new Image(skin.getDrawable("button_skin_0004_square_arrow_r")));
                break;
            default:
                // Convert the integer key code to a clean string string (e.g., 29 -> "A")

                String keyText = Input.Keys.toString(keyCode);

//                Image background;
//                if (keyText.length() > 2) {
//                    // Use your wide rectangle asset for long words (Shift, Ctrl, Space, etc.)
//                    background = new Image(skin.getDrawable("empty-rectangle"));
//                } else {
//                    // Stick to the regular square for single letters/numbers (A, W, Z, 1...)
//                    background = new Image(skin.getDrawable("empty-square"));
//                }

                // Create a centered label to lay over the empty square box
                Label keyLabel = new Label(keyText, skin);
                keyLabel.setAlignment(com.badlogic.gdx.utils.Align.center);

                keyStack.add(keyLabel);
                break;
        }

        return keyStack;
    }
}
