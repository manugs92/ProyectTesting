package com.mygdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.MyGdxGameAssetManager;
import com.mygdx.game.MyGdxGameScreen;
import com.mygdx.model.*;
import gui.BackgroundScroll;

import java.util.ArrayList;

/*
 * Screen Mostrada en duelo singlePlayer.
 * */
public class DuelScreen extends MyGdxGameScreen {


    /*TODO: Seguir optimizando esta clase, y mover cada cosa a su sitio.
    * Llevar el DuelScreenLog a otra clase y crearlo desde allí.
    * */

    private MyGdxGameAssetManager assetManager = new MyGdxGameAssetManager();

    //Variables back end.
    private Partida partida = new Partida();
    private ArrayList<Carta> manoJ1 = partida.getManoJ1();
    private Tablero tablero = new Tablero(partida, assetManager);
    private int xTablero=tablero.getCasillas().length;
    private int yTablero=tablero.getCasillas()[0].length;


    //Variables usadas por la GUI
    private Texture textureBgScroll,textureCard,textureSpriteCard;
    private SpriteBatch batch;
    private Image mazoj1GUI,mazoj2GUI;
    private Image[] casillasmagicasJ2GUI = new Image[3];
    private Image[] cartasManoJ1GUI = new Image[7];
    private Image[] cartasmanoJ2GUI = new Image[7];
    private Table tablatableroGUI = new Table();
    private ScrollPane scrollPane;


    //Atributos de la GUI
    private final int MEDIDA_CASILLA = 48;
    private float posyManoJ1 = 10;
    private float posyManoJ2 = MyGdxGame.SCREEN_HEIGHT-60;
    private float posXMazo = (tablero.POS_X_TABLERO + (MEDIDA_CASILLA*7));
    private float posYMazoJ1 = 87;
    private float posYMazoJ2 = MyGdxGame.SCREEN_HEIGHT-133;


    public DuelScreen(ScreenManager screenManagerR) {
        super(screenManagerR);
    }

    @Override
    public void show() {

        assetManager.loadImagesDuelScreen();
        assetManager.manager.finishLoading();

        //Debugeamos el stage, para ver como están posicionados los elementos.
        stage.setDebugAll(true);

        textureCard = new Texture("icons\\handled_card.png");
        tablero.setCasilla(1,0,new Criatura(),0);

        /*GUI de la vista*/
        Skin skin = new Skin(Gdx.files.internal("skin/flat-earth-ui.json"));
        dibujarScroll(skin);

        //Variables usadas para dibujar.
        batch = new SpriteBatch();

        dibujarTablero();
        dibujarMagicasJ1();
        dibujarMagicasJ2();
        dibujarManoJ1();
        dibujarManoJ2();
        dibujarMazoJ1();
        dibujarMazoJ2();
    }




