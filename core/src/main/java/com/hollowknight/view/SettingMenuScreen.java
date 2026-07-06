package com.hollowknight.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.hollowknight.model.App;
import com.hollowknight.model.enums.Texts;

public class SettingMenuScreen extends MenuScreen{
    @Override
    public void show() {
        super.show();

        Runnable backToMainMenu = () -> fadeAndSwitchScreen(new MainMenuScreen());

        SettingTable settingTable = new SettingTable(skin, backToMainMenu);
        settingTable.setFillParent(true);

        rootStack.addActor(settingTable);
    }

}
