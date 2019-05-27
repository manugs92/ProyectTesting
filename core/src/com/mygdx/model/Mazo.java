package com.mygdx.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.MyGdxGameAssetManager;


import java.util.*;

import static com.mygdx.model.Criatura.TipoElemental.FUEGO;
import static com.mygdx.model.Criatura.TipoEspecie.DRAGON;

public class Mazo {

    private final int MAX_CARDS = 40;
    private final int MIN_CARDS = 20;

    private final float POS_X_MAZO = Tablero.POS_X_TABLERO + (Casilla.MEDIDA_CASILLA*7);
    private final float POS_Y_MAZO_J1 = 87;
    private final float POS_Y_MAZO_J2 = MyGdxGame.SCREEN_HEIGHT-133;

    private ArrayList<Carta> cartasMazo = new ArrayList<>();
    private ArrayList<Carta> shuffleMazo = new ArrayList<>();
    private Texture textureMazo;
    private Image mazoGUI;
    private Vector2 positionGUI = new Vector2();

    public Mazo(MyGdxGameAssetManager assetManager, Jugador jugador) {
        assetManager.loadImageDeck();
        assetManager.manager.finishLoading();
        textureMazo = assetManager.manager.get(assetManager.imageSquare, Texture.class);
        mazoGUI = new Image(textureMazo);
        if(jugador.getId()==0) {
            positionGUI.y= POS_Y_MAZO_J1;
        }else {
            positionGUI.y= POS_Y_MAZO_J2;
        }
        positionGUI.x= POS_X_MAZO;
        mazoGUI.setPosition(positionGUI.x,positionGUI.y);

        DefaultDeck();
        mazoGUI.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float cx, float cy) {
                if(jugador.isAvoidToDrawCard() && cartasMazo.size()>0) {
                    jugador.getMano().getCartasMano().add(shuffleMazo.get(0));
                    shuffleMazo.remove(0);
                    jugador.avoidToDrawCard(false);
                }
            }
        });
        ShuffleMazo();
    }

    public ArrayList<Carta> getCartasMazo() {
        return cartasMazo;
    }

    public void addCardToDeck(Carta carta) {
        this.cartasMazo.add(carta);
    }

    public ArrayList<Carta> DefaultDeck() {
        Texture textureCard = new Texture("icons\\carta.png");
        Texture textureSpriteCard= new Texture("icons\\spyro.png");
        Texture textureCard2 = new Texture("icons\\handled_card2.png");
        Texture textureSpriteCard2= new Texture("icons\\Spritecard2.png");
        while (cartasMazo.size()<=MIN_CARDS-1) {
            Criatura golem = new Criatura(DRAGON,FUEGO,textureSpriteCard,7,10,2,1);
            golem.setNombre("DragonSp1");
            golem.setImage(textureCard);
            golem.setLastPosition(-1,-1);
            golem.setFirstPosition(-1,-1);
            cartasMazo.add(golem);
            Criatura golem2 = new Criatura(DRAGON,FUEGO,textureSpriteCard2,7,10,2,1);
            golem2.setNombre("DragonSp2");
            golem2.setImage(textureCard2);
            golem2.setLastPosition(-1,-1);
            golem2.setFirstPosition(-1,-1);
            cartasMazo.add(golem2);
        }
        return cartasMazo;
    }

    public Image getMazoGUI() {
        return mazoGUI;
    }

    public void setPositionGUI(float x, float y) {
        this.mazoGUI.setPosition(x,y);
        this.positionGUI.x = x;
        this.positionGUI.y =y;
    }

    public Vector2 getPositionGUI() {
        return positionGUI;
    }

    public void ShuffleMazo() {
        shuffleMazo = cartasMazo;
        Collections.shuffle(shuffleMazo);
    }

    public ArrayList<Carta> getShuffleMazo() {
        return shuffleMazo;
    }
}
