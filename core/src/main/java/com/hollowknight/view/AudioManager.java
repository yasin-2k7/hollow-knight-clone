package com.hollowknight.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.hollowknight.model.App;

import java.util.Random;

public class AudioManager {
    public static Sound buttonHover;
    public static Sound buttonClick;
    public static Sound swordA;
    public static Sound swordB;
    public static Sound heroFootstepStone;
    public static Sound heroFootstepGrass;
    public static Sound heroDamage;
    public static Sound heroDash;
    public static Sound heroDeath;
    public static Sound heroJump;
    public static Sound heroLand;
    public static Sound heroMantisClaw;
    public static Sound heroWallJump;
    public static Sound heroWallSlide;
    public static Sound enemyDamage;
    public static Sound knightGainSoul;
    public static Sound focusHealthCharge;
    public static Sound focusReady;
    public static Sound focusHealthHeal;
    public static Sound scream;
    public static Sound fireball;
    public static Sound zote1;
    public static Sound zote2;
    public static Sound zote3;
    public static Sound zote4;
    public static Sound zote5;

    public static Sound[] zoteSounds = new Sound[]{zote1, zote2, zote3, zote4, zote5};


    public static Music menuMusic;
    public static Music greenPathActionMusic;
    public static Music greenPathMainMusic;
    public static Music crossroadsActionMusic;
    public static Music crossroadsMainMusic;

    private static Music backgroundMusic;
    private static Music fadingOutMusic;

    private static float fadeOutTimer = 0f;
    private static final float FADE_OUT_DURATION = 0.3f;
    private static boolean isFadingOut = false;
    private static float fadeInTimer = 0f;
    private static final float FADE_IN_DURATION = 0.5f;
    private static boolean isFadingIn = false;

    private static long lastFootstepTime = 0;
    private static final long FOOTSTEP_COOLDOWN = 550;
    private static long wallSlideId = -1;

    private static Sound zoteLastSound;

    private static Random random = new Random();



    public static void load() {
        buttonHover = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/ui_change_selection.wav"));
        buttonClick = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/ui_button_confirm.wav"));
        swordA = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/sword_1.wav"));
        swordB = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/sword_2.wav"));
        heroFootstepStone = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/hero_run_footsteps_stone.wav"));
        heroFootstepGrass = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/hero_run_footsteps_grass.wav"));
        heroDamage = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/hero_damage.wav"));
        heroDash = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/hero_dash.wav"));
        heroDeath = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/hero_death_v2.wav"));
        heroJump = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/hero_jump.wav"));
        heroLand = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/hero_land_soft.wav"));
        heroMantisClaw = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/hero_mantis_claw.wav"));
        heroWallJump = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/hero_wall_jump.wav"));
        heroWallSlide = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/hero_wall_slide.wav"));
        enemyDamage = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/enemy_damage.wav"));
        knightGainSoul = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/soul_pickup_1.wav"));
        focusHealthCharge = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/focus_health_charging.wav"));
        focusReady = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/focus_ready.wav"));
        focusHealthHeal = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/focus_health_heal.wav"));
        scream = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/hero_scream_spell.wav"));
        focusHealthHeal = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/focus_health_heal.wav"));
        fireball = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/hero_fireball.wav"));
        zote1 = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/Zote_01.wav"));
        zote2 = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/Zote_02.wav"));
        zote3 = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/Zote_03.wav"));
        zote4 = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/Zote_04.wav"));
        zote5 = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/Zote_05.wav"));
        zoteSounds = new Sound[]{zote1, zote2, zote3, zote4, zote5};

        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/music/26. Hollow Knight.mp3"));
        greenPathActionMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/music/S5 Green Path Action.wav"));
        greenPathMainMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/music/S5 Green Path Main.wav"));
        crossroadsActionMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/music/S19 Action.wav"));
        crossroadsMainMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/music/S19 Crossroads Main.wav"));
    }

    public static void playHover() { if (App.isSfxEnabled()) buttonHover.play(1f);}
    public static void playClick() { if (App.isSfxEnabled()) buttonClick.play(1f);}
    public static void playSwordA() { if (App.isSfxEnabled()) swordA.play(1f);}
    public static void playSwordB() { if (App.isSfxEnabled()) swordB.play(1f);}
    public static void playHeroDamage() { if (App.isSfxEnabled()) heroDamage.play(1f);}
    public static void playHeroDash() { if (App.isSfxEnabled()) heroDash.play(1f);}
    public static void playHeroDeath() { if (App.isSfxEnabled()) heroDeath.play(1f);}
    public static void playHeroJump() { if (App.isSfxEnabled()) heroJump.play(1f);}
    public static void playHeroLand() { if (App.isSfxEnabled()) heroLand.play(1f);}
    public static void playHeroMantisClaw() { if (App.isSfxEnabled()) heroMantisClaw.play(1f);}
    public static void playHeroWallJump() { if (App.isSfxEnabled()) heroWallJump.play(1f);}
    public static void playScream() { if (App.isSfxEnabled()) scream.play(1f);}
    public static void playFireball() { if (App.isSfxEnabled()) fireball.play(1f);}
    public static void playEnemyDamage() { if (App.isSfxEnabled()) enemyDamage.play(1f);}
    public static void playKnightGainSoul() { if (App.isSfxEnabled()) knightGainSoul.play(1f);}
    public static void playFocusReady() { if (App.isSfxEnabled()) focusReady.play(1f);}
    public static void playFocusHealthCharge() { if (App.isSfxEnabled()) focusHealthCharge.play(1f);}
    public static void playFocusHealthHeal() { if (App.isSfxEnabled()){
        focusHealthHeal.play(1f);
        focusHealthCharge.stop();
    }}

