package com.hollowknight.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.hollowknight.controller.GameController;
import com.hollowknight.model.enemies.Enemy;
import com.hollowknight.model.enums.*;

import java.util.ArrayList;
import java.util.Arrays;

public class Knight {
    private Game game;
    private int masks;
    private int soul;
    private int slashDamage = 2;
    private int soulCatchAmount = 11;
    private float enemyKnockbackSpeed = 250f;

    private EntityAudioListener audioListener;

    private float width = 250f;
    private float height = 135f;
    private float wOffset = 110f;
    private float hOffsetUp = 45f;
    private float hOffsetDown = 5f;
    private Rectangle bounds;
    private boolean onGround;
    private boolean onFirstJump;
    private boolean wallInTheRight = false;
    private boolean flipped;
    private boolean canDash = true;
    private boolean canAttack = true;
    private boolean useAltSlash = false;
    private boolean noDamage = false;
    private boolean focusStart = false;
    private boolean isPogo = false;
    private boolean isSpellUpgraded = false;
    private boolean hasShadowDash = false;
    private boolean hitSpike = false;
    private boolean isDead = false;

    private float screamTimer = 0f;
    private float screamTime = 0.7f;
    private float fireballCastTimer = 0f;
    private float fireballCastTime = 0.9f;
    private float focusGetTime = 0.6f;
    private float focusStartTime = 0.2f;
    private float focusEndTime = 0.2f;
    private float focusTimer = 0f;
    private float focusTime = 0.5f;
    private float damagedTimer = 0f;
    private float damagedTime = 0.3f;
    private float noDamageTimer = 0f;
    private float noDamageTime = 1f;
    private float dashCooldownTimer = 0f;
    private float dashCooldown = 0.8f;
    private float dashTimer = 0f;
    private float dashTime = 0.2f;
    private float attackCooldownTimer = 0;
    private float attackCooldownTime = 0.55f;
    private float usingAltSlashTimer = 0f;
    private float usingAltSlashTime = 0.8f;
    private float attackTime = 0.3f;
    private float deathTime = 2f;
    private float deathTimer = 0f;

    private ArrayList<Charms> unlockedCharms = new ArrayList<>();
    private ArrayList<Charms> equippedCharms = new ArrayList<>();

    private ArrayList<Enemy> hitEnemiesByDash = new ArrayList<>();

    private KnightState state;
    private float unitScale = 1 / 6f;

    private Vector2 position;
    private Vector2 velocity;
    private Vector2 lastSafePlace;

    private final float MOVE_SPEED = 50f;
    private final float JUMP_SPEED = 125f;

    public Knight(int masks, int soul, float startX, float startY, Game game) {
        position = new Vector2(startX * unitScale, startY * unitScale);
        velocity = new Vector2(0, 0);
        lastSafePlace = new Vector2(position);
        state = KnightState.IDLE;
        onGround = false;
        this.masks = masks;
        this.soul = soul;
        this.game = game;
        bounds = new Rectangle(position.x, position.y, (width - 2*wOffset) * unitScale , (height - hOffsetDown - hOffsetUp) * unitScale);
        unlockedCharms.addAll(Arrays.asList(Charms.values()));
    }

