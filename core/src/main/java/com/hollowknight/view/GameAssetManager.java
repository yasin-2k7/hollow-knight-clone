package com.hollowknight.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GameAssetManager {
    public static Skin skin;

    public static void init() {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("ui/myskin.atlas"));
        skin = new Skin(atlas);


        skin.load(Gdx.files.internal("ui/myskin.json"));
    }

    public static void dispose() {
        if (skin != null) {
            skin.dispose();
        }
    }
}
