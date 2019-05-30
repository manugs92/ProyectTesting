package com.mygdx.model;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;


public class Casilla {

    public enum State {
        APAGADA, ILUMINADA
    }

    public static final int MEDIDA_CASILLA = 48;

    private Criatura criatura;
    private Trampa trampa;
    private Vector2 coordinatesPx = new Vector2();
    private Vector2 coordinatesMatrix = new Vector2();
    private Texture textureCasilla;
    private Texture textureCasilla2;
    private Image imageCasilla;
    private State state;
    private boolean cardInvoked;

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

    boolean tieneCriatura() {
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
        if (state == State.ILUMINADA) {
            getImageCasilla().setColor(0, 255, 255, 255);
        } else if (state == State.APAGADA) {
            getImageCasilla().setColor(255, 255, 255, 255);
        }
        this.state = state;
    }

    public void addListenerToBoard(Tablero tablero, Partida partida, int x2, int y2) {
        imageCasilla.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float cx, float cy) {

                Carta selectedCard = partida.getSelectedCard();

                partida.getJugadores().forEach(j -> {
                    partida.setSelectedCard(null);
                    j.getCementerio().setSelected(false);
                    j.getCementerio().setShowed(false);
                });

                //Seleccionamos la carta donde hemos hecho click. (Mediante partida, ya que la almacena ella)
                partida.getCardInformation().updateCardInformation(partida);

                //si la carta seleccionada no es nula, ni mágica ni equipamiento y  donde intento colocarla esta vacia y si es interactuable(a nuestro alcance y no esta ocupada por un monstruo) y ya hemos robado.
                if (selectedCard != null && selectedCard.getTipo() != Carta.Tipo.EQUIPAMIENTO && selectedCard.getTipo() != Carta.Tipo.MAGICA && getCriatura() == null && getState() != State.APAGADA && !partida.getJugador(0).isAvoidToDrawCard()) {
                    //si la ultima posicion x e y son distintas a -1(nunca se ha movido de la mano)y no es trampa
                    if (selectedCard.getLastPosition().x != -1 && selectedCard.getLastPosition().y != -1 && selectedCard.getTipo() != Carta.Tipo.TRAMPA) {
                        //aqui borras el monstruo de la casilla anterior.
                        tablero.getCasilla((int) selectedCard.getPosition().x, (int) selectedCard.getPosition().y).setCriatura(null);
                    }

                    //Si la carta seleccionada es una criatura o proviene de la mano..
                    if (selectedCard.getTipo() == Carta.Tipo.CRIATURA || selectedCard.getLastPosition().x == -1) {
                        //Si la carta proviene de la mano, la borraremos de la mano, y la colocaremos en el tablero.
                        if (selectedCard.getFirstPosition().x == -1 && selectedCard.getFirstPosition().y == -1) {
                            selectedCard.setFirstPosition(x2, y2);
                            partida.getJugador(0).addNewInvoquedCard(selectedCard);
                            partida.getJugador(0).addNewInvoquedMonster((Criatura) selectedCard);
                            partida.getJugador(0).getMano().setCartaJugada(partida.getJugador(0).getMano().getCartasMano().indexOf(selectedCard));
                            partida.getJugador(0).getMano().getCartasMano().remove(selectedCard);
                            partida.getJugador(0).removeInvocationOrbs(selectedCard.getCostInvocation());
                            for (int i = 0; i < tablero.getCasillas().length; i++) {
                                tablero.getCasilla(i, 0).setState(State.APAGADA);
                            }
                            selectedCard.setPosition(x2, y2);
                            selectedCard.setLastPosition(x2, y2);
                            partida.getDuelLog().addMsgToLog(partida.getJugador(0).getNombre().toUpperCase()+" ha invocado a "+selectedCard.getNombre().toUpperCase()+" en la CASILLA "+(int)selectedCard.getLastPosition().x+","+(int)selectedCard.getLastPosition().y);
                            partida.getDuelLog().setNewMsgTrue();
                            partida.getDuelLog().getScrollPane().remove();
                        } else {
                            tablero.setAllSquaresToOff(tablero);
                            selectedCard.setPosition(x2, y2);
                            selectedCard.setLastPosition(x2, y2);
                            partida.getDuelLog().addMsgToLog(partida.getJugador(0).getNombre().toUpperCase()+" ha movido a "+selectedCard.getNombre().toUpperCase()+" a la CASILLA "+(int)selectedCard.getLastPosition().x+","+(int)selectedCard.getLastPosition().y);
                            partida.getDuelLog().setNewMsgTrue();
                            partida.getDuelLog().getScrollPane().remove();
                        }
                        ((Criatura) selectedCard).setMoved(true);
                        setCriatura((Criatura) selectedCard);
                        partida.getCardInformation().updateCardInformation(partida);
                        partida.setSelectedCard(null);
                    }
                } else {
                    if (selectedCard != null && !selectedCard.equals(tablero.getCasilla(x2, y2).getCriatura())) {
                        tablero.setAllSquaresToOff(tablero);
                        if(tablero.getCasilla(x2, y2).tieneCriatura() && !tablero.getCasilla(x2, y2).getCriatura().isMoved() && !partida.getAvisosPartida().isShowed() && !partida.getJugador(0).isAvoidToDrawCard() && !partida.getJugador(1).getCriaturasInvocadas().contains(getCriatura())) {
                            partida.getJugador(0).getMano().desSelected(partida);
                            casillasDisponibles(tablero, x2, y2, partida);
                            casillasAvoidToAtack(tablero,x2,y2,partida);
                        } else {
                            partida.getJugador(0).getMano().desSelected(partida);
                            partida.setSelectedCard(tablero.getCasilla(x2, y2).getCriatura());
                            partida.getCardInformation().updateCardInformation(partida);
                        }
                    } else if (selectedCard != null && selectedCard.equals(tablero.getCasilla(x2, y2).getCriatura())) {
                        tablero.setAllSquaresToOff(tablero);
                        partida.setSelectedCard(null);
                        partida.getCardInformation().updateCardInformation(partida);
                        partida.getJugador(0).getMano().desSelected(partida);
                    } else {
                        //sin carta selecionada
                        if (tieneCriatura()) {
                            selectedCard = tablero.getCasilla(x2, y2).getCriatura();
                            if(!((Criatura)selectedCard).isMoved() && !partida.getAvisosPartida().isShowed() && !partida.getJugador(0).isAvoidToDrawCard() && !partida.getJugador(1).getCriaturasInvocadas().contains(getCriatura())) {
                                casillasDisponibles(tablero, x2, y2, partida);
                            }
                            partida.setSelectedCard(selectedCard);
                            partida.getCardInformation().updateCardInformation(partida);
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
        partida.getCardInformation().updateCardInformation(partida);
        for (int x = 0; x < tablero.getCasillas().length; x++) {
            for (int y = 0; y < tablero.getCasillas()[x].length; y++) {
                if (!tablero.getCasilla(x, y).tieneCriatura()) {
                    if (x <= criatura.getPosition().x + criatura.getMovimiento() && x >= criatura.getPosition().x - criatura.getMovimiento() && y <= criatura.getPosition().y + criatura.getMovimiento() && y >= criatura.getPosition().y - criatura.getMovimiento()) {
                        tablero.getCasilla(x, y).setState(State.ILUMINADA);
                    }
                } else {
                    tablero.getCasilla(x, y).setState(State.APAGADA);
                }
            }
        }
    }

    private void casillasAvoidToAtack(Tablero tablero, int x2, int y2, Partida partida) {
        Criatura selectedCard;
        selectedCard = tablero.getCasilla(x2, y2).getCriatura();
        partida.setSelectedCard(selectedCard);
        partida.getCardInformation().updateCardInformation(partida);
        for (int x = 0; x < tablero.getCasillas().length; x++) {
            for (int y = 0; y < tablero.getCasillas()[x].length; y++) {
                if (tablero.getCasilla(x, y).tieneCriatura()) {
                    //TODO: iluminar en rojo, para atacar.
                    if (x <= criatura.getPosition().x + criatura.getAlcance() && x >= criatura.getPosition().x - criatura.getAlcance() && y <= criatura.getPosition().y + criatura.getAlcance() && y >= criatura.getPosition().y - criatura.getAlcance()) {
                        tablero.getCasilla(x, y).setState(State.ILUMINADA);
                    }
                } else {
                    tablero.getCasilla(x, y).setState(State.APAGADA);
                }
            }
        }
    }

    public Array<Casilla> casillasDisponiblesIA(Tablero tablero, Criatura criaturaIa) {

        Array<Casilla> casillasIa = new Array<>();
        for (int x = 0; x < tablero.getCasillas().length; x++) {
            for (int y = 0; y < tablero.getCasillas()[x].length; y++) {
                if (!tablero.getCasilla(x,y).tieneCriatura()) {
                    if (x <= criaturaIa.getPosition().x + criaturaIa.getMovimiento() && x >= criaturaIa.getPosition().x - criaturaIa.getMovimiento() && y <= criaturaIa.getPosition().y + criaturaIa.getMovimiento() && y >= criaturaIa.getPosition().y - criaturaIa.getMovimiento()) {
                        casillasIa.add(tablero.getCasilla(x, y));
                    }
                }
            }
        }
        return casillasIa;
    }


    public Vector2 getCoordinatesMatrix() {
        return coordinatesMatrix;
    }

    public void setCoordinatesMatrix(Vector2 coordinatesMatrix) {
        this.coordinatesMatrix = coordinatesMatrix;
    }

    public boolean isCardInvoked() {
        return cardInvoked;
    }

    public void setCardInvoked(boolean cardInvoked) {
        this.cardInvoked = cardInvoked;
    }
}