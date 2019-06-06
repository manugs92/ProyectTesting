package gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.mygdx.game.MyGdxGame;

public class BackgroundScroll implements Drawable {

    Texture background;
    Batch batch;
    float x,y;
    int srcX,srcY,width,height;

    public BackgroundScroll(Batch batch,Texture background,float x,float y, int srcX, int srcY, int width, int height) {
        this.batch=batch;
        this.background=background;
        this.x=x;
        this.y=y;
        this.srcX=srcX;
        this.srcY=srcY;
        this.width=width;
        this.height=height;
    }

    @Override
    public void draw(Batch batch, float x2, float y2, float width2, float height2) {
        batch.draw(background,x,y,srcX,srcY,MyGdxGame.SCREEN_WIDTH, MyGdxGame.SCREEN_HEIGHT);

    }

    @Override
    public float getLeftWidth() {
        return 0;
    }

    @Override
    public void setLeftWidth(float leftWidth) {

    }

    @Override
    public float getRightWidth() {
        return 0;
    }

    @Override
    public void setRightWidth(float rightWidth) {

    }

    @Override
    public float getTopHeight() {
        return 0;
    }

    @Override
    public void setTopHeight(float topHeight) {

    }

    @Override
    public float getBottomHeight() {
        return 0;
    }

    @Override
    public void setBottomHeight(float bottomHeight) {

    }

    @Override
    public float getMinWidth() {
        return 0;
    }

    @Override
    public void setMinWidth(float minWidth) {

    }

    @Override
    public float getMinHeight() {
        return 0;
    }

    @Override
    public void setMinHeight(float minHeight) {

    }
}
