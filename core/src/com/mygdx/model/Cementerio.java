package com.mygdx.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.MyGdxGameAssetManager;

import java.util.ArrayList;

import static com.mygdx.model.Criatura.TipoElemental.FUEGO;
import static com.mygdx.model.Criatura.TipoEspecie.DRAGON;

public class Cementerio {

    /*
    * TODO:  Mostrar flechas debajo del visor, para ir viendo todas las cartas del cementerio.
    * TODO:  A medida que se hace click en uno u otro, se ve la siguiente o la anterior. (Si llegas al final no se ve siguiente y viceversa).
    * */

    public static final float POS_X_GRAVEYARD = Tablero.POS_X_TABLERO - Casilla.MEDIDA_CASILLA;
    public static final float POS_Y_GRAVEYARD_J1 = 87;
    public static final float POS_Y_GRAVEYARD_J2 = MyGdxGame.SCREEN_HEIGHT-133;

    private ArrayList<Carta> cardsInGraveyard = new ArrayList<>();
    private Texture textureGraveyard;
    private Image graveyardGUI;
    private boolean isSelected;
    private boolean isShowed;

    public Cementerio(MyGdxGameAssetManager assetManager, Jugador jugador) {
        assetManager.loadImageDeck();
        assetManager.manager.finishLoading();
        textureGraveyard = assetManager.manager.get(assetManager.imageSquare, Texture.class);
        graveyardGUI = new Image(textureGraveyard);
        if(jugador.getId()==0) {
            graveyardGUI.setPosition(POS_X_GRAVEYARD,POS_Y_GRAVEYARD_J1);
        }else {
            graveyardGUI.setPosition(POS_X_GRAVEYARD,POS_Y_GRAVEYARD_J2);
        }
        isSelected=false;

        //Carta de prueba en cementerio.
        Texture textureCard = new Texture("icons\\handled_card.png");
        Texture textureSpriteCard= new Texture("icons\\Spritecard.png");
        Criatura golem = new Criatura(DRAGON,FUEGO,textureSpriteCard,7,10,2,1);
        golem.setNombre("DragonSp1");
        golem.setImage(textureCard);
        golem.setLastPosition(-2,-2);
        golem.setFirstPosition(-2,-2);
        cardsInGraveyard.add(golem);
    }

    public void addListenerToGraveyard(Partida partida, int jugadorId) {
        graveyardGUI.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float cx, float cy) {

                if(!partida.getJugador(jugadorId).getCementerio().isSelected()) {
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
