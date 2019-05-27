package com.mygdx.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.MyGdxGameAssetManager;

import java.util.ArrayList;

public class Partida {

    private enum estadoPartida{ESPERANDO_JUGADORES,EMPEZADA,FINALIZADA};

    //NORMAS DE PARTIDA.
    public static final int MAX_CARDS_IN_HAND = 2;
    public static final int INITIAL_LIVES = 20;
    public static final int INITIAL_INVOCATION_ORBS = 4;

    private final float posXButtonRendirse = MyGdxGame.SCREEN_WIDTH - 208;
    private final float posYButtonRendirse = MyGdxGame.SCREEN_HEIGHT /2-50;
    private final float posXButtonPassTurn = 900;
    private final float posYButtonPassTurn = 200;

    private ArrayList<Jugador> jugadores = new ArrayList<>();
    private Carta selectedCard;
    private ArrayList<Criatura> criaturasInvocadas;
    private ArrayList<Carta> cartasColocadas;
    private Tablero tablero;
    private DuelLog duelLog;
    private CardInformation cardInformation;
    private int turn;
    private int ownerTurn;
    private Image buttonRendirse;
    private int winnerId;
    private estadoPartida estadoPartida;
    private AvisosPartida avisosPartida;
    private Image passTurn;

    public Partida(Jugador jugador, Skin skin, MyGdxGameAssetManager assetManager) {
        assetManager.loadImagesDuelScreen();
        assetManager.manager.finishLoading();
        estadoPartida = estadoPartida.EMPEZADA;
        jugadores.add(jugador);
        selectedCard = null;
        criaturasInvocadas =  new ArrayList<>();
        cartasColocadas = new ArrayList<>();
        duelLog = new DuelLog(skin);
        tablero=new Tablero(this, assetManager);
        cardInformation=new CardInformation();
        turn=0;
        ownerTurn=jugador.getId();
        jugador.avoidToDrawCard(true);
        winnerId=-1;
        buttonRendirse = new Image(assetManager.manager.get(assetManager.whiteFlagIcon, Texture.class));
        buttonRendirse.setPosition(posXButtonRendirse,posYButtonRendirse);
        buttonRendirse.addListener(new ActorGestureListener() {
            @Override
            public boolean longPress(Actor actor, float x, float y) {
                winnerId=1;
                estadoPartida = estadoPartida.FINALIZADA;
                return super.longPress(actor, x, y);
            }
        });
        avisosPartida = new AvisosPartida();
        passTurn = new Image(assetManager.manager.get(assetManager.passTurnIcon,Texture.class));
        passTurn.setPosition(posXButtonPassTurn,posYButtonPassTurn);
        passTurn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(jugadores.get(ownerTurn).getMano().getCartasMano().size()>MAX_CARDS_IN_HAND) {
                    avisosPartida.setAvisos(1,getJugador(0).getMano().getCartasMano().size());
                    getJugador(0).avoidToDrawCard(false);
                }else {
                    ownerTurn = 1;
                    turn++;
                }
                super.clicked(event, x, y);
            }
        });
    }

    public Carta getSelectedCard() {
        return selectedCard;
    }

    public void setSelectedCard(Carta carta){
        selectedCard=carta;
    }

    public void addNewInvoquedCard(Carta carta) {
        cartasColocadas.add(carta);
    }

    public  ArrayList<Carta> getInvoquedCards() {
        return cartasColocadas;
    }

    public void addNewInvoquedMonster(Criatura criatura) {
        criaturasInvocadas.add(criatura);
    }

    public  ArrayList<Criatura> getCriaturasInvocadas() {
        return criaturasInvocadas;
    }

    public void addJugador(Jugador jugador) {
        this.jugadores.add(jugador);
    }

    public DuelLog getDuelLog() {
        return duelLog;
    }

    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }

    public Jugador getJugador(int jugadorId) {
        return jugadores.get(jugadorId);
    }

    public void setDuelLog(DuelLog duelLog) {
        this.duelLog = duelLog;
    }

    public Tablero getTablero() {
        return this.tablero;
    }

    public CardInformation getCardInformation() {
        return cardInformation;
    }

    public void addTurn() {
        turn+=1;
    }

    public int getTurn() {
        return turn;
    }

    public void setOwnerTurn(int idPlayer) {
        ownerTurn=idPlayer;
    }

    public int getOwnerTurn() {
        return ownerTurn;
    }

    public Image getButtonRendirse() {
        return buttonRendirse;
    }

    public int getWinnerId() { return winnerId;}

    public void setWinnerId(int playerId) {this.winnerId=playerId;}

    public Image getPassTurn() { return passTurn; }

    public AvisosPartida getAvisosPartida() {
        return avisosPartida;
    }
}