    @Override
    public void render(float delta) {
        /*
         * Dibujamos las texturas (imagenes) y el contenido de la stage.
         * */
        Gdx.gl.glClearColor(0f, 0f, 0f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(cam.combined);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        batch.begin();
        batch.draw(textureBgScreen,0,0);
        batch.end();

        stage.draw();

        batch.begin();
        for (Criatura criatura : partida.getCriaturasInvocadas()) {
            Texture textureMonster = criatura.getSpriteCriatura();
            Vector2 positionMonster = criatura.getPosition();
            Vector2 positionSquareBoard = tablero.getCasilla((int) positionMonster.x, (int) positionMonster.y).getPositionGUI();
            batch.draw(textureMonster, positionSquareBoard.x, positionSquareBoard.y);
        }
        batch.end();
    }


    @Override
    public void dispose() {
        super.dispose();
        textureBgScreen.dispose();
    }

    private void dibujarScroll(Skin skin) {
        textureBgScroll = assetManager.manager.get(assetManager.backgroundScroll, Texture.class);
        textureBgScroll.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        Table scrollTable = new Table();
        scrollTable.setPosition(0,0);
        BackgroundScroll backgroundScroll = new BackgroundScroll(batch,textureBgScroll,0,0,0,0,MyGdxGame.SCREEN_WIDTH,MyGdxGame.SCREEN_HEIGHT);
        scrollTable.setBackground(backgroundScroll);

        scrollPane = new ScrollPane(scrollTable,skin);
        scrollPane.setColor(Color.BLUE);
        scrollPane.setPosition(0,0);
        scrollPane.setWidth(400);
        scrollPane.setHeight(stage.getHeight());
        scrollPane.setScrollbarsVisible(true);
        scrollPane.setForceScroll(false,true);
        scrollPane.setFadeScrollBars(true);

        for(int i=0;i<99;i++) {
            Label label = new Label(String.valueOf(i),skin);
            label.getStyle().fontColor.set(Color.WHITE);
            scrollTable.add(label).expandX().expandY().fillX().pad(5,5,5,5);
            scrollTable.row();
        }

        stage.addActor(scrollPane);
    }

    public void dibujarTablero() {
        tablatableroGUI.setPosition(tablero.POS_X_TABLERO,tablero.POS_Y_TABLERO);
        //Imagenes de casillas. (Es la función que las dibuja por pantalla)
        for(int x2=0;x2<=xTablero-1;x2++) {
            for(int y2=0;y2<=yTablero-1;y2++) {
                tablatableroGUI.addActor(tablero.getCasilla(x2,y2).getImageCasilla());
            }
        }
        stage.addActor(tablatableroGUI);
    }

    private void dibujarManoJ1() {
        //Dibujar manoJ1
        for(int i=0;i<partida.getManoJ1().size();i++) {
            cartasManoJ1GUI[i] = new Image(textureCard);
            cartasManoJ1GUI[i].setPosition(tablero.POS_X_TABLERO + (MEDIDA_CASILLA*i),posyManoJ1);
            final int finali = i;
            cartasManoJ1GUI[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Casilla[][] casillas = tablero.getCasillas();
                    for(int i=0;i<=casillas.length-1;i++) {
                        if(tablero.getCasilla(i,0).getCriatura()==null) {
                            tablero.getCasilla(i,0).getImageCasilla().setColor(255,0,255,255);
                            tablero.getCasilla(i,0).setState(1);
                        }
                    }
                    partida.setSelectedCard(manoJ1.get(finali));
                }});
            stage.addActor(cartasManoJ1GUI[i]);
        }
    }

    private void dibujarManoJ2() {
        //Dibujar manoJ2
        for(int i=0;i<cartasmanoJ2GUI.length;i++) {
            cartasmanoJ2GUI[i] = new Image(assetManager.manager.get(assetManager.imageSquare, Texture.class));
            cartasmanoJ2GUI[i].setPosition(tablero.POS_X_TABLERO + (MEDIDA_CASILLA*i),posyManoJ2);
            stage.addActor(cartasmanoJ2GUI[i]);
        }
    }

    private void dibujarMazoJ1(){
        //Dibujar mazoJ1
        mazoj1GUI = new Image(assetManager.manager.get(assetManager.imageSquare, Texture.class));
        mazoj1GUI.setPosition(posXMazo,posYMazoJ1);
        stage.addActor(mazoj1GUI);
    }


    private void dibujarMazoJ2() {
        //Dibujar mazoJ2
        mazoj2GUI = new Image(assetManager.manager.get(assetManager.imageSquare, Texture.class));
        mazoj2GUI.setPosition(posXMazo,posYMazoJ2);
        stage.addActor(mazoj2GUI);
    }

    private void dibujarMagicasJ1() {
        //Magicas J1
       for(int i=0;i<=tablero.MAX_MAGIC_CARDS-1;i++) {
            stage.addActor(tablero.getCasillaMagica(0,i).getImageCasilla());
        }
    }

    private void dibujarMagicasJ2() {
        //Magicas J2
        for (int i = 0; i <=tablero.MAX_MAGIC_CARDS-1; i++) {
            stage.addActor(tablero.getCasillaMagica(1,i).getImageCasilla());
        }
    }
}