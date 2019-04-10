package com.mygdx.model;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;


import java.util.ArrayList;

public class Tablero {

    private final Table tablaTablero = new Table();
    private Casilla[][] casillas = new Casilla[9][7];
    private CasillaMagica[] casillaMagicaJ1 = new CasillaMagica[3];
    private CasillaMagica[] casillaMagicaJ2 = new CasillaMagica[3];
    private ArrayList<Carta> cementerioJ1 = new ArrayList<Carta>();
    private ArrayList<Carta> cementerioJ2 = new ArrayList<Carta>();
    private Texture imagenCasilla= new Texture(Gdx.files.internal("backgrounds/bg2.jpg"));
    private boolean casillaLibre = true;

    //TODO que se pinte el tablero

    public Tablero(int posXTablero, int posYTablero) {

        tablaTablero.setPosition(posXTablero, posYTablero);
        boolean casillaInvocacion = true;

        for (int x = 0; x <= 6; x++) {

            for (int y = 0; y <= 8; y++) {
                casillas[y][x] = new Casilla(Casilla.getWIDTH() * y, Casilla.getHEIGHT() * x,imagenCasilla );
                casillas[y][x].setInvocation(casillaInvocacion);
                tablaTablero.addActor(casillas[y][x].getImagenCasilla());

            }
            casillaInvocacion = false;

        }

    }

    public void dibujarTablero(Stage stage) {
        for (Casilla[] x : casillas) {
            for (Casilla y : x){
//                y.setTexture( new Image(textureCasilla));
            }

        }
        stage.addActor(tablaTablero);

    }

    //.
    public Tablero getTablero() {
        return this;
    }


    public void setCasilla(int x, int y, Carta carta, int player) {
        if (carta.getTipo() == Carta.Tipo.TRAMPA) {
            //Solo se puede colocar si es <4 y >1 && player == 0 (Si player == 1; <7 y >4)
            if ((y < 4 && y > 1 && player == 0) || (y < 7 && y > 4 && y == 1)) {
                casillas[x][y].setTrampa((Trampa) carta);
            } else {
                System.out.println("No puedes colocar la trampa aqui.");
            }

        } else if (carta.getTipo() == Carta.Tipo.CRIATURA) {
            casillas[x][y].setCriatura((Criatura) carta);
        }
    }

    public void setCasillaMagica(int player, int pos, Carta cartaMagica) {
        if (player == 0) {
            casillaMagicaJ1[pos].setCartaMagica((Magica) cartaMagica);
        } else {
            casillaMagicaJ2[pos].setCartaMagica((Magica) cartaMagica);
        }
    }


    public Casilla getCasilla(int x, int y) {
        return casillas[x][y];
    }


    public CasillaMagica getCasillaMagica(int player, int pos) {
        if (player == 0) {
            return casillaMagicaJ1[pos];
        } else {
            return casillaMagicaJ2[pos];
        }
    }

    public Casilla[][] getCasillas() {
        return casillas;
    }


    public void addCardToGraveyard(int player, Carta carta) {
        if (player == 0) {
            cementerioJ1.add(carta);
        } else {
            cementerioJ2.add(carta);
        }
    }

    public void moverCriatura(Criatura criatura, int xOrigen, int yOrigen, int xDestino, int yDestino) {
        casillas[xOrigen][yOrigen].setCriatura(null);
        casillas[xDestino][yDestino].setCriatura(criatura);
    }

    public void AtacarCriatura(Criatura criaturaAtacante, int xOrigen, int yOrigen, Criatura criaturaAtacada, int xDestino, int yDestino) {
        //Comprobar ataques y defensas (para saber cual eliminar)
        //Borrar criatura de casilla.
    }
}
