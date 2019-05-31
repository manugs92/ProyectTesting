package com.mygdx.model;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;


public class Casilla {

    public enum State {
        APAGADA, ILUMINADA,AVOID_TO_ATACK
    }

    public static final int MEDIDA_CASILLA = 48;

    private Criatura criatura;
    private Trampa trampa;
    private Vector2 coordinatesPx = new Vector2();
    private Vector2 coordinatesMatrix = new Vector2();
    private Texture textureCasilla;
    private Texture textureCasilla2;
    private Texture textureCasilla3;
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

    public void setTextureCasilla2(Texture textureCasilla2) {
        this.textureCasilla2 = textureCasilla2;
    }

    public Texture getTextureCasilla3() {
        return textureCasilla3;
    }

    public void setTextureCasilla3(Texture textureCasilla2) {
        this.textureCasilla3 = textureCasilla2;
    }

    boolean tieneCriatura() {
        return getCriatura() != null;
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
        }else if(state == State.AVOID_TO_ATACK) {
            getImageCasilla().setColor(255, 0, 255, 255);
        }else if (state == State.APAGADA) {
            getImageCasilla().setColor(255, 255, 255, 255);
        }
        this.state = state;
    }

    public void addListenerToBoard(Tablero tablero, Partida partida, int x2, int y2) {
        imageCasilla.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float cx, float cy) {

                //Indicamos que la carta seleccionada es la de la partida.
                Carta selectedCard = partida.getSelectedCard();

                //Indicamos que no estamos seleccionando ningun cementerio.
                partida.getJugadores().forEach(j -> {
                    j.getCementerio().setSelected(false);
                    j.getCementerio().setShowed(false);
                });

                //Seleccionamos la carta donde hemos hecho click. (Mediante partida, ya que la almacena ella)
                partida.getCardInformation().updateCardInformation(partida);

                //si la carta seleccionada no es nula, ni mágica ni equipamiento y  donde intento colocarla esta vacia y si es interactuable(a nuestro alcance y no esta ocupada por un monstruo) y ya hemos robado.
                if (selectedCard != null
                        && selectedCard.getTipo() != Carta.Tipo.EQUIPAMIENTO
                        && selectedCard.getTipo() != Carta.Tipo.MAGICA
                        && getCriatura() == null
                        && getState() != State.APAGADA
                        && !partida.getJugador(0).isAvoidToDrawCard())
                {
                    //si la ultima posicion x e y son distintas a -1(nunca se ha movido de la mano)y no es trampa
                    if (selectedCard.getLastPosition().x != -1
                            && selectedCard.getLastPosition().y != -1
                            && selectedCard.getTipo() != Carta.Tipo.TRAMPA)
                    {
                        //aqui borras el monstruo de la casilla anterior.
                        tablero.getCasilla((int) selectedCard.getPosition().x, (int) selectedCard.getPosition().y).setCriatura(null);
                    }

                    //Si la carta seleccionada es una criatura o proviene de la mano..
                    if (selectedCard.getTipo() == Carta.Tipo.CRIATURA || selectedCard.getLastPosition().x == -1) {
                        //Si la carta proviene de la mano, la borraremos de la mano, y la colocaremos en el tablero.
                        if (selectedCard.getFirstPosition().x == -1 && selectedCard.getFirstPosition().y == -1) {
                            invoqueFromHand(partida,selectedCard,x2,y2);
                            announceCardInvoqued(partida,selectedCard);
                            ((Criatura) selectedCard).setMoved(true);
                            setCriatura((Criatura) selectedCard);
                        }
                        else {
                            //Si nos movemos donde hay una carta invocada (Casillas principales), la mataremos.
                            if(!isInvocationSquareFree(partida,x2,y2,1)) {
                                Carta herCard = null;
                                ArrayList<Carta> invoquedCardsJ2 = partida.getJugador(1).getInvoquedCards();
                                for(int i=0;i<invoquedCardsJ2.size();i++) {
                                    if(invoquedCardsJ2.get(i).getFirstPosition().x==x2
                                            && invoquedCardsJ2.get(i).getFirstPosition().y==y2 ) {
                                        herCard = invoquedCardsJ2.get(i);
                                    }
                                }
                                removeHerCard(partida,herCard);
                                announceInvoquedCardDead(partida,selectedCard,herCard);
                                ((Criatura) selectedCard).setMoved(true);
                                System.out.println(selectedCard.getNombre());
                                System.out.println(selectedCard.getPosition().x+" "+selectedCard.getPosition().y);
                            }
                            //Si no, solamente la moveremos.
                            else {
                                selectedCard.setPosition(x2, y2);
                                selectedCard.setLastPosition(x2, y2);
                                announceCardMoved(partida,selectedCard);
                                ((Criatura) selectedCard).setMoved(true);
                                setCriatura((Criatura) selectedCard);
                            }
                        }
                        tablero.setAllSquaresToOff(tablero);
                        partida.getCardInformation().updateCardInformation(partida);
                        partida.setSelectedCard(null);
                        System.out.println(partida.getJugador(0).getCriaturasInvocadas().size());
                        partida.getJugador(0).getCriaturasInvocadas().forEach(c -> {
                            System.out.println(c.getNombre()+" "+c.getPosition().x+" "+c.getPosition().y);
                        });
                    }
                } else {
                    //Si la carta seleccionada no es nula, ni es igual a la criatura que está en la casilla donde hemos clickado.
                    if (selectedCard != null && !selectedCard.equals(tablero.getCasilla(x2, y2).getCriatura())) {

                        tablero.setAllSquaresToOff(tablero);

                        //Si la carta donde hemos hecho click pertenece a nuestro rival... PELEAREMOS CONTRA ELLA.
                        if(partida.getJugador(1).getCriaturasInvocadas().contains(tablero.getCasilla(x2,y2).getCriatura())
                                && isAvoidToInteract(selectedCard))
                        {

                            //Seleccionamos mi carta y la suya para hacerlas pelear.
                            Carta myCard = selectedCard;
                            Carta herCard = tablero.getCasilla(x2,y2).getCriatura();

                            //Obtenemos el resultado de nuestro ataque y su ataque.
                            int resultadoAtaque = ((Criatura)herCard).getDefensa() - ((Criatura) myCard).getAtaque();
                            int resultadoDefensa = ((Criatura)myCard).getDefensa() - ((Criatura) herCard).getAtaque();

                           announcePlayerAtackToMonster(partida,myCard,herCard);
                           announceResultOfAttack(partida,myCard,herCard);

                            if (resultadoAtaque<=0) {
                                removeHerCard(partida,herCard);
                                announceCardDead(partida,herCard);
                            }

                            if(resultadoDefensa<=0) {
                                removeMyCard(partida,myCard);
                                announceCardDead(partida,myCard);
                            }

                            ((Criatura) selectedCard).setMoved(true);
                        }
                        //Si la casilla seleccionada tiene criatura
                        //Y si no se ha movido
                        //Y si no hay avisos
                        //Y si hemos robado ya
                        //Y si la carta seleccionada NO pertenece al otro jugador.
                        else if(tablero.getCasilla(x2, y2).tieneCriatura()
                                && !tablero.getCasilla(x2, y2).getCriatura().isMoved()
                                && !partida.getAvisosPartida().isShowed()
                                && !partida.getJugador(0).isAvoidToDrawCard()
                                && !partida.getJugador(1).getCriaturasInvocadas().contains(getCriatura()))
                        {
                            //Deseleccionamos la carta de la mano, e indicamos donde nos podemos mover.
                            partida.getJugador(0).getMano().desSelected(partida);
                            casillasDisponibles(tablero, x2, y2, partida);
                        }
                        //Si no es ninguno de los casos anteriores.
                        else {
                            //Deseleccionamos la carta de nuestra mano, indicamos a partida que carta hemos seleccionado
                            //Y actualizamos el visor de la carta.
                            partida.getJugador(0).getMano().desSelected(partida);
                            partida.setSelectedCard(tablero.getCasilla(x2, y2).getCriatura());
                            partida.getCardInformation().updateCardInformation(partida);
                        }
                    }
                    //Si la carta seleccionada y donde hemos hecho click son igualess..
                    else if (selectedCard != null && selectedCard.equals(tablero.getCasilla(x2, y2).getCriatura())) {
                        //Deseleccionaremos t0do, para empezar de "0".
                        tablero.setAllSquaresToOff(tablero);
                        partida.setSelectedCard(null);
                        partida.getCardInformation().updateCardInformation(partida);
                        partida.getJugador(0).getMano().desSelected(partida);
                    }
                    //Si no se cumple ninguna de esas condiciones, es que no tenemos carta, y podremos seleccionar una.
                    else {
                        //Si donde hemos hecho click, hay una criatura..
                        if (tieneCriatura()) {
                            //Seleccionamos dicha carta.
                            selectedCard = tablero.getCasilla(x2, y2).getCriatura();

                            //Si la carta seleccionada no se ha movido
                            //Si no hay avisos mostrandose
                            //Si el jugador ya ha robado
                            //Si la carta que hemos seleccionado no es de nuestro rival.
                            if(!((Criatura)selectedCard).isMoved()
                                    && !partida.getAvisosPartida().isShowed()
                                    && !partida.getJugador(0).isAvoidToDrawCard()
                                    && !partida.getJugador(1).getCriaturasInvocadas().contains(getCriatura()))
                            {
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

    private void announcePlayerAtackToMonster(Partida partida,Carta myCard, Carta herCard) {
        partida.getDuelLog().addMsgToLog(myCard.getNombre().toUpperCase()+" ha LUCHADO contra "+herCard.getNombre().toUpperCase());
        partida.getDuelLog().setNewMsgTrue();
        partida.getDuelLog().getScrollPane().remove();
    }

    private void announceResultOfAttack(Partida partida,Carta myCard,Carta herCard) {
        partida.getDuelLog().addMsgToLog(myCard.getNombre().toUpperCase()+" ha PERDIDO "+ ((Criatura) herCard).getAtaque()+ " HP");
        partida.getDuelLog().addMsgToLog(herCard.getNombre().toUpperCase()+" ha PERDIDO "+ ((Criatura) myCard).getAtaque()+ "HP");
        partida.getDuelLog().setNewMsgTrue();
        partida.getDuelLog().getScrollPane().remove();
    }

    private void announceCardDead(Partida partida, Carta card) {
        partida.getDuelLog().addMsgToLog(card.getNombre().toUpperCase()+" ha MUERTO y se ha ido al CEMENTERIO");
        partida.getDuelLog().setNewMsgTrue();
        partida.getDuelLog().getScrollPane().remove();
    }

    private void announceInvoquedCardDead(Partida partida, Carta myCard, Carta herCard) {
        partida.getDuelLog().addMsgToLog(myCard.getNombre().toUpperCase()+" ha DESTRUIDO la CARTA "+myCard.getNombre().toUpperCase());
        partida.getDuelLog().setNewMsgTrue();
        partida.getDuelLog().getScrollPane().remove();
    }

    private void casillasDisponibles(Tablero tablero, int x2, int y2, Partida partida) {
        Criatura selectedCard;
        selectedCard = tablero.getCasilla(x2, y2).getCriatura();
        partida.setSelectedCard(selectedCard);
        partida.getCardInformation().updateCardInformation(partida);
        for (int x = 0; x < tablero.getCasillas().length; x++) {
            for (int y = 0; y < tablero.getCasillas()[x].length; y++) {
                avoidToMove(partida,x,y);
                avoidToAtack(partida,x,y,criatura);
                avoidToDestroyInvoquedCards(partida,x,y,1);
            }
        }
    }

    private void avoidToDestroyInvoquedCards(Partida partida,int x,int y,int playerID) {
        Carta carta;
        for(int i=0;i<partida.getJugador(playerID).getInvoquedCards().size();i++) {
            carta = partida.getJugador(playerID).getInvoquedCards().get(i);
            if(carta.getFirstPosition().x==x && carta.getFirstPosition().y==y
                    && x <= criatura.getPosition().x + criatura.getMovimiento()
                    && x >= criatura.getPosition().x - criatura.getMovimiento()
                    && y <= criatura.getPosition().y + criatura.getMovimiento()
                    && y >= criatura.getPosition().y - criatura.getMovimiento()) {
                partida.getTablero().getCasilla(x, y).setState(State.AVOID_TO_ATACK);
            }
        }
    }

    //Función que nos dice, si está dentro de nuestro alcance de interacción. (movimiento)
    public boolean isAvoidToInteract(Carta selectedCard) {
        if(criatura.getPosition().x <= selectedCard.getPosition().x + ((Criatura)selectedCard).getMovimiento() && criatura.getPosition().x >= selectedCard.getPosition().x - ((Criatura)selectedCard).getMovimiento() && criatura.getPosition().y <= selectedCard.getPosition().y + ((Criatura)selectedCard).getMovimiento() && criatura.getPosition().y >= selectedCard.getPosition().y - ((Criatura)selectedCard).getMovimiento()) {
            return true;
        }else {
            return false;
        }
    }

    public void avoidToMove(Partida partida,int x, int y) {
        if (!partida.getTablero().getCasilla(x, y).tieneCriatura()) {
            if (x <= criatura.getPosition().x + criatura.getMovimiento()
                    && x >= criatura.getPosition().x - criatura.getMovimiento()
                    && y <= criatura.getPosition().y + criatura.getMovimiento()
                    && y >= criatura.getPosition().y - criatura.getMovimiento()
                    && isInvocationSquareFree(partida,x,y,1))
            {
                partida.getTablero().getCasilla(x, y).setState(State.ILUMINADA);
            } else {
                partida.getTablero().getCasilla(x, y).setState(State.APAGADA);
            }
        }
    }

    public boolean isInvocationSquareFree(Partida partida,int x,int y,int playerID) {
        boolean result = true;
        Carta carta;
        for(int i=0;i<partida.getJugador(playerID).getInvoquedCards().size();i++) {
            carta = partida.getJugador(playerID).getInvoquedCards().get(i);
            if(carta.getFirstPosition().x==x && carta.getFirstPosition().y==y) {
                result = false;
                break;
            }
        }
        return result;
    }

    public void avoidToAtack(Partida partida, int x, int y,Criatura criatura) {
        Tablero tablero = partida.getTablero();
        if (partida.getOwnerTurn() == 0) {
            if (x <= criatura.getPosition().x + criatura.getMovimiento()
                    && x >= criatura.getPosition().x - criatura.getMovimiento()
                    && y <= criatura.getPosition().y + criatura.getMovimiento()
                    && y >= criatura.getPosition().y - criatura.getMovimiento()
                    && partida.getJugador(1).getInvoquedCards().contains(partida.getTablero().getCasilla(x, y).getCriatura())
                    && isInvocationSquareFree(partida,x,y,1)
            )
            {
                tablero.getCasilla(x, y).setState(State.AVOID_TO_ATACK);
            }
        } else {
            if (partida.getOwnerTurn() == 1) {
                if (x <= criatura.getPosition().x + criatura.getMovimiento() && x >= criatura.getPosition().x - criatura.getMovimiento() && y <= criatura.getPosition().y + criatura.getMovimiento() && y >= criatura.getPosition().y - criatura.getMovimiento() && partida.getJugador(0).getInvoquedCards().contains(partida.getTablero().getCasilla(x, y).getCriatura())) {
                    tablero.getCasilla(x, y).setState(State.AVOID_TO_ATACK);
                }
            }
        }
    }

    public Array<Casilla> casillasDisponiblesIA(Tablero tablero, Criatura criaturaIa,Jugador player) {

        Array<Casilla> casillasIa = new Array<>();
        ArrayList<Casilla> ocupedToCardEnemy = new ArrayList<Casilla>();
        Casilla actualSquare;

        player.getInvoquedCards().forEach(c -> {
         ocupedToCardEnemy.add(tablero.getCasilla(c.getPosition().x, c.getPosition().y));
        });
        for (int x = 0; x < tablero.getCasillas().length; x++) {
            for (int y = 0; y < tablero.getCasillas()[x].length; y++) {

                actualSquare=tablero.getCasilla(x,y);

                if (!actualSquare.tieneCriatura() && !ocupedToCardEnemy.contains(actualSquare)) {
                    if (x <= criaturaIa.getPosition().x + criaturaIa.getMovimiento() && x >= criaturaIa.getPosition().x - criaturaIa.getMovimiento() && y <= criaturaIa.getPosition().y + criaturaIa.getMovimiento() && y >= criaturaIa.getPosition().y - criaturaIa.getMovimiento()) {
                        casillasIa.add(tablero.getCasilla(x, y));
                    }
                }
            }
        }
        return casillasIa;
    }

    public Vector2 getCoordinatesMatrix() { return coordinatesMatrix; }

    public void setCoordinatesMatrix(Vector2 coordinatesMatrix) { this.coordinatesMatrix = coordinatesMatrix; }

    public boolean isCardInvoked() { return cardInvoked; }

    public void setCardInvoked(boolean cardInvoked) { this.cardInvoked = cardInvoked; }

    public void announceCardInvoqued(Partida partida, Carta selectedCard) {
        partida.getDuelLog().addMsgToLog(partida.getJugador(0).getNombre().toUpperCase()+" ha invocado a "+selectedCard.getNombre().toUpperCase()+" en la CASILLA "+(int)selectedCard.getLastPosition().x+","+(int)selectedCard.getLastPosition().y);
        partida.getDuelLog().setNewMsgTrue();
        partida.getDuelLog().getScrollPane().remove();
    }

    private void announceCardMoved(Partida partida, Carta selectedCard) {
        partida.getDuelLog().addMsgToLog(partida.getJugador(0).getNombre().toUpperCase()+" ha movido a "+selectedCard.getNombre().toUpperCase()+" a la CASILLA "+(int)selectedCard.getLastPosition().x+","+(int)selectedCard.getLastPosition().y);
        partida.getDuelLog().setNewMsgTrue();
        partida.getDuelLog().getScrollPane().remove();
    }

    public void invoqueFromHand(Partida partida,Carta selectedCard,int x2, int y2) {
        selectedCard.setFirstPosition(x2, y2);
        partida.getJugador(0).addNewInvoquedCard(selectedCard);
        partida.getJugador(0).addNewInvoquedMonster((Criatura) selectedCard);
        partida.getJugador(0).getMano().setCartaJugada(partida.getJugador(0).getMano().getCartasMano().indexOf(selectedCard));
        partida.getJugador(0).getMano().getCartasMano().remove(selectedCard);
        partida.getJugador(0).removeInvocationOrbs(selectedCard.getCostInvocation());
        selectedCard.setPosition(x2, y2);
        selectedCard.setLastPosition(x2, y2);
    }

    public void removeMyCard(Partida partida,Carta myCard) {
        partida.getTablero().getCasilla(myCard.getPosition().x,myCard.getPosition().y).setCriatura(null);
        partida.getJugador(0).getInvoquedCards().remove(myCard);
        partida.getJugador(0).getCriaturasInvocadas().remove(myCard);
        partida.getJugador(0).getCementerio().setCardInGraveyard(myCard);
    }

    public void removeHerCard(Partida partida,Carta herCard) {
        partida.getTablero().getCasilla(herCard.getPosition().x,herCard.getPosition().y).setCriatura(null);
        partida.getJugador(1).getInvoquedCards().remove(herCard);
        partida.getJugador(1).getCriaturasInvocadas().remove(herCard);
        partida.getJugador(1).getCementerio().setCardInGraveyard(herCard);
    }
}