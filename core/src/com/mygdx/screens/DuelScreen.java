package com.mygdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.MyGdxGameScreen;
import com.mygdx.model.*;

import java.util.ArrayList;

/*
 * Screen Mostrada en duelo singlePlayer.
 * */
public class DuelScreen extends MyGdxGameScreen {


    //TODO: Seguir optimizando esta clase, y mover cada cosa a su sitio.
    /*
    * Llevar el DuelScreenLog a otra clase y crearlo desde allí.
    *
    * */

    //Variables back end.
    private Partida partida = new Partida();
    private ArrayList<Carta> manoJ1 = partida.getManoJ1();
    private Tablero tablero = new Tablero(partida);
    private int xTablero=tablero.getCasillas().length;
    private int yTablero=tablero.getCasillas()[0].length;


    private float  widthScreen=MyGdxGame.SCREEN_WIDTH;
    private float  heightScreen=MyGdxGame.SCREEN_HEIGHT;


    //Variables usadas por la GUI
    private Texture textureCasilla, textureBgScroll,textureCard,textureSpriteCard;
    private SpriteBatch batch;
    private Image mazoj1GUI,mazoj2GUI;
    private Image[] casillasMagicasJ1GUI = new Image[3];
    private Image[] casillasmagicasJ2GUI = new Image[3];
    private Image[] cartasManoJ1GUI = new Image[7];
    private Image[] cartasmanoJ2GUI = new Image[7];
    private Table tablatableroGUI = new Table();
    private ScrollPane scrollPane;


    //Atributos de la GUI
    private final int MEDIDA_CASILLA = 48;


    private float posyMagicasJ1 = (heightScreen/5)-53;
    private float posyMagicasJ2 = (heightScreen/5)+(MEDIDA_CASILLA*9)-5;
    private float posyManoJ1 = 10;
    private float posyManoJ2 = heightScreen-60;
    private float posXMazo = (tablero.posXTablero + (MEDIDA_CASILLA*7) + MEDIDA_CASILLA);
    private float posYMazoJ1 = 50;
    private float posYMazoJ2 = heightScreen-100;

    /*
    *     private float posXTablero = 400;
    private float posYTablero = (heightScreen/5)-5;
    private float posyMagicasJ1 = (heightScreen/5)-53;
    private float posyMagicasJ2 = (heightScreen/5)+(MEDIDA_CASILLA*9)-5;
    private float posyManoJ1 = 10;
    private float posyManoJ2 = heightScreen-60;
    private float posXMazo = (posXTablero + (MEDIDA_CASILLA*7) + MEDIDA_CASILLA);
    private float posYMazoJ1 = 50;
    private float posYMazoJ2 = heightScreen-100;
    * */


    public DuelScreen(ScreenManager screenManagerR) {
        super(screenManagerR);

    }

