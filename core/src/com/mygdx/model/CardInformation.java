package com.mygdx.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class CardInformation {

    private Image cardDetailInfo;
    private Table tableInfo;
    private boolean newCardInfo=true;
    private Image leftArrow = new Image(new Texture("icons/left_arrow.png"));
    private Image rightArrow = new Image(new Texture("icons/right_arrow.png"));
    private int positionInCementerio;

    public CardInformation(Partida partida) {
        leftArrow.setPosition(Tablero.POS_X_TABLERO - (Casilla.MEDIDA_CASILLA*3),Mano.POS_Y_MANO_J1);
        rightArrow.setPosition(Tablero.POS_X_TABLERO - (Casilla.MEDIDA_CASILLA*2),Mano.POS_Y_MANO_J1);
        leftArrow.setVisible(false);
        rightArrow.setVisible(false);
        positionInCementerio = 0;

        leftArrow.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (positionInCementerio > 0) {
                    positionInCementerio--;
                    setSelectedCardToPartida(partida);

                    //Actualizaremos la información de la carta.
                    partida.getCardInformation().setNewCardInfo(true);
                    //Actualizamos la carta que se verá en el preview.
                    partida.getCardInformation().setCardDetailInfo(partida.getSelectedCard());
                    partida.getCardInformation().getInfoPane().remove();
                }
            }
        });

        rightArrow.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                if(partida.getJugador(0).getCementerio().isSelected()) {
                    if(positionInCementerio<partida.getJugador(0).getCementerio().getCardsInGraveyard().size()) {
                        positionInCementerio++;
                        partida.setSelectedCard(partida.getJugador(0).getCementerio().getCardsInGraveyard().get(positionInCementerio));
                        partida.getCardInformation().setNewCardInfo(true);
                        //Actualizamos la carta que se verá en el preview.
                        partida.getCardInformation().setCardDetailInfo(partida.getSelectedCard());
                        partida.getCardInformation().getInfoPane().remove();
                    }
                }else if(partida.getJugador(1).getCementerio().isSelected()) {
                    if(positionInCementerio<partida.getJugador(1).getCementerio().getCardsInGraveyard().size()) {
                        positionInCementerio++;
                        partida.setSelectedCard(partida.getJugador(1).getCementerio().getCardsInGraveyard().get(positionInCementerio));
                        partida.getCardInformation().setNewCardInfo(true);
                        //Actualizamos la carta que se verá en el preview.
                        partida.getCardInformation().setCardDetailInfo(partida.getSelectedCard());
                        partida.getCardInformation().getInfoPane().remove();
                    }
                }
            }
        });

    }

    public Table writeInfoPane(){
        this.tableInfo = new Table();
        this.tableInfo.setPosition(0,222);
        if(cardDetailInfo!=null) {
            this.tableInfo.addActor(cardDetailInfo);
        }
        return tableInfo;
    }

    public Table getInfoPane() {
        return tableInfo;
    }

    public boolean isNewCardInfo() {
        return newCardInfo;
    }

    public void setNewCardInfo(boolean newCardInfo) {
        this.newCardInfo = newCardInfo;
    }

    public void setCardDetailInfo(Carta carta) {
        this.cardDetailInfo=carta.getCardDetailInfo();
    }

    public void updateCardInformation(Partida partida) {
        //Si tenemos una carta seleccionada, y la carta seleccionada no es igual a la carta que hay donde hemos hecho click.
        if( partida.getSelectedCard() != null) {
            //Actualizaremos la información de la carta.
            partida.getCardInformation().setNewCardInfo(true);
            //Actualizamos la carta que se verá en el preview.
            partida.getCardInformation().setCardDetailInfo(partida.getSelectedCard());
            partida.getCardInformation().getInfoPane().remove();
        }else {
            partida.getCardInformation().setNewCardInfo(false);
            partida.getCardInformation().getInfoPane().remove();
        }
    }

    public Image getLeftArrow() {
        return leftArrow;
    }

    public Image getRightArrow() {
        return rightArrow;
    }

    public void removeLeftArrow() {
        leftArrow.remove();
    }

    public void removeRightArrow() {
        rightArrow.remove();
    }

    public int getPositionInCementerio() {
        return positionInCementerio;
    }

    public void setPositionInCementerio(int positionInCementerio) {
        this.positionInCementerio = positionInCementerio;
    }

    public void setSelectedCardToPartida(Partida partida) {
        if(partida.getJugador(0).getCementerio().isSelected()) {
            partida.setSelectedCard(partida.getJugador(0).getCementerio().getCardsInGraveyard().get(positionInCementerio));
        }else if(partida.getJugador(1).getCementerio().isSelected()) {
            partida.setSelectedCard(partida.getJugador(1).getCementerio().getCardsInGraveyard().get(positionInCementerio));
        }
    }
}
