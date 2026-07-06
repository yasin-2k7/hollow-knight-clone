package com.hollowknight.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;
import com.hollowknight.model.App;
import com.hollowknight.model.enums.GameAction;
import com.hollowknight.model.enums.Language;

public class GameAssetManager {
    public static Skin skin;

    public static void init() {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("ui/myskin.atlas"));
        skin = new Skin(atlas);
        skin.load(Gdx.files.internal("ui/myskin.json"));
        AudioManager.load();
    }

    public static void dispose() {
        if (skin != null) {
            skin.dispose();
        }
        AudioManager.dispose();
    }
}
