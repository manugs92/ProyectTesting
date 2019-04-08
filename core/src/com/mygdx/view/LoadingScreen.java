package com.mygdx.view;


import com.badlogic.gdx.Screen;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.MyGdxGameScreen;

/*
* Screen usada para pantallas de carga.
* */
public class LoadingScreen extends MyGdxGameScreen {

    public LoadingScreen(ScreenManager screenManagerR) {
        super(screenManagerR);
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
}
