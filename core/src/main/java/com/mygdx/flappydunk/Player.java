package com.mygdx.flappydunk;

/**
 * Le joueur (l'oiseau / la balle).
 * Classe PURE : aucune dependance graphique -> testable sans contexte OpenGL.
 * Le rendu est fait par Main a partir de la position/taille exposees ici.
 */
public class Player {
    // Taille de l'image du joueur (flappy.png fait 100x100).
    public static final float WIDTH = 100f;
    public static final float HEIGHT = 100f;
    // Rayon de collision (un peu plus petit que l'image pour etre indulgent).
    public static final float RADIUS = 32f;

    // Physique.
    public static final float GRAVITY = -980f;       // chute
    public static final float FLAP_VELOCITY = 380f;  // impulsion d'un battement d'ailes
    public static final float FORWARD_SPEED = 200f;  // vitesse d'avance constante

    private float x, y;
    private float vx, vy;

    public Player() {
        reset(0f, 200f);
    }

    /** Replace le joueur a une position de depart. */
    public void reset(float startX, float startY) {
        x = startX;
        y = startY;
        vx = FORWARD_SPEED;
        vy = 0f;
    }

    /** Met a jour la physique : gravite puis deplacement. */
    public void update(float dt) {
        vy += GRAVITY * dt;
        x += vx * dt;
        y += vy * dt;
    }

    /** Battement d'ailes : impulsion vers le haut. */
    public void jump() {
        vy = FLAP_VELOCITY;
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public float getVx() { return vx; }
    public float getVy() { return vy; }
    public void setY(float y) { this.y = y; }
    public void setVy(float vy) { this.vy = vy; }

    public float getCenterX() { return x + WIDTH / 2f; }
    public float getCenterY() { return y + HEIGHT / 2f; }
}