    public void update(float delta){
        if (state == KnightState.DEATH && isDead){
            deathTimer -= delta;
            if (deathTimer <= 0){
                isDead = false;
                GameController.fadeScreen(() -> {
                    position.set(App.getCurrentGame().getMapStartX()*unitScale, App.getCurrentGame().getMapStartY()*unitScale);
                    state = KnightState.IDLE;
                    velocity.set(0, 0);
                    lastSafePlace.set(position);
                    masks = 5;
                    GameController.updateMasks();
                    soul = 0;
                }, 0.5f);
            }
            return;
        }
        boolean wasOnGround = this.onGround;

        if (state == KnightState.FOCUS_START){
            focusTimer -= delta;
            if (focusTimer <= 0){
                state = KnightState.FOCUS;
                audioListener.onAudioEvent(AudioAction.FOCUS_HEALTH_CHARGE);
                focusTimer = focusTime;
            }
            if (velocity.x != 0 || velocity.y != 0 || !Gdx.input.isKeyPressed(App.bindings.get(GameAction.FOCUS))){
                state = KnightState.IDLE;
                audioListener.onAudioEvent(AudioAction.FOCUS_NOT_FINISHED);
                focusStart = false;
            }
        }

        if (state == KnightState.FOCUS){
            focusTimer -= delta;
            if (focusTimer <= 0){
                state = KnightState.FOCUS_GET;

                focusTimer = focusGetTime;
            }
            if (velocity.x != 0 || velocity.y != 0 || !Gdx.input.isKeyPressed(App.bindings.get(GameAction.FOCUS))){
                state = KnightState.IDLE;
                audioListener.onAudioEvent(AudioAction.FOCUS_NOT_FINISHED);
                focusStart = false;
            }
        }

        if (state == KnightState.FOCUS_GET){
            focusTimer -= delta;
            if (focusTimer <= 0){
                state = KnightState.FOCUS_END;
                focusTimer = focusEndTime;
                audioListener.onAudioEvent(AudioAction.FOCUS_HEALTH_HEAL);
            }
            if (velocity.x != 0 || velocity.y != 0 || !Gdx.input.isKeyPressed(App.bindings.get(GameAction.FOCUS))){
                state = KnightState.IDLE;
                audioListener.onAudioEvent(AudioAction.FOCUS_NOT_FINISHED);
                focusStart = false;
            }
        }

        if (state == KnightState.FOCUS_END){
            focusTimer -= delta;
            if (focusTimer <= 0){
                state = KnightState.IDLE;
                soul -= 33;
                masks++;
                GameController.updateMasks();
                focusStart = false;
                audioListener.onAudioEvent(AudioAction.FOCUS_NOT_FINISHED);
            }
            if (velocity.x != 0 || velocity.y != 0 || !Gdx.input.isKeyPressed(App.bindings.get(GameAction.FOCUS))){
                state = KnightState.IDLE;
                audioListener.onAudioEvent(AudioAction.FOCUS_NOT_FINISHED);
                focusStart = false;
            }
        }

        if (state == KnightState.DASH || state == KnightState.SHADOW_DASH){
            dashTimer -= delta;
            if (dashTimer <= 0){
                hitEnemiesByDash.clear();
                dashCooldownTimer = dashCooldown;
                state = onGround ? KnightState.IDLE : KnightState.LANDING;
                velocity.x = 0;
            }
        }

        if (dashCooldownTimer > 0){
            dashCooldownTimer -= delta;
            if (dashCooldownTimer <= 0){
                canDash = true;
            }
        }

        if (attackCooldownTimer > 0){
            attackCooldownTimer -= delta;
            if (attackCooldownTimer <= 0){
                canAttack = true;
                if (state == KnightState.ATTACK_UP || state == KnightState.ATTACK_DOWN || state == KnightState.ATTACK_SLASH || state == KnightState.ATTACK_ALT_SLASH){
                    if (onGround){
                        state = KnightState.IDLE;
                    }
                    else {
                        state = KnightState.LANDING;
                    }
                }
            }
        }

        if (usingAltSlashTimer < 1f){
            usingAltSlashTimer += delta;
            if (usingAltSlashTimer > usingAltSlashTime){
                useAltSlash = false;
            }
        }

        if (state == KnightState.DAMAGED) {
            damagedTimer -= delta;
            velocity.x = MathUtils.lerp(velocity.x, 0, delta * 10f);
            if (damagedTimer <= 0) {
                if (hitSpike){
                    GameController.fadeScreen(() -> {
                        this.position.set(lastSafePlace);
                        this.velocity.set(0, 0);
                        this.hitSpike = false;
                        this.canDash = true;
                        this.canAttack = true;
                    }, 0.2f);
                }
                state = KnightState.IDLE;
                velocity.x = 0;
            }
        }

        if (noDamage){
            noDamageTimer -= delta;
            if (noDamageTimer <= 0){
                noDamage = false;
            }
        }

        if (state == KnightState.FIREBALL_CAST){
            fireballCastTimer -= delta;
            if (fireballCastTimer <= 0){
                state = KnightState.IDLE;
            }
        }

        if (state == KnightState.SCREAM){
            screamTimer -= delta;
            if (screamTimer <= 0){
                state = KnightState.IDLE;
            }
        }

        inputHandle();

        position.x += velocity.x * delta;
        bounds.setPosition(position.x, position.y);

        for (Rectangle rectangle : game.getGrounds()){
            if (Intersector.overlaps(rectangle, bounds)){
                if (velocity.x > 0){
                    position.x = rectangle.x - bounds.width;
                    wallInTheRight = true;
                }
                else if (velocity.x < 0){
                    position.x = rectangle.x + rectangle.width;
                    wallInTheRight = false;
                }
                if (!onGround && velocity.y <= 0) {
                    state = KnightState.ON_WALL;
                    velocity.y /= 2;
                }
                velocity.x = 0;
                bounds.setPosition(position.x, position.y);
            }
        }

        if (state == KnightState.DASH || state == KnightState.SHADOW_DASH) {
            velocity.y = 0;
        }
        else{
            velocity.y += game.getGRAVITY() * delta;
        }

        position.y += velocity.y * delta;
        bounds.setPosition(position.x, position.y);

        onGround = false;

        for (Rectangle rectangle : game.getGrounds()){
            if (Intersector.overlaps(rectangle, bounds)){
                if (velocity.y > 0){
                    position.y = rectangle.y - bounds.height;
                }
                else{
                    position.y = rectangle.y + rectangle.height;
                    onGround = true;
                    if (!wasOnGround){
                        audioListener.onAudioEvent(AudioAction.LAND);
                    }
                }
                velocity.y = 0;
                bounds.setPosition(position.x, position.y);
            }
        }

        if (onGround){
            isPogo = false;
            lastSafePlace.set(position);
        }

        if (onGround && velocity.x != 0){
            audioListener.onAudioEvent(AudioAction.STONE_FOOTSTEP);
        }

        if (state == KnightState.ON_WALL) {
            audioListener.onAudioEvent(AudioAction.WALL_SLIDE);
        } else {
            audioListener.onAudioEvent(AudioAction.WALL_SLIDE_STOP);
        }

    }

