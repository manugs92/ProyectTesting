package com.mygdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.mygdx.IAs.IaOne;
import com.mygdx.game.MyGdxGameAssetManager;
import com.mygdx.game.MyGdxGameScreen;
import com.mygdx.model.*;

/*
 * Screen Mostrada en duelo singlePlayer.
 * */
public class DuelScreen extends MyGdxGameScreen {


    /*
    *
    *  TODO: Método de atacar monstruo.
    *  TODO: Método para detectar el alcance de un monstruo. (si puede atacar o no).
    *  TODO: Solo un ataque de monstruo por turno. (iluminar casilla donde este dicho monstruo, verificando alcance)
    *  TODO: Si atacas, atacas a su defensa, y él a la tuya. (puede haber victoria, empate, o derrota)
    *  TODO: Sonido al realizar ataque.
    *
    *  TODO: Método de quitar vidas.
    *
    *  TODO: Todo lo que se haga, mostrarlo en el log.
    *
    *  TODO: IA que solo invoque un monstruo por turno y lo mueva para adelante (pero no te ataque)
    *
    *  TODO: Poner música de juego en (main menú,configuración), en duelo, y en resumeScreen.
    *  TODO: Poner sonidos al invocar, al robar, al atacar..
    *
    *  TODO: Ventana de configuración posibilidad de editar valores del juego. (Volumen del juego, y desactivar música/sonidos).
    *
    * */

    private MyGdxGameAssetManager assetManager = new MyGdxGameAssetManager();

    //Variables usadas por la GUI
    private SpriteBatch batch;
    private Table tablatableroGUI = new Table();
    private Skin skin = new Skin(Gdx.files.internal("skin/flat-earth-ui.json"));
    private BitmapFont font = new BitmapFont();

    //Variables temporales (hasta que el jugador se cree desde algun lado)
    private Jugador jugador = new Jugador("Manu", 0, assetManager, skin);
    private Jugador jugador2 = new Jugador("P.maquina", 1, assetManager, skin);

    //Variables back end.
    private Partida partida = new Partida(jugador, skin, assetManager);
    private Tablero tablero = partida.getTablero();
    private int xTablero = tablero.getCasillas().length;
    private int yTablero = tablero.getTablero().getCasillas()[0].length;
    private IaOne iA;

