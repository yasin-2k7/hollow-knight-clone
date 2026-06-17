package com.hollowknight.model.enums;

import com.badlogic.gdx.Input;

public enum GameAction {
    MOVE_UP(Input.Keys.UP),
    MOVE_LEFT(Input.Keys.LEFT),
    MOVE_DOWN(Input.Keys.DOWN),
    MOVE_RIGHT(Input.Keys.RIGHT),
    JUMP(Input.Keys.Z),
    QUICK_MAP(Input.Keys.TAB),
    ATTACK(Input.Keys.X),
    SUPER_DASH(Input.Keys.S),
    DASH(Input.Keys.C),
    DREAM_NAIL(Input.Keys.D),
    FOCUS_AND_CAST(Input.Keys.A),
    QUICK_CAST(Input.Keys.F),
    INVENTORY(Input.Keys.I);



    private final int defaultKey;
    GameAction(int defaultKey){
        this.defaultKey = defaultKey;
    }

    public int getDefaultKey() {
        return defaultKey;
    }
}
