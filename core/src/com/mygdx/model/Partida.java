package com.mygdx.model;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.IAs.IaOne;
import com.mygdx.game.MyGdxGame;
import com.mygdx.managers.MyGdxGameAssetManager;

import java.util.ArrayList;

public class Partida {

    private enum estadoPartida{ESPERANDO_JUGADORES,EMPEZADA,FINALIZADA};

    //NORMAS DE PARTIDA.
    public static final int MAX_CARDS_IN_HAND = 6;
    public static final int INITIAL_LIVES = 20;
    public static final int INITIAL_INVOCATION_ORBS = 6;

    public static final float POS_X_BUTTON_RENDIRSE = MyGdxGame.SCREEN_WIDTH - 208;
    public static final float POS_Y_BUTTON_RENDIRSE = MyGdxGame.SCREEN_HEIGHT /2-50;
    public static final float POS_X_BUTTON_PASS_TURN = 900;
    public static final float POS_Y_BUTTON_PASS_TURN = 200;
    public static final float POS_X_SELECTION_HAND_PASS_TURN = POS_X_BUTTON_PASS_TURN +12;
    public static final float POS_Y_SELECTION_HAND_PASS_TURN = POS_Y_BUTTON_PASS_TURN +50;


    private ArrayList<Jugador> jugadores = new ArrayList<>();
    private Carta selectedCard;
    private Tablero tablero;
    private DuelLog duelLog;
    private CardInformation cardInformation;
    private int numTurn;
    private int ownerTurn;
    private Image buttonRendirse;
    private int winnerId;
    private estadoPartida estadoPartida;
    private AvisosPartida avisosPartida;
    private Image passTurn;
    private IaOne iA;
    private MyGdxGameAssetManager assetManager = new MyGdxGameAssetManager();
    private Skin skin;

    //Temporal para añadir jugador a partida,
    private Jugador jugador2 = new Jugador("Skynet", 1, assetManager, skin);


    public Partida(Jugador jugador, Skin skin, MyGdxGameAssetManager assetManager) {
        this.skin=skin;
        assetManager.loadDuelScreenImages();
        assetManager.manager.finishLoading();
        estadoPartida = estadoPartida.EMPEZADA;
        jugadores.add(jugador);
        jugadores.add(jugador2);
        selectedCard = null;
        duelLog = new DuelLog(skin,assetManager);
        tablero = new Tablero(this, assetManager);
        cardInformation = new CardInformation(this);
        numTurn = 0;
        ownerTurn=jugador.getId();
        jugador.avoidToDrawCard(true);
        winnerId = -1;
        buttonRendirse = new Image(assetManager.manager.get(assetManager.whiteFlagIcon, Texture.class));
        buttonRendirse.setPosition(POS_X_BUTTON_RENDIRSE, POS_Y_BUTTON_RENDIRSE);
        Partida partida = this;
        addListenerToButtonRendirse(partida);
        avisosPartida = new AvisosPartida();
        passTurn = new Image(assetManager.manager.get(assetManager.passTurnIcon,Texture.class));
        passTurn.setPosition(POS_X_BUTTON_PASS_TURN, POS_Y_BUTTON_PASS_TURN);
        addlistnerToPassTurnButton(partida);
        iA = new IaOne();
    }

    public Carta getSelectedCard() {
        return selectedCard;
    }

    public void setSelectedCard(Carta carta){
        selectedCard=carta;
    }

    public void addJugador(Jugador jugador) { this.jugadores.add(jugador); }

    public DuelLog getDuelLog() { return duelLog; }

    public ArrayList<Jugador> getJugadores() { return jugadores; }

    public Jugador getJugador(int jugadorId) { return jugadores.get(jugadorId); }

    public Tablero getTablero() { return this.tablero; }

    public CardInformation getCardInformation() { return cardInformation; }

    public int getNumTurn() { return numTurn; }

    public void setOwnerTurn(int idPlayer) { ownerTurn=idPlayer; }

    public int getOwnerTurn() { return ownerTurn; }

    public Image getButtonRendirse() { return buttonRendirse; }

    public int getWinnerId() { return winnerId;}

    public void setWinnerId(int playerId) {this.winnerId=playerId;}

    public Image getPassTurn() { return passTurn; }

    public AvisosPartida getAvisosPartida() { return avisosPartida; }

    public void deleteWidgets(){
        jugadores.forEach(j -> {
            j.getCementerio().setShowed(false);
            j.getCementerio().setSelected(false);
        });
        getCardInformation().getLeftArrow().setVisible(false);
        getCardInformation().getRightArrow().setVisible(false);
        getTablero().setAllSquaresToOff(getTablero());
        setSelectedCard(null);
        getCardInformation().updateCardInformation(this);
    }

    public void pasarTurno() {
        ownerTurn=1;
        numTurn++;
        jugadores.get(1).addInvocationOrbs(1);
        jugadores.get(1).getCriaturasInvocadas().forEach(c -> c.setMoved(false));
        jugadores.get(1).avoidToDrawCard(true);
        duelLog.announcePlayerPassedHisTurn(this);
        jugadores.forEach(jugador ->  jugador.getCriaturasInvocadas().forEach(criatura -> criatura.setBufferVida(criatura.getVida())));
    }

    public IaOne getiA() { return iA; }

    public void addListenerToButtonRendirse(Partida partida) {
        buttonRendirse.addListener(new ActorGestureListener() {
            @Override
            public boolean longPress(Actor actor, float x, float y) {
                winnerId=1;
                estadoPartida = estadoPartida.FINALIZADA;
                jugadores.get(0).setLives(0);
                getDuelLog().announceGiveUp(partida);
                return super.longPress(actor, x, y);
            }
        });
    }

    public void addlistnerToPassTurnButton(Partida partida) {
        passTurn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getJugador(0).getMano().desSelectCardsInHand(partida);
                if(jugadores.get(0).isAvoidToDrawCard()) {
                    avisosPartida.setAvisos(2,getJugador(0).getMano().getCartasMano().size());
                } else if(jugadores.get(ownerTurn).getMano().getCartasMano().size()>MAX_CARDS_IN_HAND) {
                    avisosPartida.setAvisos(1,getJugador(0).getMano().getCartasMano().size());
                }else {
                    getAvisosPartida().setAvisos(0,0);
                    getAvisosPartida().setShowed(false);
                    pasarTurno();
                }
                deleteWidgets();
                super.clicked(event, x, y);
            }
        });
    }

}
