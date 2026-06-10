package com.mygdx.flappydunk;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player {
    // vitesse a laquelle la vitesse horizontale revient apres un choc
    private static final float RECUP_AVANCE = 2.5f;

    private float x;
    private float y;
    private float vx;
    private float vy;
    private float ax;
    private float ay;
    private float vxCible;   // vitesse d'avance "normale" a retrouver
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

    //remet le player au depart
    public void reset(float x, float y, float vx){
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = 0;
        this.vxCible = vx;
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
    }

    //dessine le player a sa position actuelle
    public void draw(SpriteBatch batch){
        batch.draw(image, x, y);
    }


}
