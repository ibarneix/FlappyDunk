package com.mygdx.flappydunk;

/**
 * Un cerceau horizontal (ellipse) que le joueur doit traverser.
 * Classe PURE (geometrie + etat). Le rendu est fait par Main.
 * (x, y) = centre du cerceau / centre de l'ouverture.
 */
public class Hoop {
    // Dimensions (doivent correspondre a l'image hoop.png : 280 x 172).
    public static final float OUTER_RX = 140f;   // demi-largeur de l'ellipse exterieure
    public static final float OUTER_RY = 86f;    // demi-hauteur de l'ellipse exterieure
    public static final float OPENING_RY = 64f;  // demi-hauteur du trou (pour la collision)

    private final float x;
    private final float y;
    private boolean scored = false;   // deja resolu (passe ou rate) -> on ne le rejuge plus

    public Hoop(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public boolean isScored() { return scored; }
    public void setScored(boolean scored) { this.scored = scored; }
}
