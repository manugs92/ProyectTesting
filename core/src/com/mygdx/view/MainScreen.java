package com.mygdx.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.MyGdxGameScreen;
import com.mygdx.model.Carta;
import com.mygdx.model.Criatura;

import java.util.ArrayList;



/*
 * Screen inicial para cuando abrimos el juego.
 * */
public class MainScreen extends MyGdxGameScreen {
    private Texture texture;
    private Texture icon, icon2;
    private SpriteBatch batch;
    private Image image;
    private int x, y, i;
    private boolean xState, yState;
    private Table table = new Table();
    private Table table2 = new Table();
    private float maxWidth;
    private float maxHeight;

    public MainScreen(ScreenManager screenManagerR) {
        super(screenManagerR);

        maxHeight = stage.getHeight();
        maxWidth = stage.getWidth();
    }

    @Override
    public void show() {

        /*GUI de la vista*/
        Skin skin = new Skin(Gdx.files.internal("skin/flat-earth-ui.json"));
        TextButton newGame = new TextButton("Empezar partida", skin);
        TextButton preferences = new TextButton("Preferencias", skin);
        TextButton exit = new TextButton("Salir", skin);


        //Variables usadas para dibujar.
        batch = new SpriteBatch();
        texture = new Texture(Gdx.files.internal("backgrounds\\bg.jpg"));

        //Seteamos el tamaño de los botones.
        newGame.setTransform(true);
        preferences.setTransform(true);
        exit.setTransform(true);
        newGame.setScale(2f);
        preferences.setScale(2f);
        exit.setScale(2f);

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

        //Añadimos el contenido a la tabla.
        table.add(newGame).fillX().uniformX();
        table.row().pad(50, 0, 50, 0);
        table.add(preferences).fillX().uniformX();
        table.row();
        table.add(exit).fillX().uniformX();


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
        table.setPosition((float) (width / 2) - 85, (float) (height / 2) - 50);
        image.setPosition(0, stage.getHeight() - 30);

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
        texture.dispose();
        stage.dispose();
    }
}
