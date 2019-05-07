package com.mygdx.model;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.ArrayList;

public class Partida {
    //NORMAS DE PARTIDA.
    private final int MAX_CARDS_IN_HAND = 6;

    private ArrayList<Jugador> jugadores = new ArrayList<>();
    private  Carta selectedCard = new Carta();
    private ArrayList<Criatura> criaturasInvocadas;
    private ArrayList<Carta> cartasColocadas;
    private DuelLog duelLog;
    public int init=0;


    public Partida(Jugador jugador, Skin skin) {
        this.jugadores.add(jugador);
        this.setSelectedCard(null);
        this.criaturasInvocadas =  new ArrayList<>();
        this.cartasColocadas = new ArrayList<>();
        this.duelLog = new DuelLog(skin);
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

//    public Jugador getManoPartida(int i) {
//    }
}
