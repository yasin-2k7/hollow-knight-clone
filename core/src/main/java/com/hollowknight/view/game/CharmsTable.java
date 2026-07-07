package com.hollowknight.view.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ObjectMap;
import com.hollowknight.model.Knight;
import com.hollowknight.model.enums.Charms;

public class CharmsTable extends Table {
    private final Skin skin;
    private final Knight knight;
    private final Texture glowTexture;

    private final ObjectMap<Charms, Texture> charmTextures = new ObjectMap<>();

    public CharmsTable(Skin skin, Knight knight) {
        this.skin = skin;
        this.knight = knight;
        this.glowTexture = createGlowTexture(64);

        for (Charms charm : Charms.values()) {
            String imgPath = "charms/" + charm.name() + ".png";
            Texture tex = new Texture(Gdx.files.internal(imgPath));
            charmTextures.put(charm, tex);
        }

        this.setFillParent(true);
        this.center();
        rebuildMenu();
    }

    private Texture createGlowTexture(int size) {
        Pixmap pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0.2f, 0.6f, 1f, 0.6f));
        pixmap.fillCircle(size / 2, size / 2, size / 2);
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    public void rebuildMenu() {
        this.clearChildren();

        Label titleLabel = new Label("--- CHARMS INVENTORY ---", skin);
        this.add(titleLabel).padBottom(15).row();

        Label equippedTitle = new Label("Equipped (Max 3):", skin);
        this.add(equippedTitle).padBottom(10).row();

        Table equippedTable = new Table();
        for (int i = 0; i < 3; i++) {
            if (i < knight.getEquippedCharms().size()) {
                final Charms charm = knight.getEquippedCharms().get(i);

                Texture charmTex = charmTextures.get(charm);
                Image charmImage = new Image(charmTex);

                charmImage.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        knight.unequipCharm(charm);
                        rebuildMenu();
                    }
                });
                equippedTable.add(charmImage).size(50, 50).pad(15);
            } else {
                Label emptySlot = new Label("( O )", skin);
                equippedTable.add(emptySlot).pad(10);
            }
        }
        this.add(equippedTable).padBottom(40).row();

        Label inventoryTitle = new Label("Your Unlocked Charms (Click to Equip):", skin);
        this.add(inventoryTitle).padBottom(10).row();

        Table inventoryTable = new Table();
        int columnCount = 0;

        for (final Charms charm : knight.getUnlockedCharms()) {
            Stack charmStack = new Stack();
            boolean isAlreadyEquipped = knight.getEquippedCharms().contains(charm);

            if (isAlreadyEquipped) {
                Image glowImage = new Image(glowTexture);
                charmStack.add(glowImage);
            }

            Texture charmTex = charmTextures.get(charm);
            Image charmImage = new Image(charmTex);
            charmStack.add(charmImage);

            charmStack.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (!knight.getEquippedCharms().contains(charm)) {
                        boolean success = knight.equipCharm(charm);
                        if (success) rebuildMenu();
                    } else {
                        knight.unequipCharm(charm);
                        rebuildMenu();
                    }
                }
            });

            Table charmCell = new Table();

            charmCell.add(charmStack).size(65, 65).row();

            String descText = charm.getDescription();

            Label descLabel = new Label(descText, skin, "default");
            descLabel.setAlignment(Align.center);
            descLabel.setWrap(true);

            charmCell.add(descLabel).width(400).padTop(8).center();
            inventoryTable.add(charmCell).pad(10);

            columnCount++;
            if (columnCount >= 4) {
                inventoryTable.row();
                columnCount = 0;
            }
        }

        this.add(inventoryTable).row();
    }

    public void dispose() {
        if (glowTexture != null) {
            glowTexture.dispose();
        }
        for (Texture tex : charmTextures.values()) {
            if (tex != null) {
                tex.dispose();
            }
        }
        charmTextures.clear();
    }
}
