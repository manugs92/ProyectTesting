package com.mygdx.model;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.MyGdxGameAssetManager;

import java.util.ArrayList;

public class Partida {
    //NORMAS DE PARTIDA.
    private final int MAX_CARDS_IN_HAND = 6;

    private ArrayList<Jugador> jugadores = new ArrayList<>();
    private Carta selectedCard;
    private ArrayList<Criatura> criaturasInvocadas;
    private ArrayList<Carta> cartasColocadas;
    private Tablero tablero;
    private DuelLog duelLog;
    private CardInformation cardInformation;
    private int turn;
    private int ownerTurn;


    public Partida(Jugador jugador, Skin skin, MyGdxGameAssetManager assetManager) {
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

}
