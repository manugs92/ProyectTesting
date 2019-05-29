package com.mygdx.IAs;

import com.badlogic.gdx.utils.Array;
import com.mygdx.model.*;

import java.util.ArrayList;

public class IaOne {

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
                    }
                    System.out.println("WAIT FINISH");
                    break;

                case INITIAL:
                    //robar carta
                    IA.getMazo().drawCard(IA);
                    IA.addInvocationOrbs(1);

                    state = State.MOVE;
                    System.out.println("INITIAL FINISH");

                    break;
                case INVOCATION:
                    casillas = partida.getTablero().getCasillas();

                    for (Carta carta : IA.getMano().getCartasMano()) {
                        if (IA.getInvocationOrbs() > carta.getCostInvocation()) {
                            for (int i = 1; i < 7; i++) {
                                if (casillas[i][8].getCriatura() == null && !casillas[i][8].isCardInvoked()) {
                                    carta.setPosition(i, 8);
                                    carta.setFirstPosition(i,8);
                                    casillas[i][8].setCardInvoked(true);
                                    partida.addNewInvoquedMonsterJ2((Criatura) carta);
                                    IA.setInvocationOrbs(IA.getInvocationOrbs() - ((Criatura) carta).getCostInvocation());

                                    break;
                                }
                            }
                            break;
                        }
                    }

                    state = State.WAIT;
                    partida.setOwnerTurn(0);
                    partida.getJugador(0).avoidToDrawCard(true);
                    System.out.println("INITIAL FINICSH");

                    break;

                case MOVE:
                    casillas = partida.getTablero().getCasillas();
                    criaturas = partida.getCriaturasInvocadasJ2();
                    if (criaturas.size() != 0) {
                        criatura = criaturas.get(0);
                        System.out.println((int) criatura.getPosition().y - 1 + " hola ");
                        if (casillas[(int) criatura.getPosition().x][(int) criatura.getPosition().y - 1]== null) {
                            casillasMoveIa = casillas[(int) criatura.getPosition().x][(int) criatura.getPosition().y - 1].casillasDisponiblesIA(partida.getTablero(), criatura);
                            if (criatura != null) {
                                System.out.println( " GAGA ");
                                criatura.setPosition(casillasMoveIa.get(1).getCoordinatesMatrix().x, (casillasMoveIa.get(1).getCoordinatesMatrix().y));

//                } else if (/*es posible moverse*/) {
//
//
//                } else if ( /* */) {
//
//                }
                            }
                        }
                    }

                    state = State.INVOCATION;
                    System.out.println("MOVE FINISH");

                    break;


            }
        }
    }
}