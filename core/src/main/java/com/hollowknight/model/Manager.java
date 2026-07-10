package com.hollowknight.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;
import com.hollowknight.controller.InputManager;
import com.hollowknight.model.enums.Achievement;
import com.hollowknight.model.enums.GameAction;
import com.hollowknight.model.enums.Language;

public class Manager {
    private static final Json json = new Json();

    private static final String CONFIG_FILE = "config.json";
    private static final int MAX_SLOTS = 4;

    private static class SettingsDTO {
        public Language language;
        public ObjectMap<String, Integer> keyBindings;
        public ObjectMap<String, Boolean> achievements;
        public boolean musicOn;
        public boolean sfxOn;
        public float musicVol;
    }

    public static void loadConfig() {
        Json json = new Json();
        FileHandle file = Gdx.files.local(CONFIG_FILE);

        InputManager.resetToDefaults();
        for (Achievement achievement : Achievement.values()){
            App.achievements.put(achievement, false);
        }

        if (file.exists()) {
            SettingsDTO dto = json.fromJson(SettingsDTO.class, file.readString());
            App.setCurrentLanguage(dto.language);
            App.setMusicEnabled(dto.musicOn);
            App.setSfxEnabled(dto.sfxOn);
            App.setMusicVolume(dto.musicVol);
            if (dto.keyBindings != null) {
                App.bindings.clear();
                for (ObjectMap.Entry<String, Integer> entry : dto.keyBindings.entries()) {
                    GameAction action = GameAction.valueOf(entry.key);
                    App.bindings.put(action, entry.value);
                }
            }
            if (dto.achievements != null) {
                for (ObjectMap.Entry<String, Boolean> entry : dto.achievements.entries()) {
                    Achievement achievement = Achievement.valueOf(entry.key);
                    App.achievements.put(achievement, entry.value);
                }
            }
        } else {
            saveConfig();
        }
    }

    public static void saveConfig() {
        SettingsDTO dto = new SettingsDTO();
        dto.language = App.getCurrentLanguage();
        dto.musicOn = App.isMusicEnabled();
        dto.sfxOn = App.isSfxEnabled();
        dto.musicVol = App.getMusicVolume();

        dto.keyBindings = new ObjectMap<>();
        for (ObjectMap.Entry<GameAction, Integer> entry : App.bindings.entries()) {
            dto.keyBindings.put(entry.key.name(), entry.value);
        }

        dto.achievements = new ObjectMap<>();
        for (ObjectMap.Entry<Achievement, Boolean> entry : App.achievements.entries()) {
            dto.achievements.put(entry.key.name(), entry.value);
        }

        Json json = new Json();
        Gdx.files.local(CONFIG_FILE).writeString(json.prettyPrint(dto), false);
    }



    public static void loadAllAllSlots() {
        for (int i = 0; i < MAX_SLOTS; i++) {
            FileHandle file = Gdx.files.local("saves/save_" + i + ".json");
            App.getSaveSlots()[i] = file.exists() ? json.fromJson(GameSave.class, file.readString()) : null;
        }
    }

    public static void saveGame(int slotIndex, GameSave data) {
        App.getSaveSlots()[slotIndex] = data;
        FileHandle file = Gdx.files.local("saves/save_" + slotIndex + ".json");
        file.writeString(json.prettyPrint(data), false);
    }

    public static GameSave createNewGame(int slotIndex) {
        GameSave newSave = new GameSave(200f, 50f, 5, 5, 0, "map/map2.tmx", 200f, 50f, "Area_Green_Path");
        App.getSaveSlots()[slotIndex] = newSave;
        saveGame(slotIndex, newSave);
        return newSave;
    }

    public static void clearSave(int slotIndex) {
        FileHandle file = Gdx.files.local("saves/save_" + slotIndex + ".json");
        if (file.exists()) {
            file.delete();
        }
    }

}