    public static void playZoteSound(){
        Sound zoteSoundToPlay;
        int randomNum = random.nextInt(5);
        if (zoteLastSound != null && zoteSounds[randomNum] == zoteLastSound){
            zoteSoundToPlay = (zoteSounds[(randomNum + random.nextInt(4) + 1) % 5]);
        }
        else{
            zoteSoundToPlay = zoteSounds[randomNum];
        }
        if (App.isSfxEnabled()){
            zoteSoundToPlay.play(1f);
            zoteLastSound = zoteSoundToPlay;
        }
    }



    public static void playStoneFootstep() {
        if (!App.isSfxEnabled()) return;
        long currentTime = TimeUtils.millis();
        if (currentTime - lastFootstepTime < FOOTSTEP_COOLDOWN) {
            return;
        }
        lastFootstepTime = currentTime;
        float randomPitch = MathUtils.random(0.9f, 1.1f);
        heroFootstepStone.play(0.7f, randomPitch, 0f);
    }

    public static void playGrassFootstep() {
        if (!App.isSfxEnabled()) return;
        long currentTime = TimeUtils.millis();
        if (currentTime - lastFootstepTime < FOOTSTEP_COOLDOWN) {
            return;
        }
        lastFootstepTime = currentTime;
        float randomPitch = MathUtils.random(0.99f, 1.01f);
        heroFootstepGrass.play(0.7f, randomPitch, 0f);
    }

    public static void playWallSlide() {
        if (!App.isSfxEnabled()) return;
        if (wallSlideId == -1) {
            wallSlideId = heroWallSlide.loop(0.7f, 1f, 0f);
        }
    }

    public static void stopWallSlide() {
        if (wallSlideId != -1) {
            heroWallSlide.stop(wallSlideId);
            wallSlideId = -1;
        }
    }

    public static void stopFocus(){
        focusHealthCharge.stop();
    }
    public static void stopFireball(){
        fireball.stop();
    }







    public static void update(float delta) {
        handleFadeOut(delta);
        handleFadeIn(delta);
    }

    private static void handleFadeOut(float delta) {
        if (!isFadingOut) return;

        fadeOutTimer += delta;
        float progress = fadeOutTimer / FADE_OUT_DURATION;

        if (progress >= 1f) {
            if (fadingOutMusic != null) {
                fadingOutMusic.stop();
            }
            fadingOutMusic = null;
            isFadingOut = false;
        } else {
            if (fadingOutMusic != null) {
                fadingOutMusic.setVolume((1 - progress) * App.getMusicVolume());
            }
        }
    }

    private static void handleFadeIn(float delta) {
        if (!isFadingIn) return;

        fadeInTimer += delta;
        float progress = fadeInTimer / FADE_IN_DURATION;

        if (progress >= 1f) {
            if (backgroundMusic != null) {
                backgroundMusic.setVolume(App.getMusicVolume());
            }
            isFadingIn = false;
        } else {
            if (backgroundMusic != null) {
                backgroundMusic.setVolume(progress * App.getMusicVolume());
            }
        }
    }

    public static void fadeOutCurrentMusic() {
        if (backgroundMusic != null && backgroundMusic.isPlaying()) {
            fadingOutMusic = backgroundMusic;
            backgroundMusic = null;
            isFadingOut = true;
            fadeOutTimer = 0f;
        }
    }

    public static void fadeInMusic(Music newMusic) {
        if (backgroundMusic == newMusic) return;

        if (!App.isMusicEnabled()) {
            backgroundMusic = newMusic;
            return;
        }

        if (backgroundMusic != null) {
            backgroundMusic.stop();
        }

        backgroundMusic = newMusic;
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0f);
        backgroundMusic.play();

        isFadingIn = true;
        fadeInTimer = 0f;
    }

    public static void stopMusic() {
        if (backgroundMusic != null && backgroundMusic.isPlaying()) {
            backgroundMusic.stop();
        }
        if (fadingOutMusic != null && fadingOutMusic.isPlaying()) {
            fadingOutMusic.stop();
        }
        fadingOutMusic = null;
        isFadingOut = false;
        isFadingIn = false;
    }

    public static void updateMusicBoolean(){
        if (App.isMusicEnabled()){
            if (backgroundMusic != null && !backgroundMusic.isPlaying()) {
                backgroundMusic.setLooping(true);
                backgroundMusic.setVolume(App.getMusicVolume());
                backgroundMusic.play();
            }
        }
        else {
            backgroundMusic.stop();
        }
    }

    public static void updateMusicVolume(){
        backgroundMusic.setVolume(App.getMusicVolume());
    }

    public static void dispose() {
        if (buttonHover != null) buttonHover.dispose();
        if (buttonClick != null) buttonClick.dispose();


        if (menuMusic != null) menuMusic.dispose();
        if (greenPathMainMusic != null) greenPathMainMusic.dispose();
        if (greenPathActionMusic != null) greenPathActionMusic.dispose();
        if (crossroadsMainMusic != null) crossroadsMainMusic.dispose();
        if (crossroadsActionMusic != null) crossroadsActionMusic.dispose();



    }
}
