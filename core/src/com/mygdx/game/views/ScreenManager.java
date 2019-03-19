package com.mygdx.game.views;

import com.mygdx.game.MyGdxGame;

public class ScreenManager {

    MyGdxGame parent;

    private MainScreen mainScreen;
    private LoadingScreen loadingScreen;
    private PreferencesScreen preferencesScreen;
    private StartGame startGame;

    public final static int MAIN_SCREEN = 0;
    public final static int LOADING_SCREEN  = 1;
    public final static int START_GAME = 2;
    public final static int PREFERENCES = 3;

    public ScreenManager(MyGdxGame myGdxGame) {
        this.parent=myGdxGame;
    }

    public void setDefaultScreen() {
        mainScreen = new MainScreen(this);
        parent.setScreen(mainScreen);
    }

    public void changeScreen(int screen){
        switch(screen){
            case MAIN_SCREEN:
                if(mainScreen == null) mainScreen = new MainScreen(this);
                parent.setScreen(mainScreen);
                break;

            case LOADING_SCREEN:
                if(loadingScreen == null) loadingScreen = new LoadingScreen(this);
                parent.setScreen(loadingScreen);
                break;

            case START_GAME:
                if(startGame == null) startGame = new StartGame(this);
                parent.setScreen(startGame);
                break;

            case PREFERENCES:
                if(preferencesScreen == null) preferencesScreen = new PreferencesScreen(this);
                parent.setScreen(preferencesScreen);
                break;
        }
    }
}
