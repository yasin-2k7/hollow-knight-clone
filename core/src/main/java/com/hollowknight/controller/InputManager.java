package com.hollowknight.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;
import com.hollowknight.Main;
import com.hollowknight.model.App;
import com.hollowknight.model.Manager;
import com.hollowknight.model.enums.GameAction;
import static com.hollowknight.model.App.bindings;

public class InputManager {

    public static void resetToDefaults(){
        for (GameAction action : GameAction.values()){
            bindings.put(action, action.getDefaultKey());
        }
        Manager.saveConfig();
    }

    public static void rebindKey(GameAction action, int newKeyCode) {
        bindings.put(action, newKeyCode);
        Manager.saveConfig();
    }

    public static ObjectMap<GameAction, Integer> getBindings() {
        return bindings;
    }

    public static boolean isActionPressed(GameAction action) {
        int boundKey = bindings.get(action, action.getDefaultKey());
        return Gdx.input.isKeyPressed(boundKey);
    }
}
