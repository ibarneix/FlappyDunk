package com.mygdx.flappydunk;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Player player;
    private OrthographicCamera cam;


    @Override
    public void create() {
        batch = new SpriteBatch();
        player = new Player(new Texture("flappy.png"));
        player.setVelocity(200,200);

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        cam = new OrthographicCamera();
        // Y vers le haut : la gravité (ay negatif) fait tomber le joueur,
        // et un saut correspond a une velocite Y positive.
        cam.setToOrtho(false, w, h);


    }

    @Override
    public void render() {
        ScreenUtils.clear(1f, 1f, 1f, 1f);

        // Saut : impulsion au moment ou on appuie (pas tant qu'on maintient),
        // sinon la velocite serait remise a zero a chaque frame et la gravite n'agirait jamais.
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            player.setVelocity(200.0f, 400.0f);
        }

        // La camera suit le joueur uniquement sur l'axe X.
        // On ne touche pas a position.y -> elle reste au centre vertical de l'ecran.
        cam.position.x = player.getX();
        cam.update();

        // ESSENTIEL : applique la camera au batch, sinon deplacer la camera n'a aucun effet.
        batch.setProjectionMatrix(cam.combined);

        batch.begin();
        player.update(Gdx.graphics.getDeltaTime(), batch);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        //image.dispose();
    }
}

