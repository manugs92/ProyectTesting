package com.mygdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.MyGdxGameAssetManager;
import com.mygdx.game.MyGdxGameScreen;
import com.mygdx.model.*;

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

    //Variables usadas por la GUI
    private SpriteBatch batch;
    private Table tablatableroGUI = new Table();
    private Skin skin = new Skin(Gdx.files.internal("skin/flat-earth-ui.json"));

    //Atributos de la GUI
    private final int MEDIDA_CASILLA = 48;
    private float posyManoJ1 = 10;
    private float posyManoJ2 = MyGdxGame.SCREEN_HEIGHT-60;

    //Variables temporales (hasta que el jugador se cree desde algun lado)
    Jugador jugador = new Jugador("Manu",0,assetManager);
    Jugador jugador2 = new Jugador("Isma",1,assetManager);

    //Variables back end.
    private Partida partida = new Partida(jugador,skin);
    private Tablero tablero = new Tablero(partida, assetManager);
    private int xTablero=tablero.getCasillas().length;
    private int yTablero=tablero.getCasillas()[0].length;

    //Informacion de carta
    CardInformation cardInformation=new CardInformation();


    public DuelScreen(ScreenManager screenManagerR) {
        super(screenManagerR);
    }

    @Override
    public void show() {
        assetManager.loadImagesDuelScreen();
        assetManager.manager.finishLoading();

        //Añadimos jugador2 a la partida.
        partida.addJugador(jugador2);

        //Debugeamos el stage, para ver como están posicionados los elementos.
        stage.setDebugAll(false);

        //Variables usadas para dibujar.
        batch = new SpriteBatch();

        //Dibujamos todos las imagenes "estaticas".
        dibujarTablero();
        dibujarMagicasJ1();
        dibujarMagicasJ2();
//        dibujarManoJ2();
//        dibujarMazoJ1();
//        dibujarMazoJ2();
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(cam.combined);

        stage.act(Math.min(delta, 1 / 30f));
        batch.begin();
        batch.draw(textureBgScreen,0,0);
        if(partida.getDuelLog().isNewMsg()) {
            dibujarLog();
            partida.getDuelLog().setNewMsgFalse();
        }
        batch.end();
      //  dibujarManoJ1();
        stage.draw();
        batch.begin();

       //Mostramos la informacion de la carta
        if(cardInformation.isNewCardInfo())
       cardInformation.updateInfoPane();

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

    private void dibujarLog() {
       stage.addActor(partida.getDuelLog().writeLog(assetManager,batch));
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

//    private void dibujarManoJ1() {
//        //Dibujar manoJ1
//        if(partida.init==0) {
//            for(int i = 0; i<partida.getJugador(jugador.getId()).getMano().getCartasMano().size(); i++) {
//                partida.getJugador(jugador.getId()).getMano().drawHand(i, partida,  tablero,  MEDIDA_CASILLA,  posyManoJ1);
//                stage.addActor(partida.getJugador(jugador.getId()).getMano().getCartaManoGUI()[i]);
//            }
//            partida.init=1;
//        }
//        updateHand(jugador.getId());
//    }

//    private void dibujarManoJ2() {
//        //Dibujar manoJ2
//        if(partida.init==1) {
//            for(int i=0;i<partida.getManoPartida(1).getMano().size();i++) {
//                partida.getManoPartida(0).drawHand(i, partida,  tablero,  MEDIDA_CASILLA,  posyManoJ1);
//                stage.addActor(partida.getManoPartida(1).getCartaManoGUI()[i]);
//        }
//        partida.init=2;
//        }
//        updateHand(jugador2.getId());
//    }
//
//    private void updateHand(int jugadorid) {
//        partida.getJugador(jugadorid).getCardsInHand();
//        if(partida.getJugador(jugadorid).getCardsInHand()<partida.getManoPartida(jugadorid).getMano().size()) {
//            partida.getManoPartida(jugadorid).getCartaManoGUI()[partida.getCantidadCartas()-1].remove();
//            partida.setCantidadCartas(numCardsInHand);
//        }
//    }

//    private void dibujarMazoJ1(){
//        //Dibujar mazoJ1
//        stage.addActor(partida.getMazos().get(0).getMazoGUI());
//    }
//
//    private void dibujarMazoJ2() {
//        //Dibujar mazoJ2
//        stage.addActor(partida.getMazos().get(1).getMazoGUI());
//    }

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