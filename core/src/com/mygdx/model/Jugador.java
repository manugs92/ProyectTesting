package com.mygdx.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.MyGdxGameAssetManager;

public class Jugador {

    private String nombre;
    private Mazo mazo;
    private Mano mano;
    private Cementerio cementerio;
    private int id;
    private boolean avoidToDrawCard;
    private Image avatar;
    private Vector2 posAvatar = new Vector2();

    public Jugador(String nombre, int id, MyGdxGameAssetManager assetManager, Skin skin) {
        this.nombre=nombre;
        this.id=id;
        this.mazo = new Mazo(assetManager,this);
        this.mano=new Mano(this.mazo);
        this.cementerio=new Cementerio(assetManager,this);
        assetManager.loadAvatars();
        assetManager.manager.finishLoading();
        if(id==0) {
            avatar = new Image(assetManager.manager.get(assetManager.myAvatar,Texture.class));
            posAvatar.x = MyGdxGame.SCREEN_WIDTH - 128;
            posAvatar.y = 10;
        }else {
            avatar = new Image(assetManager.manager.get(assetManager.rivalAvatar,Texture.class));
            posAvatar.x = MyGdxGame.SCREEN_WIDTH-128;
            posAvatar.y = MyGdxGame.SCREEN_HEIGHT-128;
        }
        avatar.setPosition(posAvatar.x,posAvatar.y);
    }

    public void setMazo(Mazo mazo) {
        this.mazo=mazo;
    }

    public void setNombre(String nombre) {
        this.nombre=nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public Mazo getMazo() {
        return mazo;
    }

    public int getId() {
        return id;
    }

    public Mano getMano() {
        return mano;
    }

    public boolean isAvoidToDrawCard() { return avoidToDrawCard; }

    public void avoidToDrawCard(Boolean isAvoid) { avoidToDrawCard=isAvoid; }

    public Cementerio getCementerio() {
        return cementerio;
    }

    public void setCementerio(Cementerio cementerio) {
        this.cementerio = cementerio;
    }

    public Image getAvatar() {
        return avatar;
    }
}
