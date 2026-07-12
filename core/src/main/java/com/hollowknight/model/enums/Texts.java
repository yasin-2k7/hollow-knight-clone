package com.hollowknight.model.enums;

import com.badlogic.gdx.Input;
import com.hollowknight.model.App;

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
    ),
    COMPLETION("GAME COMPLETED", "JEU TERMINÉ"),
    COMPLETION_DESC("COMPLETE THE GAME.", "TERMINEZ LE JEU."),
    SPEEDRUN("SPEEDRUN", "SPEEDRUN"),
    SPEEDRUN_DESC("COMPLETE THE GAME IN LESS THAN 15 MINUTES.", "TERMINEZ LE JEU EN MOINS DE 15 MINUTES."),
    TRUE_HUNTER("TRUE HUNTER", "VRAI CHASSEUR"),
    TRUE_HUNTER_DESC("KILL ALL TYPES OF ENEMIES IN THE GAME.", "TUEZ TOUS LES TYPES D'ENNEMIS DU JEU."),
    DEFEAT_FALSE_KNIGHT("DEFEAT FALSE KNIGHT", "VAINCRE LE FAUX CHEVALIER"),
    DEFEAT_FALSE_KNIGHT_DESC("DEFEAT THE FALSE KNIGHT.", "BATTEZ LE FAUX CHEVALIER."),
    ZOTE("ZOTE", "ZOTE"),
    ZOTE_DESC("TALK TO ZOTE.", "PARLEZ À ZOTE."),
    ACHIEVEMENT_UNLOCKED("ACHIEVEMENT UNLOCKED", "SUCCÈS DÉVERROUILLÉ"),
    KNIGHT_PROPERTIES("KNIGHT PROPERTIES", "PROPRIÉTÉS DU CHEVALIER"),
    KNIGHT_ACTIONS("KNIGHT ACTIONS", "ACTIONS DU CHEVALIER"),
    MOVEMENT("MOVEMENT", "MOUVEMENT"),
    MOVE_INSTRUCTIONS("USE THE " + Input.Keys.toString(App.bindings.get(GameAction.MOVE_RIGHT)) + " AND " + Input.Keys.toString(App.bindings.get(GameAction.MOVE_LEFT)) + " BUTTONS TO MOVE RIGHT AND LEFT RESPECTIVELY.",
        "UTILISEZ LES BOUTONS " + Input.Keys.toString(App.bindings.get(GameAction.MOVE_RIGHT)) + " ET " + Input.Keys.toString(App.bindings.get(GameAction.MOVE_LEFT)) + " POUR VOUS DÉPLACER VERS LA DROITE ET LA GAUCHE RESPECTIVEMENT."),

    JUMP_INSTRUCTIONS("USE THE " + Input.Keys.toString(App.bindings.get(GameAction.JUMP)) + " BUTTON TO JUMP. THE LONGER YOU HOLD THE BUTTON, THE HIGHER THE KNIGHT JUMPS.",
        "UTILISEZ LE BOUTON " + Input.Keys.toString(App.bindings.get(GameAction.JUMP)) + " POUR FAIRE SAUTER LE CHEVALIER. PLUS VOUS MAINTENEZ LE BOUTON ENFONCÉ, PLUS LE CHEVALIER SAUTE HAUT."),

    ATTACK_INSTRUCTIONS("TO ATTACK WITH THE SWORD, PRESS THE " + Input.Keys.toString(App.bindings.get(GameAction.ATTACK)) + " BUTTON. TO CHOOSE THE ATTACK DIRECTION, YOU CAN USE THE " + Input.Keys.toString(App.bindings.get(GameAction.MOVE_UP)) + ", " + Input.Keys.toString(App.bindings.get(GameAction.MOVE_DOWN)) + ", " + Input.Keys.toString(App.bindings.get(GameAction.MOVE_LEFT)) + ", " + Input.Keys.toString(App.bindings.get(GameAction.MOVE_RIGHT)) + " BUTTONS.",
        "POUR ATTAQUER AVEC L'ÉPÉE, APPUYEZ SUR LE BOUTON " + Input.Keys.toString(App.bindings.get(GameAction.ATTACK)) + ". POUR CHOISIR LA DIRECTION DE L'ATTAQUE, VOUS POUVEZ UTILISER LES BOUTONS " + Input.Keys.toString(App.bindings.get(GameAction.MOVE_UP)) + ", " + Input.Keys.toString(App.bindings.get(GameAction.MOVE_DOWN)) + ", " + Input.Keys.toString(App.bindings.get(GameAction.MOVE_LEFT)) + " ET " + Input.Keys.toString(App.bindings.get(GameAction.MOVE_RIGHT)) + "."),

    DASH_INSTRUCTIONS("USING THE " + Input.Keys.toString(App.bindings.get(GameAction.DASH)) + " BUTTON, THE KNIGHT MOVES QUICKLY HORIZONTALLY FOR A SHORT TIME.",
        "EN UTILISANT LE BOUTON " + Input.Keys.toString(App.bindings.get(GameAction.DASH)) + ", LE CHEVALIER SE DÉPLACE RAPIDEMENT ET HORIZONTALEMENT PENDANT UN MOMENT."),

    HEAL_INSTRUCTIONS("USING THE " + Input.Keys.toString(App.bindings.get(GameAction.FOCUS)) + " BUTTON, YOU CAN GAIN ONE LIFE BY SPENDING SOME SOUL.",
        "EN UTILISANT LE BOUTON " + Input.Keys.toString(App.bindings.get(GameAction.FOCUS)) + ", VOUS POUVEZ GAGNER UNE VIE EN DÉPENSANT DE L'ÂME."),
    SPELLS_INSTRUCTIONS("PRESS THE " + Input.Keys.toString(App.bindings.get(GameAction.CAST)) + " BUTTON TO FIRE A VENGEFUL SPIRIT FORWARD. HOLD " + Input.Keys.toString(App.bindings.get(GameAction.MOVE_UP)) + " AND PRESS " + Input.Keys.toString(App.bindings.get(GameAction.CAST)) + " TO RELEASE HOWLING WRAITHS UPWARD.",
        "APPUYEZ SUR LE BOUTON " + Input.Keys.toString(App.bindings.get(GameAction.CAST)) + " POUR LANCER UN ESPRIT VENGEUR EN AVANT. MAINTENEZ " + Input.Keys.toString(App.bindings.get(GameAction.MOVE_UP)) + " ET APPUYEZ SUR " + Input.Keys.toString(App.bindings.get(GameAction.CAST)) + " POUR LIBÉRER DES SPECTRES HURLANTS VERS LE HAUT."),

    SOUL_INSTRUCTIONS("MASKS REPRESENT YOUR HEALTH. STRIKE ENEMIES TO FILL THE SOUL VESSEL, WHICH IS USED TO HEAL OR CAST SPELLS.",
        "LES MASQUES REPRÉSENTENT VOTRE SANTÉ. FRAPPEZ LES ENNEMIS POUR REMPLIR LE VASE D'ÂME, UTILISÉ POUR GUÉRIR OU LANCER DES SORTS."),

    CHARMS_INSTRUCTIONS("CHARMS GRANT UNIQUE ABILITIES. PRESS THE " + Input.Keys.toString(App.bindings.get(GameAction.INVENTORY)) + " BUTTON TO VIEW AND EQUIP THEM.",
        "LES CHARMES ACCORDENT DES CAPACITÉS UNIQUES. APPUYEZ SUR LE BOUTON " + Input.Keys.toString(App.bindings.get(GameAction.INVENTORY)) + " POUR LES VOIR ET LES ÉQUIPER."),
    ZOTE_BOSS_WARNING("A COLOSSAL AND MINDLESS BEAST LURKS JUST AHEAD. IT HEARD THE HEAVY FOOTSTEPS OF MY NAIL, LIFE ENDER, AND HID IN THE SHADOWS OUT OF SHEER TERROR! GO ON, CREATURE OF NO RENOWN, BUT DO NOT EXPECT ME TO SAVE YOU WHEN IT CRUSHES YOUR TINY SHELL. (PRESS ENTER)",
        "UNE BÊTE COLOSSALE ET SANS CERVEAU RÔDE JUSTE DEVANT. ELLE A ENTENDU LES PAS LOURDS DE MON AIGUILLE, LA FIN DE VIE, ET S'EST CACHÉE DANS L'OMBRE PAR PURE TERREUR ! CONTINUE, CRÉATURE SANS RENOMMÉE, MAIS N'ATTENDS PAS QUE JE TE SAUVE QUAND ELLE ÉCRASERA TA PETITE COQUILLE. (APPUYEZ SUR ENTRÉE)"),

    ZOTE_BOAST_1("HMPH! YOU ARE A CREATURE OF NO RENOWN. DO NOT GAZE UPON MY MAJESTIC CLOAK FOR TOO LONG, OR THE SPLENDOR OF ZOTE THE MIGHTY WILL BLIND YOUR RECKLESS EYEBALLS! (PRESS ENTER)",
        "HMPH ! TU ES UNE CRÉATURE SANS RENOMMÉE. NE CONTEMPLE PAS MON MANTEAU MAJESTUEUX TROP LONGTEMPS, OU LA SPLENDEUR DE ZOTE LE PUISSANT AVEUGLERA TES YEUX TEMÉRAIRES ! (APPUYEZ SUR ENTRÉE)"),

    ZOTE_MEDITATION("DO NOT LOOK AT ME WITH SUCH PITY! I AM NOT RESTING BECAUSE I AM TIRED. I AM TACTICALLY MEDITATING TO GIVE THE BEASTS IN THIS AREA A FAIR CHANCE duly TO RUN AWAY FROM ME. (PRESS ENTER)",
        "NE ME REGARDE PAS AVEC TANT DE PITIÉ ! JE NE ME REPOSE PAS PARCE QUE JE SUIS FATIGUÉ. JE MÉDITE TACTIQUEMENT POUR DONNER AUX BÊTES DE CETTE RÉGION UNE CHANCE ÉQUITABLE DE S'ENFUIR. (APPUYEZ SUR ENTRÉE)"),

    ZOTE_ANNOYED("HA! A FEEBLE ATTACK! MY SHELL IS AS HARD AS A GEODE. I MERELY CHOSE TO ABSORB YOUR STRIKE TO TEST YOUR PATHETIC STRENGTH. CONGRATULATIONS, YOU FAILED! (PRESS ENTER)",
        "HA ! UNE ATTAQUE FAIBLE ! MA COQUILLE EST AUSSI DURE QU'UNE GÉODE. J'AI SIMPLEMENT CHOISI D'ABSORBER TON COUP POUR TESTER TA FORCE PATHÉTIQUE. FÉLICITATIONS, TU AS ÉCHOUÉ ! (APPUYEZ SUR ENTRÉE)"),

    ZOTE_DISMISS("WELL, THAT IS QUITE ENOUGH! DO NOT TROUBLE ME ANY FURTHER! (PRESS ENTER)",
        "EH BIEN, CELA SUFFIT ! NE ME DÉRANGE PAS DAVANTAGE ! (APPUYEZ SUR ENTRÉE)"),
    THE_END("THE END", ""),
    DEATH_NUMBER("DEATH NUMBER", ""),
    ENEMY_DEATH_NUMBER("ENEMY DEATH NUMER", ""),
    PLAY_TIME("PLAY TIME", ""),
    RESTART("RESTART THE GAME", "");




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
