package com.mygdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.MyGdxGameAssetManager;
import com.mygdx.game.MyGdxGameScreen;


public class SummaryScreen extends MyGdxGameScreen {

    Stage stage;
    public SpriteBatch spriteBatch;
    private MyGdxGameAssetManager assetManager=new MyGdxGameAssetManager();
    public OrthographicCamera camera;
    public Viewport viewport;
    private Image boton;
    int puntuacion;
    boolean win;
    int SCENE_HEIGHT=MyGdxGame.SCREEN_WIDTH;
    int SCREEN_WIDTH=MyGdxGame.SCREEN_WIDTH;


    private BitmapFont font = new BitmapFont();


    public SummaryScreen(ScreenManager screenManager) {
        super(screenManager);
        this.puntuacion = puntuacion;
        this.win = win;
    }

    @Override
    public void show() {

        camera = new OrthographicCamera();
        camera.position.set( SCREEN_WIDTH / 2, SCREEN_WIDTH / 2, 0);
        viewport = new FitViewport(MyGdxGame.SCREEN_WIDTH , MyGdxGame.SCREEN_WIDTH , camera);
        viewport.apply();
        spriteBatch = new SpriteBatch();

        stage = new Stage(viewport);

        //table=new Table();

        boton = new Image(new Texture(assetManager.lives));
        //detectar clicks
        Gdx.input.setInputProcessor(stage);
        boton.setPosition((MyGdxGame.SCREEN_WIDTH  / 2) - 24, (MyGdxGame.SCREEN_WIDTH  / 2) - 48);

        stage.addActor(boton);


        boton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                screenManager.changeScreen(screenManager.MAIN_SCREEN);
            }
        });

    }


    @Override
    public void render(float delta) {
        spriteBatch.setProjectionMatrix(camera.combined);
        update(delta);
        Gdx.gl.glClearColor(0f, 0f, 0f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();
    }

    void update(float delta) {

    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        viewport.update(width, height);
    }

}
