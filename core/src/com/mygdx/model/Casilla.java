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
error pal manu
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

    public void setCriatura(Criatura criatura) { this.criatura = criatura; }

    public void setTrampa(Trampa trampa) { this.trampa = trampa; }

    public Criatura getCriatura() { return this.criatura; }

    public Trampa getTrampa() { return this.trampa; }

    public Vector2 getCoordinatesPx() { return coordinatesPx; }

    public void setPositionGUI(float x, float y) {
        this.coordinatesPx.x = x;
        this.coordinatesPx.y = y;
    }

    public Texture getTextureCasilla() { return textureCasilla; }

    public void setTextureCasilla(Texture textureCasilla) { this.textureCasilla = textureCasilla; }

    public Texture getTextureCasilla2() { return textureCasilla2; }

    public void setTextureCasilla2(Texture textureCasilla2) { this.textureCasilla2 = textureCasilla2; }

    public Texture getTextureCasilla3() { return textureCasilla3; }

    public void setTextureCasilla3(Texture textureCasilla2) { this.textureCasilla3 = textureCasilla2; }

    boolean tieneCriatura() { return getCriatura() != null; }

    public Image getImageCasilla() { return imageCasilla; }

    public void setImageCasilla(Image imageCasilla) { this.imageCasilla = imageCasilla; }

    public State getState() { return state; }

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

    public Vector2 getCoordinatesMatrix() { return coordinatesMatrix; }

    public void setCoordinatesMatrix(Vector2 coordinatesMatrix) { this.coordinatesMatrix = coordinatesMatrix; }

    public boolean isCardInvoked() { return cardInvoked; }

    public void setCardInvoked(boolean cardInvoked) { this.cardInvoked = cardInvoked; }

    public void addListenerToBoard(Tablero tablero, Partida partida) {
        imageCasilla.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float cx, float cy) {

                //Indicamos que la carta seleccionada es la de la partida.
                Carta selectedCard = partida.getSelectedCard();

                //Ponemos por defecto los campos del jugador. (Cementerio y daño a jugador)
               setPlayerFieldsToDefault(partida);

                //si la carta seleccionada no es nula, ni mágica ni equipamiento y  donde intento colocarla esta vacia y si es interactuable(a nuestro alcance y no esta ocupada por un monstruo) y ya hemos robado.
                if (selectedCard != null && selectedCard.getTipo() != Carta.Tipo.EQUIPAMIENTO
                        && selectedCard.getTipo() != Carta.Tipo.MAGICA && getCriatura() == null
                        && getState() != State.APAGADA && !partida.getJugador(0).isAvoidToDrawCard())
                {
                    //Si la carta seleccionada es una criatura o proviene de la mano..
                    if (selectedCard.getTipo() == Carta.Tipo.CRIATURA || selectedCard.getLastPosition().x == -1) {
                        //Si la carta proviene de la mano, la borraremos de la mano, y la colocaremos en el tablero.
                        if (selectedCard.getFirstPosition().x == -1 && selectedCard.getFirstPosition().y == -1) {
                            invoqueFromHand(partida,selectedCard);
                        } else {
                            //Si nos movemos donde hay una carta invocada (Casillas principales), la mataremos.
                            if(!isInvocationSquareFree(partida,(int)getCoordinatesMatrix().x,(int)getCoordinatesMatrix().y,1)) {
                                AttackToPlacedCard(partida,selectedCard);
                            }else {
                                //Si no, solamente la moveremos.
                                //si la ultima posicion x e y son distintas a -1(nunca se ha movido de la mano)y no es trampa
                                if (selectedCard.getLastPosition().x != -1 && selectedCard.getLastPosition().y != -1
                                        && selectedCard.getTipo() != Carta.Tipo.TRAMPA)
                                {
                                    //aqui borras el monstruo de la casilla anterior.
                                    tablero.getCasilla((int) selectedCard.getPosition().x, (int) selectedCard.getPosition().y).setCriatura(null);
                                }
                                updateCreaturePosition(partida,selectedCard);
                            }
                        }
                    }
                } else {
                    //Si la carta seleccionada no es nula, ni es igual a la criatura que está en la casilla donde hemos clickado.
                    if (selectedCard != null && !selectedCard.equals(getCriatura())) {
                        //Si la carta donde hemos hecho click pertenece a nuestro rival... PELEAREMOS CONTRA ELLA.
                        if(partida.getJugador(1).getCriaturasInvocadas().contains(getCriatura())
                                && partida.getJugador(0).getCriaturasInvocadas().contains(selectedCard)
                                && isAvoidToInteract(selectedCard) && !((Criatura)selectedCard).isMoved()
                                && !partida.getJugador(0).isAvoidToDrawCard()
                        )
                        {
                            atackToCreatureEnemy(partida,selectedCard);
                        }
                        //Si la casilla seleccionada tiene criatura, ni se ha movido, ni hay avisos
                        //ni hemos robado ya, ni carta seleccionada pertenece al otro jugador.
                        else if(tieneCriatura() && !getCriatura().isMoved() && !partida.getAvisosPartida().isShowed()
                                && !partida.getJugador(0).isAvoidToDrawCard()
                                && !partida.getJugador(1).getCriaturasInvocadas().contains(getCriatura()))
                        {
                            changeToClickedCreature(partida,getCriatura());
                        }
                        //Si tiene criatura enemiga..
                        else if(tieneCriatura()){
                            tablero.setAllSquaresToOff(tablero);
                            //Deseleccionamos la carta de nuestra mano, indicamos a partida que carta hemos seleccionado
                            //Y actualizamos el visor de la carta.
                            partida.getJugador(0).getMano().desSelected(partida);
                            partida.setSelectedCard(getCriatura());
                            partida.getCardInformation().updateCardInformation(partida);
                        }
                        //Si no es ninguno de los casos anteriores.
                        else {
                            checkIfWeClickedInPlacedCard(partida);
                        }
                    }
                    //Si la carta seleccionada y donde hemos hecho click son igualess..
                    else if (selectedCard != null && selectedCard.equals(getCriatura())) {
                        //Comprobamos si la casilla donde hemos hecho click está iluminada, y si la carta seleccionada no se ha movido
                        //Y sila carta seleccionada pertenece al jugador 0, y si no hay ningun aviso activo.
                        if(getState() == State.ILUMINADA && !((Criatura)selectedCard).isMoved()
                                && partida.getJugador(0).getInvoquedCards().contains(selectedCard)
                                && !partida.getJugador(0).isAvoidToDrawCard() && !partida.getAvisosPartida().isShowed()
                        ) {
                            //Habilitamos el movimiento de dicha carta.
                            tablero.setAllSquaresToOff(tablero);
                            casillasDisponibles(tablero,partida);
                        }else {
                            //Deseleccionaremos t0do, para empezar de "0".
                            tablero.setAllSquaresToOff(tablero);
                            dropSelectedCard(partida);
                            partida.getJugador(0).getMano().desSelected(partida);
                        }
                    }
                    //Si no se cumple ninguna de esas condiciones, es que no tenemos carta, y podremos seleccionar una.
                    else {
                        //Si donde hemos hecho click, hay una criatura..
                        if (tieneCriatura()) {
                            //Seleccionamos dicha carta.
                            selectedCard = getCriatura();
                            //Si la carta seleccionada no se ha movido y Si no hay avisos mostrandose
                            //Si el jugador ya ha robado y si la carta que hemos seleccionado no es de nuestro rival.
                            if(!((Criatura)selectedCard).isMoved() && !partida.getAvisosPartida().isShowed()
                                    && !partida.getJugador(0).isAvoidToDrawCard()
                                    && !partida.getJugador(1).getCriaturasInvocadas().contains(getCriatura()))
                            {
                                casillasDisponibles(tablero, partida);
                                if(selectedCard.getPosition().y==8) {
                                    partida.getJugador(1).setAvoidToDamage(true);
                                    partida.getJugador(1).setDamageToLose(((Criatura) selectedCard).getAtaque());
                                }
                            }
                            partida.setSelectedCard(selectedCard);
                            partida.getCardInformation().updateCardInformation(partida);
                        }
                        //Si donde hemos hecho click sin carta seleccionada tampoco tiene una carta invocada..
                        else {
                            checkIfWeClickedInPlacedCard(partida);
                        }
                    }
                }
            }
        });
    }

    //Método que nos permite cambiar la carta seleccionada a la que hemos clickado.
    private void changeToClickedCreature(Partida partida, Carta selectedCard) {
        partida.getTablero().setAllSquaresToOff(partida.getTablero());
        //Deseleccionamos la carta de la mano, e indicamos donde nos podemos mover.
        partida.getJugador(0).getMano().desSelected(partida);
        partida.setSelectedCard(selectedCard);
        casillasDisponibles(partida.getTablero(),partida);
        //Si donde hemos hecho click, estaba en la zona de daño, lo habilitamos.
        if(selectedCard.getPosition().y==8) {
            partida.getJugador(1).setAvoidToDamage(true);
            partida.getJugador(1).setDamageToLose(((Criatura) selectedCard).getAtaque());
        }
    }

    //Método que nos permite atacar a una criatura enemiga.
    private void atackToCreatureEnemy(Partida partida, Carta myCard) {
        Casilla casilla = partida.getTablero().getCasilla(myCard.getPosition().x,myCard.getPosition().y);
        if(casilla.getState()!= State.ILUMINADA) {
            //Seleccionamos mi carta y la suya para hacerlas pelear.
            Carta herCard = getCriatura();
            //Obtenemos el resultado de nuestro ataque y su ataque.
            int resultadoAtaque = ((Criatura)herCard).getDefensa() - ((Criatura) myCard).getAtaque();
            int resultadoDefensa = ((Criatura)myCard).getDefensa() - ((Criatura) herCard).getAtaque();
            //Anunciamos que se ataca a un monstruo.
            partida.getDuelLog().announcePlayerAtackToMonster(partida,myCard,herCard);
            //Anunciamos el resultado del ataque.
            partida.getDuelLog().announceResultOfAttack(partida,myCard,herCard);
            //Si el resultado del ataque<=0, eliminamos su carta, y actualizamos el log.
            if (resultadoAtaque<=0) {
                removeHerCard(partida,herCard);
                partida.getDuelLog().announceCardDead(partida,herCard);
            }
            //Si el resultado de nuestra defensa es <=0, eliminamos nuestra carta y actualizamos el log.
            if(resultadoDefensa<=0) {
                removeMyCard(partida,myCard);
                partida.getDuelLog().announceCardDead(partida,myCard);
            }
            //Indicamos que nuestra carta se ha movido.
            ((Criatura) myCard).setMoved(true);
        }
        dropSelectedCard(partida);
    }

    //Método que hace que se dropee la carta seleccionada.
    private void dropSelectedCard(Partida partida) {
        partida.setSelectedCard(null);
        partida.getCardInformation().updateCardInformation(partida);
        partida.getTablero().setAllSquaresToOff(partida.getTablero());
    }

    //Método que nos permite actualizar la posicion de la carta seleccionada a una nueva posición.
    private void updateCreaturePosition(Partida partida,Carta selectedCard) {
        selectedCard.setPosition(getCoordinatesMatrix().x,getCoordinatesMatrix().y);
        selectedCard.setLastPosition(getCoordinatesMatrix().x,getCoordinatesMatrix().y);
        partida.getDuelLog().announceCardMoved(partida,selectedCard);
        ((Criatura) selectedCard).setMoved(true);
        setCriatura((Criatura) selectedCard);
        dropSelectedCard(partida);
    }

    //Método que nos permite atacar a una carta en la zona de invocación.
    private void AttackToPlacedCard(Partida partida, Carta selectedCard) {
        Carta herCard = null;
        ArrayList<Carta> invoquedCardsJ2 = partida.getJugador(1).getInvoquedCards();
        for(int i=0;i<invoquedCardsJ2.size();i++) {
            if(invoquedCardsJ2.get(i).getFirstPosition().x==getCoordinatesMatrix().x
                    && invoquedCardsJ2.get(i).getFirstPosition().y==getCoordinatesMatrix().y ) {
                herCard = invoquedCardsJ2.get(i);
            }
        }
        removeHerCard(partida,herCard);
        partida.getDuelLog().announceInvoquedCardDead(partida,selectedCard,herCard);
        ((Criatura) selectedCard).setMoved(true);
        dropSelectedCard(partida);
    }

    //Método que chequea si hemos hecho click en la zona de invocación
    private void checkIfWeClickedInPlacedCard(Partida partida) {
        //Si hacemos click en una casilla mientras tenemos una carta seleccionada.
        if(partida.getSelectedCard()!=null) {
            searchPlacedCardWhileWeHaveSelectedCard(partida);
            partida.getJugador(0).getMano().desSelected(partida);
        }
        //Si hacemos click en una casilla que tiene una carta invocada sin una carta seleccionada
        else  {
            seachPlacedCardWithNoSelectedCard(partida);
        }
    }

    //Método que busca si hay una carta invocada en esa posición sin una carta seleccionada.
    private void seachPlacedCardWithNoSelectedCard(Partida partida) {
        partida.getTablero().setAllSquaresToOff(partida.getTablero());
        partida.getJugadores().forEach(j -> {
            for (int i = 0; i < j.getInvoquedCards().size(); i++) {
                if (j.getInvoquedCards().get(i).getFirstPosition().x == getCoordinatesMatrix().x
                        && j.getInvoquedCards().get(i).getFirstPosition().y == getCoordinatesMatrix().y) {
                    partida.setSelectedCard(j.getInvoquedCards().get(i));
                    partida.getCardInformation().updateCardInformation(partida);
                    partida.getTablero().getCasilla( partida.getSelectedCard().getPosition().x, partida.getSelectedCard().getPosition().y).setState(State.ILUMINADA);
                    i = j.getInvoquedCards().size();
                }
            }
        });
    }

    //Método que busca si hay una carta invocada en esa posición teniendo una carta seleccionada.
    private void searchPlacedCardWhileWeHaveSelectedCard(Partida partida) {
        final boolean[] founded = {false};
        partida.getJugadores().forEach(j -> {
            if(!founded[0]) {
                for (int i = 0; i < j.getInvoquedCards().size(); i++) {
                    if (j.getInvoquedCards().get(i).getFirstPosition().x == getCoordinatesMatrix().x
                            && j.getInvoquedCards().get(i).getFirstPosition().y == getCoordinatesMatrix().y) {
                        //Comprobamos si es la misma carta, y de ser así la deseleccionamos,
                        if(partida.getSelectedCard().equals(j.getInvoquedCards().get(i))) {
                            System.out.println(partida.getTablero().getCasilla(partida.getSelectedCard().getPosition().x,partida.getSelectedCard().getPosition().y).getState());
                            if(partida.getTablero().getCasilla(partida.getSelectedCard().getPosition().x,partida.getSelectedCard().getPosition().y).getState()==State.APAGADA){
                                partida.getTablero().setAllSquaresToOff(partida.getTablero());
                                partida.getTablero().getCasilla(partida.getSelectedCard().getPosition().x,partida.getSelectedCard().getPosition().y).setState(State.ILUMINADA);
                            }else {
                                dropSelectedCard(partida);
                            }
                        }
                        //Si no, la seleccionamos para mostrarla.
                        else {
                            partida.getTablero().setAllSquaresToOff(partida.getTablero());
                            partida.setSelectedCard(j.getInvoquedCards().get(i));
                            partida.getCardInformation().updateCardInformation(partida);
                            partida.getTablero().getCasilla( partida.getSelectedCard().getPosition().x, partida.getSelectedCard().getPosition().y).setState(State.ILUMINADA);
                        }
                        founded[0] = true;
                    }
                    if (founded[0]) {
                        i = j.getInvoquedCards().size();
                    }
                }
            }
        });
        //Si no se ha encontrado nada, no mostraremos nada.
        if(!founded[0]) {
            dropSelectedCard(partida);
        }
    }

    //Método que nos muestra las casillas del tablero con las que podemos interactuar.
    private void casillasDisponibles(Tablero tablero, Partida partida) {
        Criatura selectedCard;
        selectedCard = getCriatura();
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

    //Método que nos muestra las casillas donode hay una criatura enemiga y le podamos atacar.
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

    //Método que nos dice, si está dentro de nuestro alcance de interacción. (movimiento)
    public boolean isAvoidToInteract(Carta selectedCard) {
        if(criatura.getPosition().x <= selectedCard.getPosition().x + ((Criatura)selectedCard).getMovimiento() && criatura.getPosition().x >= selectedCard.getPosition().x - ((Criatura)selectedCard).getMovimiento() && criatura.getPosition().y <= selectedCard.getPosition().y + ((Criatura)selectedCard).getMovimiento() && criatura.getPosition().y >= selectedCard.getPosition().y - ((Criatura)selectedCard).getMovimiento()) {
            return true;
        }else {
            return false;
        }
    }

    //Método que nos muestra las casillas donde nos podemos mover.
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

    //Método que nos indica si una casilla tiene una carta colocada.
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

    //Método que nos indica si la criatura puede atacar.
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

    //Método que le revela a la IA las casillas disponibles.
    public Array<Casilla> casillasDisponiblesIA(Tablero tablero, Criatura criaturaIa,Jugador player) {
        Array<Casilla> casillasIa = new Array<>();
        ArrayList<Casilla> ocupedToCardEnemy = new ArrayList<Casilla>();
        Casilla actualSquare;
        player.getInvoquedCards().forEach(c -> {
         ocupedToCardEnemy.add(tablero.getCasilla(c.getFirstPosition().x, c.getFirstPosition().y));
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

    //Método que invoca una criatura desde la mano.
    public void invoqueFromHand(Partida partida,Carta selectedCard) {
        selectedCard.setFirstPosition(getCoordinatesMatrix().x,getCoordinatesMatrix().y);
        selectedCard.setLastPosition(getCoordinatesMatrix().x,getCoordinatesMatrix().y);
        selectedCard.setPosition(getCoordinatesMatrix().x,getCoordinatesMatrix().y);
        partida.getJugador(0).addNewInvoquedCard(selectedCard);
        partida.getJugador(0).addNewInvoquedMonster((Criatura) selectedCard);
        partida.getJugador(0).getMano().setCartaJugada(partida.getJugador(0).getMano().getCartasMano().indexOf(selectedCard));
        partida.getJugador(0).getMano().getCartasMano().remove(selectedCard);
        partida.getJugador(0).removeInvocationOrbs(selectedCard.getCostInvocation());
        partida.getDuelLog().announceCardInvoqued(partida,selectedCard);
        ((Criatura) selectedCard).setMoved(true);
        setCriatura((Criatura) selectedCard);
        dropSelectedCard(partida);
    }

    //Método que elimina una carta nuestra.
    public void removeMyCard(Partida partida,Carta myCard) {
        partida.getTablero().getCasilla(myCard.getPosition().x,myCard.getPosition().y).setCriatura(null);
        partida.getJugador(0).getInvoquedCards().remove(myCard);
        partida.getJugador(0).getCriaturasInvocadas().remove(myCard);
        partida.getJugador(0).getCementerio().setCardInGraveyard(myCard);
    }

    //Método que elimina una carta del enemigo
    public void removeHerCard(Partida partida,Carta herCard) {
        partida.getTablero().getCasilla(herCard.getPosition().x,herCard.getPosition().y).setCriatura(null);
        partida.getTablero().getCasilla(herCard.getPosition().x,herCard.getPosition().y).setCardInvoked(false);
        partida.getJugador(1).getInvoquedCards().remove(herCard);
        partida.getJugador(1).getCriaturasInvocadas().remove(herCard);
        partida.getJugador(1).getCementerio().setCardInGraveyard(herCard);
    }

    //Método que setea los campos del jugador por defecto.
    public void setPlayerFieldsToDefault(Partida partida) {
        //Indicamos que no estamos seleccionando ningun cementerio.
        partida.getJugadores().forEach(j -> {
            j.getCementerio().setSelected(false);
            j.getCementerio().setShowed(false);
        });
        //Desactivamos el daño al otro jugador.
        partida.getJugador(1).setAvoidToDamage(false);
        partida.getJugador(1).setDamageToLose(0);
    }
}