package com.hollowknight.view;

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
