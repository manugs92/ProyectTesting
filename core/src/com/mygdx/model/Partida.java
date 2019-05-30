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
    public static final int MAX_CARDS_IN_HAND = 6;
    public static final int INITIAL_LIVES = 20;
    public static final int INITIAL_INVOCATION_ORBS = 6;

    private final float posXButtonRendirse = MyGdxGame.SCREEN_WIDTH - 208;
    private final float posYButtonRendirse = MyGdxGame.SCREEN_HEIGHT /2-50;
    private final float posXButtonPassTurn = 900;
    private final float posYButtonPassTurn = 200;

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


    public Partida(Jugador jugador, Skin skin, MyGdxGameAssetManager assetManager) {
        assetManager.loadImagesDuelScreen();
        assetManager.manager.finishLoading();
        estadoPartida = estadoPartida.EMPEZADA;
        jugadores.add(jugador);
        selectedCard = null;

        //cartasColocadas = new ArrayList<>();
        duelLog = new DuelLog(skin);
        tablero=new Tablero(this, assetManager);
        cardInformation=new CardInformation(this);
        numTurn =0;
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
                jugadores.get(0).setLives(0);
                getDuelLog().addMsgToLog(getJugador(0).getNombre().toUpperCase()+" se ha rendido.");
                getDuelLog().addMsgToLog(getJugador(0).getNombre().toUpperCase()+" ha perdido el duelo.");
                getDuelLog().setNewMsgTrue();
                getDuelLog().getScrollPane().remove();
                return super.longPress(actor, x, y);
            }
        });
        avisosPartida = new AvisosPartida();
        passTurn = new Image(assetManager.manager.get(assetManager.passTurnIcon,Texture.class));
        passTurn.setPosition(posXButtonPassTurn,posYButtonPassTurn);
        Partida partida = this;
        passTurn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getJugador(0).getMano().desSelected(partida);
                if(jugadores.get(0).isAvoidToDrawCard()) {
                    avisosPartida.setAvisos(2,getJugador(0).getMano().getCartasMano().size());
                } else if(jugadores.get(ownerTurn).getMano().getCartasMano().size()>MAX_CARDS_IN_HAND) {
                    avisosPartida.setAvisos(1,getJugador(0).getMano().getCartasMano().size());
                }else {
                    getAvisosPartida().setAvisos(0,0);
                    getAvisosPartida().setShowed(false);
                    pasarTurno();
                    getDuelLog().addMsgToLog(getJugador(0).getNombre().toUpperCase()+" ha finalizado su turno.");
                    getDuelLog().setNewMsgTrue();
                    getDuelLog().getScrollPane().remove();
                }
                deleteWidgets();
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



    public void addJugador(Jugador jugador) { this.jugadores.add(jugador); }

    public DuelLog getDuelLog() { return duelLog; }

    public ArrayList<Jugador> getJugadores() { return jugadores; }

    public Jugador getJugador(int jugadorId) { return jugadores.get(jugadorId); }

    public void setDuelLog(DuelLog duelLog) { this.duelLog = duelLog; }

    public Tablero getTablero() { return this.tablero; }

    public CardInformation getCardInformation() { return cardInformation; }

    public void addTurn() { numTurn +=1; }

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
        getJugador(0).avoidToDrawCard(true);
        getJugador(0).getCriaturasInvocadas().forEach(c -> c.setMoved(false));
        //getCriaturasInvocadasJ1().forEach(c -> c.setMoved(false));
        jugadores.get(1).addInvocationOrbs(1);
    }

}
