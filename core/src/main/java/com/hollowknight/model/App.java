package com.hollowknight.model;

import com.badlogic.gdx.utils.ObjectMap;
import com.hollowknight.model.enums.Achievement;
import com.hollowknight.model.enums.GameAction;
import com.hollowknight.model.enums.Language;

public class App {
    private static Language currentLanguage = Language.ENGLISH;
    public static final ObjectMap<GameAction, Integer> bindings = new ObjectMap<>();
    private static GameSave[] saveSlots = new GameSave[4];
    private static Game currentGame;
    private static float unitScale = 1 / 6f;
    private static boolean musicEnabled = true;
    private static boolean sfxEnabled = true;
    private static float musicVolume = 0.6f;
    public static final ObjectMap<Achievement, Boolean> achievements = new ObjectMap<>();

    public static boolean isMusicEnabled() {
        return musicEnabled;
    }

    public static boolean isSfxEnabled() {
        return sfxEnabled;
    }

    public static float getMusicVolume() {
        return musicVolume;
    }

    public static void setMusicEnabled(boolean musicEnabled) {
        App.musicEnabled = musicEnabled;
    }

    public static void setSfxEnabled(boolean sfxEnabled) {
        App.sfxEnabled = sfxEnabled;
    }

    public static void setMusicVolume(float musicVolume) {
        App.musicVolume = musicVolume;
    }

    public static void setCurrentLanguage(Language currentLanguage) {
        App.currentLanguage = currentLanguage;
    }

    public static Language getCurrentLanguage() {
        return currentLanguage;
    }

    public static GameSave[] getSaveSlots() {
        return saveSlots;
    }

    public static Game getCurrentGame() {
        return currentGame;
    }

    public static void setCurrentGame(Game currentGame) {
        App.currentGame = currentGame;
    }

    public static float getUnitScale() {
        return unitScale;
    }

    public static void updateAchievements(Achievement achievement) {
        achievements.put(achievement, true);
        Manager.saveConfig();
    }
}
