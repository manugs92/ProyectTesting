package com.mygdx.configuration;

public class SoundsConfiguration {

    float GLOBAL_SOUND = 1f;
    float volumeAlienShoot = GLOBAL_SOUND;
    float volumeAlienDie = GLOBAL_SOUND;
    float volumeShipShoot = GLOBAL_SOUND;
    float volumeShipDamage = GLOBAL_SOUND;
    float getVolumeShipMegaShoot = GLOBAL_SOUND;


    public SoundsConfiguration() {
    }


    public void update() {
        controlVolume();

    }

    private void controlVolume() {

        if (Controls.isMinusPressed()) {

            this.volumeAlienShoot = (volumeAlienShoot <= 0) ? 0 : volumeAlienShoot - 0.002f;
            this.volumeAlienDie = (volumeAlienDie <= 0) ? 0 : volumeAlienDie - 0.002f;
            this.volumeShipShoot = (volumeShipShoot <= 0) ? 0 : volumeShipShoot - 0.002f;
            this.volumeShipDamage = (volumeShipDamage <= 0) ? 0 : volumeShipDamage - 0.002f;
            this.getVolumeShipMegaShoot = (getVolumeShipMegaShoot < 0) ? 0 : getVolumeShipMegaShoot - 0.002f;

        } else if (Controls.isPluslePressed()) {
            this.volumeAlienShoot = (volumeAlienShoot >= 1) ? 1 : volumeAlienShoot + 0.002f;
            this.volumeAlienDie = (volumeAlienDie >= 1) ? 1 : volumeAlienDie + 0.002f;
            this.volumeShipShoot = (volumeShipShoot >= 1) ? 1 : volumeShipShoot + 0.002f;
            this.volumeShipDamage = (volumeShipDamage >= 1) ? 1 : volumeShipDamage + 0.002f;
            this.getVolumeShipMegaShoot = (getVolumeShipMegaShoot > 1) ? 1 : getVolumeShipMegaShoot + 0.002f;
        }


        //System.out.println(volumeAlienShoot + "aa");


    }

    public void mute(boolean mute) {

        if (mute) {
            this.GLOBAL_SOUND = 0;
            setVolume(GLOBAL_SOUND);
        } else {
            this.GLOBAL_SOUND = 1;
        }
    }

    public void setVolume(float volume) {
        volumeAlienShoot = volume;
        volumeAlienDie = volume;
        volumeShipShoot = volume;
        volumeShipDamage = volume;
        getVolumeShipMegaShoot = volume;
    }


    public float getGLOBAL_SOUND() {
        return GLOBAL_SOUND;
    }

    public void setGLOBAL_SOUND(float GLOBAL_SOUND) {
        this.GLOBAL_SOUND = GLOBAL_SOUND;
    }

    public float getVolumeAlienShoot() {
        return volumeAlienShoot;
    }

    public void setVolumeAlienShoot(float volumeAlienShoot) {
        this.volumeAlienShoot = volumeAlienShoot;
    }

    public float getVolumeShipShoot() {
        return volumeShipShoot;
    }

    public void setVolumeShipShoot(float volumeShipShoot) {
        this.volumeShipShoot = volumeShipShoot;
    }

    public float getVolumeAlienDie() {
        return volumeAlienDie;
    }

    public void setVolumeAlienDie(float volumeAlienDie) {
        this.volumeAlienDie = volumeAlienDie;
    }

    public float getVolumeShipDamage() {
        return volumeShipDamage;
    }

    public void setVolumeShipDamage(float volumeShipDamage) {
        this.volumeShipDamage = volumeShipDamage;
    }

    public float getGetVolumeShipMegaShoot() {
        return getVolumeShipMegaShoot;
    }

    public void setGetVolumeShipMegaShoot(float getVolumeShipMegaShoot) {
        this.getVolumeShipMegaShoot = getVolumeShipMegaShoot;
    }


}
