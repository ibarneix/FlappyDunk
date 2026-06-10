package com.mygdx.flappydunk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/** Tests des fonctions pures de collision : c'est ici qu'on garantit des collisions "parfaites". */
public class CollisionsTest {

    private static final float OPENING = Hoop.OPENING_RY; // 64
    private static final float R = Player.RADIUS;          // 32
    private static final float EPS = 1e-3f;

    // ---- crossedPlane ----

    @Test public void crossing_isDetected_whenPassingThrough() {
        assertTrue(Collisions.crossedPlane(90f, 110f, 100f));
        assertTrue(Collisions.crossedPlane(99.999f, 100f, 100f));
    }

    @Test public void crossing_isFalse_whenNotReachedOrAlreadyPast() {
        assertFalse("pas encore atteint", Collisions.crossedPlane(80f, 95f, 100f));
        assertFalse("deja passe", Collisions.crossedPlane(110f, 130f, 100f));
        assertFalse("pile sur le plan au depart = pas un franchissement", Collisions.crossedPlane(100f, 110f, 100f));
    }

    @Test public void crossing_isFalse_whenMovingBackwards() {
        assertFalse(Collisions.crossedPlane(110f, 90f, 100f));
    }

    // ---- yAtCrossing (interpolation) ----

    @Test public void yAtCrossing_straightLine_keepsHeight() {
        assertEquals(200f, Collisions.yAtCrossing(90f, 200f, 110f, 200f, 100f), EPS);
    }

    @Test public void yAtCrossing_diagonal_interpolatesMidpoint() {
        // de (90,100) a (110,300), le plan x=100 est a mi-chemin -> y = 200
        assertEquals(200f, Collisions.yAtCrossing(90f, 100f, 110f, 300f, 100f), EPS);
    }

    @Test public void yAtCrossing_verticalMove_returnsCurrentY() {
        // dx = 0 -> pas de division par zero
        assertEquals(250f, Collisions.yAtCrossing(100f, 150f, 100f, 250f, 100f), EPS);
    }

    // ---- passesThroughOpening ----

    @Test public void opening_acceptsCentered() {
        assertTrue(Collisions.passesThroughOpening(300f, 300f, OPENING, R));
    }

    @Test public void opening_acceptsExactBoundary() {
        // marge exacte : offset == openingRy - radius (64 - 32 = 32)
        assertTrue(Collisions.passesThroughOpening(300f + (OPENING - R), 300f, OPENING, R));
    }

    @Test public void opening_rejectsJustOutsideBoundary() {
        assertFalse(Collisions.passesThroughOpening(300f + (OPENING - R) + 0.01f, 300f, OPENING, R));
    }

    @Test public void opening_rejectsFarAboveAndBelow() {
        assertFalse(Collisions.passesThroughOpening(400f, 300f, OPENING, R));
        assertFalse(Collisions.passesThroughOpening(200f, 300f, OPENING, R));
    }

    // ---- evaluate (cas complets) ----

    @Test public void evaluate_centeredPass_isScore() {
        Collisions.Pass p = Collisions.evaluate(90f, 300f, 110f, 300f, 100f, 300f, OPENING, R);
        assertEquals(Collisions.Pass.SCORE, p);
    }

    @Test public void evaluate_tooHigh_isHit() {
        Collisions.Pass p = Collisions.evaluate(90f, 400f, 110f, 400f, 100f, 300f, OPENING, R);
        assertEquals(Collisions.Pass.HIT, p);
    }

    @Test public void evaluate_tooLow_isHit() {
        Collisions.Pass p = Collisions.evaluate(90f, 200f, 110f, 200f, 100f, 300f, OPENING, R);
        assertEquals(Collisions.Pass.HIT, p);
    }

    @Test public void evaluate_noCrossing_isNone() {
        Collisions.Pass p = Collisions.evaluate(80f, 300f, 95f, 300f, 100f, 300f, OPENING, R);
        assertEquals(Collisions.Pass.NONE, p);
    }

    @Test public void evaluate_usesInterpolatedHeight_notEndOfFrame() {
        // Le joueur traverse le plan tout en haut de l'ouverture (entree pile au bord),
        // mais finit la frame loin au-dessus. Juger a la fin dirait HIT ; l'interpolation
        // au plan dit SCORE. C'est ce qui rend la collision juste.
        float plane = 96f;
        // a x=96 (t petit), y ~ 300 ; a la fin (x=135) y = 500
        Collisions.Pass p = Collisions.evaluate(95f, 300f, 135f, 500f, plane, 300f, OPENING, R);
        assertEquals(Collisions.Pass.SCORE, p);
    }
}
