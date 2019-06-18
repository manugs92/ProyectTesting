package com.mygdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.MyGdxGameScreen;
import com.mygdx.managers.MyGdxGameScreenManager;

/*
 * Screen inicial para cuando abrimos el juego.
 * */
public class MainScreen extends MyGdxGameScreen {

    public MainScreen(MyGdxGameScreenManager myGdxGameScreenManagerR) {
        super(myGdxGameScreenManagerR);
    }

    @Override
    public void show() {
        stage.setViewport(myGdxGameScreenManager.fitViewport);

        /*GUI de la vista*/
        TextButton newGame = new TextButton("Empezar partida", myGdxGameScreenManager.skin);
        TextButton preferences = new TextButton("Preferencias", myGdxGameScreenManager.skin);
        TextButton exit = new TextButton("Salir", myGdxGameScreenManager.skin);

        newGame.getLabel().setFontScale(2,2);
        preferences.getLabel().setFontScale(2,2);
        exit.getLabel().setFontScale(2,2);

        //Listeners de los botones.
        newGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) { myGdxGameScreenManager.changeScreen(myGdxGameScreenManager.DUEL_SCREEN); }
        });

        preferences.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) { myGdxGameScreenManager.changeScreen(myGdxGameScreenManager.PREFERENCES_SCREEN); }
        });

        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) { Gdx.app.exit(); }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(false);

        //Añadimos el contenido a la tabla.
        table.add(newGame).fillX().uniformX().size(500,100);
        table.row().pad(20, 0, 20, 0);
        table.add(preferences).fillX().uniformX().size(500,100);
        table.row().pad(20, 0, 20, 0);
        table.add(exit).fillX().uniformX().size(500,100);

        //Asignamos la imagen al stage.
        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        /*
         * Dibujamos las texturas (imagenes) y el contenido de la stage.
         * */

        Gdx.gl.glClearColor(0f, 00, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
