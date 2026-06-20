package com.hollowknight.view;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class PointerMenuButton extends Table {

    private TextButton textButton;
    private Image leftPointer;
    private Image rightPointer;

    private Animation<TextureRegion> pointerAnim;
    private float stateTime = 0f;
    private boolean isHovered = false;

    public PointerMenuButton(String text, Skin skin, String styleName, TextureAtlas atlas) {
        // 1. Create the core text button using your Skin Composer style
        this.textButton = new TextButton(text, skin, styleName);

        // 2. Load your pointer frame animation sequence
        pointerAnim = new Animation<>(0.03f,
            atlas.findRegion("main_menu_pointer_anim0000"),
            atlas.findRegion("main_menu_pointer_anim0001"),
            atlas.findRegion("main_menu_pointer_anim0002"),
            atlas.findRegion("main_menu_pointer_anim0003"),
            atlas.findRegion("main_menu_pointer_anim0004"),
            atlas.findRegion("main_menu_pointer_anim0005"),
            atlas.findRegion("main_menu_pointer_anim0006"),
            atlas.findRegion("main_menu_pointer_anim0007"),
            atlas.findRegion("main_menu_pointer_anim0008"),
            atlas.findRegion("main_menu_pointer_anim0009"),
            atlas.findRegion("main_menu_pointer_anim0010")

            );
        pointerAnim.setPlayMode(Animation.PlayMode.NORMAL);

        // 3. Create Scene2D Image actors for the pointers
        // Start them out with the first frame, but we will hide them initially
        TextureRegionDrawable firstFrame = new TextureRegionDrawable(pointerAnim.getKeyFrame(0));

        this.leftPointer = new Image(firstFrame);
        this.rightPointer = new Image(firstFrame);


        rightPointer.setOrigin(rightPointer.getWidth() / 2f, rightPointer.getHeight() / 2f);
        rightPointer.setScaleX(-1f);

        // Hide pointers by default until hovered
        leftPointer.setVisible(false);
        rightPointer.setVisible(false);

        // 4. Arrange them in a clean horizontal grid row
        this.add(leftPointer).padRight(15);
        this.add(textButton);
        this.add(rightPointer).padLeft(15);

        // 5. Add the hover input listener to the whole row container
        this.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor fromActor) {
                isHovered = true;
                leftPointer.setVisible(true);
                rightPointer.setVisible(true);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor toActor) {
                isHovered = false;
                leftPointer.setVisible(false);
                rightPointer.setVisible(false);
                stateTime = 0f;
            }
        });
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (isHovered) {
            stateTime += delta;
            TextureRegion currentFrame = pointerAnim.getKeyFrame(stateTime);

            // Update the graphic region inside both pointer images frame-by-frame
            ((TextureRegionDrawable) leftPointer.getDrawable()).setRegion(currentFrame);
            ((TextureRegionDrawable) rightPointer.getDrawable()).setRegion(currentFrame);
        }
    }

    // Quick helper to easily attach your MVC click routes to the inner text button
    public TextButton getTextButton() {
        return this.textButton;
    }
}
