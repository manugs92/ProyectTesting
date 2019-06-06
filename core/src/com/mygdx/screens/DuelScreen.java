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
     *  TODO: Sonido al realizar ataque.
     *
     *  TODO: Mostrar en el log cuando el rival te ataque, cuando tú pierdes vidas.
     *  TODO: Mostrar en el log info cuando se roba, y cuando se descarta una carta al cementerio.
     *  TODO: Mostrar en el log, la vida restante del bicho.
     *  TODO: Mostrar en el log, la vida restante del jugador tras recibir daño.
     *
     *  TODO: Que el rival te ataque, y tú pierdes vidas.
     *
     *  TODO: IA que te pueda atacar si estás a su alcance.
     *  TODO: IA que te pueda atacar a ti si está en tus casillas de invocación.
     *
     *  TODO: Buffer de vida de criatura, para que puedas atacarle dos veces y matarla con dos criaturas debiles.
     *  TODO: Al pasar turno, el buffer ponerlo normal.
     *
     *  TODO: Poner música de juego en (main menú,configuración), en duelo, y en resumeScreen.
     *  TODO: Poner sonidos al invocar, al robar, al atacar..
     *
     *  TODO: HUD donde sale el turno, volumen (mas y menos)
     *
     *  TODO: Ventana de configuración posibilidad de editar valores del juego. (Volumen del juego, y desactivar música/sonidos).
     *
     *  TODO: Optimizar código de Casilla.
     * */

    private MyGdxGameAssetManager assetManager = new MyGdxGameAssetManager();

    //Variables usadas por la GUI
    private Table tablatableroGUI = new Table();

    //Fixme hacerlo global

    //Variables temporales (hasta que el jugador se cree desde algun lado)
    private Jugador jugador;

    //Variables back end.
    private Partida partida;
    private Tablero tablero;
    private int xTablero;
    private int yTablero;
    private IaOne iA ;
    private long timer = System.currentTimeMillis();

    public DuelScreen(ScreenManager screenManagerR) {
        super(screenManagerR);
    }

    @Override
    public void show() {
        assetManager.loadImagesDuelScreen();
        assetManager.manager.finishLoading();

        jugador = new Jugador("Manu", 0, assetManager, screenManager.skin);
        partida = new Partida(jugador, screenManager.skin, assetManager);
        tablero = partida.getTablero();
        xTablero = tablero.getCasillas().length;
        yTablero = tablero.getTablero().getCasillas()[0].length;
        iA = partida.getiA();

        //Dibujamos todos las imagenes "estaticas".
        dibujarTablero();

        //Por cada jugador, dibujaremos su cartas mágicas, el cementerio y añadiremos el listener a su cementerio.
        partida.getJugadores().forEach(j -> {
            dibujarMagicas(j.getId());
            dibujarCementerio(j.getId());
            partida.getJugador(j.getId()).getCementerio().addListenerToGraveyard(partida, j.getId());
        });

        //Dibujamos los botones.
        dibujarBotones();
        //Mostramos el log.
        partida.getDuelLog().announceStartMsgLog(partida);
    }

    @Override
    public void render(float delta) {

        //Configuración del render
        Gdx.gl.glClearColor(0f, 0f, 0f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.getBatch().setProjectionMatrix(cam.combined);
        stage.act(Math.min(delta, 1 / 30f));
        stage.getBatch().begin();

        //Si el log de la partida tiene un nuevo mensaje, lo actualizamos.
        if (partida.getDuelLog().isNewMsg()) {
            dibujarLog();
        }

        //Por cada jugador, dibujamos la mano, el mazo y los avatares.
        dibujarMano(0);
        partida.getJugadores().forEach(j -> {
            dibujarMano(j.getId());
            dibujarMazo(j.getId());
            dibujarAvatarJugadores(j.getId());
        });

        //Dibujamos las flechas de siguiente y anterior del cementerio.
        drawArrows();

        //Finalizamos el batch.
        stage.getBatch().end();

        //Mostramos el visor de la información de la carta, si ha recibido nueva información.
        if (partida.getCardInformation().isNewCardInfo()) {
            dibujarInformacionCarta();
        }

        //Mostramos botón de pasar turno si es tu turno.
        if (partida.getOwnerTurn() == 0) {
            partida.getPassTurn().setVisible(true);
        } else {
            partida.getPassTurn().setVisible(false);
        }

        stage.draw();
        stage.getBatch().begin();

        partida.getJugador(0).getInvoquedCards().forEach(carta -> dibujarCartasColocadas(carta));
        partida.getJugador(1).getInvoquedCards().forEach(carta -> dibujarCartasColocadas(carta));
        //Dibujamos cantidad de cartas en mazo, nombres de jugadores, vidas y mana, y cartas en el cementerio.
        partida.getJugadores().forEach(j -> {
            j.getCriaturasInvocadas().forEach(criatura -> dibujarCriaturasInvocadas(criatura, j.getId()));
            dibujarCantidadCartasMazo(j.getId());
            dibujarNombreJugador(j.getId());
            drawLivesAndMana(j.getId());
            dibujarCartaCemenerio(j.getId());
        });

        dibujarAvisos();

        stage.getBatch().end();

        //La máquina se mueve
        partida.getiA().executeIA(partida);

        if (partida.getWinnerId() != -1) { screenManager.changeScreenToResume(screenManager.SUMMARY_SCREEN, partida); }
    }

    @Override
    public void dispose() {
        super.dispose();
    }


    private void dibujarLog() {
        stage.addActor(partida.getDuelLog().writeLog(assetManager, stage.getBatch()));
        partida.getDuelLog().setNewMsgFalse();
    }

    //Método que se encarga de añadir las casillas del tablero al stage.
    public void dibujarTablero() {
        tablatableroGUI.setPosition(tablero.POS_X_TABLERO, tablero.POS_Y_TABLERO);
        for (int x2 = 0; x2 <= xTablero - 1; x2++) {
            for (int y2 = 0; y2 <= yTablero - 1; y2++) {
                tablatableroGUI.addActor(tablero.getCasilla(x2, y2).getImageCasilla());
            }
        }
        stage.addActor(tablatableroGUI);
    }

    private void dibujarMano(int jugadorId) {
        //Dibujar manos
        if (!partida.getJugador(jugadorId).getMano().isManoCargada()) {
            for (int i = 0; i < partida.getJugador(jugadorId).getMano().getCartasMano().size(); i++) {
                partida.getJugador(jugadorId).getMano().drawHand(i, partida, jugadorId);
                stage.addActor(partida.getJugador(jugadorId).getMano().getCartaManoGUI().get(i));
            }
        }
        partida.getJugador(jugadorId).getMano().updateHand(jugadorId);
    }

    private void dibujarMazo(int jugadorId) {
        if (!partida.getJugador(jugadorId).isAvoidToDrawCard() || partida.getOwnerTurn() != jugadorId) {
            stage.addActor(partida.getJugador(jugadorId).getMazo().getMazoDefaultGUI());
            partida.getJugador(jugadorId).getMazo().getMazoAvoidToDrawGUI().remove();
        } else {
            stage.addActor(partida.getJugador(jugadorId).getMazo().getMazoAvoidToDrawGUI());
            partida.getJugador(jugadorId).getMazo().getMazoDefaultGUI().remove();
        }
    }

    private void dibujarCantidadCartasMazo(int jugadorId) {
        font.setColor(255, 255, 255, 255);
        font.draw( stage.getBatch(), String.valueOf(partida.getJugador(jugadorId).getMazo().getCartasMazo().size()), partida.getJugador(jugadorId).getMazo().getPositionGUI().x + 15, partida.getJugador(jugadorId).getMazo().getPositionGUI().y + 30);
    }

    //Cargamos en el stage las casillas mágicas de los jugadores.
    private void dibujarMagicas(int jugadorID) {
        for (int i = 0; i <= tablero.MAX_MAGIC_CARDS - 1; i++) {
            stage.addActor(tablero.getCasillaMagica(jugadorID, i).getImageCasilla());
        }
    }

    private void dibujarCriaturasInvocadas(Criatura criatura, int idPlayer) {
        Vector2 positionSquareBoard = tablero.getCasilla(criatura.getPosition()).getCoordinatesPx();
        if (idPlayer == 0) {
            stage.getBatch().draw(criatura.getSprite(), positionSquareBoard.x, positionSquareBoard.y);
        } else {
            stage.getBatch().draw(criatura.getSpriteFront(), positionSquareBoard.x, positionSquareBoard.y);
        }
    }

    private void dibujarInformacionCarta() {
        stage.addActor(partida.getCardInformation().writeInfoPane());
        partida.getCardInformation().setNewCardInfo(false);
    }

    private void dibujarCartasColocadas(Carta carta) {
        Casilla casillaFirstPostitionCard = tablero.getCasilla(carta.getFirstPosition());
        Vector2 positionCardPx = casillaFirstPostitionCard.getCoordinatesPx();
        if (casillaFirstPostitionCard.getState() == Casilla.State.ILUMINADA) {
            stage.getBatch().draw(casillaFirstPostitionCard.getTextureCasilla2(), positionCardPx.x, positionCardPx.y);
        } else if (casillaFirstPostitionCard.getState() == Casilla.State.AVOID_TO_ATACK) {
            stage.getBatch().draw(casillaFirstPostitionCard.getTextureCasilla3(), positionCardPx.x, positionCardPx.y);
        } else {
            stage.getBatch().draw(carta.getImage(), positionCardPx.x, positionCardPx.y);
        }
    }

    //Cargamos en el stage el cementerio de cada jugador.
    private void dibujarCementerio(int jugadorId) {
        stage.addActor(partida.getJugador(jugadorId).getCementerio().getGraveyardGUI());
    }

    private void dibujarCartaCemenerio(int jugadorId) {
        Cementerio cementerio = partida.getJugador(jugadorId).getCementerio();
        if (jugadorId == 0) {
            //Si tiene cartas en el cementerio, mostrar la ultima, si no mostrar una casilla vacia.
            if (cementerio.getCardsInGraveyard().size() > 0) {
                stage.getBatch().draw(cementerio.getCardsInGraveyard().get(cementerio.getCardsInGraveyard().size() - 1).getImage(), cementerio.POS_X_GRAVEYARD, cementerio.POS_Y_GRAVEYARD_J1);
            } else {
                stage.getBatch().draw(cementerio.getTextureGraveyard(), cementerio.POS_X_GRAVEYARD, cementerio.POS_Y_GRAVEYARD_J1);
            }
        } else {
            if (cementerio.getCardsInGraveyard().size() > 0) {
                stage.getBatch().draw(cementerio.getCardsInGraveyard().get(cementerio.getCardsInGraveyard().size() - 1).getImage(), cementerio.POS_X_GRAVEYARD, cementerio.POS_Y_GRAVEYARD_J2);
            } else {
                stage.getBatch().draw(cementerio.getTextureGraveyard(), cementerio.POS_X_GRAVEYARD, cementerio.POS_Y_GRAVEYARD_J2);
            }
        }
    }

    //Función que muestra las flechas de la paginación del cementerio.
    private void drawArrows() {
        if (partida.getJugador(0).getCementerio().isShowed() && partida.getJugador(0).getCementerio().getCardsInGraveyard().size() > 1) {
            if (partida.getCardInformation().getPositionInCementerio() > 0) {
                partida.getCardInformation().getLeftArrow().setVisible(true);
            } else {
                partida.getCardInformation().getLeftArrow().setVisible(false);
            }
            if (partida.getCardInformation().getPositionInCementerio() < partida.getJugador(0).getCementerio().getCardsInGraveyard().size() - 1) {
                partida.getCardInformation().getRightArrow().setVisible(true);
            } else {
                partida.getCardInformation().getRightArrow().setVisible(false);
            }
        } else if (partida.getJugador(1).getCementerio().isShowed() && partida.getJugador(1).getCementerio().getCardsInGraveyard().size() > 1) {
            if (partida.getCardInformation().getPositionInCementerio() > 0) {
                partida.getCardInformation().getLeftArrow().setVisible(true);
            } else {
                partida.getCardInformation().getLeftArrow().setVisible(false);
            }
            if (partida.getCardInformation().getPositionInCementerio() < partida.getJugador(1).getCementerio().getCardsInGraveyard().size() - 1) {
                partida.getCardInformation().getRightArrow().setVisible(true);
            } else {
                partida.getCardInformation().getRightArrow().setVisible(false);
            }
        } else {
            partida.getCardInformation().getLeftArrow().setVisible(false);
            partida.getCardInformation().getRightArrow().setVisible(false);
        }
    }

    private void dibujarAvatarJugadores(int jugadorId) {
        if (partida.getOwnerTurn() == jugadorId) {
            stage.addActor(partida.getJugador(jugadorId).getAvatar());
            partida.getJugador(jugadorId).getAvatar2().remove();
            if (partida.getJugador(1).isAvoidToDamage()) {
                stage.addActor(partida.getJugador(1).getAvatar3());
            }
        } else {
            if (!partida.getJugador(1).isAvoidToDamage()) {
                stage.addActor(partida.getJugador(jugadorId).getAvatar2());
                partida.getJugador(jugadorId).getAvatar().remove();
                partida.getJugador(1).getAvatar3().remove();
            }
        }
    }

    private void dibujarNombreJugador(int jugadorId) {
        font.setColor(255, 255, 255, 255);
        font.draw( stage.getBatch(), String.valueOf(partida.getJugador(jugadorId).getNombre()), partida.getJugador(jugadorId).getPosName().x, partida.getJugador(jugadorId).getPosName().y);
    }

    //Añadimos al stage las imágenes de rendirse, pasar turno, flecha izquierda y derecha.
    private void dibujarBotones() {
        stage.addActor(partida.getButtonRendirse());
        stage.addActor(partida.getPassTurn());
        stage.addActor(partida.getCardInformation().getLeftArrow());
        stage.addActor(partida.getCardInformation().getRightArrow());
    }

    private void drawLivesAndMana(int jugadorId) {
        stage.addActor(partida.getJugador(jugadorId).getLivesGUI());
        stage.addActor(partida.getJugador(jugadorId).getManaOrbGUI());
        font.setColor(255, 255, 255, 255);
        font.draw( stage.getBatch(), String.valueOf(partida.getJugador(jugadorId).getLives()), partida.getJugador(jugadorId).getPoslives().x, partida.getJugador(jugadorId).getPoslives().y);
        font.draw( stage.getBatch(), String.valueOf(partida.getJugador(jugadorId).getInvocationOrbs()), partida.getJugador(jugadorId).getPosInvocationOrbs().x, partida.getJugador(jugadorId).getPosInvocationOrbs().y);
    }


    private void dibujarAvisos() {
        if (partida.getAvisosPartida().getAvisos() == AvisosPartida.avisos.DESCARTAR_CARTAS && partida.getJugador(0).getMano().getCartasMano().size() > Partida.MAX_CARDS_IN_HAND) {
            font.setColor(255, 255, 255, 255);
            font.draw( stage.getBatch(), partida.getAvisosPartida().getTexttoShow(), partida.getAvisosPartida().getPositionAviso().x, partida.getAvisosPartida().getPositionAviso().y);
        } else if (partida.getAvisosPartida().getAvisos() == AvisosPartida.avisos.ANTES_DEBES_ROBAR && partida.getJugador(0).isAvoidToDrawCard()) {
            font.setColor(255, 255, 255, 255);
            font.draw( stage.getBatch(), partida.getAvisosPartida().getTexttoShow(), partida.getAvisosPartida().getPositionAviso().x, partida.getAvisosPartida().getPositionAviso().y);
        } else {
            partida.getAvisosPartida().setShowed(false);
        }
    }



}