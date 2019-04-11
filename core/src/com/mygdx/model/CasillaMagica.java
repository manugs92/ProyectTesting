package com.mygdx.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class CasillaMagica {

    //CONSTANTES DE CASILLAS MAGICAS
    public static final int MEDIDA_CASILLA = 48;

    private Magica cartaMagica;
    private Vector2 positionGUI = new Vector2();
    private Texture textureCasilla;
    private Image imageCasilla;
    private int state;

    public Magica getCartaMagica() {
        return cartaMagica;
    }

    public void setCartaMagica(Magica cartaMagica) {
        this.cartaMagica = cartaMagica;
    }

    public Vector2 getPositionGUI() {
        return positionGUI;
    }

    public void setPositionGUI(float x,float y) {

        this.positionGUI.x = x;
        this.positionGUI.y = y;
    }

    public Texture getTextureCasilla() {
        return textureCasilla;
    }

    public void setTextureCasilla(Texture textureCasilla) {
        this.textureCasilla = textureCasilla;
    }

    public Image getImageCasilla() {
        return imageCasilla;
    }

    public void setImageCasilla(Image imageCasilla) {
        this.imageCasilla = imageCasilla;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state= state;
    }

    public void addListenerToMagicSquare(int pos) {
        this.imageCasilla.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Has pulsado en la casilla mágica " + pos);
            }
        });
    }
}
