package com.mygdx.screens;

import com.mygdx.game.MyGdxGame;

public class ScreenManager {

    public MyGdxGame parent;

    private MainScreen mainScreen;
    private LoadingScreen loadingScreen;
    private PreferencesScreen preferencesScreen;
    private StartGame startGame;
    private DuelScreen duelScreen;
    private SummaryScreen summaryScreen;

    public final static int MAIN_SCREEN = 0;
    public final static int LOADING_SCREEN  = 1;
    public final static int START_GAME = 2;
    public final static int PREFERENCES = 3;
    public final static int DUEL_SCREEN = 4;
    public final static int SUMMARY_SCREEN = 5;


    public ScreenManager(MyGdxGame myGdxGame) {
        this.parent=myGdxGame;
    }

    public void setDefaultScreen() {
        mainScreen = new MainScreen(this);
        parent.setScreen(mainScreen);
        //parent.setScreen(new DuelScreen(this));
    }

    public void changeScreen(int screen){
        switch(screen){
            case MAIN_SCREEN:
                mainScreen = new MainScreen(this);
                parent.setScreen(mainScreen);
                break;

            case LOADING_SCREEN:
               loadingScreen = new LoadingScreen(this);
                parent.setScreen(loadingScreen);
                break;

            case START_GAME:
                startGame = new StartGame(this);
                parent.setScreen(startGame);
                break;

            case PREFERENCES:
                preferencesScreen = new PreferencesScreen(this);
                parent.setScreen(preferencesScreen);
                break;

            case DUEL_SCREEN:
                duelScreen = new DuelScreen(this);
                parent.setScreen(duelScreen);
                break;

            case SUMMARY_SCREEN:
                summaryScreen = new SummaryScreen(this);
                parent.setScreen(summaryScreen);
                break;
        }
    }
}
