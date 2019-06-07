package com.mygdx.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.MyGdxGameAssetManager;

import static com.mygdx.model.Casilla.MEDIDA_CASILLA;

public class Tablero {

    //Constantes del tablero (GUI)
    public static final float POS_X_TABLERO = 483;
    public final float POS_Y_TABLERO = 135;
    public final int MAX_MAGIC_CARDS = 3;
    public float POS_Y_MAGICAS_J1 = 87;
    public float POS_Y_MAGICAS_J2 = MyGdxGame.SCREEN_HEIGHT-133;


    //Constantes del tablero. (internas)
    public final static int TOTAL_CASILLAS_X = 7;
    public final static int TOTAL_CASILLAS_Y = 9;

    //Variables de tablero (internas)
    private Casilla[][] casillas= new Casilla[TOTAL_CASILLAS_X][TOTAL_CASILLAS_Y];
    private CasillaMagica[] casillaMagicaJ1 = new CasillaMagica[3];
    private CasillaMagica[] casillaMagicaJ2  = new CasillaMagica[3];


    public Tablero(Partida partida,MyGdxGameAssetManager assetManager) {
        assetManager.loadDuelScreenImages();
        assetManager.manager.finishLoading();


        //Creamos casillas de movimiento del tablero.
        for(int x=0;x<=TOTAL_CASILLAS_X-1;x++) {
            for(int y=0;y<=TOTAL_CASILLAS_Y-1;y++) {
                casillas[x][y] = new Casilla();
                casillas[x][y].setTextureCasilla(assetManager.manager.get(assetManager.imageSquare, Texture.class));
                casillas[x][y].setTextureCasilla2(assetManager.manager.get(assetManager.imageSquare2, Texture.class));
                casillas[x][y].setTextureCasilla3(assetManager.manager.get(assetManager.imageSquare3, Texture.class));
                casillas[x][y].setImageCasilla(new Image(casillas[x][y].getTextureCasilla()));
                casillas[x][y].setState(Casilla.State.APAGADA);
                casillas[x][y].setCoordinatesMatrix(new Vector2(x,y));
                casillas[x][y].setPositionGUI(POS_X_TABLERO +(MEDIDA_CASILLA*x), POS_Y_TABLERO +(MEDIDA_CASILLA*y));
                casillas[x][y].getImageCasilla().setPosition(MEDIDA_CASILLA*x,MEDIDA_CASILLA*y);
                casillas[x][y].addListenerToBoard(this,partida);
            }
        }

        //Creamos casillas mágicas J1
        for(int i=0;i<=MAX_MAGIC_CARDS-1;i++) {
            casillaMagicaJ1[i] = new CasillaMagica();
            casillaMagicaJ1[i].setTextureCasilla(assetManager.manager.get(assetManager.imageSquare, Texture.class));
            casillaMagicaJ1[i].setImageCasilla(new Image(casillaMagicaJ1[i].getTextureCasilla()));
            if(i==0) {
                casillaMagicaJ1[i].setPositionGUI(POS_X_TABLERO + (MEDIDA_CASILLA * (i + 1)),POS_Y_MAGICAS_J1);
            }
            else {
                casillaMagicaJ1[i].setPositionGUI(POS_X_TABLERO + (MEDIDA_CASILLA * (i + i + 1)),POS_Y_MAGICAS_J1);
            }
            casillaMagicaJ1[i].getImageCasilla().setPosition( casillaMagicaJ1[i].getPositionGUI().x,casillaMagicaJ1[i].getPositionGUI().y);
            casillaMagicaJ1[i].addListenerToMagicSquare(i);
            casillaMagicaJ1[i].setState(0);
        }

        //Creamos casillas mágicas J2
        for(int i=0;i<=MAX_MAGIC_CARDS-1;i++) {
            casillaMagicaJ2[i] = new CasillaMagica();
            casillaMagicaJ2[i].setTextureCasilla(assetManager.manager.get(assetManager.imageSquare, Texture.class));
            casillaMagicaJ2[i].setImageCasilla(new Image(casillaMagicaJ2[i].getTextureCasilla()));
            if(i==0) {
                casillaMagicaJ2[i].setPositionGUI(POS_X_TABLERO + (MEDIDA_CASILLA * (i + 1)),POS_Y_MAGICAS_J2);
            }
            else {
                casillaMagicaJ2[i].setPositionGUI(POS_X_TABLERO + (MEDIDA_CASILLA * (i + i + 1)),POS_Y_MAGICAS_J2);
            }
            casillaMagicaJ2[i].getImageCasilla().setPosition( casillaMagicaJ2[i].getPositionGUI().x,casillaMagicaJ2[i].getPositionGUI().y);
            casillaMagicaJ2[i].addListenerToMagicSquare(i);
            casillaMagicaJ2[i].setState(0);
        }
        listenerToDamageUser(partida);
        listenerToDraw(partida);
    }

