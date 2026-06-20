package com.hollowknight.model;

import com.badlogic.gdx.utils.ObjectMap;
import com.hollowknight.model.enums.GameAction;
import com.hollowknight.model.enums.Language;

public class App {
    private static Language currentLanguage = Language.ENGLISH;
    public static final ObjectMap<GameAction, Integer> bindings = new ObjectMap<>();
    private static GameSave[] saveSlots = new GameSave[4];

    public static void setCurrentLanguage(Language currentLanguage) {
        App.currentLanguage = currentLanguage;
    }

    public static Language getCurrentLanguage() {
        return currentLanguage;
    }

    public static GameSave[] getSaveSlots() {
        return saveSlots;
    }
}
