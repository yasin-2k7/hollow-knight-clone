package com.hollowknight.view;

public class GuideMenuScreen extends MenuScreen{
    @Override
    public void show() {
        super.show();

        Runnable backToMainMenu = () -> fadeAndSwitchScreen(new MainMenuScreen());

        GuideTable guideTable = new GuideTable(skin, backToMainMenu);
        guideTable.setFillParent(true);

        rootStack.addActor(guideTable);
    }
}
