package com.mygdx.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.MyGdxGameAssetManager;

import java.util.ArrayList;

public class Mano {

    private float posyManoJ1 = 10;
    private float posyManoJ2 = MyGdxGame.SCREEN_HEIGHT-60;

    private ArrayList<Carta> cartasMano = new ArrayList<>();
    private boolean cartaJugada;
    private boolean manoCargada;

    private Image[] cartasManoGUI = new Image[7];
    private Image[] defaultImage = new Image[7];


    public Mano(Mazo mazo){
        MyGdxGameAssetManager assetManager =  new MyGdxGameAssetManager();
        assetManager.loadBackCard();
        assetManager.loadImagesDuelScreen();
        assetManager.manager.finishLoading();
        for(int i=0;i<5;i++) {
            this.cartasMano.add(mazo.getCartasMazo().get(i));
            this.defaultImage[i] = new Image(assetManager.manager.get(assetManager.imageBackCard, Texture.class));
        }
        this.cartaJugada=false;
        this.manoCargada=false;
    }

    public void robar(Mazo mazo){ cartasMano.add(mazo.getCartasMazo().get(0)); }

    public void drawHand(int i, Partida partida, Tablero tablero, int MEDIDA_CASILLA, int jugadorId) {

        if(jugadorId==0) {
            cartasManoGUI[i] = new Image(cartasMano.get(i).getImage());
            cartasManoGUI[i].setPosition(tablero.POS_X_TABLERO + (MEDIDA_CASILLA*i),posyManoJ1);
        }else {
            cartasManoGUI[i] = defaultImage[i];
            cartasManoGUI[i].setPosition(tablero.POS_X_TABLERO + (MEDIDA_CASILLA*i),posyManoJ2);
        }

        final int finali = i;

        if(jugadorId==0) {
            //Añadimos listener a cada casilla.
            cartasManoGUI[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {

                    //Obtenemos todas las casillas de invocación (x = 0-6) e (y = 0) y a cada una de ellas le seteamos la
                    //disponibilidad de invocación a true.
                    Casilla[][] casillas = tablero.getCasillas();
                    tablero.setAllSquaresToOff(tablero);
                    for(int i=0;i<=casillas.length-1;i++) {
                        boolean avoidInvoke = true;
                        //Comprobamos si la casilla no tiene un monstruo invocado.
                        if(tablero.getCasilla(i,0).getCriatura()==null) {
                            //Si no hemos invocado ninguna carta, podremos invocar.
                            if(partida.getInvoquedCards().size()>0) {
                                //Si hemos invocado alguna carta, hemos de buscar que cartas hemos invocado y dond están.
                                for (Carta carta : partida.getInvoquedCards()) {
                                    //Si la posición de alguna carta invocada coincide, no se podrá invocar.
                                    if(carta.getFirstPosition().x == i && carta.getFirstPosition().y == 0) {
                                        avoidInvoke = false;
                                    }
                                }
                                //Si hemos mirado todas las cartas invocadas y no hay ninguna en esa posición, podremos invocar.
                                if(avoidInvoke) {
                                    tablero.getCasilla(i,0).setState(Casilla.State.ILUMINADA);
                                }
                                //Si no tiene ningun monstruo invocado, podremos invocar.
                            }else {
                                tablero.getCasilla(i,0).setState(Casilla.State.ILUMINADA);
                            }
                        }
                    }
                    partida.setSelectedCard(cartasMano.get(finali));
                }});
        }
    }


    public ArrayList<Carta> getCartasMano() {
        return cartasMano;
    }

    public void setCartasMano(ArrayList<Carta> cartasMano) {
        this.cartasMano = cartasMano;
    }

    public Image[] getCartaManoGUI() {
        return cartasManoGUI;
    }

    public boolean isCartaJugada() { return cartaJugada; }

    public void setCartaJugada(boolean cartaJugada) { this.cartaJugada = cartaJugada; }

    public boolean isManoCargada() { return manoCargada; }

    public void setManoCargada(boolean manoCargada) { this.manoCargada = manoCargada; }
}
