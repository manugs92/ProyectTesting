package com.mygdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.mygdx.game.MyGdxGameAssetManager;
import com.mygdx.game.MyGdxGameScreen;
import com.mygdx.model.*;

import java.util.ArrayList;

/*
 * Screen Mostrada en duelo singlePlayer.
 * */
public class DuelScreen extends MyGdxGameScreen {


    /*
    //
    *  Crear el "shuffle", y coger las cartas de ahí para añadirlas a la mano.
    *  En el mazo renderizado, listener de robar cartas cuando es tu turno.
    *  TODO: No poder tener más cartas del máximo permitido. (Obligar a tirar, haciendo click derecho)
    *  TODO: En partida poner de quien es el turno. (Iluminar el avatar de ese jugador)
    *  TODO: Solo un ataque de monstruo por turno.
    *  TODO: Botón de pasar turno. (solo cuando sea tu turno esté activo)
    *
    *  TODO: Botón de rendirse. (Hacerlo con longclick)
    *  TODO: Método para detectar el alcance de un monstruo. (si puede atacar o no).
    *  TODO: Método para atacar al monstruo.
    * */

    private MyGdxGameAssetManager assetManager = new MyGdxGameAssetManager();

    //Variables usadas por la GUI
    private SpriteBatch batch;
    private Table tablatableroGUI = new Table();
    private Skin skin = new Skin(Gdx.files.internal("skin/flat-earth-ui.json"));
    private BitmapFont font = new BitmapFont();



    //Variables temporales (hasta que el jugador se cree desde algun lado)
    private Jugador jugador = new Jugador("Manu",0,assetManager,skin);
    private Jugador jugador2 = new Jugador("Isma",1,assetManager,skin);

    //Variables back end.
    private Partida partida = new Partida(jugador,skin,assetManager);
    private Tablero tablero = partida.getTablero();
    private int xTablero=tablero.getCasillas().length;
    private int yTablero=tablero.getTablero().getCasillas()[0].length;


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
            dibujarCementerio(j.getId());
            dibujarAvatarJugadores(j.getId());
            partida.getJugador(j.getId()).getCementerio().addListenerToGraveyard(partida,j.getId());
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

        if(partida.getDuelLog().isNewMsg()) { dibujarLog(); }

        partida.getJugadores().forEach(j -> {
            dibujarMano(j.getId());
            dibujarMazo(j.getId());
        });

        drawArrows();

        batch.end();

        //Mostramos la informacion de la carta
        if(partida.getCardInformation().isNewCardInfo()) { dibujarInformacionCarta(); }

        stage.draw();
        batch.begin();

        //Dibujamos las cartas invocadas.
        partida.getInvoquedCards().forEach(carta -> dibujarCartasColocadas(carta));

        //Dibujamos los sprites de las cartas invocadas. (Estos se moverán por el tablero)
        partida.getCriaturasInvocadas().forEach(criatura -> dibujarCriaturasInvocadas(criatura));

        //Dibujamos cantidad de cartas en mazo.
        partida.getJugadores().forEach(j -> dibujarCantidadCartasMazo(j.getId()));

