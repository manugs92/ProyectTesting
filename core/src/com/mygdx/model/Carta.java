package com.mygdx.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Carta {
    private String nombre;
    private int costeInvocacion;
    private Tipo tipo;
    private String pathImage;

//129


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

    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    public enum Tipo {
    CRIATURA,
    MAGICA,
    EQUIPAMIENTO,
    TRAMPA;
}
}