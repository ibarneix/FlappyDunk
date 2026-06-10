package com.mygdx.flappydunk;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/** Tests de la physique du joueur. */
public class PlayerTest {

    private static final float EPS = 1e-3f;

    @Test public void reset_setsStartState() {
        Player p = new Player();
        p.reset(10f, 200f);
        assertEquals(10f, p.getX(), EPS);
        assertEquals(200f, p.getY(), EPS);
        assertEquals(Player.FORWARD_SPEED, p.getVx(), EPS);
        assertEquals(0f, p.getVy(), EPS);
    }

    @Test public void jump_setsUpwardVelocity() {
        Player p = new Player();
        p.jump();
        assertEquals(Player.FLAP_VELOCITY, p.getVy(), EPS);
    }

    @Test public void update_appliesGravityAndMotion() {
        Player p = new Player();
        p.reset(0f, 200f);
        p.update(0.1f);
        // vy = 0 + (-980)*0.1 = -98 ; x = 200*0.1 = 20 ; y = 200 + (-98)*0.1 = 190.2
        assertEquals(-98f, p.getVy(), EPS);
        assertEquals(20f, p.getX(), EPS);
        assertEquals(190.2f, p.getY(), EPS);
    }

    @Test public void center_isOffsetByHalfSize() {
        Player p = new Player();
        p.reset(0f, 0f);
        assertEquals(Player.WIDTH / 2f, p.getCenterX(), EPS);
        assertEquals(Player.HEIGHT / 2f, p.getCenterY(), EPS);
    }
}
