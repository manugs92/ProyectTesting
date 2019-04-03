package com.mygdx.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.model.Carta;
import com.mygdx.model.Criatura;
import com.mygdx.model.Tablero;

import java.util.ArrayList;



/*
 * Screen inicial para cuando abrimos el juego.
 * */
public class MainScreen implements Screen {

    private ScreenManager screenManager;
    private Stage stage;
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
        this.screenManager = screenManagerR;
        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        maxHeight = stage.getHeight();
        maxWidth = stage.getWidth();
    }

    @Override
    public void show() {
        ArrayList<Carta> cartas = new ArrayList<Carta>();
        Criatura golem = new Criatura();
        golem.setNombre("golem");
        golem.setAtaque(5);
        cartas.add(golem);







        /*GUI de la vista*/
        Skin skin = new Skin(Gdx.files.internal("skin/flat-earth-ui.json"));
        TextButton newGame = new TextButton("Empezar partida", skin);
        TextButton preferences = new TextButton("Preferencias", skin);
        TextButton exit = new TextButton("Salir", skin);

        //Variable usadas para controlar el flujo de movimiento.
        x = 0;
        y = 0;
        i = 0;
        xState = true;
        yState = true;

        //Variables usadas para dibujar.
        batch = new SpriteBatch();
        texture = new Texture(Gdx.files.internal("backgrounds\\bg.jpg"));
        icon = new Texture(Gdx.files.internal("icons\\1.png"));
        icon2 = new Texture(Gdx.files.internal("icons\\2.png"));
        image = new Image(icon);

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

        Button button = new Button(skin);
        button.add("xd");
        button.setColor(255,0,0,255);
        table2.add(button);
        table2.setPosition(100,100);

        //Asignamos la imagen al stage.
        stage.addActor(table);
        stage.addActor(table2);
        stage.addActor(image);
    }


    @Override
    public void render(float delta) {
        /*
         * Dibujamos las texturas (imagenes) y el contenido de la stage.
         * */
        update(delta);
        Gdx.gl.glClearColor(0f, 00, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        batch.begin();
        batch.draw(texture, 0, 0);
        if (i < 10) {
            batch.draw(icon, x, y);
            i++;
        } else {
            batch.draw(icon2, x, y);
            i++;
            if (i == 20) {
                i = 0;
            }
        }

        //image.setPosition(x,y);
        batch.end();
        stage.draw();
    }

    void update(float delta) {
        if (xState) {
            x += 2;
            if (x > maxWidth - 15) {
                xState = false;
            }
        } else {
            x -= 2;
            if (x == 0) {
                xState = true;
            }
        }
        if (yState) {
            y += 2;
            if (y > maxHeight - 30) {
                yState = false;
            }
        } else {
            y -= 2;
            if (y == 0) {
                yState = true;
            }
        }

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
