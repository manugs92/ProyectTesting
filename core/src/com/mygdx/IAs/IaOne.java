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
                case MOVE:
                    casillas = partida.getTablero().getCasillas();
                    criaturas = partida.getCriaturasInvocadasJ2();
                    if (criaturas.size() != 0) {
                        for (Criatura criatura: criaturas) {
                            //criatura = criaturas.get(0);
                            System.out.println((int) criatura.getPosition().y - 1 + " hola ");

                            casillasMoveIa = casillas[(int) criatura.getPosition().x][(int) criatura.getPosition().y ].casillasDisponiblesIA(partida.getTablero(), criatura);

                            for (Casilla casilla:casillasMoveIa ) {
                                System.out.println(casilla.getCoordinatesMatrix());
                            }
                            if (criatura != null) {
                                moveDestiny=(int)Math.random()*(casillasMoveIa.size*1000);
                                System.out.println(" GAGA ->" +moveDestiny);
                                criatura.setPosition(casillasMoveIa.get(moveDestiny).getCoordinatesMatrix().x, (casillasMoveIa.get(moveDestiny).getCoordinatesMatrix().y));

                            }
                        }
                    }
                    state = State.INVOCATION;
                    System.out.println("MOVE FINISH");

                    break;
                case INVOCATION:
                    casillas = partida.getTablero().getCasillas();

                    for (Carta cartaMano : IA.getMano().getCartasMano()) {
                        if (IA.getInvocationOrbs() > cartaMano.getCostInvocation()) {
                            for (int i = 0; i < 6; i++) {
                                if (casillas[i][8].getCriatura() == null && !casillas[i][8].isCardInvoked()) {
                                    cartaMano.setPosition(i, 8);
                                    cartaMano.setFirstPosition(i, 8);
                                    //Informa de que la casilla ha sido ocupada por una carta
                                    casillas[i][8].setCardInvoked(true);
                                    partida.addNewInvoquedMonsterJ2((Criatura) cartaMano);
                                    IA.setInvocationOrbs(IA.getInvocationOrbs() - ((Criatura) cartaMano).getCostInvocation());

                                    break;
                                }
                            }
                            break;
                        }
                    }

                    state = State.WAIT;
                    partida.setOwnerTurn(0);
                    partida.getJugador(0).avoidToDrawCard(true);
                    partida.getJugador(0).addInvocationOrbs(1);
                    System.out.println("INITIAL FINICSH");
                    break;
            }
        }
    }
}