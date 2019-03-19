package com.mygdx.model;



import java.util.ArrayList;

public class Tablero {

    private Casilla[][] casillas= new Casilla[9][7];
    private CasillaMagica[] casillaMagicaJ1 = new CasillaMagica[3];
    private CasillaMagica[] casillaMagicaJ2  = new CasillaMagica[3];
    private ArrayList<Carta> cementerioJ1;
    private ArrayList<Carta> cementerioJ2;

    public Tablero getTablero() {
        return this;
    }

    public void setCasilla(int x, int y, Carta carta) {
        if(carta.getTipo() == Carta.Tipo.TRAMPA) {
            this.casillas[x][y].setTrampa((Trampa) carta);
        }else if(carta.getTipo() == Carta.Tipo.CRIATURA) {
            this.casillas[x][y].setCriatura((Criatura) carta);
        }
    }

    public void setCasillaMagica(int player,int pos, Carta cartaMagica) {
        if(player ==0) {
            this.casillaMagicaJ1[pos].setCartaMagica((Magica) cartaMagica);
        }else {
            this.casillaMagicaJ1[pos].setCartaMagica((Magica) cartaMagica);
        }
    }
}
