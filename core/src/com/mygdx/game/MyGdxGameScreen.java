package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.configuration.SoundsConfiguration;
import com.mygdx.screens.ScreenManager;

public class MyGdxGameScreen implements Screen {
    public ScreenManager screenManager;
    public OrthographicCamera cam;
    public FitViewport fitViewport;
    public Stage stage;
    public Texture textureBgScreen;
    SpriteBatch spriteBatch=new SpriteBatch();
    MyGdxGameAssetManager assetManager;
    SoundsConfiguration sounds;

    public MyGdxGameScreen(ScreenManager screenManager){
        this.screenManager = screenManager;
        this.cam = screenManager.parent.camera;
        this.fitViewport = screenManager.parent.fitViewport;
        this.stage = new Stage(fitViewport);
        Gdx.input.setInputProcessor(stage);
        assetManager=screenManager.asset;
        sounds=screenManager.sounds;


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
