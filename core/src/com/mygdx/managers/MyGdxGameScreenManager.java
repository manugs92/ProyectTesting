package com.mygdx.managers;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.model.Partida;
import com.mygdx.screens.*;

public class MyGdxGameScreenManager {

    public MyGdxGame parent;

    private MainScreen mainScreen;
    private LoadingScreen loadingScreen;
    private PreferencesScreen preferencesScreen;
    private StartGame startGame;
    private DuelScreen duelScreen;
    private SummaryScreen summaryScreen;

    public MyGdxGameAssetManager asset;
    public MyGdxGameConfigurationManager myGdxGameConfigurationManager;
    public Skin skin;
    public FitViewport fitViewport;
    public BitmapFont font;


    public final static int MAIN_SCREEN = 0;
    public final static int LOADING_SCREEN  = 1;
    public final static int START_GAME_SCREEN = 2;
    public final static int PREFERENCES_SCREEN = 3;
    public final static int DUEL_SCREEN = 4;
    public final static int SUMMARY_SCREEN = 5;


    public MyGdxGameScreenManager(MyGdxGame myGdxGame) {
        this.parent=myGdxGame;
        this.asset=myGdxGame.assets;
        this.myGdxGameConfigurationManager =myGdxGame.myGdxGameConfigurationManager;
        this.skin=myGdxGame.skin;
        this.fitViewport=myGdxGame.fitViewport;
       this.font=myGdxGame.font;
    }

    public void setDefaultScreen() {
        mainScreen = new MainScreen(this);
        parent.setScreen(mainScreen);
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

            case START_GAME_SCREEN:
                startGame = new StartGame(this);
                parent.setScreen(startGame);
                break;

            case PREFERENCES_SCREEN:
                preferencesScreen = new PreferencesScreen(this);
                parent.setScreen(preferencesScreen);
                break;

            case DUEL_SCREEN:
                duelScreen = new DuelScreen(this);
                parent.setScreen(duelScreen);
                break;
        }
    }

    public void changeScreenToResume(int screen, Partida partida) {
        summaryScreen = new SummaryScreen(this,partida);
        parent.setScreen(summaryScreen);
    }
}