    public void inputHandle(){
        boolean jumpJustPressed = Gdx.input.isKeyJustPressed(App.bindings.get(GameAction.JUMP));
        boolean jumpPressed = Gdx.input.isKeyPressed(App.bindings.get(GameAction.JUMP));

        boolean leftJustPressed = Gdx.input.isKeyJustPressed(App.bindings.get(GameAction.MOVE_LEFT));
        boolean rightJustPressed = Gdx.input.isKeyJustPressed(App.bindings.get(GameAction.MOVE_RIGHT));

        boolean leftPressed = Gdx.input.isKeyPressed(App.bindings.get(GameAction.MOVE_LEFT));
        boolean rightPressed = Gdx.input.isKeyPressed(App.bindings.get(GameAction.MOVE_RIGHT));

        if (state == KnightState.SHADOW_DASH || state == KnightState.DASH || state == KnightState.ATTACK_UP || state == KnightState.ATTACK_DOWN || state == KnightState.ATTACK_SLASH || state == KnightState.ATTACK_ALT_SLASH || state == KnightState.DAMAGED || state == KnightState.FIREBALL_CAST || state == KnightState.SCREAM){
            return;
        }

        if (masks < 5 && soul >= 33 && Gdx.input.isKeyPressed(App.bindings.get(GameAction.FOCUS)) && velocity.x == 0 && velocity.y == 0){
            if (!focusStart){
                focusStart = true;
                focusTimer = focusTime;
                audioListener.onAudioEvent(AudioAction.FOCUS_READY);
                state = KnightState.FOCUS_START;
            }
            return;
        }

        if (state == KnightState.ON_WALL){
            boolean wallJump = false;
            onFirstJump = false;
            if (jumpJustPressed && ((wallInTheRight && leftPressed) || (!wallInTheRight && rightPressed))) {
                wallJump = true;
            }
            else if (jumpPressed && ((wallInTheRight && leftJustPressed) || (!wallInTheRight && rightJustPressed))) {
                wallJump = true;
            }

            if (wallJump){
                audioListener.onAudioEvent(AudioAction.WALL_JUMP);
                velocity.y = JUMP_SPEED;
                state = KnightState.WALL_JUMP;
                onFirstJump = true;
                if (wallInTheRight){
                    flipped = false;
                    velocity.x = - 1.5f * MOVE_SPEED;
                }
                else{
                    flipped = true;
                    velocity.x = 1.5f * MOVE_SPEED;
                }
                return;
            }
        }

        if (Gdx.input.isKeyPressed(App.bindings.get(GameAction.DASH))){
            if (canDash){
                audioListener.onAudioEvent(AudioAction.KNIGHT_DASH);
                canDash = false;
                dashTimer = dashTime;
                state = hasShadowDash ? KnightState.SHADOW_DASH : KnightState.DASH;
                velocity.x = (flipped ? 1 : -1 ) * MOVE_SPEED * 3;
                return;
            }
        }

        if (Gdx.input.isKeyJustPressed(App.bindings.get(GameAction.ATTACK)) && canAttack){
            canAttack = false;
            attackCooldownTimer = attackCooldownTime;

            if (Gdx.input.isKeyPressed(App.bindings.get(GameAction.MOVE_UP))){
                state = KnightState.ATTACK_UP;
                audioListener.onAudioEvent(AudioAction.KNIGHT_ATTACK);
            }
            else if (Gdx.input.isKeyPressed(App.bindings.get(GameAction.MOVE_DOWN))){
                if (onGround){
                    if (useAltSlash){
                        state = KnightState.ATTACK_ALT_SLASH;
                        audioListener.onAudioEvent(AudioAction.KNIGHT_ATTACK_ALT);
                    }
                    else {
                        state = KnightState.ATTACK_SLASH;
                        audioListener.onAudioEvent(AudioAction.KNIGHT_ATTACK);
                    }
                    useAltSlash = !useAltSlash;
                }
                else{
                    state = KnightState.ATTACK_DOWN;
                    audioListener.onAudioEvent(AudioAction.KNIGHT_ATTACK);
                }
            }
            else {
                if (useAltSlash){
                    state = KnightState.ATTACK_ALT_SLASH;
                    audioListener.onAudioEvent(AudioAction.KNIGHT_ATTACK_ALT);
                }
                else {
                    state = KnightState.ATTACK_SLASH;
                    audioListener.onAudioEvent(AudioAction.KNIGHT_ATTACK);
                }
                useAltSlash = !useAltSlash;
            }
            GameController.setSlashEffect(this, attackTime);
            return;
        }

        if (Gdx.input.isKeyJustPressed(App.bindings.get(GameAction.CAST)) && !Gdx.input.isKeyPressed(App.bindings.get(GameAction.MOVE_UP))){
            if (soul >= 33){
                soul -= 33;
                audioListener.onAudioEvent(AudioAction.FIREBALL);
                state = KnightState.FIREBALL_CAST;
                fireballCastTimer = fireballCastTime;
                castVengefulSpirit();
                return;
            }
        }

        if (Gdx.input.isKeyJustPressed(App.bindings.get(GameAction.CAST)) && Gdx.input.isKeyPressed(App.bindings.get(GameAction.MOVE_UP))){
            if (soul >= 33){
                soul -= 33;
                audioListener.onAudioEvent(AudioAction.SCREAM);
                state = KnightState.SCREAM;
                screamTimer = screamTime;
                castHowlingWraiths();
                return;
            }
        }

        if (jumpJustPressed) {
            if (onGround) {
                audioListener.onAudioEvent(AudioAction.KNIGHT_JUMP);
                velocity.y = JUMP_SPEED;
                state = KnightState.JUMP;
                onGround = false;
                onFirstJump = true;
                return;
            } else if (onFirstJump) {
                audioListener.onAudioEvent(AudioAction.KNIGHT_JUMP);
                velocity.y = JUMP_SPEED;
                state = KnightState.DOUBLE_JUMP;
                onFirstJump = false;
                return;
            }
        }

        if (Gdx.input.isKeyPressed(App.bindings.get(GameAction.MOVE_RIGHT))){
            flipped = true;
            velocity.x = MOVE_SPEED;
            if (onGround) {
                state = KnightState.RUN;
            }
        }
        else if (Gdx.input.isKeyPressed(App.bindings.get(GameAction.MOVE_LEFT))){
            flipped = false;
            velocity.x = -MOVE_SPEED;
            if (onGround) {
                state = KnightState.RUN;
            }
        }
        else {
            velocity.x = 0;
            if (onGround) {state = KnightState.IDLE;}
        }

        if (!isPogo && !Gdx.input.isKeyPressed(App.bindings.get(GameAction.JUMP)) && velocity.y > 0){
            velocity.y = velocity.y * 0.7f;
        }
    }

