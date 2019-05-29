package com.mygdx.model;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.MyGdxGameAssetManager;

import java.util.ArrayList;

import static com.mygdx.model.Casilla.MEDIDA_CASILLA;

public class Mano {

    public final static float POS_Y_MANO_J1 = 10;
    private final float POS_Y_MANO_J2 = MyGdxGame.SCREEN_HEIGHT-60;

    private ArrayList<Carta> cartasMano = new ArrayList<>();
    private int numberCardsInHand;
    private int cartaJugada;
    private boolean manoCargada;


    private ArrayList<Image> cartasManoGUI = new ArrayList<>();
    private ArrayList<Image> defaultImage = new ArrayList<>();
    private MyGdxGameAssetManager assetManager =  new MyGdxGameAssetManager();


    public Mano(Mazo mazo){
        assetManager.loadBackCard();
        assetManager.loadImagesDuelScreen();
        assetManager.manager.finishLoading();
        ArrayList<Carta> cartasToRemove = new ArrayList<>();
        for(int i=0;i<5;i++) {
            cartasMano.add(mazo.getShuffleMazo().get(i));
            cartasToRemove.add(mazo.getShuffleMazo().get(i));
            defaultImage.add(i,new Image(assetManager.manager.get(assetManager.imageBackCard, Texture.class)));
        }
        mazo.getShuffleMazo().removeAll(cartasToRemove);
        cartaJugada=0;
        manoCargada=false;
        numberCardsInHand=5;
    }

    public void drawHand(int i, Partida partida, int jugadorId) {

        partida.getJugadores().forEach(j -> {
            if(j.getCementerio().isSelected()) {
                partida.setSelectedCard(null);
                partida.getTablero().setAllSquaresToOff(partida.getTablero());
            }
        });

        Tablero tablero = partida.getTablero();

        if(jugadorId==0) {
            cartasManoGUI.add(i,new Image(cartasMano.get(i).getImage()));
            cartasManoGUI.get(i).setPosition(tablero.POS_X_TABLERO + (MEDIDA_CASILLA*i), POS_Y_MANO_J1);
        }else {
            cartasManoGUI.add(i,defaultImage.get(i));
            cartasManoGUI.get(i).setPosition(tablero.POS_X_TABLERO + (MEDIDA_CASILLA*i), POS_Y_MANO_J2);
        }

        final int finali = i;

        if(jugadorId==0) {
            //Añadimos listener a cada casilla.
            cartasManoGUI.get(i).addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {


                    Casilla[][] casillas = tablero.getCasillas();
                    tablero.setAllSquaresToOff(tablero);
                    partida.getJugadores().forEach(j -> {
                        j.getCementerio().setSelected(false);
                        j.getCementerio().setShowed(false);
                    });


                    if((partida.getSelectedCard()==null || !partida.getSelectedCard().equals(cartasMano.get(finali))) && partida.getOwnerTurn()==0 && !partida.getAvisosPartida().isShowed() && !partida.getJugador(0).isAvoidToDrawCard()) {
                        if(cartasMano.get(finali).getTipo() == Carta.Tipo.CRIATURA && cartasMano.get(finali).getCostInvocation()<=partida.getJugador(0).getInvocationOrbs()) {
                            for(int i=0;i<=casillas.length-1;i++) {
                                boolean avoidInvoke = true;
                                //Comprobamos si la casilla no tiene un monstruo invocado.
                                if(tablero.getCasilla(i,0).getCriatura()==null) {
                                    //Si no hemos invocado ninguna carta, podremos invocar.
                                    if(partida.getInvoquedCards().size()>0) {
                                        //Si hemos invocado alguna carta, hemos de buscar que cartas hemos invocado y donde están.
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



                            desSelected(partida, finali);

                        }else {
                            partida.setSelectedCard(cartasMano.get(finali));
                            partida.getCardInformation().updateCardInformation(partida);
                        }
                    }else if(partida.getSelectedCard()==null || !partida.getSelectedCard().equals(cartasMano.get(finali))) {
                        partida.setSelectedCard(cartasMano.get(finali));
                        partida.getCardInformation().updateCardInformation(partida);
                    } else {
                        partida.setSelectedCard(null);
                        partida.getJugador(0).getMano().getCartaManoGUI().get(finali).setColor(255,255,255,1f);

                        partida.getCardInformation().updateCardInformation(partida);
                    }

                    if(partida.getSelectedCard()!= null) {
                        partida.getCardInformation().setNewCardInfo(true);
                        partida.getCardInformation().setCardDetailInfo(partida.getSelectedCard());
                        partida.getCardInformation().getInfoPane().remove();
                    }
                }});

            cartasManoGUI.get(i).addCaptureListener(new ClickListener(Input.Buttons.RIGHT) {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    if(cartasMano.size()>Partida.MAX_CARDS_IN_HAND && partida.getAvisosPartida().isShowed()) {
                        partida.getJugador(0).getCementerio().setCardInGraveyard(cartasMano.get(i));
                        cartasMano.remove(cartasMano.get(i));
                        partida.setSelectedCard(null);
                        partida.getCardInformation().updateCardInformation(partida);
                        if(cartasMano.size()<=Partida.MAX_CARDS_IN_HAND) {
                            partida.deleteWidgets();
                        }else {
                            partida.getAvisosPartida().setAvisos(1,cartasMano.size());
                        }
                    }
                }
            });
        }
    }

    private void desSelected(Partida partida, int finali) {
        for (int j = 0; j < cartasMano.size(); j++) {
        partida.getJugador(0).getMano().getCartaManoGUI().get(j).setColor(255,255,255,1f);

        }
        partida.getJugador(0).getMano().getCartaManoGUI().get(finali).setColor(50,50,0,1f);

//        if(partida.getSelectedCard().equals(cartasMano.get(finali))){
//            partida.getJugador(0).getMano().getCartaManoGUI().get(finali).setColor(255,255,255,1f);
//        }
    }

    public ArrayList<Carta> getCartasMano() {
        return cartasMano;
    }

    public void setCartasMano(ArrayList<Carta> cartasMano) {
        this.cartasMano = cartasMano;
    }

    public ArrayList<Image> getCartaManoGUI() {
        return cartasManoGUI;
    }

    public int getCartaJugada() { return cartaJugada; }

    public void setCartaJugada(int cartaJugada) { this.cartaJugada = cartaJugada; }

    public boolean isManoCargada() { return manoCargada; }

    public void setManoCargada(boolean manoCargada) { this.manoCargada = manoCargada; }

    public int getCardsInHand() {
        return numberCardsInHand;
    }

    public void setCardsInHand(int number) {
        this.numberCardsInHand=number;
    }

    public void updateHand() {

        manoCargada=true;
        ArrayList<Image> cardsToRemove = new ArrayList<>();

        if(numberCardsInHand > cartasMano.size()) {
            numberCardsInHand = cartasMano.size();
            cartasManoGUI.get(cartaJugada).remove();
            cartasManoGUI.get(cartasMano.size()).remove();
            for(int i=0;i<cartasMano.size();i++) { cartasManoGUI.get(i).remove(); }
            cardsToRemove=cartasManoGUI;
            cartasManoGUI.removeAll(cardsToRemove);
            manoCargada=false;
        }

        if(numberCardsInHand < cartasMano.size()) {
            numberCardsInHand = cartasMano.size();
            for(int i=0;i<cartasMano.size()-1;i++) { cartasManoGUI.get(i).remove(); }
            cardsToRemove=cartasManoGUI;
            cartasManoGUI.removeAll(cardsToRemove);
            manoCargada=false;
        }
    }
}
