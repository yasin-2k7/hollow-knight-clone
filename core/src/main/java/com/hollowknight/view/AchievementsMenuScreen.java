package com.hollowknight.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.ObjectMap;
import com.hollowknight.model.App;
import com.hollowknight.model.enums.Achievement;
import com.hollowknight.model.enums.Texts;

public class AchievementsMenuScreen extends MenuScreen{
    @Override
    public void show() {
        super.show();

        Table backTable = new Table();

        Image title_up = new Image(skin.getDrawable("title_up_others"));
        Label title = new Label(Texts.ACHIEVEMENTS.get(App.getCurrentLanguage()), skin, "title");
        TextButton backBtn = new TextButton(Texts.BACK.get(App.getCurrentLanguage()), skin, "default");

        rootTable.setFillParent(true);

        rootTable.pad(40).top();
        rootTable.defaults().pad(20);
        rootTable.add(title).colspan(2).row();
        rootTable.add(title_up).colspan(2).spaceBottom(40).row();

        for (ObjectMap.Entry<Achievement, Boolean> achievement : App.achievements){
            Table achievementTable = new Table();
            String imagePath = "achievements/" + achievement.key.name() + ".png";
            Texture iconTexture = new Texture(Gdx.files.internal(imagePath));
            Image achievementIcon = new Image(iconTexture);
            if (achievement.value){
                achievementIcon.setColor(Color.WHITE);
            }
            else{
                achievementIcon.setColor(0.2f, 0.2f, 0.2f, 0.4f);
            }

            achievementTable.left();
            achievementTable.add(achievementIcon).size(64f, 64f).left().padRight(100);
            achievementTable.add(new Label(achievement.key.title.get(App.getCurrentLanguage()), skin, "default")).left().growX().row();
            Label desc = new Label(achievement.key.description.get(App.getCurrentLanguage()), skin, "small");
            achievementTable.add().padRight(100);
            achievementTable.add(desc).padTop(6).left().growX();
            rootTable.add(achievementTable).width(500).row();
        }



        backTable.pad(40);
        backTable.bottom().left().add(backBtn).pad(10);
        rootStack.add(backTable);

        backBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                fadeAndSwitchScreen(new MainMenuScreen());
                AudioManager.playClick();
            }
        });

        backBtn.addListener(new InputListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                AudioManager.playHover();
            }
        });
    }
}
