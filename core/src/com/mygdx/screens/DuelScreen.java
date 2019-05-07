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
    //private float posyManoJ1 = 10;
    //private float posyManoJ2 = MyGdxGame.SCREEN_HEIGHT-60;

    //Variables temporales (hasta que el jugador se cree desde algun lado)
    Jugador jugador = new Jugador("Manu",0,assetManager);
    Jugador jugador2 = new Jugador("Isma",1,assetManager);

    //Variables back end.
    private Partida partida = new Partida(jugador,skin);
    private Tablero tablero = new Tablero(partida, assetManager);
    private int xTablero=tablero.getCasillas().length;
    private int yTablero=tablero.getCasillas()[0].length;

    //Informacion de carta
    CardInformation cardInformation= new CardInformation();


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
        partida.getJugadores().forEach(j -> {
            dibujarMagicas(j.getId());
            dibujarMazo(j.getId());
        });
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
        partida.getJugadores().forEach(j -> {
            dibujarMano(j.getId());
        });
        stage.draw();
        batch.begin();

       //Mostramos la informacion de la carta
        if(cardInformation.isNewCardInfo()) cardInformation.updateInfoPane();

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

    private void dibujarMano(int jugadorId) {
        //Dibujar manos
        if(partida.getJugador(jugadorId).getMano().isCartaJugada() ||!partida.getJugador(jugadorId).getMano().isManoCargada() ) {
            System.out.println("dibujo la mano de "+jugadorId);
            for(int i = 0; i<partida.getJugador(jugadorId).getMano().getCartasMano().size(); i++) {
                partida.getJugador(jugadorId).getMano().drawHand(i, partida,  tablero,  MEDIDA_CASILLA,  jugadorId);
                stage.addActor(partida.getJugador(jugadorId).getMano().getCartaManoGUI()[i]);
            }
            if(partida.getJugador(jugadorId).getMano().isCartaJugada()) {
                partida.getJugador(jugadorId).getMano().setCartaJugada(false);
            }
            partida.getJugador(jugadorId).getMano().setManoCargada(true);
            updateHand(jugadorId);
        }
    }

    private void updateHand(int jugadorId) {
        if(partida.getJugador(jugadorId).getCardsInHand()>partida.getJugador(jugadorId).getMano().getCartasMano().size()) {
            partida.getJugador(jugadorId).getMano().getCartaManoGUI()[partida.getJugador(jugadorId).getCardsInHand()-1].remove();
            partida.getJugador(jugadorId).setCardsInHand(partida.getJugador(jugadorId).getMano().getCartasMano().size());
        }
    }

    private void dibujarMazo(int jugadorId){
        //Dibujar mazoJ1
        stage.addActor(partida.getJugador(jugadorId).getMazo().getMazoGUI());
    }


    private void dibujarMagicas(int playerId) {
        //Magicas J1
       for(int i=0;i<=tablero.MAX_MAGIC_CARDS-1;i++) {
            stage.addActor(tablero.getCasillaMagica(playerId,i).getImageCasilla());
        }
    }
}