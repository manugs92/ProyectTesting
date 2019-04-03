package com.mygdx.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.model.Carta;
import com.mygdx.model.Casilla;
import com.mygdx.model.Criatura;
import com.mygdx.model.Tablero;

import java.util.ArrayList;

//TODO: Todas las cartas tendrán un Vector2, representando la posicion x/y.
//TODO: Los listener mientras juegas, ponerlos a casilla y no a imagen. -> ok
//TODO: ArrayList de cartasInvocadas. (Para poderlas animar una vez colocadas en el campo)
//TODO: Mapa de X/Y para saber las marcadas. Y así despues desmarcarlas más rapido.

/*
 * Screen inicial para cuando abrimos el juego.
 * */
public class DuelScreen implements Screen {


    private ScreenManager screenManager;
    private Stage stage;
    private Texture texture,textureCasilla,texturebg,monsterTest;
    private SpriteBatch batch;
    private Image imageCasilla,mazoj1,mazoj2;
    private Image[][] images = new Image[7][9];
    private Image[] magicasJ1 = new Image[3];
    private Image[] magicasJ2 = new Image[3];
    private Image[] manoJ1 = new Image[7];
    private Image[] manoJ2 = new Image[7];
    private Table table2 = new Table();
    private float  widthScreen=ScreenManager.SCREEN_WIDTH;
    private float  heightScreen=ScreenManager.SCREEN_HEIGHT;


    private ScrollPane scrollPane;



    private Tablero tablero = new Tablero();
    private static Carta selectedCard = new Carta();
    private static int posx = -1;
    private static int posy = -1;
    int x=tablero.getCasillas().length;
    int y=tablero.getCasillas()[0].length;

    private final int MEDIDA_CASILLA = 48;

    private float posXTablero = (widthScreen/ 3) + 56;
    private float posYTablero = (heightScreen/5)-5;
    private float posyMagicasJ1 = (heightScreen/5)-53;
    private float posyMagicasJ2 = (heightScreen/5)+(MEDIDA_CASILLA*9)-5;
    private float posyManoJ1 = 10;
    private float posyManoJ2 = heightScreen-60;
    private float posXMazo = (posXTablero + (MEDIDA_CASILLA*7) + MEDIDA_CASILLA);
    private float posYMazoJ1 = 50;
    private float posYMazoJ2 = heightScreen-100;



    public DuelScreen(ScreenManager screenManagerR) {
        this.screenManager = screenManagerR;
        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {

        stage.setDebugAll(true);
        ArrayList<Carta> cartas = new ArrayList<Carta>();
        Criatura golem = new Criatura();
        golem.setNombre("golem");
        golem.setAtaque(5);
        golem.setMovimiento(3);
        cartas.add(golem);

        Criatura xd = new Criatura();
        tablero.setCasilla(1,0,xd,0);


        /*GUI de la vista*/
        Skin skin = new Skin(Gdx.files.internal("skin/flat-earth-ui.json"));
        texturebg = new Texture(Gdx.files.internal("backgrounds/bg_scroll.png"));
        texturebg.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        Table scrollTable = new Table();
        //scrollTable.setHeight(maxHeight);
        scrollTable.setPosition(0,0);
        Drawable background =  new Drawable() {
            @Override
            public void draw(Batch batch, float x, float y, float width, float height) {
                batch.draw(texturebg,0,0,0,0,(int) widthScreen,(int) heightScreen+ 200);
            }

            @Override
            public float getLeftWidth() {
                return 0;
            }

            @Override
            public void setLeftWidth(float leftWidth) {

            }

            @Override
            public float getRightWidth() {
                return 0;
            }

            @Override
            public void setRightWidth(float rightWidth) {

            }

            @Override
            public float getTopHeight() {
                return 0;
            }

            @Override
            public void setTopHeight(float topHeight) {

            }

            @Override
            public float getBottomHeight() {
                return 0;
            }

            @Override
            public void setBottomHeight(float bottomHeight) {

            }

            @Override
            public float getMinWidth() {
                return 0;
            }

            @Override
            public void setMinWidth(float minWidth) {

            }

            @Override
            public float getMinHeight() {
                return 0;
            }

            @Override
            public void setMinHeight(float minHeight) {

            }
        };
        scrollTable.setBackground(background);

        scrollPane = new ScrollPane(scrollTable,skin);
        scrollPane.setColor(Color.BLUE);
        scrollPane.setPosition(0,0);
        scrollPane.setWidth(420);
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

        //Variables usadas para dibujar.
        batch = new SpriteBatch();
        texture = new Texture(Gdx.files.internal("backgrounds\\bg.jpg"));
        textureCasilla = new Texture(Gdx.files.internal("icons\\casilla48.png"));
        monsterTest = new Texture(Gdx.files.internal("icons\\0.png"));
        imageCasilla = new Image(monsterTest);

        table2.setPosition(posXTablero,posYTablero);

        //Imagenes de casillas. (Es la función que las dibuja por pantalla)
        for(int x2=0;x2<=x-1;x2++) {
            for(int y2=0;y2<=y-1;y2++) {
                int finalY = y2;
                images[x2][y2] = new Image(textureCasilla);
                images[x2][y2].setPosition((MEDIDA_CASILLA*x2),MEDIDA_CASILLA*y2);
                int finalX = x2;
                images[x2][y2].addListener(new ClickListener() {
                   @Override
                   public void clicked(InputEvent event, float x, float y) {
                       //Listener cuando no tenemos nada escogido.
                       if(tablero.getCasilla(finalX,finalY).getCriatura()!= null) {
                           System.out.println("xd");
                           Casilla[][] casillas = tablero.getCasillas();
                           for(int i=0;i<=casillas.length-1;i++) {
                               if(tablero.getCasilla(i,0).getCriatura()==null) {
                                   images[i][0].setColor(255,0,255,255);
                               }
                           }
                           selectedCard=golem;
                       }

                       //Listener cuando tenemos una carta escogida.
                       if(selectedCard!=null && images[finalX][finalY].getColor().equals(Color.valueOf("ff00ffff"))) {
                           if(posx!= -1 && posy != -1) {
                               tablero.getCasilla(posx,posy).setCriatura(null);
                           }
                           tablero.setCasilla(finalX,finalY,selectedCard,0);
                           posx=finalX;
                           posy=finalY;
                           imageCasilla.setPosition(posXTablero+(MEDIDA_CASILLA*finalX),posYTablero + (MEDIDA_CASILLA*finalY));
                           selectedCard=null;
                           Color defaultColor = images[1][1].getColor();
                           Casilla[][] casillas = tablero.getCasillas();
                           for(int i=0;i<=casillas.length-1;i++) {
                               images[i][0].setColor(defaultColor);
                           }
                       }
                   }
               });
                table2.addActor(images[x2][y2]);
            }
        }

        stage.addActor(table2);

        //Magicas J1
        for(int i=0;i<magicasJ1.length;i++) {
            magicasJ1[i] = new Image(textureCasilla);
            if(i==0) {
                magicasJ1[i].setPosition(posXTablero + (MEDIDA_CASILLA*(i+1)),posyMagicasJ1);
            }
            else {
                magicasJ1[i].setPosition(posXTablero + (MEDIDA_CASILLA*(i+i+1)),posyMagicasJ1);
            }
            final int finali = i;
            magicasJ1[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    System.out.println("Has pulsado en la casilla mágica "+finali);
                }
            });
            stage.addActor(magicasJ1[i]);
        }

