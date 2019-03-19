package com.mygdx.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Carta {

    private String nombre;
    private int costeInvocacion;
    private Tipo tipo;
    private String pathImage;

//129
public enum Tipo {
    CRIATURA,
    MAGICA,
    EQUIPAMIENTO,
    TRAMPA;
}
}