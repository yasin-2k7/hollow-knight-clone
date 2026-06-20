package com.hollowknight.model.enums;

import com.badlogic.gdx.Input;

public enum Texts {
    GAME_TITLE("HOLLOW KNIGHT","HOLLOW KNIGHT"),
    START_GAME("START GAME", "COMMENCER LA PARTIE"),
    SETTINGS("SETTING", "PARAMÈTRES"),
    GUIDE("GUIDE", "GUIDE"),
    ACHIEVEMENTS("ACHIEVEMENTS", "SUCCÈS"),
    EXIT("EXIT", "QUITTER"),
    AUDIO("AUDIO", "AUDIO"),
    KEYBOARD("KEYBOARD", "CLAVIER"),
    LANGUAGE("LANGUAGE", "LANGUE"),
    BACK("BACK", "RETOUR"),
    AUDIO_SETTINGS("AUDIO SETTING", "PARAMÈTRES AUDIO"),
    MUSIC("MUSIC", "MUSIQUE"),
    SFX("SFX", "EFFETS SONORES"),
    MUSIC_VOLUME("MUSIC VOLUME", "VOLUME DE LA MUSIQUE"),
    RESET("RESET TO DEFAULTS", "REMETTRE PAR DÉFAUT"),
    MOVE_UP("MOVE UP", "HAUT"),
    MOVE_LEFT("MOVE LEFT", "GAUCHE"),
    MOVE_DOWN("MOVE DOWN", "BAS"),
    MOVE_RIGHT("MOVE RIGHT", "DROITE"),
    JUMP("JUMP", "SAUTER"),
    QUICK_MAP("QUICK MAP", "CARTE RAPIDE"),
    ATTACK("ATTACK", "ATTAQUER"),
    SUPER_DASH("SUPER DASH", "SUPER RUÉE"),
    DASH("DASH", "RUÉE"),
    DREAM_NAIL("DREAM NAIL", "AIGUILLON DES RÊVES"),
    FOCUS_AND_CAST("FOCUS / CAST", "CONCENTRATION / SORT"),
    QUICK_CAST("QUICK CAST", "SORT RAPIDE"),
    INVENTORY("INVENTORY", "INVENTAIRE"),
    SELECT_PROFILE("SELECT PROFILE", "SÉLECTIONNER UN PROFIL"),
    CLEAR_SAVE("CLEAR SAVE", "EFFACER LA SAUVEGRADE"),
    NEW_GAME("NEW GAME", "NOUVELLE PARTIE");



    private final String english;
    private final String french;
    Texts(String english, String french){
        this.english =english;
        this.french = french;
    }

    public String get(Language language){
        switch (language){
            case ENGLISH -> {
                return english;
            }
            case FRENCH -> {
                return french;
            }
        }
        return null;
    }

    public Texts ActionToText(GameAction action){
        for (Texts texts : Texts.values()){
            if (action.toString().equals(texts.toString())){
                return texts;
            }
        }
        return null;
    }
}