        //Magicas J2
        for(int i=0;i<magicasJ2.length;i++) {
            magicasJ2[i] = new Image(textureCasilla);
            if(i==0) {
                magicasJ2[i].setPosition(posXTablero + (48*(i+1)),posyMagicasJ2);
            }
            else {
                magicasJ2[i].setPosition(posXTablero + (48*(i+i+1)),posyMagicasJ2);
            }
            final int finali = i;
            magicasJ1[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    System.out.println("Has pulsado en la casilla mágica "+finali);
                }
            });
            stage.addActor(magicasJ2[i]);
        }

        //Dibujar manoJ1
        for(int i=0;i<manoJ1.length;i++) {
            manoJ1[i] = new Image(textureCasilla);
            manoJ1[i].setPosition(posXTablero + (MEDIDA_CASILLA*i),posyManoJ1);
            stage.addActor(manoJ1[i]);
        }

        //Dibujar manoJ2
        for(int i=0;i<manoJ2.length;i++) {
            manoJ2[i] = new Image(textureCasilla);
            manoJ2[i].setPosition(posXTablero+ (MEDIDA_CASILLA*i),posyManoJ2);
            stage.addActor(manoJ2[i]);
        }

        //Dibujar mazoJ1
        mazoj1 = new Image(textureCasilla);
        mazoj1.setPosition(posXMazo,posYMazoJ1);
        stage.addActor(mazoj1);

        //Dibujar mazoJ2
        mazoj2 = new Image(textureCasilla);
        mazoj2.setPosition(posXMazo,posYMazoJ2);
        stage.addActor(mazoj2);


        imageCasilla.setPosition(widthScreen-100,48);
        imageCasilla.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Casilla[][] casillas = tablero.getCasillas();
                for(int i=0;i<=casillas.length-1;i++) {
                    if(tablero.getCasilla(i,0).getCriatura()==null) {
                        images[i][0].setColor(255,0,255,255);
                    }
                }
                selectedCard=golem;
        }
        });
        stage.addActor(imageCasilla);
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
        batch.draw(texture,0,0);
        batch.end();
        stage.draw();
    }

    void update(float delta) {
    }

    @Override
    public void resize(int width, int height) {
        /*
         * Seteamos el tamaño de la ventana, y seteamos los elementos en función de su tamaño.
         * */
        stage.getViewport().update(width, height, true);
        //table2.setPosition( (width / 3) + 56, (height/5)-5);
        //table2.setPosition(posXTablero,posYTablero);
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
