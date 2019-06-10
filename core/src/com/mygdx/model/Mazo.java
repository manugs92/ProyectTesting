package com.mygdx.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.MyGdxGameAssetManager;


import java.util.*;

import static com.mygdx.model.Criatura.TipoElemental.FUEGO;
import static com.mygdx.model.Criatura.TipoElemental.ROCK;
import static com.mygdx.model.Criatura.TipoEspecie.DRAGON;
import static com.mygdx.model.Criatura.TipoEspecie.GOLEM;

public class Mazo {

    public static final int MAX_CARDS = 40;
    public static final int MIN_CARDS = 20;

    public static final float POS_X_MAZO = Tablero.POS_X_TABLERO + (Casilla.MEDIDA_CASILLA*7);
    public static final float POS_Y_MAZO_J1 = 87;
    public static final float POS_Y_MAZO_J2 = MyGdxGame.SCREEN_HEIGHT-133;
    public static final float POS_X_SELECTION_HAND = POS_X_MAZO + 12;
    public static final float POS_Y_SELECTION_HAND = POS_Y_MAZO_J1 + 50;

    private ArrayList<Carta> cartasMazo = new ArrayList<>();
    private ArrayList<Carta> shuffleMazo = new ArrayList<>();
    private Texture textureMazoDefault,textureMazoAvoidToDraw;
    private Texture textureSelectionHand, textureSelectionHand2;
    private Image mazoDefaultGUI,mazoAvoidToDrawGUI;
    private Vector2 positionGUI = new Vector2();

    public Mazo(MyGdxGameAssetManager assetManager, Jugador jugador) {
        assetManager.loadDeckImages();
        assetManager.manager.finishLoading();
        textureMazoDefault = assetManager.manager.get(assetManager.imageBackCard, Texture.class);
        mazoDefaultGUI = new Image(textureMazoDefault);
        textureMazoAvoidToDraw = assetManager.manager.get(assetManager.imageBackCardToDraw,Texture.class);
        mazoAvoidToDrawGUI = new Image(textureMazoAvoidToDraw);
        if(jugador.getId()==0) {
            positionGUI.y= POS_Y_MAZO_J1;
            textureSelectionHand = assetManager.manager.get(assetManager.handSelection,Texture.class);
            textureSelectionHand2 = assetManager.manager.get(assetManager.handSelection2,Texture.class);
        }else {
            positionGUI.y= POS_Y_MAZO_J2;
        }
        positionGUI.x= POS_X_MAZO;
        mazoDefaultGUI.setPosition(positionGUI.x,positionGUI.y);
        mazoAvoidToDrawGUI.setPosition(positionGUI.x,positionGUI.y);

        DefaultDeck(assetManager,jugador);
        ShuffleMazo();
    }

    public void drawCard(Jugador jugador) {
        if(jugador.isAvoidToDrawCard() && cartasMazo.size()>0) {
            jugador.getMano().getCartasMano().add(shuffleMazo.get(0));
            shuffleMazo.remove(0);
            jugador.avoidToDrawCard(false);
        }
    }

    public ArrayList<Carta> getCartasMazo() {
        return cartasMazo;
    }

    public void addCardToDeck(Carta carta) {
        this.cartasMazo.add(carta);
    }

    public ArrayList<Carta> DefaultDeck(MyGdxGameAssetManager assetManager,Jugador jugador) {

        assetManager.loadCardsInfoImages();
        assetManager.manager.finishLoading();

        Texture textureCard_Spyro = assetManager.manager.get(assetManager.textureCard_Spyro);
        Texture textureCardInfo_Spyro = assetManager.manager.get(assetManager.textureCardInfo_Spyro);
        Texture textureSpriteSpyro=  assetManager.manager.get(assetManager.textureSpriteSpyro);
        Texture textureSpriteSpyroFront= assetManager.manager.get(assetManager.textureSpriteSpyroFront);

        Texture textureCard_LavaGolem = assetManager.manager.get(assetManager.textureCard_LavaGolem);
        Texture textureCardInfo_LavaGolem = assetManager.manager.get(assetManager.textureCardInfo_LavaGolem);
        Texture textureSpriteLavaGolem= assetManager.manager.get(assetManager.textureSpriteLavaGolem);
        Texture textureSpriteLavaGolemFront= assetManager.manager.get(assetManager.textureSpriteLavaGolemFront);

        Texture textureCard_RockElemental = assetManager.manager.get(assetManager.textureCard_RockElemental);
        Texture textureCardInfo_RockElemental = assetManager.manager.get( assetManager.textureCardInfo_RockElemental);
        Texture textureSpriteRockElemental= assetManager.manager.get(assetManager.textureSpriteRockElemental);
        Texture textureSpriteRockElementalFront= assetManager.manager.get(assetManager.textureSpriteRockElementalFront);


        while (cartasMazo.size()<=MIN_CARDS-1) {
            Criatura spyro = new Criatura(DRAGON,FUEGO,
                    textureSpriteSpyro,textureSpriteSpyroFront,
                    textureCardInfo_Spyro,
                    3,2,22,0,jugador.getId());
            instanceCreatureDetail(spyro,"spyro",
                    textureCard_Spyro,2);

            Criatura lavaGolem = new Criatura(GOLEM,FUEGO,
                    textureSpriteLavaGolem,
                    textureSpriteLavaGolemFront,
                    textureCardInfo_LavaGolem,
                    3,3,2,0,jugador.getId());
            instanceCreatureDetail(lavaGolem,"GolemManu",
                    textureCard_LavaGolem,3 );

            Criatura rockElemental = new Criatura(GOLEM,ROCK,
                    textureSpriteRockElemental,
                    textureSpriteRockElementalFront,
                    textureCardInfo_RockElemental,
                    4,6,1,0,jugador.getId());
            instanceCreatureDetail(rockElemental,"RockElemental",
                    textureCard_RockElemental,4 );
        }
        return cartasMazo;
    }

    private void instanceCreatureDetail(Criatura creature ,String name,Texture texture,int costInvocation) {
        creature.setNombre(name);
        creature.setImage(texture);
        creature.setLastPosition(-1,-1);
        creature.setFirstPosition(-1,-1);
        creature.setCostInvocation(costInvocation);
        cartasMazo.add(creature);
    }

    public Image getMazoDefaultGUI() { return mazoDefaultGUI; }

    public Image getMazoAvoidToDrawGUI() {return  mazoAvoidToDrawGUI;}

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

    public Texture getTextureSelectionHand() { return textureSelectionHand; }

    public Texture getTextureSelectionHand2() { return textureSelectionHand2; }

}
