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

    private int cantidadCartas;
    public int init=0;

    private ArrayList<Mazo> mazos = new ArrayList<>();
    private ArrayList<Mano> manos = new ArrayList<>();




    public Partida(Jugador jugador, Skin skin) {
        this.jugadores.add(jugador);
        this.setSelectedCard(null);
        this.criaturasInvocadas =  new ArrayList<>();
        this.cartasColocadas = new ArrayList<>();
        this.mazos.add(jugador.getMazo());
        this.manos.add(new Mano(mazos.get(0)));
        this.cantidadCartas= manos.get(0).getMano().size();
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


    public int getCantidadCartas() { return cantidadCartas; }

    public void setCantidadCartas(int cantidadCartas) {
        this.cantidadCartas = cantidadCartas;
    }

    public Mano getManoPartida(int jugador) {
            return manos.get(jugador);
    }

    public void setManoPartida(Mano manoPartida,int jugador) {
        this.manos.set(jugador,manoPartida);
    }

    public ArrayList<Mazo> getMazos() { return mazos; }

    public void addJugador(Jugador jugador) {
        this.jugadores.add(jugador);
        this.mazos.add(jugador.getMazo());
    }

    public DuelLog getDuelLog() {
        return duelLog;
    }

    public void setDuelLog(DuelLog duelLog) {
        this.duelLog = duelLog;
    }
}
