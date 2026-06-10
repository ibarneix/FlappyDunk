package com.mygdx.flappydunk;

/**
 * Fonctions PURES de collision pour le passage d'un cerceau.
 *
 * Principe : on detecte l'instant precis ou le centre du joueur franchit le plan
 * vertical du cerceau (x = hoopX), on interpole sa hauteur exacte a ce moment-la,
 * puis on regarde si tout le disque du joueur tient dans l'ouverture.
 * Cette approche est deterministe et insensible a la taille du pas de temps,
 * ce qui rend les collisions fiables (et faciles a tester).
 */
public final class Collisions {

    private Collisions() {}

    /** Resultat de l'evaluation d'un cerceau pour une frame. */
    public enum Pass {
        NONE,   // le joueur n'a pas franchi le cerceau cette frame
        SCORE,  // il est passe proprement dans l'ouverture
        HIT     // il a touche le bord
    }

    /** Le joueur (allant vers la droite) a-t-il franchi le plan x = planeX cette frame ? */
    public static boolean crossedPlane(float prevX, float curX, float planeX) {
        return prevX < planeX && curX >= planeX;
    }

    /**
     * Hauteur (y) du joueur a l'instant exact ou il franchit x = planeX,
     * par interpolation lineaire entre la position precedente et la courante.
     */
    public static float yAtCrossing(float prevX, float prevY, float curX, float curY, float planeX) {
        float dx = curX - prevX;
        if (dx == 0f) {
            return curY;
        }
        float t = (planeX - prevX) / dx;
        if (t < 0f) t = 0f;
        else if (t > 1f) t = 1f;
        return prevY + t * (curY - prevY);
    }

    /**
     * Le disque du joueur (rayon radius, centre en yAtCross) tient-il entierement
     * dans l'ouverture (demi-hauteur openingRy autour de hoopY) ?
     */
    public static boolean passesThroughOpening(float yAtCross, float hoopY, float openingRy, float radius) {
        return Math.abs(yAtCross - hoopY) <= openingRy - radius;
    }

    /** Evalue le passage d'un cerceau pour une frame (NONE / SCORE / HIT). */
    public static Pass evaluate(float prevX, float prevY, float curX, float curY,
                                float hoopX, float hoopY, float openingRy, float radius) {
        if (!crossedPlane(prevX, curX, hoopX)) {
            return Pass.NONE;
        }
        float y = yAtCrossing(prevX, prevY, curX, curY, hoopX);
        return passesThroughOpening(y, hoopY, openingRy, radius) ? Pass.SCORE : Pass.HIT;
    }
}
