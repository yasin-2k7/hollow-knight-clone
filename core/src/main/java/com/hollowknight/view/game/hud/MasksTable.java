package com.hollowknight.view.game.hud;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.hollowknight.model.Knight;
import com.hollowknight.model.enums.MaskState;

public class MasksTable extends Table {
    private Array<Mask> masks;
    private int lastHealth;

    public MasksTable(Knight knight, Skin skin) {
        this.masks = new Array<>();
        this.lastHealth = knight.getMasks();

        this.left();

        for (int i = 0; i < 5; i++) {
            Mask mask = new Mask(lastHealth > i ? MaskState.FULL : MaskState.EMPTY);
            masks.add(mask);
            this.add(mask).size(100, 120).padRight(1);
        }
    }

    public void updateHealth(int newHealth) {
        if (newHealth < lastHealth) {
            for (int i = newHealth; i < lastHealth; i++) {
                masks.get(i).breaking();
            }
        }
        else if (newHealth > lastHealth) {
            for (int i = lastHealth; i < newHealth; i++) {
                masks.get(i).fill();
            }
        }

        lastHealth = newHealth;
    }
}
