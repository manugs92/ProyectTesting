package com.mygdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.MyGdxGameScreen;
import com.mygdx.managers.MyGdxGameScreenManager;

public class StartGame extends MyGdxGameScreen {


    public StartGame(MyGdxGameScreenManager myGdxGameScreenManagerR) { super(myGdxGameScreenManagerR); }

    @Override
    public void show() {
        Skin skin = new Skin(Gdx.files.internal("skin/flat-earth-ui.json"));
        TextField textField = new TextField("Empezar juego.",skin);
        stage.addActor(textField);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void dispose() { super.dispose(); }

}