    @Override
    public void show() {

        textureCasilla = tablero.getCasilla(0,0).getTextureCasilla();

        //Debugeamos el stage, para ver como están posicionados los elementos.
        stage.setDebugAll(true);

        textureCard = new Texture("icons\\handled_card.png");
        textureSpriteCard= new Texture("icons\\Spritecard.png");

        Criatura golem = new Criatura();
        golem.setNombre("golem");
        golem.setAtaque(5);
        golem.setMovimiento(1);
        golem.setDefensa(5);
        golem.setAlcance(1);
        golem.setImage(textureCard);
        golem.setSpriteCriatura(textureSpriteCard);
        golem.setLastPosition(-1,-1);
        manoJ1.add(golem);
        Criatura golem2 = new Criatura();
        golem2.setNombre("golema");
        golem2.setAtaque(5);
        golem2.setMovimiento(1);
        golem2.setDefensa(5);
        golem2.setAlcance(1);
        golem2.setImage(textureCard);
        golem2.setSpriteCriatura(textureSpriteCard);
        golem2.setLastPosition(-1,-1);
        manoJ1.add(golem2);
        manoJ1.add(golem);
        manoJ1.add(golem);
        manoJ1.add(golem);
        manoJ1.add(golem);
        manoJ1.add(golem);


        tablero.setCasilla(1,0,new Criatura(),0);


        /*GUI de la vista*/
        Skin skin = new Skin(Gdx.files.internal("skin/flat-earth-ui.json"));
        dibujarScroll(skin);

        //Variables usadas para dibujar.
        batch = new SpriteBatch();
        //textureCasilla = new Texture(Gdx.files.internal("icons\\casilla48.png"));


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
        Gdx.gl.glClearColor(0f, 00, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(cam.combined);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        batch.begin();
        batch.draw(textureBgScreen,0,0);
        batch.end();
        stage.draw();
        batch.begin();
        partida.getCriaturasInvocadas().forEach(criatura -> {
            Texture textureMonster = criatura.getSpriteCriatura();
            Vector2 positionMonster = criatura.getPosition();
            Vector2 positionSquareBoard = tablero.getCasilla((int) positionMonster.x,(int)positionMonster.y).getPositionGUI();
            batch.draw(textureMonster,positionSquareBoard.x,positionSquareBoard.y);
        });
        batch.end();
    }



    @Override
    public void dispose() {
        super.dispose();
        textureBgScreen.dispose();
    }

    private void dibujarScroll(Skin skin) {
        textureBgScroll = new Texture(Gdx.files.internal("backgrounds/bg_scroll.png"));
        textureBgScroll.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        Table scrollTable = new Table();
        scrollTable.setPosition(0,0);
        Drawable background =  new Drawable() {
            @Override public void draw(Batch batch, float x, float y, float width, float height) { batch.draw(textureBgScroll,0,0,0,0,(int) MyGdxGame.SCREEN_WIDTH,(int) MyGdxGame.SCREEN_HEIGHT); }

            @Override public float getLeftWidth() {
                return 0;
            }

            @Override public void setLeftWidth(float leftWidth) { }

            @Override public float getRightWidth() {
                return 0;
            }

            @Override public void setRightWidth(float rightWidth) { }

            @Override public float getTopHeight() {
                return 0;
            }

            @Override public void setTopHeight(float topHeight) { }

            @Override public float getBottomHeight() {
                return 0;
            }

            @Override public void setBottomHeight(float bottomHeight) { }

            @Override public float getMinWidth() {
                return 0;
            }

            @Override public void setMinWidth(float minWidth) { }

            @Override public float getMinHeight() {
                return 0;
            }

            @Override public void setMinHeight(float minHeight) { }
        };
        scrollTable.setBackground(background);

        scrollPane = new ScrollPane(scrollTable,skin);
        scrollPane.setColor(Color.BLUE);
        scrollPane.setPosition(0,0);
        scrollPane.setWidth(640);
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
        tablatableroGUI.setPosition(tablero.posXTablero,tablero.posYTablero);
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
        for(int i=0;i<cartasManoJ1GUI.length;i++) {
            cartasManoJ1GUI[i] = new Image(textureCard);
            cartasManoJ1GUI[i].setPosition(tablero.posXTablero + (MEDIDA_CASILLA*i),posyManoJ1);
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
            cartasmanoJ2GUI[i] = new Image(textureCasilla);
            cartasmanoJ2GUI[i].setPosition(tablero.posXTablero+ (MEDIDA_CASILLA*i),posyManoJ2);
            stage.addActor(cartasmanoJ2GUI[i]);
        }
    }

    private void dibujarMazoJ1(){
        //Dibujar mazoJ1
        mazoj1GUI = new Image(textureCasilla);
        mazoj1GUI.setPosition(posXMazo,posYMazoJ1);
        stage.addActor(mazoj1GUI);
    }


    private void dibujarMazoJ2() {
        //Dibujar mazoJ2
        mazoj2GUI = new Image(textureCasilla);
        mazoj2GUI.setPosition(posXMazo,posYMazoJ2);
        stage.addActor(mazoj2GUI);
    }

    private void dibujarMagicasJ1() {

        //Magicas J1
        for (int i = 0; i < casillasMagicasJ1GUI.length; i++) {
            casillasMagicasJ1GUI[i] = new Image(textureCasilla);
            if (i == 0) {
                casillasMagicasJ1GUI[i].setPosition(tablero.posXTablero + (MEDIDA_CASILLA * (i + 1)), posyMagicasJ1);
            } else {
                casillasMagicasJ1GUI[i].setPosition(tablero.posXTablero + (MEDIDA_CASILLA * (i + i + 1)), posyMagicasJ1);
            }
            final int finali = i;
            casillasMagicasJ1GUI[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    System.out.println("Has pulsado en la casilla mágica " + finali);
                }
            });
            stage.addActor(casillasMagicasJ1GUI[i]);
        }
    }

    private void dibujarMagicasJ2() {
        //Magicas J2
        for (int i = 0; i < casillasmagicasJ2GUI.length; i++) {
            casillasmagicasJ2GUI[i] = new Image(textureCasilla);
            if (i == 0) {
                casillasmagicasJ2GUI[i].setPosition(tablero.posXTablero + (MEDIDA_CASILLA * (i + 1)), posyMagicasJ2);
            } else {
                casillasmagicasJ2GUI[i].setPosition(tablero.posXTablero + (MEDIDA_CASILLA * (i + i + 1)), posyMagicasJ2);
            }
            final int finali = i;
            casillasmagicasJ2GUI[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    System.out.println("Has pulsado en la casilla mágica " + finali);
                }
            });
            stage.addActor(casillasmagicasJ2GUI[i]);
        }
    }
}