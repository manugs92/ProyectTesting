package com.mygdx.model;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Carta {
    private String nombre;
    private int costeInvocacion;
    private Tipo tipo;
    private Texture image;
    private Vector2 position = new Vector2();
    //private Vector2 position



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