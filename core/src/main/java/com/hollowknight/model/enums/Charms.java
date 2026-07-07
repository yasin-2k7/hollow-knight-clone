package com.hollowknight.model.enums;

import com.hollowknight.model.App;
import com.hollowknight.model.Knight;

public enum Charms {
    DASH_MASTER(Texts.DESC_DASH_MASTER){
        @Override
        public void apply(Knight knight) {
            knight.setDashCooldown(0.6f);
        }
        @Override
        public void remove(Knight knight) {
            knight.setDashCooldown(0.8f);
        }
    },
    HEAVY_BLOW(Texts.DESC_HEAVY_BLOW){
        @Override
        public void apply(Knight knight) {
            knight.setEnemyKnockbackSpeed(350f);
        }
        @Override
        public void remove(Knight knight) {
            knight.setEnemyKnockbackSpeed(250f);
        }
    },
    QUICK_FOCUS(Texts.DESC_QUICK_FOCUS){
        @Override
        public void apply(Knight knight) {
            knight.setFocusTime(0.3f);
        }
        @Override
        public void remove(Knight knight) {
            knight.setFocusTime(0.5f);
        }
    },
    QUICK_SLASH(Texts.DESC_QUICK_SLASH){
        @Override
        public void apply(Knight knight) {
            knight.setAttackTime(0.2f);
            knight.setAttackCooldownTime(0.4f);
        }
        @Override
        public void remove(Knight knight) {
            knight.setAttackTime(0.3f);
            knight.setAttackCooldownTime(0.55f);
        }
    },
    SOUL_CATCHER(Texts.DESC_SOUL_CATCHER){
        @Override
        public void apply(Knight knight) {
            knight.setSoulCatchAmount(22);
        }
        @Override
        public void remove(Knight knight) {
            knight.setSoulCatchAmount(11);
        }
    },
    UNBREAKABLE_STRENGTH(Texts.DESC_UNBREAKABLE_STRENGTH){
        @Override
        public void apply(Knight knight) {
            knight.setSlashDamage(3);
        }
        @Override
        public void remove(Knight knight) {
            knight.setSlashDamage(2);
        }
    },
    SHARP_SHADOW(Texts.DESC_SHARP_SHADOW){
        @Override
        public void apply(Knight knight) {
            knight.setHasShadowDash(true);
            knight.setDashTime(0.24f);
        }
        @Override
        public void remove(Knight knight) {
            knight.setHasShadowDash(false);
            knight.setDashTime(0.2f);
        }
    },
    VOID_HEART(Texts.DESC_VOID_HERAT){
        @Override
        public void apply(Knight knight) {
            knight.setSpellUpgraded(true);
        }
        @Override
        public void remove(Knight knight) {
            knight.setSpellUpgraded(false);
        }
    };

    private final Texts desc;
    Charms(Texts desc){
        this.desc = desc;
    }
    public abstract void apply(Knight knight);
    public String getDescription() {
        return desc.get(App.getCurrentLanguage());
    }
    public abstract void remove(Knight knight);
}
