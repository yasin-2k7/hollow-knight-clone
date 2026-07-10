package com.hollowknight.view.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.hollowknight.model.App;
import com.hollowknight.model.Zote;
import com.hollowknight.model.enums.EnemyState;
import com.hollowknight.model.enums.ZoteState;
import com.hollowknight.view.GameAssetManager;

public class ZoteView extends Group {
    private Texture idleSheet;
    private Texture turnSheet;
    private Texture speakSheet;


    private Zote model;

    private Table promptTable;
    private boolean isPromptShowing = false;


    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> turnAnimation;
    private Animation<TextureRegion> speakAnimation;


    private float stateTime = 0;
    private ZoteState previousState = ZoteState.IDLE;

    public ZoteView(Zote model){
        this.model = model;

        setTransform(true);
        this.setSize(model.getWidth(), model.getHeight());


        int frameW = 349;
        int frameH = 186;



        idleSheet = new Texture("zote/Idle.png");
        TextureRegion[][] frames = TextureRegion.split(idleSheet, frameW, frameH);
        TextureRegion[] idleFrames = new TextureRegion[5];
        System.arraycopy(frames[0], 0, idleFrames, 0, 5);
        idleAnimation = new Animation<>(0.2f, idleFrames);

        turnSheet = new Texture("zote/Turn.png");
        frames = TextureRegion.split(turnSheet, frameW, frameH);
        TextureRegion[] turnFrames = new TextureRegion[2];
        System.arraycopy(frames[0], 0, turnFrames, 0, 2);
        turnAnimation = new Animation<>(0.2f, turnFrames);

        speakSheet = new Texture("zote/Talk.png");
        frames = TextureRegion.split(speakSheet, frameW, frameH);
        TextureRegion[] speakFrames = new TextureRegion[5];
        System.arraycopy(frames[0], 0, speakFrames, 0, 5);
        speakAnimation = new Animation<>(0.3f, speakFrames);

        initInteractionPrompt(GameAssetManager.skin);
    }

    private void initInteractionPrompt(Skin skin) {
        promptTable = new Table();
        promptTable.setBackground(skin.newDrawable("white", new Color(0f, 0f, 0f, 0.8f)));
        promptTable.pad(4*App.getUnitScale(), 8*App.getUnitScale(), 4*App.getUnitScale(), 8*App.getUnitScale());

        Label enterLabel = new Label(" E ", skin, "default");
        enterLabel.setFontScale(App.getUnitScale());
        enterLabel.setAlignment(Align.center);
        promptTable.center().add(enterLabel);
        promptTable.pack();

        promptTable.setVisible(false);
        promptTable.setColor(promptTable.getColor().r, promptTable.getColor().g, promptTable.getColor().b, 0f);


        this.addActor(promptTable);
        float visualCenter = (model.getWidth() / 2f) - model.getwOffset();
        float promptX = visualCenter - (promptTable.getPrefWidth() / 2f);
        promptTable.setPosition(
            promptX,
            this.getHeight() + 10f*App.getUnitScale()
        );
    }

    public void toggleInteractionPrompt(boolean show) {
        if (show == isPromptShowing) return;
        isPromptShowing = show;

        promptTable.clearActions();

        if (show) {
            promptTable.setVisible(true);
            float targetY = this.getHeight() + 10f*App.getUnitScale();
            promptTable.setY(targetY - 5f*App.getUnitScale());
            promptTable.addAction(Actions.parallel(
                Actions.fadeIn(0.2f),
                Actions.moveTo(promptTable.getX(), targetY, 0.2f, Interpolation.sineOut)
            ));
        } else {
            promptTable.addAction(Actions.sequence(
                Actions.parallel(
                    Actions.fadeOut(0.2f),
                    Actions.moveBy(0, -5f, 0.2f, Interpolation.sineIn)
                ),
                Actions.visible(false)
            ));
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        this.setPosition(model.getPosition().x, model.getPosition().y);

        if (model.getState() != previousState) {
            stateTime = 0;
            previousState = model.getState();
        }
        stateTime += delta;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        TextureRegion currentFrame;
        switch (model.getState()) {
            case TURN:
                currentFrame = turnAnimation.getKeyFrame(stateTime, false);
                break;
            case SPEAK:
                currentFrame = speakAnimation.getKeyFrame(stateTime, false);
                break;
            default:
                currentFrame = idleAnimation.getKeyFrame(stateTime, true);
        }

        if (model.isFlipped() && !currentFrame.isFlipX()){
            currentFrame.flip(true, false);
        }
        else if (!model.isFlipped() && currentFrame.isFlipX()){
            currentFrame.flip(true, false);
        }

        applyTransform(batch, computeTransform());

        float renderX = 0 - model.getwOffset();
        float renderY = 0 - model.gethOffsetDown();
        float renderWidth = model.getWidth();
        float renderHeight = model.getHeight();

        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * parentAlpha);

        batch.draw(currentFrame, renderX, renderY, renderWidth, renderHeight);

        drawChildren(batch, parentAlpha);

        resetTransform(batch);
    }

    public void dispose() {
        if (idleSheet != null) idleSheet.dispose();
        if (turnSheet != null) turnSheet.dispose();
        if (speakSheet != null) speakSheet.dispose();
    }
}
