package com.mygdx.model;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class Casilla {

    private Criatura criatura;
    private Trampa trampa;
    private Vector2 coordinatesPx = new Vector2();
    private Texture textureCasilla;
    private Image imageCasilla;
    private int state;

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

    public Image getImageCasilla() {
        return imageCasilla;
    }

    public void setImageCasilla(Image imageCasilla) {
        this.imageCasilla = imageCasilla;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void addListenerToBoard(Tablero tablero, Partida partida, int x2, int y2) {
        this.imageCasilla.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //Listener cuando tenemos una carta escogida.
                Carta selectedCard = partida.getSelectedCard();
                //si la carta seleccionada no es nula, y donde intento colocarla esta vacia y si es interactuable(a nuestro alcance y no esta ocupada por un monstruo)
                if(selectedCard !=null && Casilla.this.getCriatura()== null && Casilla.this.getState() != 0) {
                    //si la ultima posicion x e y son distintas a -1(nunca se ha movido de la mano)
                    if(selectedCard.getLastPosition().x != -1 && selectedCard.getLastPosition().y != -1) {
                       //aqui se borra la anterior posicion
                        tablero.getCasilla((int)selectedCard.getPosition().x,(int)selectedCard.getPosition().y).setCriatura(null);
                    }

                    if (selectedCard.getTipo().equals("CRIATURA") || selectedCard.getLastPosition().x == -1) {
                        selectedCard.setPosition(x2, y2);
                        selectedCard.setLastPosition(x2, y2);
                        Casilla.this.setCriatura((Criatura) selectedCard);
                        partida.addNewInvoquedMonster((Criatura) selectedCard);
                        selectedCard = null;
                        partida.setSelectedCard(selectedCard);


                        for (int i = 0; i <= tablero.getCasillas().length - 1; i++) {
                            tablero.getCasilla(i, 0).getImageCasilla().setColor(255, 255, 255, 255);
                            tablero.getCasilla(i, 0).setState(0);
                        }
                    }

                }else {
                    //TODO mover por tablero
                    //sin carta selecionada
                    sinCartaSelecionada(tablero, x2, y2, partida);
                }
            }
        });
    }

    private void sinCartaSelecionada(Tablero tablero, int x2, int y2, Partida partida) {
        if (Casilla.this.getCriatura() != null) {
            casillasDisponibles(tablero, x2, y2, partida);
        }
    }

    private void casillasDisponibles(Tablero tablero, int x2, int y2, Partida partida) {

        Criatura selectedCard;
        for (int i = 0; i <= tablero.getCasillas().length - 1; i++) {
            if (tablero.getCasilla(i, 0).getCriatura() == null) {
                tablero.getCasilla(i, 0).getImageCasilla().setColor(255, 0, 255, 255);
                tablero.getCasilla(i, 0).setState(1);
            }
            selectedCard = tablero.getCasilla(x2, y2).getCriatura();

            System.out.println(selectedCard.getAlcance());
            partida.setSelectedCard(selectedCard);
        }
    }
}
