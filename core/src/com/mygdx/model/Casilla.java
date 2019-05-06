package com.mygdx.model;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;

public class Casilla {

    public enum State {
        APAGADA, ILUMINADA
    }

    private Criatura criatura;
    private Trampa trampa;
    private Vector2 coordinatesPx = new Vector2();
    private Texture textureCasilla;
    private Texture textureCasilla2;
    private Image imageCasilla;
    private State state;

    public static final int MEDIDA_CASILLA = 48;

    public void setCriatura(Criatura criatura) {
        this.criatura = criatura;
    }

    public void setTrampa(Trampa trampa) {
        this.trampa = trampa;
    }

    public Criatura getCriatura() {
        return this.criatura;
    }

    public Trampa getTrampa() {
        return this.trampa;
    }

    public Vector2 getCoordinatesPx() {
        return coordinatesPx;
    }

    public void setPositionGUI(float x, float y) {
        this.coordinatesPx.x = x;
        this.coordinatesPx.y = y;
    }

    public Texture getTextureCasilla() {
        return textureCasilla;
    }

    public void setTextureCasilla(Texture textureCasilla) {
        this.textureCasilla = textureCasilla;
    }

    public Texture getTextureCasilla2() {
        return textureCasilla2;
    }

    public Texture getTextureCasilla2(State state) {
        if(state == State.APAGADA){
            return textureCasilla2;
        }
        if(state == State.ILUMINADA){
            return textureCasilla;
        }
        return textureCasilla;
    }

    boolean tieneCriatura(){
        return getCriatura() != null;
    }

    public void setTextureCasilla2(Texture textureCasilla2) {
        this.textureCasilla2 = textureCasilla2;
    }

    public Image getImageCasilla() {
        return imageCasilla;
    }

    public void setImageCasilla(Image imageCasilla) {
        this.imageCasilla = imageCasilla;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        if(state == State.ILUMINADA){
            getImageCasilla().setColor(255,0,255,255);
        }else if(state == State.APAGADA){
            getImageCasilla().setColor(255,255,255,255);
        }
        this.state = state;
    }

    public void addListenerToBoard(Tablero tablero, Partida partida, int x2, int y2) {
        imageCasilla.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float cx, float cy) {
                //Listener cuando tenemos una carta escogida.
                Carta selectedCard = partida.getSelectedCard();

                //si la carta seleccionada no es nula, ni mágica ni equipamiento y  donde intento colocarla esta vacia y si es interactuable(a nuestro alcance y no esta ocupada por un monstruo)
                if (selectedCard != null && selectedCard.getTipo() != Carta.Tipo.EQUIPAMIENTO && selectedCard.getTipo() != Carta.Tipo.MAGICA && Casilla.this.getCriatura() == null && Casilla.this.getState() != State.APAGADA) {
                    //si la ultima posicion x e y son distintas a -1(nunca se ha movido de la mano)y no es trampa
                    if (selectedCard.getLastPosition().x != -1 && selectedCard.getLastPosition().y != -1 && selectedCard.getTipo() != Carta.Tipo.TRAMPA) {
                        //aqui borras el monstruo de la casilla anterior.
                        tablero.getCasilla((int) selectedCard.getPosition().x, (int) selectedCard.getPosition().y).setCriatura(null);
                    }

                    //Si la carta seleccionada es una criatura o proviene de la mano..
                    if (selectedCard.getTipo() == Carta.Tipo.CRIATURA || selectedCard.getLastPosition().x == -1) {
                        //Si la carta proviene de la mano, la borraremos de la mano, y la colocaremos en el tablero.
                        if(selectedCard.getFirstPosition().x == -1 && selectedCard.getFirstPosition().y == -1) {
                            selectedCard.setFirstPosition(x2,y2);
                            partida.addNewInvoquedCard(selectedCard);
                            ArrayList<Carta> manoJ1 = partida.getManoPartida().getMano();
                            manoJ1.remove(selectedCard);
                            partida.getManoPartida().setMano(manoJ1);
                            for (int i = 0; i < tablero.getCasillas().length; i++) {
                                tablero.getCasilla(i, 0).setState(State.APAGADA);
                            }
                        }else {
                            for (int x = 0; x < tablero.getCasillas().length; x++) {
                                for (int y = 0; y < tablero.getCasillas()[x].length; y++) {
                                    tablero.getCasilla(x, y).setState(State.APAGADA);
                                }
                            }
                        }
                        selectedCard.setPosition(x2, y2);
                        selectedCard.setLastPosition(x2, y2);
                        setCriatura((Criatura) selectedCard);
                        partida.addNewInvoquedMonster((Criatura) selectedCard);
                        partida.setSelectedCard(null);


                    }
                } else {
                    if(selectedCard != null) {
                        for (int x = 0; x < tablero.getCasillas().length; x++) {
                            for (int y = 0; y < tablero.getCasillas()[x].length; y++) {
                                tablero.getCasilla(x, y).setState(State.APAGADA);
                            }
                        }
                        partida.setSelectedCard(null);
                    }else {
                        //TODO mover por tablero
                        //sin carta selecionada
                        if (tieneCriatura()) {
                            casillasDisponibles(tablero, x2, y2, partida);
                        }
                    }
                }
            }
        });
    }

    private void casillasDisponibles(Tablero tablero, int x2, int y2, Partida partida) {
        Criatura selectedCard;
        selectedCard = tablero.getCasilla(x2, y2).getCriatura();
        partida.setSelectedCard(selectedCard);
        System.out.println(criatura.getMovimiento());
        for (int x = 0; x < tablero.getCasillas().length; x++) {
            for(int y=0;y<tablero.getCasillas()[x].length;y++) {
                if (!tablero.getCasilla(x, y).tieneCriatura()) {
                    if(x<=criatura.getPosition().x+criatura.getMovimiento() && x>=criatura.getPosition().x-criatura.getMovimiento() && y<=criatura.getPosition().y+criatura.getMovimiento() && y>=criatura.getPosition().y-criatura.getMovimiento()) {
                        tablero.getCasilla(x, y).setState(State.ILUMINADA);
                    }
                }else {
                    tablero.getCasilla(x, y).setState(State.APAGADA);
                }

            }
        }
    }

}