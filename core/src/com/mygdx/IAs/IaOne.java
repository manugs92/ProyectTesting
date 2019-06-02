package com.mygdx.IAs;

import com.badlogic.gdx.utils.Array;
import com.mygdx.model.*;

import java.util.ArrayList;

/* TODO que las criaturas IA no puedan pisar las cartas enemigas*/

public class IaOne {

    private int moveDestiny;

    public enum State {
        WAIT, INITIAL, MOVE, INVOCATION, FINAL
    }

    State state;
    int turn;
    Casilla[][] casillas;
    ArrayList<Criatura> criaturas = new ArrayList<Criatura>();
    ArrayList<Carta> cardsToRemove = new ArrayList<>();
    Array<Casilla> casillasMoveIa;
    Criatura criatura;
    Jugador IA;



    public IaOne() {
        state = State.WAIT;
    }

    public State getState() { return state; }

    public void setState(State state) { this.state = state; }

    public void play(Partida partida, float delta) {

        IA = partida.getJugador(1);
        switch (state) {
            case WAIT:
                state = State.INITIAL;
                break;

            case INITIAL:
                logInfoTourn(partida,1);
                //robar carta
                IA.getMazo().drawCard(IA);
                IA.getMano().addDefaultImage();
                IA.getMano().updateHand(1);
                state = State.INVOCATION;
                break;

            case INVOCATION:
                casillas = partida.getTablero().getCasillas();
                for (Carta cartaMano : IA.getMano().getCartasMano()) {
                    if (IA.getInvocationOrbs() > cartaMano.getCostInvocation()) {
                        for (int i = 0; i < 7; i++) {
                            if (casillas[i][8].getCriatura() == null && !casillas[i][8].isCardInvoked()) {
                                invocationCreature(partida, cartaMano, i);
                                logInfoInvocation(partida, cartaMano);
                                i=6;
                            }
                        }
                    }
                }
                IA.getMano().getCartasMano().removeAll(cardsToRemove);
                IA.getMano().updateHand(1);
                state = State.MOVE;
                break;

            case MOVE:
                casillas = partida.getTablero().getCasillas();
                criaturas = partida.getJugador(1).getCriaturasInvocadas();
                if (criaturas.size()-1 > 0) {
                    for (Criatura criatura: criaturas) {
                        casillasMoveIa = casillas[0][0].casillasDisponiblesIA(partida.getTablero(), criatura,partida.getJugador(0));
                        if (criatura != null && !criatura.isMoved()) {
                            moveDestiny = calcMoveToDestinity(casillasMoveIa);
                            updatePosition(partida, criatura, moveDestiny);
                            logInfoMove(partida, criatura);
                        }
                    }
                }
                state = State.FINAL;
                break;

            case FINAL:

                    //Comprobamos si hemos perido via por alguna trampa, si estamos muertos no
                    if (IA.getLives()<0){
                        partida.setWinnerId(1);

                    }

                checkAndDescardCard();
                finalizeTurn(partida);
                logInfoTourn(partida,0);
                cardsToRemove = new ArrayList<>();
                IA.getMano().updateHand(1);
                state = State.WAIT;
                partida.getJugador(1).getInvoquedCards().forEach(c -> ((Criatura)c).setMoved(false));
                break;
        }
    }

    public void iaIsThinking(long timer) {
        while(((System.nanoTime() / 1000) - timer)  < 1000000) {
        }
    }

    private int calcMoveToDestinity(Array<Casilla> casillasMoveIa) {
        /*Con el size al cuadrado, sabremos el numero de casillas dispoibles por fila y por columna (en caso de poder movernos en 9 filas, son 3 por columna)
         *con el modulo de size, nos aseguraremos de que llege del 0 al 2, de esta manera al multiplicarlo por 3 (size)
         * siempre se obtendra las posiziones inferiores (0,3,6)  */
        //moveDestiny=(((int)(Math.random()*( Math.sqrt(size+1)))%size)*size  );

        int moveDestiny = (int)(Math.random()*((casillasMoveIa.size-1)/2));
        if(moveDestiny>casillasMoveIa.size) {
            moveDestiny = calcMoveToDestinity(casillasMoveIa);
        }
        return moveDestiny;
    }

