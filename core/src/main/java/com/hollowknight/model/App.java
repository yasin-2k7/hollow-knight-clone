package com.hollowknight.model;

import com.hollowknight.model.enums.Language;

public class App {
    private static Language currentLanguage = Language.ENGLISH;

    public static void setCurrentLanguage(Language currentLanguage) {
        App.currentLanguage = currentLanguage;
    }

    public static Language getCurrentLanguage() {
        return currentLanguage;
    }
}