    private void listenerToDraw(Partida partida) {
        partida.getJugador(0).getMazo().getMazoAvoidToDrawGUI().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float cx, float cy) {
                partida.getJugador(0).getMazo().drawCard(partida.getJugador(0));
                partida.setSelectedCard(null);
                partida.getCardInformation().updateCardInformation(partida);
                partida.getJugadores().forEach(p -> p.getCementerio().setShowed(false));

            }
        });
    }

    public Tablero getTablero() {
        return this;
    }

    public void setCasilla(int x, int y, Carta carta, int player) {
        if(carta.getTipo() == Carta.Tipo.TRAMPA) {
            //Solo se puede colocar si es <4 y >1 && player == 0 (Si player == 1; <7 y >4)
            if((y<4 && y>1 && player==0) ||(y<7 && y>4 && y==1) ) {
                casillas[x][y].setTrampa((Trampa) carta);
            }else {
                System.out.println("No puedes colocar la trampa aqui.");
            }

        }else if(carta.getTipo() == Carta.Tipo.CRIATURA) {
            casillas[x][y].setCriatura((Criatura) carta);
        }
    }

    public void setCasillaMagica(int player,int pos, Carta cartaMagica) {
        if(player ==0) {
            casillaMagicaJ1[pos].setCartaMagica((Magica) cartaMagica);
        }else {
            casillaMagicaJ2[pos].setCartaMagica((Magica) cartaMagica);
        }
    }

    public Casilla getCasilla(float x,float y) {
        return casillas[(int) x][(int) y];
    }

    public Casilla getCasilla(Vector2 pos) {
        return casillas[(int) pos.x][(int) pos.y];
    }

    public CasillaMagica getCasillaMagica(int player, int pos) {
        if(player==0) {
         return casillaMagicaJ1[pos];
        }else {
            return casillaMagicaJ2[pos];
        }
    }

    public Casilla[][] getCasillas() {
        return casillas;
    }

    public void setAllSquaresToOff(Tablero tablero) {
        for (int x = 0; x < tablero.getCasillas().length; x++) {
            for (int y = 0; y < tablero.getCasillas()[x].length; y++) {
                tablero.getCasilla(x, y).setState(Casilla.State.APAGADA);
            }
        }
    }

    private void listenerToDamageUser(Partida partida) {
        Jugador jugador2 = partida.getJugador(1);
        partida.getJugador(1).getAvatar3().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (jugador2.isAvoidToDamage()) {
                    jugador2.setLives(jugador2.getLives() - ((Criatura) partida.getSelectedCard()).getAtaque());
                    jugador2.setAvoidToDamage(false);
                    ((Criatura) partida.getSelectedCard()).setMoved(true);
                    setAllSquaresToOff(Tablero.this);
                    announceDamageToUser(partida,jugador2);
                    if(jugador2.getLives()<=0) {
                        announceUserFainted(partida,jugador2);
                        partida.setWinnerId(0);
                    }
                }
            }
        });
    }

    private void announceDamageToUser(Partida partida,Jugador jugador2) {
        Carta selectedCard = partida.getSelectedCard();
        partida.getDuelLog().addMsgToLog(partida.getSelectedCard().getNombre().toUpperCase()+" ha ATACADO a "+jugador2.getNombre().toUpperCase());
        partida.getDuelLog().addMsgToLog(jugador2.getNombre().toUpperCase()+" ha PERDIDO "+((Criatura)selectedCard).getAtaque()+" HP");
        partida.getDuelLog().setNewMsgTrue();
        partida.getDuelLog().getScrollPane().remove();
    }

    private void announceUserFainted(Partida partida,Jugador jugador2) {
        partida.getDuelLog().addMsgToLog(jugador2.getNombre().toUpperCase()+" ha PERDIDO");
        partida.getDuelLog().setNewMsgTrue();
        partida.getDuelLog().getScrollPane().remove();
    }
}
