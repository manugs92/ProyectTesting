package com.mygdx.configuration;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class Controls {

    public static boolean isMinusPressed(){
        return Gdx.input.isKeyPressed(Input.Keys.MINUS);
    }
    public static boolean isPluslePressed(){
        return Gdx.input.isKeyPressed(Input.Keys.PLUS);
    }
}
