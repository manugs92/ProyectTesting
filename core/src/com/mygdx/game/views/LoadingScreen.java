package com.mygdx.game.views;


import com.badlogic.gdx.Screen;
import com.mygdx.game.MyGdxGame;

/*
* Screen usada para pantallas de carga.
* */
public class LoadingScreen implements Screen {


    private ScreenManager screenManager;

    public LoadingScreen(ScreenManager screenManagerR) {
        this.screenManager=screenManagerR;
    }

    @Override
     public void show() {
        // TODO Auto-generated method stub
        System.out.println("Pantalla de carga.");
    }

    @Override
    public void render(float delta) {
        // TODO Auto-generated method stub
    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub
    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub
    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub
    }
}
