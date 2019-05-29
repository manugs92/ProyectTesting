package com.mygdx.IAs;

import com.badlogic.gdx.utils.Array;
import com.mygdx.model.*;

import java.util.ArrayList;

public class IaOne {

    enum State {
        WAIT, MOVE, INITIAL
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

        while (partida.getOwnerTurn() == 1) {

            switch (state) {
                case WAIT:
                    if (partida.getOwnerTurn() == 1) {
                        state = State.INITIAL;
                    }
                    System.out.println("1");
                    break;

                case INITIAL:
                    IA = partida.getJugador(1);
                    IA.setInvocationOrbs(partida.getJugador(1).getInvocationOrbs()+1);
                    casillas = partida.getTablero().getCasillas();


                    //robar carta
                    IA.getMazo().drawCard(IA);
                    IA.getMano().getCartasMano().forEach(carta -> {

                        if (IA.getInvocationOrbs() > carta.getCostInvocation()) {
                            for (int i = 1; i < 7; i++) {
                                if (casillas[i][8].getCriatura() == null) {
                                    carta.setPosition(i, 8);
                                    partida.addNewInvoquedMonsterJ2((Criatura) carta);

                                    break;
                                }
                            }
                        }

                    });

                    state = State.MOVE;
                    System.out.println("2");

                    break;

                case MOVE:
                    casillas = partida.getTablero().getCasillas();
                    criaturas = partida.getCriaturasInvocadasJ2();
                    if (criaturas.size()!=0) {
                        criatura = criaturas.get(0);
                        System.out.println((int) criatura.getPosition().y-1+" ");
                        casillasMoveIa = casillas[(int) criatura.getPosition().x-1][(int) criatura.getPosition().y-1].casillasDisponiblesIA(partida.getTablero(), criatura);

                        if (criatura != null) {
                            criatura.setPosition(casillasMoveIa.get(0).getCoordinatesMatrix().x , (casillasMoveIa.get(0).getCoordinatesMatrix().y ));

//                } else if (/*es posible moverse*/) {
//
//
//                } else if ( /* */) {
//
//                }
                        }
                    }
                    partida.setOwnerTurn(0);
                    partida.getJugador(0).avoidToDrawCard(true);
                        state = State.WAIT;
                        System.out.println("3");

                        break;



            }
        }
    }
}