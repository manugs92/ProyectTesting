package com.mygdx.view;

import com.badlogic.gdx.Gdx;
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
    private Texture texture,textureCasilla,texturebg,monsterTest,monsterTest2;
    private SpriteBatch batch;
    private Image imageCasilla,imageCasilla2,i3,i4,i5,i6,i7;
    private Image[][] images = new Image[7][9];
    private Table table2 = new Table();
    private float maxWidth;
    private float maxHeight;
    private ScrollPane scrollPane;



    private static Carta selectedCard = new Carta();
    private static int posx = -1;
    private static int posy = -1;


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


        Tablero tablero = new Tablero();
        tablero.setCasilla(0,0, golem,0);
        System.out.println(tablero.getCasilla(0,0).getCriatura().getAtaque());
        tablero.moverCriatura(tablero.getCasilla(0,0).getCriatura(),0,0,2,2);
        System.out.println(tablero.getCasilla(2,2).getCriatura().getAtaque());


        /*GUI de la vista*/
        Skin skin = new Skin(Gdx.files.internal("skin/flat-earth-ui.json"));
        texturebg = new Texture(Gdx.files.internal("backgrounds/bg_scroll.png"));
        texturebg.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        Table scrollTable = new Table();
        Drawable background =  new Drawable() {
            @Override
            public void draw(Batch batch, float x, float y, float width, float height) {
                batch.draw(texturebg,0,0,0,0,(int) stage.getWidth(),(int) stage.getHeight());
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
        Label label = new Label("jaskjdksahkjd b ashkdjhsajkdhsa " +
                "hajkshdkashkdas\n ajsidhoaishsa",skin);
        label.getStyle().fontColor.set(Color.WHITE);
        label.setBounds(0,0,scrollTable.getWidth(),scrollTable.getHeight());
        scrollTable.add(label).expandX().expandY();
        scrollPane = new ScrollPane(scrollTable,skin);
        scrollPane.setColor(Color.BLUE);
        scrollPane.setPosition(0,40);
        scrollPane.setWidth(420);
        scrollPane.setHeight(stage.getHeight());
        scrollPane.setScrollbarsVisible(true);
        scrollPane.setForceScroll(false,true);
        scrollPane.setFadeScrollBars(true);
        stage.addActor(scrollPane);

        //Variables usadas para dibujar.
        batch = new SpriteBatch();
        texture = new Texture(Gdx.files.internal("backgrounds\\bg.jpg"));
        textureCasilla = new Texture(Gdx.files.internal("icons\\casilla48.png"));
        monsterTest = new Texture(Gdx.files.internal("icons\\c1.png"));
        monsterTest2 =  new Texture(Gdx.files.internal("icons\\c2.png"));
        imageCasilla = new Image(monsterTest);
        imageCasilla2 = new Image(monsterTest2);
        i3 = new Image(monsterTest2);
        i4 = new Image(monsterTest2);
        i5  = new Image(monsterTest2);
        i6  = new Image(monsterTest2);
        i7  = new Image(monsterTest2);



        int x=tablero.getCasillas().length;
        int y=tablero.getCasillas()[0].length;

        float sx = maxWidth /7;
        float sy = maxHeight /9;


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
                       System.out.println("Hola"+ finalX +"-"+ finalY);
                       System.out.println(images[finalX][finalY].getColor());
                       if(selectedCard!=null && tablero.getCasilla(finalX,finalY).getCriatura()==null && images[finalX][finalY].getColor().equals(Color.valueOf("ff00ffff"))) {
                           if(posx!= -1 && posy != -1) {
                               tablero.getCasilla(posx,posy).setCriatura(null);
                           }
                           tablero.setCasilla(finalX,finalY,selectedCard,0);
                           posx=finalX;
                           posy=finalY;
                           imageCasilla.setPosition((maxWidth /3)+(48*finalX),80 + (48*finalY));
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
        table2.row().pad(0, 0, 0, 0);
        stage.addActor(table2);

        imageCasilla.setPosition(0,48);
        imageCasilla2.setPosition(48*1,48);
        i3.setPosition(48*2,48);
        i4.setPosition(48*3,48);
        i5.setPosition(48*4,48);
        i6.setPosition(48*5,48);
        i7.setPosition(48*6,48);
        imageCasilla.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Murk");
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
        stage.addActor(imageCasilla2);
        stage.addActor(i3);
        stage.addActor(i4);
        stage.addActor(i5);
        stage.addActor(i6);
        stage.addActor(i7);
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

        //batch.draw(monsterTest,0,0);
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
        table2.setPosition( (maxWidth / 3) + 54, 150);
        System.out.println(height);
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
