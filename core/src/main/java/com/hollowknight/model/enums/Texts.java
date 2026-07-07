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
    ATTACK("ATTACK", "ATTAQUER"),
    DASH("DASH", "RUÉE"),
    FOCUS("FOCUS", "CONCENTRATION"),
    CAST("CAST", "SORT"),
    INVENTORY("INVENTORY", "INVENTAIRE"),
    SELECT_PROFILE("SELECT PROFILE", "SÉLECTIONNER UN PROFIL"),
    CLEAR_SAVE("CLEAR SAVE", "EFFACER LA SAUVEGRADE"),
    NEW_GAME("NEW GAME", "NOUVELLE PARTIE"),
    PAUSED("PAUSED", "PAUSE"),
    RESUME("RESUME", "REPRENDRE"),
    CHEAT_CODES("CHEAT CODES", "CODES DE TRICHE"),
    SAVE_AND_EXIT("SAVE AND EXIT", "SAUVEGARDER ET QUITTER"),
    BOSS_ARENA_TELEPORT("Boss Arena Teleport", "Téléportation vers l'arène du boss"),
    SPECTATOR_MODE("Spectator Mode", "Mode spectateur"),
    EMERGENCY_HEAL("Emergency Heal", "Soin d'urgence"),
    REFILL_SOUL_VESSEL("Refill Soul Vessel", "Remplir le réceptacle d'âme"),
    GOD_MODE("God Mode", "Mode Dieu"),
    KILL_NEARBY_ENEMIES("Kill Nearby Enemies", "Tuer les ennemis proches"),
    SHIFT("Shift", "La touche Maj"),
    DESC_DASH_MASTER(
        "Allows the bearer to dash more frequently.",
        "Permet de se ruer plus fréquemment."
    ),
    DESC_HEAVY_BLOW(
        "Increases the knockback of the Nail, pushing enemies further away.",
        "Augmente le recul de l'Aiguillon, repoussant les ennemis plus loin."
    ),
    DESC_QUICK_FOCUS(
        "Increases the speed of focusing Soul, allowing for faster healing.",
        "Augmente la vitesse de canalisation de l'Âme pour guérir plus vite."
    ),
    DESC_QUICK_SLASH(
        "Allows the bearer to slash much more rapidly with their Nail.",
        "Permet de frapper beaucoup plus rapidement avec l'Aiguillon."
    ),
    DESC_SOUL_CATCHER(
        "Increases the amount of Soul gained when striking enemies.",
        "Augmente la quantité d'Âme gagnée en frappant les ennemis."
    ),
    DESC_UNBREAKABLE_STRENGTH(
        "Significantly increases the raw damage dealt by the Nail.",
        "Augmente considérablement les dégâts bruts de l'Aiguillon."
    ),
    DESC_SHARP_SHADOW(
        "Turns the shadow dash into a sharp attack that damages enemies.",
        "Transforme la ruée d'ombre en une attaque qui blesse les ennemis."
    ),
    DESC_VOID_HERAT(
        "Unifies the Void under the bearer's will. Void entities become passive.",
        "Unifie le Vide sous la volonté du porteur. Les entités du Vide deviennent passives."
    );




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
