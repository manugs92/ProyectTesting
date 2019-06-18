package com.mygdx.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.MyGdxGame;
import com.mygdx.managers.MyGdxGameAssetManager;

import java.util.ArrayList;


public class Cementerio {
//.

    public static final float POS_X_GRAVEYARD = Tablero.POS_X_TABLERO - Casilla.MEDIDA_CASILLA;
    public static final float POS_Y_GRAVEYARD_J1 = 87;
    public static final float POS_Y_GRAVEYARD_J2 = MyGdxGame.SCREEN_HEIGHT-133;

    private ArrayList<Carta> cardsInGraveyard = new ArrayList<>();
    private Texture textureGraveyard;
    private Image graveyardGUI;
    private boolean isSelected;
    private boolean isShowed;

    public Cementerio(MyGdxGameAssetManager assetManager, Jugador jugador) {
        assetManager.loadGraveyardImages();
        assetManager.manager.finishLoading();
        textureGraveyard = assetManager.manager.get(assetManager.imageSquare, Texture.class);
        graveyardGUI = new Image(textureGraveyard);
        if(jugador.getId()==0) {
            graveyardGUI.setPosition(POS_X_GRAVEYARD,POS_Y_GRAVEYARD_J1);
        }else {
            graveyardGUI.setPosition(POS_X_GRAVEYARD,POS_Y_GRAVEYARD_J2);
        }
        isSelected=false;
    }

    //Añadimos el listener al cementerio, para poder interactuar con él.
    public void addListenerToGraveyard(Partida partida, int jugadorId) {
        graveyardGUI.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float cx, float cy) {

                partida.getJugador(0).getMano().desSelectCardsInHand(partida);
                partida.getTablero().setAllSquaresToOff(partida.getTablero());
                if(!partida.getJugador(jugadorId).getCementerio().isSelected() && cardsInGraveyard.size()>0) {
                    /*Test de criatura en el cementerio*/
                    partida.setSelectedCard(cardsInGraveyard.get(cardsInGraveyard.size()-1));
                    partida.getCardInformation().setNewCardInfo(true);
                    partida.getCardInformation().setCardDetailInfo(partida.getSelectedCard());
                    partida.getCardInformation().getInfoPane().remove();
                    partida.getTablero().setAllSquaresToOff(partida.getTablero());
                    isSelected=true;
                    isShowed=true;
                    final int fjugadorId = jugadorId;
                    partida.getJugadores().forEach( j -> {
                        if(j.getId() != fjugadorId) {
                            j.getCementerio().setShowed(false);
                            j.getCementerio().setSelected(false);
                        }
                    });
                    partida.getCardInformation().setPositionInCementerio(cardsInGraveyard.size()-1);
                    //partida.getCardInformation().getLeftArrow().setVisible(false);
                    //partida.getCardInformation().getRightArrow().setVisible(false);
                }else {
                    partida.setSelectedCard(null);
                    partida.getCardInformation().updateCardInformation(partida);
                    isSelected=false;
                    isShowed=false;
                }
            }
        });
    }


    public ArrayList<Carta> getCardsInGraveyard() { return cardsInGraveyard; }

    public void setCardsInGraveyard(ArrayList<Carta> cardsInGraveyard) { this.cardsInGraveyard = cardsInGraveyard; }

    public Texture getTextureGraveyard() { return textureGraveyard; }

    public Image getGraveyardGUI() { return graveyardGUI; }

    public void setGraveyardGUI(Image graveyardGUI) { this.graveyardGUI = graveyardGUI; }

    public boolean isSelected() { return isSelected; }

    public void setSelected(boolean selected) { isSelected = selected; }

    public boolean isShowed() {
        return isShowed;
    }

    public void setShowed(boolean showed) {
        isShowed = showed;
    }

    public void setCardInGraveyard(Carta carta) {
        this.cardsInGraveyard.add(carta);
    }
}
