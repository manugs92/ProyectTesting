package com.mygdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.mygdx.IAs.IaOne;
import com.mygdx.animations.HandDownIndicationAnimation;
import com.mygdx.game.MyGdxGameAssetManager;
import com.mygdx.game.MyGdxGameScreen;
import com.mygdx.model.*;

import java.util.ArrayList;

import static com.mygdx.model.Casilla.MEDIDA_CASILLA;
import static com.mygdx.model.Mano.POS_Y_MANO_J1;

/*
 * Screen Mostrada en duelo singlePlayer.
 * */
public class DuelScreen extends MyGdxGameScreen {

    /*
     *
     * To make the JarFile: gradlew desktop:dist
     *
     * TODO: Animación de todos los monstruos (proceso).
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
    private SpriteBatch batch;
    private HandDownIndicationAnimation handDownIndicationAnimation;
    private float animationTimer;


    public DuelScreen(ScreenManager screenManagerR) {
        super(screenManagerR);
    }

    @Override
    public void show() {
        assetManager.loadDuelScreenImages();
        assetManager.manager.finishLoading();

        batch = new SpriteBatch();

        handDownIndicationAnimation = new HandDownIndicationAnimation(batch);

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
        animationTimer += delta*1000;
        //Configuración del render
        Gdx.gl.glClearColor(0f, 0f, 0f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(cam.combined);

        stage.act(Math.min(delta, 1 / 30f));
        batch.begin();

        //Si el log de la partida tiene un nuevo mensaje, lo actualizamos.
        if (partida.getDuelLog().isNewMsg()) {
            dibujarLog();
        }

        //Por cada jugador, dibujamos la mano, el mazo y los avatares.
        partida.getJugadores().forEach(j -> {
            dibujarMano(j.getId());
            dibujarMazo(j.getId());
            dibujarAvatarJugadores(j.getId());
        });

        //Dibujamos las flechas de siguiente y anterior del cementerio.
        drawArrows();

        //Finalizamos el batch.
        batch.end();

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
        makeAnimationsOfSquares(partida.getSelectedCard(),partida.getJugador(0),tablero,animationTimer);
        batch.begin();

        partida.getJugador(0).getInvoquedCards().forEach(carta -> dibujarCartasColocadas(carta,animationTimer, partida.getJugador(0),partida.getSelectedCard()));
        partida.getJugador(1).getInvoquedCards().forEach(carta -> dibujarCartasColocadas(carta,animationTimer,partida.getJugador(1),partida.getSelectedCard()));
        //Dibujamos cantidad de cartas en mazo, nombres de jugadores, vidas y mana, y cartas en el cementerio.
        partida.getJugadores().forEach(j -> {
            j.getCriaturasInvocadas().forEach(criatura -> dibujarCriaturasInvocadas(criatura, j.getId()));
            dibujarCantidadCartasMazo(j.getId());
            dibujarNombreJugador(j.getId());
            drawLivesAndMana(j.getId());
            dibujarCartaCemenerio(j.getId());
        });

        dibujarAvisos();

        //We will draw all the animations here.

        handDownIndicationAnimation.render(partida.getJugador(0),delta,partida.getOwnerTurn());
        drawAnimationArrowUp(0);
        batch.end();

        //La máquina se mueve
        partida.getiA().executeIA(partida,assetManager);

        if (partida.getWinnerId() != -1) { screenManager.changeScreenToResume(screenManager.SUMMARY_SCREEN, partida); }
        if(animationTimer>1600) {animationTimer=0;}
    }

    private void makeAnimationsOfSquares(Carta carta,Jugador jugador,Tablero tablero, float animationTimer) {
        for(int x=0;x<Tablero.TOTAL_CASILLAS_X;x++) {
            for(int y=0;y<Tablero.TOTAL_CASILLAS_Y;y++) {
                tablero.getCasilla(x,y).animationAvoidSquare(animationTimer);
                tablero.getCasilla(x,y).animationAvoidToAtack(animationTimer);
                tablero.getCasilla(x,y).animationAvoidToMove(carta,jugador,animationTimer);
            }
        }
    }

    @Override
    public void dispose() { super.dispose(); }

    private void dibujarLog() {
        stage.addActor(partida.getDuelLog().writeLog(assetManager, batch));
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
                partida.getJugador(jugadorId).getMano().drawHand(i, partida, jugadorId,assetManager);
                if(jugadorId == 0
                        && partida.getJugador(0).getMano().getCartasMano().get(i).getCostInvocation() <= partida.getJugador(0).getInvocationOrbs())
                {

                    stage.addActor(partida.getJugador(0).getMano().getAvoidToInvoque().get(i));
                }
                stage.addActor(partida.getJugador(jugadorId).getMano().getCartaManoGUI().get(i));
            }
        }
        partida.getJugador(jugadorId).getMano().updateHand(jugadorId);
        drawAnimationCardsAbleToInvoque(jugadorId);
        if(jugadorId==0) {
            partida.getJugador(0).getMano().upSelectedCard(partida,0);
        }


    }



    private void drawAnimationArrowUp(int jugadorId) {
        ArrayList<Carta> manoJugador = partida.getJugador(jugadorId).getMano().getCartasMano();
        Carta selectedCard = partida.getSelectedCard();
        if(selectedCard !=null && jugadorId==0 && manoJugador.contains(selectedCard)) {
            int indexCard = manoJugador.indexOf(partida.getSelectedCard());
            float positionX = partida.getJugador(0).getMano().getCartaManoGUI().get(indexCard).getX();
            float positionY = partida.getJugador(0).getMano().getCartaManoGUI().get(indexCard).getY() ;
            if (animationTimer < 600 && selectedCard.getCostInvocation()<=partida.getJugador(0).getInvocationOrbs()
                    && !partida.getJugador(0).isAvoidToDrawCard() && partida.getOwnerTurn()==0) {
               batch.draw(partida.getJugador(0).getMano().getArrowUp(),positionX,positionY+48);
            }
        }
    }

    public void drawAnimationCardsAbleToInvoque(int jugadorId) {
        if(jugadorId==0) {
            for (int i = 0; i < partida.getJugador(0).getMano().getCartaManoGUI().size(); i++) {
                if (animationTimer < 600) {
                    partida.getJugador(0).getMano().getAvoidToInvoque().get(i).setVisible(true);
                } else {
                    partida.getJugador(0).getMano().getAvoidToInvoque().get(i).setVisible(false);
                }
            }
        }
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
        font.draw( batch, String.valueOf(partida.getJugador(jugadorId).getMazo().getCartasMazo().size()), partida.getJugador(jugadorId).getMazo().getPositionGUI().x + 15, partida.getJugador(jugadorId).getMazo().getPositionGUI().y + 30);
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
            batch.draw(criatura.getSprite(), positionSquareBoard.x, positionSquareBoard.y);
        } else {
            batch.draw(criatura.getSpriteFront(), positionSquareBoard.x, positionSquareBoard.y);
        }
    }

    private void dibujarInformacionCarta() {
        stage.addActor(partida.getCardInformation().writeInfoPane());
        partida.getCardInformation().setNewCardInfo(false);
    }

    private void dibujarCartasColocadas(Carta carta,float animationTimer,Jugador jugador,Carta selectedCard) {
        Casilla casillaFirstPostitionCard = tablero.getCasilla(carta.getFirstPosition());
        Vector2 positionCardPx = casillaFirstPostitionCard.getCoordinatesPx();
        if (casillaFirstPostitionCard.getState() == Casilla.State.ILUMINADA) {
            if(animationTimer  < 600) {
                batch.draw(carta.getImage(), positionCardPx.x, positionCardPx.y);
            }else {
                batch.draw(casillaFirstPostitionCard.getTextureCasilla2(), positionCardPx.x, positionCardPx.y);
            }
        } else if (casillaFirstPostitionCard.getState() == Casilla.State.AVOID_TO_ATACK) {
            if(animationTimer  < 600) {
                batch.draw(carta.getImage(), positionCardPx.x, positionCardPx.y);
            }else {
                batch.draw(casillaFirstPostitionCard.getTextureCasilla3(), positionCardPx.x, positionCardPx.y);
            }
        } else if(casillaFirstPostitionCard.getState() == Casilla.State.APAGADA && casillaFirstPostitionCard.tieneCriatura()
                && casillaFirstPostitionCard.getCriatura().getOwnerId()==0 && !casillaFirstPostitionCard.getCriatura().isMoved()
        && jugador.getId()==0 && !jugador.isAvoidToDrawCard() && selectedCard == null) {
            if(animationTimer  < 600) {
                batch.draw(carta.getImage(), positionCardPx.x, positionCardPx.y);
            }else {
                batch.draw(casillaFirstPostitionCard.getTextureCasilla4(), positionCardPx.x, positionCardPx.y);
            }
        }else {
            batch.draw(carta.getImage(), positionCardPx.x, positionCardPx.y);
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
                batch.draw(cementerio.getCardsInGraveyard().get(cementerio.getCardsInGraveyard().size() - 1).getImage(), cementerio.POS_X_GRAVEYARD, cementerio.POS_Y_GRAVEYARD_J1);
            } else {
                batch.draw(cementerio.getTextureGraveyard(), cementerio.POS_X_GRAVEYARD, cementerio.POS_Y_GRAVEYARD_J1);
            }
        } else {
            if (cementerio.getCardsInGraveyard().size() > 0) {
                batch.draw(cementerio.getCardsInGraveyard().get(cementerio.getCardsInGraveyard().size() - 1).getImage(), cementerio.POS_X_GRAVEYARD, cementerio.POS_Y_GRAVEYARD_J2);
            } else {
                batch.draw(cementerio.getTextureGraveyard(), cementerio.POS_X_GRAVEYARD, cementerio.POS_Y_GRAVEYARD_J2);
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
                if(animationTimer  < 600) {
                    //La desiluminamos
                    partida.getJugador(1).getAvatar2().setColor(255, 255, 255, 255);
                }else {
                    partida.getJugador(1).getAvatar2().setColor(Color.RED);
                }
                //stage.addActor(partida.getJugador(1).getAvatar3());
            }else {
                partida.getJugador(1).getAvatar2().setColor(255, 255, 255, 255);
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
        font.draw( batch, String.valueOf(partida.getJugador(jugadorId).getNombre()), partida.getJugador(jugadorId).getPosName().x, partida.getJugador(jugadorId).getPosName().y);
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
        font.draw( batch, String.valueOf(partida.getJugador(jugadorId).getLives()), partida.getJugador(jugadorId).getPoslives().x, partida.getJugador(jugadorId).getPoslives().y);
        font.draw( batch, String.valueOf(partida.getJugador(jugadorId).getInvocationOrbs()), partida.getJugador(jugadorId).getPosInvocationOrbs().x, partida.getJugador(jugadorId).getPosInvocationOrbs().y);
    }

    private void dibujarAvisos() {
        if (partida.getAvisosPartida().getAvisos() == AvisosPartida.avisos.DESCARTAR_CARTAS && partida.getJugador(0).getMano().getCartasMano().size() > Partida.MAX_CARDS_IN_HAND) {
            font.setColor(255, 255, 255, 255);
            font.draw( batch, partida.getAvisosPartida().getTexttoShow(), partida.getAvisosPartida().getPositionAviso().x, partida.getAvisosPartida().getPositionAviso().y);
        } else if (partida.getAvisosPartida().getAvisos() == AvisosPartida.avisos.ANTES_DEBES_ROBAR && partida.getJugador(0).isAvoidToDrawCard()) {
            font.setColor(255, 255, 255, 255);
            font.draw( batch, partida.getAvisosPartida().getTexttoShow(), partida.getAvisosPartida().getPositionAviso().x, partida.getAvisosPartida().getPositionAviso().y);
        } else {
            partida.getAvisosPartida().setShowed(false);
        }
    }
}