package com.hollowknight.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;
import com.hollowknight.controller.InputManager;
import com.hollowknight.model.enums.GameAction;
import com.hollowknight.model.enums.Language;

public class Manager {
    private static final Json json = new Json();

    private static final String CONFIG_FILE = "config.json";
    private static final int MAX_SLOTS = 4;

    private static class SettingsDTO {
        public Language language;
        public ObjectMap<GameAction, Integer> keyBindings;
        public boolean musicOn;
        public boolean sfxOn;
        public float musicVol;
    }

    public static void loadConfig() {
        Json json = new Json();
        FileHandle file = Gdx.files.local(CONFIG_FILE);

        InputManager.resetToDefaults();

        if (file.exists()) {
            SettingsDTO dto = json.fromJson(SettingsDTO.class, file.readString());
            App.setCurrentLanguage(dto.language);
            App.setMusicEnabled(dto.musicOn);
            App.setSfxEnabled(dto.sfxOn);
            App.setMusicVolume(dto.musicVol);
            if (dto.keyBindings != null){
                App.bindings.putAll(dto.keyBindings);
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
        dto.keyBindings = App.bindings;

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
        GameSave newSave = new GameSave(2000f, 250f, 5, 5, 0, "map/map.tmx");
        App.getSaveSlots()[slotIndex] = newSave;
        saveGame(slotIndex, newSave);
        return newSave;
    }

}
