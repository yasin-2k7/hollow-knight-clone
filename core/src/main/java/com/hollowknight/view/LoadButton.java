package com.hollowknight.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.hollowknight.model.App;
import com.hollowknight.model.enums.Texts;

public class LoadButton extends Button {
    private boolean empty;
    private int playTime;
    private int buttonNum;
    private int masks;
    private Stack stack;

    public LoadButton(boolean empty, int playTime, int buttonNum, int masks) {
        super(new ButtonStyle());

        float buttonWidth = 600f;
        float buttonHeight = 100f;

        Skin skin = GameAssetManager.skin;
        stack = new Stack();
        stack.setFillParent(false);
        Image saveUp = new Image(skin.getDrawable("save_up"));
        Table details = new Table();
        Label saveNum = new Label(Integer.toString(buttonNum) + ".", skin);

        saveUp.setScaling(com.badlogic.gdx.utils.Scaling.stretch);



        Table saveUpTable = new Table();
        saveUpTable.add(saveUp).size(buttonWidth, buttonHeight);

        if (empty){
            Label newGame = new Label(Texts.NEW_GAME.get(App.getCurrentLanguage()), skin);
            stack.add(saveUp);
            details.left().add(saveNum).padLeft(60);
            details.add(newGame).padLeft(40);
            details.add().expandX().fillX();
            stack.add(details);

        }
        else {
            Image bg = new Image(skin.getDrawable("Area_Forgotten Crossroads"));
            bg.setScaling(com.badlogic.gdx.utils.Scaling.stretch);

            Table bgTable = new Table();
            bgTable.bottom().add(bg).bottom().size(400, 80);

            Table hud = new Table();
            hud.left().pad(10).padLeft(120);
            Image soul = new Image(skin.getDrawable("select_game_HUD_0002_health_frame"));
            hud.add(soul).size(90, 60).padTop(10);
            for (int i = 0; i < masks; i++){
                Image mask = new Image(skin.getDrawable("select_game_HUD_0001_health"));
                hud.add(mask).size(30,30).padBottom(5);
            }
            Label playTimeLabel = new Label(Integer.toString(playTime) + "M", skin);
            hud.add(playTimeLabel).right().bottom().expand();
            details.left().add(saveNum).padLeft(60);

            stack.add(bgTable);
            stack.add(saveUpTable);
            stack.add(details);
            stack.add(hud);
        }

        Image over = new Image(skin.getDrawable("selector"));
        over.setVisible(false);
        Table overTable = new Table();
        overTable.add(over).size(600, 80).padTop(20);
        stack.add(overTable);

        this.add(stack).expand().fill().size(buttonWidth, buttonHeight);


        this.addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                over.setVisible(true);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                over.setVisible(false);
            }
        });
    }




}