    public DuelScreen(ScreenManager screenManagerR) { super(screenManagerR); }

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
            partida.getJugador(j.getId()).getCementerio().addListenerToGraveyard(partida, j.getId());
        });

        dibujarBotones();
        iA = new IaOne();
        stage.addActor(partida.getPassTurn());
        stage.addActor(partida.getCardInformation().getLeftArrow());
        stage.addActor(partida.getCardInformation().getRightArrow());

        if(partida.getOwnerTurn()==0) {
            partida.getDuelLog().addMsgToLog("Turno de "+partida.getJugador(0).getNombre()+".");
        }else {
            partida.getDuelLog().addMsgToLog("Turno de "+partida.getJugador(1).getNombre()+".");
        }
        partida.getDuelLog().setNewMsgTrue();
        //partida.getDuelLog().getScrollPane().remove();
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        batch.setProjectionMatrix(cam.combined);
        stage.act(Math.min(delta, 1 / 30f));
        batch.begin();

        if(partida.getDuelLog().isNewMsg()) { dibujarLog(); }

        partida.getJugadores().forEach(j -> {
            dibujarMano(j.getId());
            dibujarMazo(j.getId());
            dibujarAvatarJugadores(j.getId());
        });

        drawArrows();


        batch.end();

        //Mostramos la informacion de la carta
        if(partida.getCardInformation().isNewCardInfo()) { dibujarInformacionCarta(); }

        //Mostramos botón de pasar turno si es tu turno.
        if(partida.getOwnerTurn()==0) { partida.getPassTurn().setVisible(true); }
        else { partida.getPassTurn().setVisible(false); }

        //maquinna
        iA.iaMove(partida);
        partida.setOwnerTurn(0);

        stage.draw();
        batch.begin();

        //Dibujamos las cartas invocadas.
        partida.getInvoquedCards().forEach(carta -> dibujarCartasColocadas(carta));

        //Dibujamos los sprites de las cartas invocadas. (Estos se moverán por el tablero)
        partida.getCriaturasInvocadasJ1().forEach(criatura -> dibujarCriaturasInvocadas(criatura));
        partida.getCriaturasInvocadasJ2().forEach(criatura -> dibujarCriaturasInvocadas(criatura));

        //Dibujamos cantidad de cartas en mazo, nombres de jugadores, vidas y mana, y cartas en el cementerio.
        partida.getJugadores().forEach(j -> {
            dibujarCantidadCartasMazo(j.getId());
            dibujarNombreJugador(j.getId());
            drawLivesAndMana(j.getId());
            dibujarCartaCemenerio(j.getId());
        });

        dibujarAvisos();

        batch.end();

        if(partida.getWinnerId()!=-1) { screenManager.changeScreenToResume(screenManager.SUMMARY_SCREEN,partida); }
    }

    @Override
    public void dispose() {
        super.dispose();
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
        partida.getJugador(jugadorId).getMano().updateHand();
    }

    private void dibujarMazo(int jugadorId){
        if(!partida.getJugador(jugadorId).isAvoidToDrawCard()) {
            stage.addActor(partida.getJugador(jugadorId).getMazo().getMazoDefaultGUI());
            partida.getJugador(jugadorId).getMazo().getMazoAvoidToDrawGUI().remove();
        }else {
            stage.addActor(partida.getJugador(jugadorId).getMazo().getMazoAvoidToDrawGUI());
            partida.getJugador(jugadorId).getMazo().getMazoDefaultGUI().remove();
        }
    }

    private void dibujarCantidadCartasMazo(int jugadorId) {
        font.setColor(255,255,255,255);
        font.draw(batch, String.valueOf(partida.getJugador(jugadorId).getMazo().getCartasMazo().size()), partida.getJugador(jugadorId).getMazo().getPositionGUI().x+15, partida.getJugador(jugadorId).getMazo().getPositionGUI().y+30);
    }

    private void dibujarMagicas(int jugadorID) {
       for(int i=0;i<=tablero.MAX_MAGIC_CARDS-1;i++) { stage.addActor(tablero.getCasillaMagica(jugadorID,i).getImageCasilla()); }
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

    private void dibujarCementerio(int jugadorId) { stage.addActor(partida.getJugador(jugadorId).getCementerio().getGraveyardGUI()); }

    private void dibujarCartaCemenerio(int jugadorId) {
        Cementerio cementerio = partida.getJugador(jugadorId).getCementerio();
        if(jugadorId==0) {
            //Si tiene cartas en el cementerio, mostrar la ultima, si no mostrar una casilla vacia.
            if(cementerio.getCardsInGraveyard().size()>0) { batch.draw(cementerio.getCardsInGraveyard().get(cementerio.getCardsInGraveyard().size()-1).getImage(),cementerio.POS_X_GRAVEYARD,cementerio.POS_Y_GRAVEYARD_J1); }
            else { batch.draw(cementerio.getTextureGraveyard(),cementerio.POS_X_GRAVEYARD,cementerio.POS_Y_GRAVEYARD_J1); }
        }else {
            if(cementerio.getCardsInGraveyard().size()>0) { batch.draw(cementerio.getCardsInGraveyard().get(cementerio.getCardsInGraveyard().size()-1).getImage(),cementerio.POS_X_GRAVEYARD,cementerio.POS_Y_GRAVEYARD_J2); }
            else { batch.draw(cementerio.getTextureGraveyard(),cementerio.POS_X_GRAVEYARD,cementerio.POS_Y_GRAVEYARD_J2); }
        }
    }

    private void drawArrows() {
        if(partida.getJugador(0).getCementerio().isShowed() && partida.getJugador(0).getCementerio().getCardsInGraveyard().size()>1) {
            if(partida.getCardInformation().getPositionInCementerio()>0) {
                partida.getCardInformation().getLeftArrow().setVisible(true);
            }else{
                partida.getCardInformation().getLeftArrow().setVisible(false);
            }
            if(partida.getCardInformation().getPositionInCementerio()<partida.getJugador(0).getCementerio().getCardsInGraveyard().size()-1) {
                partida.getCardInformation().getRightArrow().setVisible(true);
            }else {
                partida.getCardInformation().getRightArrow().setVisible(false);
            }
        }else if(partida.getJugador(1).getCementerio().isShowed() && partida.getJugador(1).getCementerio().getCardsInGraveyard().size()>1) {
            if(partida.getCardInformation().getPositionInCementerio()>0) {
                partida.getCardInformation().getLeftArrow().setVisible(true);
            }else {
                partida.getCardInformation().getLeftArrow().setVisible(false);
            }
            if(partida.getCardInformation().getPositionInCementerio()<partida.getJugador(1).getCementerio().getCardsInGraveyard().size()-1) {
                partida.getCardInformation().getRightArrow().setVisible(true);
            }else {
                partida.getCardInformation().getRightArrow().setVisible(false);
            }
        }else {
            partida.getCardInformation().getLeftArrow().setVisible(false);
            partida.getCardInformation().getRightArrow().setVisible(false);
        }
    }

    private void dibujarAvatarJugadores(int jugadorId) {
        if(partida.getOwnerTurn()==jugadorId) {
            stage.addActor(partida.getJugador(jugadorId).getAvatar());
            partida.getJugador(jugadorId).getAvatar2().remove();
        }else {
            stage.addActor(partida.getJugador(jugadorId).getAvatar2());
            partida.getJugador(jugadorId).getAvatar().remove();
        }
    }

    private void dibujarNombreJugador(int jugadorId) {
        font.setColor(255,255,255,255);
        font.draw(batch, String.valueOf(partida.getJugador(jugadorId).getNombre()), partida.getJugador(jugadorId).getPosName().x, partida.getJugador(jugadorId).getPosName().y);
    }

    private void dibujarBotones() { stage.addActor(partida.getButtonRendirse()); }

    private void drawLivesAndMana(int jugadorId){
        stage.addActor(partida.getJugador(jugadorId).getLivesGUI());
        stage.addActor(partida.getJugador(jugadorId).getManaOrbGUI());
        font.setColor(255,255,255,255);
        font.draw(batch, String.valueOf(partida.getJugador(jugadorId).getLives()), partida.getJugador(jugadorId).getPoslives().x, partida.getJugador(jugadorId).getPoslives().y);
        font.draw(batch, String.valueOf(partida.getJugador(jugadorId).getInvocationOrbs()), partida.getJugador(jugadorId).getPosInvocationOrbs().x, partida.getJugador(jugadorId).getPosInvocationOrbs().y);
    }

    private void dibujarAvisos() {
        if(partida.getAvisosPartida().getAvisos() == AvisosPartida.avisos.DESCARTAR_CARTAS && partida.getJugador(0).getMano().getCartasMano().size()> Partida.MAX_CARDS_IN_HAND ) {
            font.setColor(255,255,255,255);
            font.draw(batch, partida.getAvisosPartida().getTexttoShow(),partida.getAvisosPartida().getPositionAviso().x,partida.getAvisosPartida().getPositionAviso().y);
        }else if(partida.getAvisosPartida().getAvisos() == AvisosPartida.avisos.ANTES_DEBES_ROBAR && partida.getJugador(0).isAvoidToDrawCard()) {
            font.setColor(255,255,255,255);
            font.draw(batch,partida.getAvisosPartida().getTexttoShow(),partida.getAvisosPartida().getPositionAviso().x,partida.getAvisosPartida().getPositionAviso().y);
        }else {
            partida.getAvisosPartida().setShowed(false);
        }
    }
}