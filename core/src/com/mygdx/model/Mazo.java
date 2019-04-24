package com.mygdx.model;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

import static com.mygdx.model.Criatura.TipoElemental.FUEGO;
import static com.mygdx.model.Criatura.TipoEspecie.DRAGON;

public class Mazo {

    public final int MAX_CARDS = 40;
    public final int MIN_CARDS = 20;

    private ArrayList<Carta> mazo = new ArrayList<>();

    public ArrayList<Carta> getMazo() {
        return mazo;
    }

    public void addCardToDeck(Carta carta) {
        this.mazo.add(carta);
    }

    public ArrayList<Carta> DefaultDeck() {
        Texture textureCard = new Texture("icons\\handled_card.png");
        Texture textureSpriteCard= new Texture("icons\\Spritecard.png");
        for(int i=0;i<=MIN_CARDS-1;i++) {
            Criatura golem = new Criatura(DRAGON,FUEGO,textureSpriteCard,7,10,3,1);
            golem.setNombre("DragonSp");
            golem.setImage(textureCard);
            golem.setLastPosition(-1,-1);
            golem.setFirstPosition(-1,-1);
            mazo.add(golem);
        }
        return mazo;
    }

}
