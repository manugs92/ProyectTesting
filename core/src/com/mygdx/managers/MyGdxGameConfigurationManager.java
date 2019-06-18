package com.mygdx.managers;

import com.mygdx.configuration.Controls;
import com.mygdx.configuration.SoundsConfiguration;

public class MyGdxGameConfigurationManager {

    public Controls controlsOfGame;
    public SoundsConfiguration soundsConfiguration;

    public MyGdxGameConfigurationManager() {
        controlsOfGame = new Controls();
        soundsConfiguration = new SoundsConfiguration();
    }
}
