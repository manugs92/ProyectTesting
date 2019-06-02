package com.mygdx.configuration;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class Controls {
    public static boolean isLeftPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A);
    }

    public static boolean isRightPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D);
    }


    public static boolean notKeyPressed(){
        return !isLeftPressed() && ! isRightPressed();
    }

    public static boolean isMinusPressed(){
        return Gdx.input.isKeyPressed(Input.Keys.MINUS);
    }
    public static boolean isPluslePressed(){
        return Gdx.input.isKeyPressed(Input.Keys.PLUS);
    }
}
