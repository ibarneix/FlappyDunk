package com.mygdx.flappydunk;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player {
    // vitesse a laquelle la vitesse horizontale revient apres un choc
    private static final float RECUP_AVANCE = 2.5f;
    // roulement : degres de rotation par unite de vitesse horizontale
    private static final float ROULEMENT = 0.7f;
    // attenuation du spin supplementaire donne par un choc
    private static final float SPIN_ATTENUE = 2.5f;

    private float x;
    private float y;
    private float vx;
    private float vy;
    private float ax;
    private float ay;
    private float vxCible;   // vitesse d'avance "normale" a retrouver
    private float rotation;  // angle d'affichage de la balle (degres)
    private float spin;      // rotation supplementaire donnee par un choc (deg/s)
    private Texture image;

    public Player(Texture image){
        //position
        this.x=0;
        this.y=0;
        //velocité
        this.vx=0;
        this.vy=0;
        //acceleration
        this.ax = 0.0f;
        this.ay = -9.8f * 100;
        //vitesse d'avance visee
        this.vxCible = 0;
        //rotation
        this.rotation = 0;
        this.spin = 0;
        //image
        this.image=image;
    }

    public void setVelocity(float vx, float vy){
        this.vx = vx;
        this.vy = vy;
    }
    public void addVelcity(float vx, float vy){
        this.vx += vx;
        this.vy += vy;
    }

    // acces a la vitesse (pour le rebond contre les anneaux)
    public float getVx(){ return vx; }
    public float getVy(){ return vy; }
    public void setVx(float vx){ this.vx = vx; }
    public void setVy(float vy){ this.vy = vy; }

    // deplace la balle par son centre (pour la sortir d'un anneau)
    public void setCenter(float cx, float cy){
        this.x = cx - image.getWidth()  / 2f;
        this.y = cy - image.getHeight() / 2f;
    }
    public float getX(){
       return x;
    }
    public float getY(){
        return y;
    }

    //un coup d'aile : on garde la vitesse horizontale, on pousse vers le haut
    public void flap(float impulsionY){
        this.vy = impulsionY;
    }

    //ajoute de la rotation (au contact d'un cerceau)
    public void addSpin(float deltaSpin){
        this.spin += deltaSpin;
    }

    //remet le player au depart
    public void reset(float x, float y, float vx){
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = 0;
        this.vxCible = vx;
        this.rotation = 0;
        this.spin = 0;
    }

    public float getWidth(){
        return image.getWidth();
    }
    public float getHeight(){
        return image.getHeight();
    }
    //centre de la balle (pour la camera et les collisions)
    public float getCenterX(){
        return x + image.getWidth() / 2f;
    }
    public float getCenterY(){
        return y + image.getHeight() / 2f;
    }



    //met a jour la physique, sans dessiner
    public void update(float dt){
        // la vitesse horizontale revient en douceur vers la vitesse d'avance :
        // apres un choc qui la freine ou l'inverse, la balle repart vers l'avant
        vx += (vxCible - vx) * RECUP_AVANCE * dt;
        vy += ay * dt;
        x+=vx*dt;
        y+=vy*dt;

        // rotation : roulement de base lie a l'avance + spin du dernier choc
        float roulement = -vx * ROULEMENT;          // roule vers l'avant
        rotation += (roulement + spin) * dt;
        spin -= spin * SPIN_ATTENUE * dt;           // le spin s'attenue
    }

    //dessine le player a sa position actuelle (avec sa rotation)
    public void draw(SpriteBatch batch){
        float w = image.getWidth();
        float h = image.getHeight();
        batch.draw(image, x, y,           // position du coin bas-gauche
                w / 2f, h / 2f,            // centre de rotation
                w, h,                       // taille affichee
                1f, 1f,                     // echelle
                rotation,                   // angle
                0, 0, (int) w, (int) h,     // zone source dans la texture
                false, false);              // pas de miroir
    }


}
