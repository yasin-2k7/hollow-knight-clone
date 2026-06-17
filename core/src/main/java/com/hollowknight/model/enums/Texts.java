package com.hollowknight.model.enums;

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
    RESET("RESET TO DEFAULTS", "REMETTRE PAR DÉFAUT");


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
}
