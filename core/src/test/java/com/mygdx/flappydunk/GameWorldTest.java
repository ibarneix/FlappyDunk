package com.mygdx.flappydunk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/** Tests d'integration de la logique du jeu. */
public class GameWorldTest {

    private static final float EPS = 1e-3f;

    @Test public void startsInReadyState() {
        GameWorld w = new GameWorld();
        assertEquals(GameWorld.State.READY, w.getState());
        assertEquals(0, w.getScore());
    }

    @Test public void flap_startsTheGame() {
        GameWorld w = new GameWorld();
        w.flap();
        assertEquals(GameWorld.State.PLAYING, w.getState());
    }

    @Test public void waterMakesPlayerBounce() {
        GameWorld w = new GameWorld();
        w.flap();                 // PLAYING
        w.update(1.0f);           // grosse chute -> passe sous l'eau
        Player p = w.getPlayer();
        assertEquals("colle au niveau de l'eau", GameWorld.GROUND_Y, p.getY(), EPS);
        assertEquals("rebondit vers le haut", GameWorld.BOUNCE_VELOCITY, p.getVy(), EPS);
        assertEquals(GameWorld.State.PLAYING, w.getState());
    }

    @Test public void passingThroughCenteredHoop_scores() {
        GameWorld w = new GameWorld();
        w.setAutoSpawn(false);
        w.startForTest();         // PLAYING sans saut (vy = 0)
        // cerceau juste devant, a la hauteur de depart du joueur
        float startCenterY = w.getPlayer().getCenterY();
        w.addHoop(70f, startCenterY);

        step(w, 70f);

        assertTrue("le score doit augmenter", w.getScore() >= 1);
        assertEquals(GameWorld.State.PLAYING, w.getState());
    }

    @Test public void hittingHoopRim_endsGame() {
        GameWorld w = new GameWorld();
        w.setAutoSpawn(false);
        w.startForTest();
        float startCenterY = w.getPlayer().getCenterY();
        // cerceau tres haut -> le joueur tape le bord en le franchissant
        w.addHoop(70f, startCenterY + 200f);

        step(w, 70f);

        assertEquals(GameWorld.State.GAME_OVER, w.getState());
        assertEquals(0, w.getScore());
    }

    /** Avance le monde par petits pas jusqu'a ce que le joueur depasse targetX. */
    private void step(GameWorld w, float targetX) {
        for (int i = 0; i < 200 && w.getPlayer().getCenterX() < targetX + 5f; i++) {
            w.update(0.02f);
        }
    }
}
