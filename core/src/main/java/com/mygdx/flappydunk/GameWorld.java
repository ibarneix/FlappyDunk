package com.mygdx.flappydunk;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

/**
 * Toute la logique du jeu, sans aucun rendu (donc testable).
 * Main se contente de lire l'etat de ce monde pour l'afficher.
 */
public class GameWorld {

    public enum State { READY, PLAYING, GAME_OVER }

    // --- Reglages du monde ---
    public static final float START_Y = 200f;
    public static final float GROUND_Y = 40f;          // niveau de l'eau (rebond)
    public static final float CEILING_Y = 380f;        // plafond
    public static final float BOUNCE_VELOCITY = 520f;  // rebond sur l'eau
    // Cerceaux
    public static final float FIRST_HOOP_X = 560f;
    public static final float HOOP_SPACING = 450f;
    public static final float HOOP_MIN_Y = 120f;
    public static final float HOOP_MAX_Y = 360f;
    public static final float HOOP_MAX_DELTA = 150f;   // ecart vertical max entre 2 cerceaux
    public static final float SPAWN_AHEAD = 760f;      // distance d'apparition devant le joueur
    public static final float DESPAWN_BEHIND = 400f;   // distance de suppression derriere
    // Swish (passage pile au centre)
    public static final float SWISH_THRESHOLD = 14f;
    public static final float SWISH_FLASH_TIME = 0.8f;

    private final Player player = new Player();
    private final Array<Hoop> hoops = new Array<Hoop>();
    private State state;
    private int score;
    private float nextHoopX;
    private float lastHoopY;
    private float swishFlash;

    // Desactivable pour les tests afin de controler les cerceaux a la main.
    private boolean autoSpawn = true;

    public GameWorld() {
        reset();
    }

    public final void reset() {
        player.reset(0f, START_Y);
        hoops.clear();
        nextHoopX = FIRST_HOOP_X;
        lastHoopY = (HOOP_MIN_Y + HOOP_MAX_Y) / 2f;
        score = 0;
        swishFlash = 0f;
        state = State.READY;
    }

    /** Action du joueur (tap / espace) : demarre, saute, ou relance selon l'etat. */
    public void flap() {
        switch (state) {
            case READY:
                state = State.PLAYING;
                player.jump();
                break;
            case PLAYING:
                player.jump();
                break;
            case GAME_OVER:
                reset();
                state = State.PLAYING;
                player.jump();
                break;
        }
    }

    public void update(float dt) {
        if (swishFlash > 0f) {
            swishFlash = Math.max(0f, swishFlash - dt);
        }
        if (state != State.PLAYING) {
            return;
        }

        float prevX = player.getCenterX();
        float prevY = player.getCenterY();

        player.update(dt);
        applyBounds();

        if (autoSpawn) {
            spawnHoops();
        }
        resolveHoops(prevX, prevY);
        despawnHoops();
    }

    /** Rebond sur l'eau en bas, plafond en haut. */
    private void applyBounds() {
        if (player.getY() <= GROUND_Y) {
            player.setY(GROUND_Y);
            player.setVy(BOUNCE_VELOCITY);
        }
        if (player.getY() >= CEILING_Y) {
            player.setY(CEILING_Y);
            if (player.getVy() > 0f) {
                player.setVy(0f);
            }
        }
    }

    private void spawnHoops() {
        while (nextHoopX < player.getCenterX() + SPAWN_AHEAD) {
            hoops.add(new Hoop(nextHoopX, nextHoopY()));
            nextHoopX += HOOP_SPACING;
        }
    }

    /** Hauteur du prochain cerceau : marche aleatoire bornee pour rester jouable. */
    private float nextHoopY() {
        float min = Math.max(HOOP_MIN_Y, lastHoopY - HOOP_MAX_DELTA);
        float max = Math.min(HOOP_MAX_Y, lastHoopY + HOOP_MAX_DELTA);
        lastHoopY = MathUtils.random(min, max);
        return lastHoopY;
    }

    /** Detecte le passage du joueur a travers les cerceaux. */
    private void resolveHoops(float prevX, float prevY) {
        float curX = player.getCenterX();
        float curY = player.getCenterY();
        for (int i = 0; i < hoops.size; i++) {
            Hoop h = hoops.get(i);
            if (h.isScored() || !Collisions.crossedPlane(prevX, curX, h.getX())) {
                continue;
            }
            float y = Collisions.yAtCrossing(prevX, prevY, curX, curY, h.getX());
            float off = Math.abs(y - h.getY());
            h.setScored(true);
            if (off <= Hoop.OPENING_RY - Player.RADIUS) {
                if (off <= SWISH_THRESHOLD) {
                    score += 2;                 // swish : pile au centre
                    swishFlash = SWISH_FLASH_TIME;
                } else {
                    score += 1;
                }
            } else {
                state = State.GAME_OVER;        // a touche le bord
            }
        }
    }

    private void despawnHoops() {
        float limit = player.getCenterX() - DESPAWN_BEHIND;
        for (int i = hoops.size - 1; i >= 0; i--) {
            if (hoops.get(i).getX() < limit) {
                hoops.removeIndex(i);
            }
        }
    }

    // --- Accesseurs (lecture pour le rendu) ---
    public Player getPlayer() { return player; }
    public Array<Hoop> getHoops() { return hoops; }
    public State getState() { return state; }
    public int getScore() { return score; }
    public boolean isSwish() { return swishFlash > 0f; }

    // --- Crochets pour les tests (package-private) ---
    void setAutoSpawn(boolean v) { autoSpawn = v; }
    void startForTest() { state = State.PLAYING; }
    void addHoop(float x, float y) { hoops.add(new Hoop(x, y)); }
}
