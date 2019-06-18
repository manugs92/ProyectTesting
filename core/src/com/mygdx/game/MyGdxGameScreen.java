package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.managers.MyGdxGameConfigurationManager;
import com.mygdx.managers.MyGdxGameAssetManager;
import com.mygdx.managers.MyGdxGameScreenManager;

public class MyGdxGameScreen implements Screen {
    public MyGdxGameScreenManager myGdxGameScreenManager;
    public OrthographicCamera cam;
    public FitViewport fitViewport;
    public Stage stage;
    public Texture textureBgScreen;
    public MyGdxGameAssetManager assetManager;
    public MyGdxGameConfigurationManager myGdxGameConfigurationManager;
    public BitmapFont font;
    public Skin skin;

    public MyGdxGameScreen(MyGdxGameScreenManager myGdxGameScreenManager){
        this.myGdxGameScreenManager = myGdxGameScreenManager;
        this.assetManager= myGdxGameScreenManager.asset;
        this.myGdxGameConfigurationManager = myGdxGameScreenManager.myGdxGameConfigurationManager;
        this.font= myGdxGameScreenManager.font;
        this.cam = myGdxGameScreenManager.parent.camera;
        this.fitViewport = myGdxGameScreenManager.parent.fitViewport;
        this.stage = new Stage(fitViewport);
        Gdx.input.setInputProcessor(stage);
        this.skin=myGdxGameScreenManager.skin;

        textureBgScreen = new Texture(Gdx.files.internal("backgrounds\\bg.png"));
        textureBgScreen.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        stage.addActor(new Image(textureBgScreen));
    }

    @Override
    public void show() { }

    @Override
    public void render(float delta) { }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
        fitViewport.update(width, height);
    }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
