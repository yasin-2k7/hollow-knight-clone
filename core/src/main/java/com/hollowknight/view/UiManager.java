package com.hollowknight.view;

import com.badlogic.gdx.Screen;
import com.hollowknight.Main;

public class UiManager {
    private static Main main;

    public static void init(Main main){
        UiManager.main = main;
    }

    public static void setScreen(Screen screen){
        main.setScreen(screen);
    }

    public static MenuScreen getScreen(){
        if (main.getScreen() instanceof MenuScreen abstractScreen){
            return abstractScreen;
        }
        return null;
    }

    public static Main getMain() {
        return main;
    }
}
