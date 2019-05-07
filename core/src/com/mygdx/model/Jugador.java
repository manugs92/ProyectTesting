package com.mygdx.model;

import com.mygdx.game.MyGdxGame;
import com.mygdx.game.MyGdxGameAssetManager;

public class Jugador {

    private String nombre;
    private Mazo mazo;
    private Mano mano;
    private int id;
    private int cardsInHand;

    private float posXMazo = (Tablero.POS_X_TABLERO + (Casilla.MEDIDA_CASILLA*7));
    private float posYMazoJ1 = 87;
    private float posYMazoJ2 = MyGdxGame.SCREEN_HEIGHT-133;


    public Jugador(String nombre, int id, MyGdxGameAssetManager assetManager) {
        this.nombre=nombre;
        this.mazo = new Mazo(assetManager);
        this.id=id;
        if(this.id==0) {
            mazo.setPositionGUI(posXMazo,posYMazoJ1);
            System.out.println(posXMazo+" "+posYMazoJ1);
        }else {
            mazo.setPositionGUI(posXMazo,posYMazoJ2);
        }
    }

    public void setMazo(Mazo mazo) {
        this.mazo=mazo;
    }

    public void setNombre(String nombre) {
        this.nombre=nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public Mazo getMazo() {
        return mazo;
    }

    public int getId() {
        return id;
    }

    public int getCardsInHand() {
        return cardsInHand;
    }

    public Mano getMano() {
        return mano;
    }

}
