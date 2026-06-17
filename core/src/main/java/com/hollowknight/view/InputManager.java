package com.hollowknight.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;
import com.hollowknight.Main;
import com.hollowknight.model.enums.GameAction;

public class InputManager {
    private static final ObjectMap<GameAction, Integer> bindings = new ObjectMap<>();

    public static void resetToDefaults(){
        for (GameAction action : GameAction.values()){
            bindings.put(action, action.getDefaultKey());
        }
    }

    public static void rebindKey(GameAction action, int newKeyCode) {
        bindings.put(action, newKeyCode);
    }

    public static ObjectMap<GameAction, Integer> getBindings() {
        return bindings;
    }

    public static boolean isActionPressed(GameAction action) {
        int boundKey = bindings.get(action, action.getDefaultKey());
        return Gdx.input.isKeyPressed(boundKey);
    }
}