        //Dibujamos última carta enviada al cementerio.
        partida.getJugadores().forEach(j -> dibujarCartaCemenerio(j.getId()));
        batch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        textureBgScreen.dispose();
    }

    private void dibujarLog() {
       stage.addActor(partida.getDuelLog().writeLog(assetManager,batch));
       partida.getDuelLog().setNewMsgFalse();
    }

    public void dibujarTablero() {
        tablatableroGUI.setPosition(tablero.POS_X_TABLERO,tablero.POS_Y_TABLERO);
        //Imagenes de casillas. (Es la función que las dibuja por pantalla)
        for(int x2=0;x2<=xTablero-1;x2++) {
            for(int y2=0;y2<=yTablero-1;y2++) { tablatableroGUI.addActor(tablero.getCasilla(x2, y2).getImageCasilla()); }
        }
        stage.addActor(tablatableroGUI);
    }

    private void dibujarMano(int jugadorId) {
        //Dibujar manos
        if(!partida.getJugador(jugadorId).getMano().isManoCargada() ) {
            for(int i = 0; i<partida.getJugador(jugadorId).getMano().getCartasMano().size(); i++) {
                partida.getJugador(jugadorId).getMano().drawHand(i, partida, jugadorId);
                stage.addActor(partida.getJugador(jugadorId).getMano().getCartaManoGUI().get(i));
            }
        }
        partida.getJugador(jugadorId).getMano().updateHand(partida);
    }


    private void dibujarMazo(int jugadorId){
        //Dibujar mazoJ1
        stage.addActor(partida.getJugador(jugadorId).getMazo().getMazoGUI());
    }


    private void dibujarCantidadCartasMazo(int jugadorId) {
        font.setColor(0,0,0,255);
        font.draw(batch, String.valueOf(partida.getJugador(jugadorId).getMazo().getCartasMazo().size()), partida.getJugador(jugadorId).getMazo().getPositionGUI().x+15, partida.getJugador(jugadorId).getMazo().getPositionGUI().y+30);
    }

    private void dibujarMagicas(int jugadorID) {
       for(int i=0;i<=tablero.MAX_MAGIC_CARDS-1;i++) {
            stage.addActor(tablero.getCasillaMagica(jugadorID,i).getImageCasilla());
        }
    }

    private void dibujarCriaturasInvocadas(Criatura criatura) {
        Vector2 positionSquareBoard = tablero.getCasilla(criatura.getPosition()).getCoordinatesPx();
        batch.draw(criatura.getSprite(), positionSquareBoard.x, positionSquareBoard.y);
    }

    private void dibujarInformacionCarta() {
        stage.addActor(partida.getCardInformation().writeInfoPane());
        partida.getCardInformation().setNewCardInfo(false);
    }

    private void dibujarCartasColocadas(Carta carta) {
        Casilla casillaFirstPostitionCard = tablero.getCasilla(carta.getFirstPosition());
        Vector2 positionCardPx = casillaFirstPostitionCard.getCoordinatesPx();
        if(casillaFirstPostitionCard.getState() == Casilla.State.ILUMINADA) { batch.draw(casillaFirstPostitionCard.getTextureCasilla2(),positionCardPx.x, positionCardPx.y); }
        else { batch.draw(carta.getImage(), positionCardPx.x, positionCardPx.y); }
    }

    private void dibujarCementerio(int jugadorId) {
        stage.addActor(partida.getJugador(jugadorId).getCementerio().getGraveyardGUI());
    }

    private void dibujarCartaCemenerio(int jugadorId) {
        Cementerio cementerio = partida.getJugador(jugadorId).getCementerio();
        if(jugadorId==0) {
            batch.draw(cementerio.getCardsInGraveyard().get(cementerio.getCardsInGraveyard().size()-1).getImage(),cementerio.POS_X_GRAVEYARD,cementerio.POS_Y_GRAVEYARD_J1);
        }else {
            batch.draw(cementerio.getCardsInGraveyard().get(cementerio.getCardsInGraveyard().size()-1).getImage(),cementerio.POS_X_GRAVEYARD,cementerio.POS_Y_GRAVEYARD_J2);
        }
    }

    private void drawArrows() {

        if(partida.getJugador(0).getCementerio().isShowed() ||partida.getJugador(1).getCementerio().isShowed() ) {
            stage.addActor(partida.getCardInformation().getLeftArrow());
            stage.addActor(partida.getCardInformation().getRightArrow());
        }else {
           partida.getCardInformation().removeLeftArrow();
            partida.getCardInformation().removeRightArrow();
        }
    }

    private void dibujarAvatarJugadores(int jugadorId) {
        stage.addActor(partida.getJugador(jugadorId).getAvatar());
    }
}