package com.hollowknight.model.enums;

import com.badlogic.gdx.Input;

public enum GameAction {
    MOVE_LEFT(Input.Keys.SHIFT_LEFT),
    MOVE_RIGHT(Input.Keys.RIGHT),
    MOVE_UP(Input.Keys.UP),
    MOVE_DOWN(Input.Keys.DOWN);


    private final int defaultKey;
    GameAction(int defaultKey){
        this.defaultKey = defaultKey;
    }

    public int getDefaultKey() {
        return defaultKey;
    }
}
