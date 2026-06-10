package com.mygdx.flappydunk;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Player player;
    private OrthographicCamera cam;

    // Fond qui defile derriere le joueur.
    private Texture background;
    private TextureRegion bgRegion;


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

        // Fond : wrap en Repeat pour pouvoir le faire defiler a l'infini
        // (on fera glisser les coordonnees de texture, voir render()).
        background = new Texture("background.png");
        background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        bgRegion = new TextureRegion(background);
    }

    @Override
    public void render() {
        ScreenUtils.clear(1f, 1f, 1f, 1f);

        // Saut : impulsion au moment ou on appuie (pas tant qu'on maintient),
        // sinon la velocite serait remise a zero a chaque frame et la gravite n'agirait jamais.
        // On garde vx = 200 -> le joueur continue d'avancer vers la droite.
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            player.setVelocity(200.0f, 400.0f);
        }

        // La camera suit le joueur uniquement sur l'axe X.
        // On ne touche pas a position.y -> elle reste au centre vertical de l'ecran.
        cam.position.x = player.getX();
        cam.update();

        // ESSENTIEL : applique la camera au batch, sinon deplacer la camera n'a aucun effet.
        batch.setProjectionMatrix(cam.combined);

        // Rectangle visible par la camera (en coordonnees monde).
        float w = cam.viewportWidth;
        float h = cam.viewportHeight;
        float left = cam.position.x - w / 2f;
        float bottom = cam.position.y - h / 2f;

        // Defilement du fond : on fait glisser les coordonnees de texture (u)
        // en fonction de la position de la camera. parallax < 1 => le fond
        // defile plus lentement que le joueur -> effet de profondeur.
        float parallax = 0.3f;
        float u = (cam.position.x * parallax) / background.getWidth();
        float u2 = u + w / background.getWidth();
        bgRegion.setRegion(u, 0f, u2, 1f);

        batch.begin();
        // 1) le fond d'abord (donc dessine derriere tout le reste)
        batch.draw(bgRegion, left, bottom, w, h);
        // 2) le joueur par-dessus
        player.update(Gdx.graphics.getDeltaTime(), batch);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
    }
}
