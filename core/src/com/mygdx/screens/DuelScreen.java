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
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.MyGdxGameAssetManager;
import com.mygdx.game.MyGdxGameScreen;
import com.mygdx.model.*;
import gui.BackgroundScroll;

import java.util.ArrayList;

import static com.mygdx.model.Criatura.TipoElemental.FUEGO;
import static com.mygdx.model.Criatura.TipoEspecie.DRAGON;

/*
 * Screen Mostrada en duelo singlePlayer.
 * */
public class DuelScreen extends MyGdxGameScreen {


    /* TODO: DibujarMano, crearla desde la clase mano, y desde aquí, solo pasar el actor. (Habrá dos manos en partida, J1 y J2)
    *  TODO: DibujarMazo, crear el "shuffle", y coger las cartas de ahí para añadirlas a la mano.
    *  TODO: El mazo, será un gráfico que hay que renderizar en el render continuamente. (Todo lo que se modifique por pantalla estará ahí).
    *  TODO: Método para moverse por el tablero en base a movimiento.
    *  TODO: Método para detectar el alcance de un monstruo. (si puede atacar o no).
    *  TODO: Método para atacar al monstruo.
    *  TODO: Llevar el DuelScreenLog a otra clase y crearlo desde allí.
    *
    * */

    private MyGdxGameAssetManager assetManager = new MyGdxGameAssetManager();

    //Variables temporales (hasta que el jugador se cree desde algun lado)
    Jugador jugador = new Jugador("Manu",0,assetManager);
    Jugador jugador2 = new Jugador("Isma",1,assetManager);

    //Variables back end.
    private Partida partida = new Partida(jugador);
    private Tablero tablero = new Tablero(partida, assetManager);
    private int xTablero=tablero.getCasillas().length;
    private int yTablero=tablero.getCasillas()[0].length;
    private int numCardsInHand;


    //Variables usadas por la GUI
    private Texture textureBgScroll,textureCard,textureSpriteCard;
    private SpriteBatch batch;
    private Image[] cartasManoJ1GUI = new Image[7];
    private Image[] cartasmanoJ2GUI = new Image[7];
    private Table tablatableroGUI = new Table();
    private ScrollPane scrollPane;


    //Atributos de la GUI
    private final int MEDIDA_CASILLA = 48;
    private float posyManoJ1 = 10;
    private float posyManoJ2 = MyGdxGame.SCREEN_HEIGHT-60;


    public DuelScreen(ScreenManager screenManagerR) {
        super(screenManagerR);
    }

    @Override
    public void show() {

        assetManager.loadImagesDuelScreen();
        assetManager.manager.finishLoading();

        partida.addJugador(jugador2);

        //Debugeamos el stage, para ver como están posicionados los elementos.
        stage.setDebugAll(false);

        textureCard = new Texture("icons\\handled_card.png");
        tablero.setCasilla(1,0,new Criatura(DRAGON,FUEGO,textureSpriteCard,7,10,3,1),0);

        /*GUI de la vista*/
        Skin skin = new Skin(Gdx.files.internal("skin/flat-earth-ui.json"));
        dibujarScroll(skin);

        //Variables usadas para dibujar.
        batch = new SpriteBatch();

        dibujarTablero();
        dibujarMagicasJ1();
        dibujarMagicasJ2();
        dibujarManoJ2();
        dibujarMazoJ1();
        dibujarMazoJ2();
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(cam.combined);

        stage.act(Math.min(delta, 1 / 30f));
        batch.begin();
        batch.draw(textureBgScreen,0,0);
        batch.end();
        dibujarManoJ1();
        stage.draw();

        batch.begin();

        //Dibujamos las cartas invocadas.
        for (Carta carta : partida.getInvoquedCards()) {
            Casilla casillaFirstPostitionCard = tablero.getCasilla(carta.getFirstPosition());
            Vector2 positionCardPx = casillaFirstPostitionCard.getCoordinatesPx();
            if(casillaFirstPostitionCard.getState() == Casilla.State.ILUMINADA) {
               batch.draw(casillaFirstPostitionCard.getTextureCasilla2(),positionCardPx.x, positionCardPx.y);
            }else {
                batch.draw(carta.getImage(), positionCardPx.x, positionCardPx.y);
            }
        }

        //Dibujamos los sprites de las cartas invocadas. (Estos se moverán por el tablero)
        for (Criatura criatura : partida.getCriaturasInvocadas()) {
            Vector2 positionSquareBoard = tablero.getCasilla(criatura.getPosition()).getCoordinatesPx();
            batch.draw(criatura.getSprite(), positionSquareBoard.x, positionSquareBoard.y);
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
                tablatableroGUI.addActor(tablero.getCasilla(x2, y2).getImageCasilla());
            }
        }
        stage.addActor(tablatableroGUI);
    }

    private void dibujarManoJ1() {
        //Dibujar manoJ1
        if(partida.init==0) {
            for(int i = 0; i<partida.getManoPartida().getMano().size(); i++) {
                partida.getManoPartida().drawHand(i, partida,  tablero,  MEDIDA_CASILLA,  cartasManoJ1GUI,  posyManoJ1, textureCard);
                stage.addActor(cartasManoJ1GUI[i]);
            }
            partida.init=1;
        }
        drawHandGraphic();
    }




    private void drawHandGraphic() {
        numCardsInHand=partida.getManoPartida().getMano().size();

        if(numCardsInHand<partida.getCantidadCartas()) {
            cartasManoJ1GUI[partida.getCantidadCartas()-1].remove();
            partida.setCantidadCartas(numCardsInHand);
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
        stage.addActor(partida.getMazos().get(0).getMazoGUI());
    }

    private void dibujarMazoJ2() {
        //Dibujar mazoJ2
        stage.addActor(partida.getMazos().get(1).getMazoGUI());
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