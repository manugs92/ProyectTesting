package com.mygdx.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.MyGdxGameAssetManager;


import java.util.ArrayList;

import static com.mygdx.model.Criatura.TipoElemental.FUEGO;
import static com.mygdx.model.Criatura.TipoEspecie.DRAGON;

public class Mazo {

    public final int MAX_CARDS = 40;
    public final int MIN_CARDS = 20;

    private ArrayList<Carta> cartasMazo = new ArrayList<>();
    private Texture textureMazo;
    private Image mazoGUI;

    public Mazo(MyGdxGameAssetManager assetManager) {
        assetManager.loadImageDeck();
        assetManager.manager.finishLoading();
        this.textureMazo = assetManager.manager.get(assetManager.imageSquare, Texture.class);
        this.mazoGUI = new Image(textureMazo);
        DefaultDeck();
    }

    public ArrayList<Carta> getCartasMazo() {
        return cartasMazo;
    }

    public void addCardToDeck(Carta carta) {
        this.cartasMazo.add(carta);
    }

    public ArrayList<Carta> DefaultDeck() {
        Texture textureCard = new Texture("icons\\handled_card.png");
        Texture textureSpriteCard= new Texture("icons\\Spritecard.png");
        for(int i=0;i<=MIN_CARDS-1;i++) {
            Criatura golem = new Criatura(DRAGON,FUEGO,textureSpriteCard,7,10,2,1);
            golem.setNombre("DragonSp");
            golem.setImage(textureCard);
            golem.setLastPosition(-1,-1);
            golem.setFirstPosition(-1,-1);
            cartasMazo.add(golem);
        }
        return cartasMazo;
    }

    public Image getMazoGUI() {
        return mazoGUI;
    }

    public void setPositionGUI(float x, float y) {
        this.mazoGUI.setPosition(x,y);
    }

}
