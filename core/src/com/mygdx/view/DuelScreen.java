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
    private float maxWidth;
    private float maxHeight;
    private ScrollPane scrollPane;



    Tablero tablero = new Tablero();
    private static Carta selectedCard = new Carta();
    private static int posx = -1;
    private static int posy = -1;
    int x=tablero.getCasillas().length;
    int y=tablero.getCasillas()[0].length;


    public DuelScreen(ScreenManager screenManagerR) {
        this.screenManager = screenManagerR;
        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        maxHeight = stage.getHeight();
        maxWidth = stage.getWidth();
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
        xd.setNombre("xd");
        xd.setAtaque(2);
        xd.setMovimiento(2);


        tablero.setCasilla(0,0, golem,0);
        System.out.println(tablero.getCasilla(0,0).getCriatura().getAtaque());
        tablero.moverCriatura(tablero.getCasilla(0,0).getCriatura(),0,0,2,2);
        System.out.println(tablero.getCasilla(2,2).getCriatura().getAtaque());


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
                batch.draw(texturebg,0,0,0,0,(int) maxWidth,(int) maxHeight + 200);
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


        //Imagenes de casillas. (Es la función que las dibuja por pantalla)
        for(int x2=0;x2<=x-1;x2++) {
            for(int y2=0;y2<=y-1;y2++) {
                int finalY = y2;
                images[x2][y2] = new Image(textureCasilla);
                images[x2][y2].setPosition(0+(48*x2),0+(48*y2));
                int finalX = x2;
                images[x2][y2].addListener(new ClickListener() {
                   @Override
                   public void clicked(InputEvent event, float x, float y) {
                       if(selectedCard!=null && tablero.getCasilla(finalX,finalY).getCriatura()==null && images[finalX][finalY].getColor().equals(Color.valueOf("ff00ffff"))) {
                           if(posx!= -1 && posy != -1) {
                               tablero.getCasilla(posx,posy).setCriatura(null);
                           }
                           tablero.setCasilla(finalX,finalY,selectedCard,0);
                           posx=finalX;
                           posy=finalY;
                           //table2.setPosition( (width / 3) + 56, (height/5)-5);
                           imageCasilla.setPosition(((maxWidth /3)+56)+(48*finalX),(maxHeight/5) - 5 + (48*finalY));
                           selectedCard=null;
                           Color defaultColor = images[1][1].getColor();
                           Casilla[][] casillas = tablero.getCasillas();
                           for(int i=0;i<=casillas.length-1;i++) {
                               images[i][0].setColor(defaultColor);
                           }
                       }
                       if(tablero.getCasilla(0,0).getCriatura()!= null) {
                          //Comprobamos si la criatura es nuestra, y si es sí, podemos moverla.
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
            //magicasJ1[i].setPosition(0+(96*(i+1)),((maxWidth / 3) + 56) + (48*9));
            if(i==0) {
                magicasJ1[i].setPosition(((maxWidth / 3) + 55) + (48*(i+1)),(maxHeight/5)-53);
            }
            else {
                magicasJ1[i].setPosition(((maxWidth / 3) + 55) + (48*(i+i+1)),(maxHeight/5)-53);
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
                magicasJ2[i].setPosition(((maxWidth / 3) + 55) + (48*(i+1)),(maxHeight/5)+(48*9)-5);
            }
            else {
                magicasJ2[i].setPosition(((maxWidth / 3) + 55) + (48*(i+i+1)),(maxHeight/5)+(48*9)-5);
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
            manoJ1[i].setPosition((maxWidth/ 3) + 56 + (48*i),10);
            stage.addActor(manoJ1[i]);
        }

        //Dibujar manoJ2
        for(int i=0;i<manoJ2.length;i++) {
            manoJ2[i] = new Image(textureCasilla);
            manoJ2[i].setPosition((maxWidth/ 3) + 56 + (48*i),maxHeight-60);
            stage.addActor(manoJ2[i]);
        }

        //Dibujar mazoJ1
        mazoj1 = new Image(textureCasilla);
        mazoj1.setPosition(((maxWidth/ 3) + 56 + (48*7) + 48),50);
        stage.addActor(mazoj1);

        //Dibujar mazoJ2
        mazoj2 = new Image(textureCasilla);
        mazoj2.setPosition(((maxWidth/ 3) + 56 + (48*7) + 48),maxHeight-100);
        stage.addActor(mazoj2);


        imageCasilla.setPosition(maxWidth-100,48);
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
        maxWidth=width;
        maxHeight=height;
        table2.setPosition( (width / 3) + 56, (height/5)-5);
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
