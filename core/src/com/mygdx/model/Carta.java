package com.mygdx.model;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public abstract class Carta {
    public enum Tipo {
        CRIATURA,
        MAGICA,
        EQUIPAMIENTO,
        TRAMPA;
    }


    private String nombre;
    private int costInvocation;
    private Tipo tipo;
    protected Image cardDetailInfo;
    private Texture image;
    private Vector2 position = new Vector2(); //Posicion actual dentro del tablero
    private Vector2 lastPosition = new Vector2(); //Ultima posicion dentro del tablero
    private Vector2 firstPosition = new Vector2(); //Primera posicion dentro del tablero.
    private int ownerId;
    private boolean clicked;


    public Vector2 getFirstPosition() {
        return firstPosition;
    }

    public void setFirstPosition(float x, float y) {
        this.firstPosition.x = x;
        this.firstPosition.y = y;
    }

    public Vector2 getLastPosition() {
        return this.lastPosition;
    }

    public void setLastPosition(float x, float y) { this.lastPosition.x = x;this.lastPosition.y=y; }

    public int getAlcance() {return 0;}

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCostInvocation() {
        return costInvocation;
    }

    public void setCostInvocation(int costInvocation) {
        this.costInvocation = costInvocation;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public Texture getImage() {
        return image;
    }

    public void setImage(Texture image) {
        this.image = image;
    }

    public void setPosition(float x, float y) {
        this.position.x=x;
        this.position.y=y;
    }

    public Vector2 getPosition() {
        return this.position;
    }

    public Image getCardDetailInfo() {
        return cardDetailInfo;
    }

    public void setCardDetailInfo(Image cardDetailInfo) {
        this.cardDetailInfo = cardDetailInfo;
    }

    public int getOwnerId() { return ownerId; }

    public void setOwnerId(int ownerId) { this.ownerId = ownerId; }

    public boolean isClicked() { return clicked; }

    public void setClicked(boolean clicked) { this.clicked = clicked; }
}