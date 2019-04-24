package com.mygdx.model;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Carta {
    private String nombre;
    private int costeInvocacion;
    private Tipo tipo;
    private Texture image;
    private Vector2 position = new Vector2(); //Posicion actual dentro del tablero
    private Vector2 lastPosition = new Vector2(); //Ultima posicion dentro del tablero
    private Vector2 firstPosition = new Vector2(); //Primera posicion dentro del tablero.


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

    public int getCosteInvocacion() {
        return costeInvocacion;
    }

    public void setCosteInvocacion(int costeInvocacion) {
        this.costeInvocacion = costeInvocacion;
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

    public Texture getSpriteCriatura() {
        return null;
    }

    public void setSpriteCriatura(Texture spriteCriatura) { }

    public enum Tipo {
    CRIATURA,
    MAGICA,
    EQUIPAMIENTO,
    TRAMPA;
}
}