package com.mygdx.model;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class CardInformation {

    Carta carta;
    Image cardDetailInfo;
    Table tableInfo;
    private boolean newCardInfo=false;


    public CardInformation(){

    }


    public Table updateInfoPane(){
        cardDetailInfo = carta.getCardDetailInfo();
        this.tableInfo = new Table();
            tableInfo.setPosition(0,222);
            tableInfo.addActor(cardDetailInfo);


        return tableInfo;
    }

    public boolean isNewCardInfo() {
        return newCardInfo;
    }

    public void setNewCardInfo(boolean newCardInfo) {
        this.newCardInfo = newCardInfo;
    }
}