    private void checkAndDescardCard() {
        if(IA.getMano().getCartasMano().size()>Partida.MAX_CARDS_IN_HAND){
            int cardToRemove = chooseCardToRemove(IA.getMano().getCartasMano().size()-1);
            IA.getCementerio().setCardInGraveyard( IA.getMano().getCartasMano().get(cardToRemove));
            IA.getMano().getCartasMano().remove(cardToRemove);
        }
    }

    private int chooseCardToRemove(int handSize) {
        int cardToRemove=(int)(Math.random()*Partida.MAX_CARDS_IN_HAND);
        if(cardToRemove>handSize) {
            chooseCardToRemove(handSize);
        }
        return cardToRemove;
    }

    private void invocationCreature(Partida partida, Carta cartaMano, int i) {
        cartaMano.setPosition(i, 8);
        cartaMano.setFirstPosition(i, 8);
        cartaMano.setLastPosition(i,8);
        if(cartaMano.getTipo() == Carta.Tipo.CRIATURA) {
            ((Criatura) cartaMano).setMoved(true);
        }

        cardsToRemove.add(cartaMano);

        //Informa de que la casilla ha sido ocupada por una carta
        casillas[i][8].setCardInvoked(true);
        casillas[i][8].setCriatura((Criatura) cartaMano);
        partida.getJugador(1).addNewInvoquedMonster((Criatura) cartaMano);
        partida.getJugador(1).addNewInvoquedCard(cartaMano);
        IA.addInvocationOrbs(-(cartaMano).getCostInvocation());
    }

    private void logInfoInvocation(Partida partida, Carta cartaMano) {
        partida.getDuelLog().addMsgToLog(partida.getJugador(1).getNombre().toUpperCase() +" ha invocado a "+cartaMano.getNombre().toUpperCase()+" en la CASILLA "+(int)cartaMano.getLastPosition().x+","+(int)cartaMano.getLastPosition().y);
        partida.getDuelLog().setNewMsgTrue();
        partida.getDuelLog().getScrollPane().remove();
    }

    private void logInfoTourn(Partida partida, int idPlayer) {
        partida.getDuelLog().addMsgToLog("Turno de "+partida.getJugador(idPlayer).getNombre().toUpperCase());
        partida.getDuelLog().setNewMsgTrue();
        partida.getDuelLog().getScrollPane().remove();
    }

    private void finalizeTurn(Partida partida) {
        partida.setOwnerTurn(0);
        partida.getJugador(0).avoidToDrawCard(true);
        partida.getJugador(0).addInvocationOrbs(1);
    }

    private void logInfoMove(Partida partida, Criatura criatura) {
        partida.getDuelLog().addMsgToLog(partida.getJugador(1).getNombre().toUpperCase() + " ha movido a " + criatura.getNombre().toUpperCase() + " a la CASILLA " + (int) criatura.getLastPosition().x + "," + (int) criatura.getLastPosition().y);
        partida.getDuelLog().setNewMsgTrue();
        partida.getDuelLog().getScrollPane().remove();
    }

    private void updatePosition(Partida partida, Criatura criatura, int moveDestiny) {
        //borrar casilla anterior
        partida.getTablero().getCasilla(criatura.getLastPosition().x, criatura.getLastPosition().y).setCriatura(null);
        criatura.setPosition(casillasMoveIa.get(moveDestiny).getCoordinatesMatrix().x, (casillasMoveIa.get(moveDestiny).getCoordinatesMatrix().y));
        criatura.setLastPosition(casillasMoveIa.get(moveDestiny).getCoordinatesMatrix().x, (casillasMoveIa.get(moveDestiny).getCoordinatesMatrix().y));
        criatura.setMoved(true);
        //seteamos la criatura en la casilla
        partida.getTablero().getCasilla(criatura.getLastPosition().x, criatura.getLastPosition().y).setCriatura(criatura);
    }
}