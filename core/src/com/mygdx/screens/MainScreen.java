package com.mygdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.MyGdxGameScreen;




/*
 * Screen inicial para cuando abrimos el juego.
 * */
public class MainScreen extends MyGdxGameScreen {
    private Texture texture;
    private SpriteBatch batch;

    public MainScreen(ScreenManager screenManagerR) {
        super(screenManagerR);
    }

    @Override
    public void show() {

        /*GUI de la vista*/
        Skin skin = new Skin(Gdx.files.internal("skin/flat-earth-ui.json"));
        TextButton newGame = new TextButton("Empezar partida", skin);
        TextButton preferences = new TextButton("Preferencias", skin);
        TextButton exit = new TextButton("Salir", skin);


        newGame.getLabel().setFontScale(2,2);
        preferences.getLabel().setFontScale(2,2);
        exit.getLabel().setFontScale(2,2);


        //Variables usadas para dibujar.
        batch = new SpriteBatch();
        texture = new Texture(Gdx.files.internal("backgrounds\\bg.jpg"));


        //Listeners de los botones.
        preferences.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screenManager.changeScreen(screenManager.PREFERENCES);
            }
        });

        newGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screenManager.changeScreen(screenManager.START_GAME);
            }
        });

        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screenManager.changeScreen(screenManager.DUEL_SCREEN);
            }
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
        batch.begin();
        batch.draw(texture, 0, 0);
        batch.end();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        /*
         * Seteamos el tamaño de la ventana, y seteamos los elementos en función de su tamaño.
         * */
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        texture.dispose();
        stage.dispose();
    }
}