package com.mygdx.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.MyGdxGameAssetManager;

import java.util.ArrayList;

import static com.mygdx.model.Casilla.MEDIDA_CASILLA;

public class Tablero {


    public final float posXTablero = 400;
    public final float posYTablero = (MyGdxGame.SCREEN_HEIGHT /5)-5;

    private Casilla[][] casillas= new Casilla[7][9];
    private CasillaMagica[] casillaMagicaJ1 = new CasillaMagica[3];
    private CasillaMagica[] casillaMagicaJ2  = new CasillaMagica[3];
    private ArrayList<Carta> cementerioJ1 = new ArrayList<Carta>();
    private ArrayList<Carta> cementerioJ2 = new ArrayList<Carta>();
    private MyGdxGameAssetManager assetManager= new MyGdxGameAssetManager();

    public Tablero(Partida partida) {
        assetManager.loadImagesDuelScreen();
        assetManager.manager.finishLoading();
        for(int x=0;x<=6;x++) {
            for(int y=0;y<=8;y++) {
                casillas[x][y] = new Casilla();
                casillas[x][y].setState(0);
                casillas[x][y].setTextureCasilla(assetManager.manager.get(assetManager.imageSquare, Texture.class));
                casillas[x][y].setImageCasilla(new Image(casillas[x][y].getTextureCasilla()));
                casillas[x][y].setPositionGUI(posXTablero+(MEDIDA_CASILLA*x),posYTablero+(MEDIDA_CASILLA*y));
                casillas[x][y].getImageCasilla().setPosition(MEDIDA_CASILLA*x,MEDIDA_CASILLA*y);
                casillas[x][y].addListenerToBoard(this,partida,x, y);
            }
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
