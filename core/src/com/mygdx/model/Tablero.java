package com.mygdx.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.MyGdxGameAssetManager;

import java.util.ArrayList;

import static com.mygdx.model.Casilla.MEDIDA_CASILLA;

public class Tablero {

    //Constantes del tablero (GUI)
    public static final float POS_X_TABLERO = 483;
    public final float POS_Y_TABLERO = 135;
    public final int MAX_MAGIC_CARDS = 3;
    public float POS_Y_MAGICAS_J1 = 87;
    public float POS_Y_MAGICAS_J2 = MyGdxGame.SCREEN_HEIGHT-133;

    //Constantes del tablero. (internas)
    public final int TOTAL_CASILLAS_X = 7;
    public final int TOTAL_CASILLAS_Y = 9;

    //Variables de tablero (internas)
    private Casilla[][] casillas= new Casilla[TOTAL_CASILLAS_X][TOTAL_CASILLAS_Y];
    private CasillaMagica[] casillaMagicaJ1 = new CasillaMagica[3];
    private CasillaMagica[] casillaMagicaJ2  = new CasillaMagica[3];
    private ArrayList<Carta> cementerioJ1 = new ArrayList<Carta>();
    private ArrayList<Carta> cementerioJ2 = new ArrayList<Carta>();

    public Tablero(Partida partida,MyGdxGameAssetManager assetManager) {
        assetManager.loadImagesDuelScreen();
        assetManager.manager.finishLoading();

        //Creamos casillas de movimiento del tablero.
        for(int x=0;x<=TOTAL_CASILLAS_X-1;x++) {
            for(int y=0;y<=TOTAL_CASILLAS_Y-1;y++) {
                casillas[x][y] = new Casilla();
                casillas[x][y].setState(0);
                casillas[x][y].setTextureCasilla(assetManager.manager.get(assetManager.imageSquare, Texture.class));
                casillas[x][y].setImageCasilla(new Image(casillas[x][y].getTextureCasilla()));
                casillas[x][y].setPositionGUI(POS_X_TABLERO +(MEDIDA_CASILLA*x), POS_Y_TABLERO +(MEDIDA_CASILLA*y));
                casillas[x][y].getImageCasilla().setPosition(MEDIDA_CASILLA*x,MEDIDA_CASILLA*y);
                casillas[x][y].addListenerToBoard(this,partida,x, y);
            }
        }

        //Creamos casillas mágicas J1
        for(int i=0;i<=MAX_MAGIC_CARDS-1;i++) {
            casillaMagicaJ1[i] = new CasillaMagica();
            casillaMagicaJ1[i].setTextureCasilla(assetManager.manager.get(assetManager.imageSquare, Texture.class));
            casillaMagicaJ1[i].setImageCasilla(new Image(casillaMagicaJ1[i].getTextureCasilla()));
            if(i==0) {
                casillaMagicaJ1[i].setPositionGUI(POS_X_TABLERO + (MEDIDA_CASILLA * (i + 1)),POS_Y_MAGICAS_J1);
            }
            else {
                casillaMagicaJ1[i].setPositionGUI(POS_X_TABLERO + (MEDIDA_CASILLA * (i + i + 1)),POS_Y_MAGICAS_J1);
            }
            casillaMagicaJ1[i].getImageCasilla().setPosition( casillaMagicaJ1[i].getPositionGUI().x,casillaMagicaJ1[i].getPositionGUI().y);
            casillaMagicaJ1[i].addListenerToMagicSquare(i);
            casillaMagicaJ1[i].setState(0);
        }

        //Creamos casillas mágicas J2
        for(int i=0;i<=MAX_MAGIC_CARDS-1;i++) {
            casillaMagicaJ2[i] = new CasillaMagica();
            casillaMagicaJ2[i].setTextureCasilla(assetManager.manager.get(assetManager.imageSquare, Texture.class));
            casillaMagicaJ2[i].setImageCasilla(new Image(casillaMagicaJ2[i].getTextureCasilla()));
            if(i==0) {
                casillaMagicaJ2[i].setPositionGUI(POS_X_TABLERO + (MEDIDA_CASILLA * (i + 1)),POS_Y_MAGICAS_J2);
            }
            else {
                casillaMagicaJ2[i].setPositionGUI(POS_X_TABLERO + (MEDIDA_CASILLA * (i + i + 1)),POS_Y_MAGICAS_J2);
            }
            casillaMagicaJ2[i].getImageCasilla().setPosition( casillaMagicaJ2[i].getPositionGUI().x,casillaMagicaJ2[i].getPositionGUI().y);
            casillaMagicaJ2[i].addListenerToMagicSquare(i);
            casillaMagicaJ2[i].setState(0);
        }
    }

    public Tablero getTablero() {
        return this;
    }

    public void setCasilla(int x, int y, Carta carta, int player) {
        if(carta.getTipo() == Carta.Tipo.TRAMPA) {
            //Solo se puede colocar si es <4 y >1 && player == 0 (Si player == 1; <7 y >4)
            if((y<4 && y>1 && player==0) ||(y<7 && y>4 && y==1) ) {
                casillas[x][y].setTrampa((Trampa) carta);
            }else {
                System.out.println("No puedes colocar la trampa aqui.");
            }

        }else if(carta.getTipo() == Carta.Tipo.CRIATURA) {
            casillas[x][y].setCriatura((Criatura) carta);
        }
    }

    public void setCasillaMagica(int player,int pos, Carta cartaMagica) {
        if(player ==0) {
            casillaMagicaJ1[pos].setCartaMagica((Magica) cartaMagica);
        }else {
            casillaMagicaJ2[pos].setCartaMagica((Magica) cartaMagica);
        }
    }

    public Casilla getCasilla(int x,int y) {
        return casillas[x][y];
    }

    public CasillaMagica getCasillaMagica(int player, int pos) {
        if(player==0) {
         return casillaMagicaJ1[pos];
        }else {
            return casillaMagicaJ2[pos];
        }
    }

    public Casilla[][] getCasillas() {
        return casillas;
    }

    public void addCardToGraveyard(int player,Carta carta) {
        if(player==0) {
            cementerioJ1.add(carta);
        }else{
            cementerioJ2.add(carta);
        }
    }

    public void AtacarCriatura(Criatura criaturaAtacante,int xOrigen, int yOrigen, Criatura criaturaAtacada, int xDestino, int yDestino) {
        //Comprobar ataques y defensas (para saber cual eliminar)
        //Borrar criatura de casilla.
    }
}
