package com.mygdx.IAs;

import com.badlogic.gdx.utils.Array;
import com.mygdx.model.*;

import java.util.ArrayList;

public class IaOne {

    private int moveDestiny;

    enum State {
        WAIT, INITIAL, MOVE, INVOCATION
    }

    State state;
    int turn;
    Casilla[][] casillas;
    ArrayList<Criatura> criaturas = new ArrayList<Criatura>();
    Array<Casilla> casillasMoveIa;
    Criatura criatura;
    Jugador IA;


    public IaOne() {
        state = State.WAIT;
    }


    public void iaMove(Partida partida) {
        IA = partida.getJugador(1);
        while (partida.getOwnerTurn() == 1) {
            switch (state) {
                case WAIT:
                    if (partida.getOwnerTurn() == 1) {
                        state = State.INITIAL;
                        partida.getDuelLog().addMsgToLog("Turno de "+partida.getJugador(1).getNombre().toUpperCase());
                        partida.getDuelLog().setNewMsgTrue();
                        partida.getDuelLog().getScrollPane().remove();
                    }
                    //System.out.println("WAIT FINISH");
                    break;
                case INITIAL:
                    //robar carta
                    IA.getMazo().drawCard(IA);
                    IA.addInvocationOrbs(1);

                    state = State.INVOCATION;
                    //System.out.println("INITIAL FINISH");

                    break;
                case INVOCATION:
                    casillas = partida.getTablero().getCasillas();

                    for (Carta cartaMano : IA.getMano().getCartasMano()) {
                        if (IA.getInvocationOrbs() > cartaMano.getCostInvocation()) {
                            for (int i = 0; i < 6; i++) {
                                if (casillas[i][8].getCriatura() == null && !casillas[i][8].isCardInvoked()) {
                                    cartaMano.setPosition(i, 8);
                                    cartaMano.setFirstPosition(i, 8);
                                    cartaMano.setLastPosition(i,8);
                                    //Informa de que la casilla ha sido ocupada por una carta
                                    casillas[i][8].setCardInvoked(true);
                                    casillas[i][8].setCriatura((Criatura) cartaMano);
                                    partida.getJugador(1).addNewInvoquedMonster((Criatura) cartaMano);
                                    partida.getJugador(1).addNewInvoquedCard(cartaMano);
                                    IA.setInvocationOrbs(IA.getInvocationOrbs() - ((Criatura) cartaMano).getCostInvocation());

                                    partida.getDuelLog().addMsgToLog(partida.getJugador(1).getNombre().toUpperCase()+" ha invocado a "+cartaMano.getNombre().toUpperCase()+" en la CASILLA "+(int)cartaMano.getLastPosition().x+","+(int)cartaMano.getLastPosition().y);
                                    partida.getDuelLog().setNewMsgTrue();
                                    partida.getDuelLog().getScrollPane().remove();

                                    break;
                                }
                            }
                            break;
                        }
                    }

                    state = State.MOVE;


                    partida.getDuelLog().addMsgToLog("Turno de "+partida.getJugador(0).getNombre().toUpperCase());
                    partida.getDuelLog().setNewMsgTrue();
                    partida.getDuelLog().getScrollPane().remove();
                    break;
                case MOVE:
                    casillas = partida.getTablero().getCasillas();
                    //criaturas = partida.getCriaturasInvocadasJ2();
                    criaturas = partida.getJugador(1).getCriaturasInvocadas();
                    ArrayList<Criatura> temporal = new ArrayList<>();
                    if (criaturas.size() != 0) {
                        for (Criatura criatura: criaturas) {
                            //criatura = criaturas.get(0);
                            System.out.println((int) criatura.getPosition().y - 1 + " hola ");

                            casillasMoveIa = casillas[(int) criatura.getPosition().x][(int) criatura.getPosition().y ].casillasDisponiblesIA(partida.getTablero(), criatura);

                            if (criatura != null) {
                                moveDestiny=(int)Math.random()*(casillasMoveIa.size*1000);
                                System.out.println(" GAGA ->" +moveDestiny);

                                updatePosition(partida, criatura);
                                logInfoMove(partida, criatura);

                                temporal.add(criatura);
                            }
                        }
                        partida.getJugador(1).addInvoquedMonsters(temporal);
                    }
                    state = State.WAIT;
                    partida.setOwnerTurn(0);
                    partida.getJugador(0).avoidToDrawCard(true);
                    partida.getJugador(0).addInvocationOrbs(1);
                    System.out.println("INITIAL FINICSH");
                    System.out.println("MOVE FINISH");

                    break;

            }
        }
    }
    private void logInfoMove(Partida partida, Criatura criatura) {
        partida.getDuelLog().addMsgToLog(partida.getJugador(1).getNombre().toUpperCase() + " ha movido a " + criatura.getNombre().toUpperCase() + " a la CASILLA " + (int) criatura.getLastPosition().x + "," + (int) criatura.getLastPosition().y);
        partida.getDuelLog().setNewMsgTrue();
        partida.getDuelLog().getScrollPane().remove();
    }

    private void updatePosition(Partida partida, Criatura criatura) {
        //borrar casilla anterior
        partida.getTablero().getCasilla(criatura.getLastPosition().x, criatura.getLastPosition().y).setCriatura(null);
        criatura.setPosition(casillasMoveIa.get(moveDestiny).getCoordinatesMatrix().x, (casillasMoveIa.get(moveDestiny).getCoordinatesMatrix().y));
        criatura.setLastPosition(casillasMoveIa.get(moveDestiny).getCoordinatesMatrix().x, (casillasMoveIa.get(moveDestiny).getCoordinatesMatrix().y));
        criatura.setMoved(true);
        //seteamos la criatura en la casilla
        partida.getTablero().getCasilla(criatura.getLastPosition().x, criatura.getLastPosition().y).setCriatura(criatura);
    }
}