    public void takeDamage(int sign){
        takeDamage(sign, false);
    }

    public void takeDamage(int sign, boolean isSpike){
        if (state == KnightState.DEATH) return;
        this.hitSpike = isSpike;
        audioListener.onAudioEvent(AudioAction.KNIGHT_TAKE_DAMAGE);
        velocity.x = 250f * sign;
        masks--;
        if (masks <= 0){
            masks = 0;
            GameController.updateMasks();
            isDead = true;
            state = KnightState.DEATH;
            deathTimer = deathTime;
            return;
        }
        GameController.updateMasks();
        noDamage = true;
        noDamageTimer = noDamageTime;
        state = KnightState.DAMAGED;
        damagedTimer = damagedTime;
        velocity.y = 70f;
    }

    public void pogoJump(){
        audioListener.onAudioEvent(AudioAction.KNIGHT_JUMP);
        velocity.y = JUMP_SPEED;
        state = KnightState.JUMP;
        onGround = false;
        isPogo = true;
        onFirstJump = true;
    }

    public int getMasks() {
        return masks;
    }

    public int getSoul() {
        return soul;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public boolean isFlipped() {
        return flipped;
    }

    public KnightState getState() {
        return state;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getwOffset() {
        return wOffset;
    }

    public float gethOffsetDown() {
        return hOffsetDown;
    }

    public float gethOffsetUp() {
        return hOffsetUp;
    }

    public boolean isNoDamage() {
        return noDamage;
    }

    public void setAudioListener(EntityAudioListener audioListener) {
        this.audioListener = audioListener;
    }

    public void increaseSoul(int amount){
        if (soul < 99){
            float delayInSeconds = 0.5f;
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    audioListener.onAudioEvent(AudioAction.KNIGHT_GAIN_SOUL);
                    soul = Math.min(99, soul+amount);
                }
            }, delayInSeconds);
        }
    }

    private void castVengefulSpirit() {
        float spawnX = flipped ? position.x : position.x + bounds.width;
        float spawnY = position.y + 5f;
        GameController.setSpellEffect(isSpellUpgraded, SpellType.VENGEFUL_SPIRIT, spawnX, spawnY, 20, 10, isSpellUpgraded ? 3 : 2, flipped);
    }

    private void castHowlingWraiths() {
        float areaWidth = 100;
        float areaHeight = 100;
        float spawnX = position.x + bounds.width/2 - areaWidth/2;
        float spawnY = bounds.y;
        GameController.setSpellEffect(isSpellUpgraded, SpellType.HOWLING_WRAITHS, spawnX, spawnY, areaWidth, areaHeight, isSpellUpgraded ? 3 : 2, flipped);
    }

    public boolean equipCharm(Charms charm) {
        if (equippedCharms.size() >= 3) {
            return false;
        }
        if (!equippedCharms.contains(charm)) {
            equippedCharms.add(charm);
            charm.apply(this);
            return true;
        }
        return false;
    }
    public void unequipCharm(Charms charm) {
        if (equippedCharms.contains(charm)) {
            equippedCharms.remove(charm);
            charm.remove(this);
        }
    }

    public ArrayList<Charms> getEquippedCharms() {
        return equippedCharms;
    }

    public ArrayList<Charms> getUnlockedCharms() {
        return unlockedCharms;
    }

    public void setSpellUpgraded(boolean spellUpgraded) {
        isSpellUpgraded = spellUpgraded;
    }

    public void setHasShadowDash(boolean hasShadowDash) {
        this.hasShadowDash = hasShadowDash;
    }

    public void setAttackCooldownTime(float attackCooldownTime) {
        this.attackCooldownTime = attackCooldownTime;
    }

    public void setDashTime(float dashTime) {
        this.dashTime = dashTime;
    }

    public void setDashCooldown(float dashCooldown) {
        this.dashCooldown = dashCooldown;
    }

    public void setFocusTime(float focusTime) {
        this.focusTime = focusTime;
    }

    public void setSlashDamage(int slashDamage) {
        this.slashDamage = slashDamage;
    }

    public int getSlashDamage() {
        return slashDamage;
    }

    public int getSoulCatchAmount() {
        return soulCatchAmount;
    }

    public void setSoulCatchAmount(int soulCatchAmount) {
        this.soulCatchAmount = soulCatchAmount;
    }

    public void setAttackTime(float attackTime) {
        this.attackTime = attackTime;
    }

    public void setEnemyKnockbackSpeed(float enemyKnockbackSpeed) {
        this.enemyKnockbackSpeed = enemyKnockbackSpeed;
    }

    public float getEnemyKnockbackSpeed() {
        return enemyKnockbackSpeed;
    }

    public ArrayList<Enemy> getHitEnemiesByDash() {
        return hitEnemiesByDash;
    }
}
