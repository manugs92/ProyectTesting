package com.mygdx.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.model.Jugador;

public class NeedsToDrawAnimation {

    // Constant rows and columns of the sprite sheet
    private static final int FRAME_COLS = 2, FRAME_ROWS = 1;

    // Objects used
    Animation<TextureRegion> handSelection;
    Texture handSelectionSheet;
    SpriteBatch spriteBatch;

    // A variable for tracking elapsed time for the animation
    float stateTime;

    public NeedsToDrawAnimation(SpriteBatch spriteBatch) {
        this.spriteBatch=spriteBatch;

        // Load the sprite sheet as a Texture
        handSelectionSheet= new Texture(Gdx.files.internal("sheets/hand_selection_sheet.png"));

        // Use the split utility method to create a 2D array of TextureRegions. This is
        // possible because this sprite sheet contains frames of equal size and they are
        // all aligned.
        TextureRegion[][] tmp = TextureRegion.split(handSelectionSheet,
                handSelectionSheet.getWidth() / FRAME_COLS,
                handSelectionSheet.getHeight() / FRAME_ROWS);

        // Place the regions into a 1D array in the correct order, starting from the top
        // left, going across first. The Animation constructor requires a 1D array.
        TextureRegion[] walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }

        // Initialize the Animation with the frame interval and array of frames
        handSelection = new Animation<>(0.5f, walkFrames);

        // Instantiate a SpriteBatch for drawing and reset the elapsed animation
        // time to 0
        stateTime = 0f;
    }

    public void render(Jugador player,float delta) {
        stateTime += delta; // Accumulate elapsed animation time
        TextureRegion currentFrame = handSelection.getKeyFrame(stateTime, true);
        if(player.isAvoidToDrawCard()) {
            spriteBatch.draw(currentFrame, player.getMazo().getPositionSelectionHand().x, player.getMazo().getPositionSelectionHand().y);
        }

    }

}
