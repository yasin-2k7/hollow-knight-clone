package com.hollowknight.model.enums;

import com.badlogic.gdx.Input;

public enum GameAction {
    MOVE_UP(Input.Keys.UP),
    MOVE_LEFT(Input.Keys.LEFT),
    MOVE_DOWN(Input.Keys.DOWN),
    MOVE_RIGHT(Input.Keys.RIGHT),
    JUMP(Input.Keys.Z),
    ATTACK(Input.Keys.X),
    DASH(Input.Keys.C),
    FOCUS(Input.Keys.A),
    CAST(Input.Keys.F),
    INVENTORY(Input.Keys.I);



    private final int defaultKey;
    GameAction(int defaultKey){
        this.defaultKey = defaultKey;
    }

    public int getDefaultKey() {
        return defaultKey;
    }
}
