package blog.gamedevelopment.box2d.views;

import blog.gamedevelopment.box2d.B2DModel;
import blog.gamedevelopment.box2d.Box2DTutorial;
import blog.gamedevelopment.box2d.input.KeyboardController;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

public class MainScreen implements Screen {

    private Box2DTutorial parent;
    private B2DModel model;
    private OrthographicCamera camera;
    KeyboardController controller;
    private Box2DDebugRenderer debugRenderer;

    SpriteBatch sb;

    TextureAtlas atlas;
    TextureAtlas.AtlasRegion playerTex;

    public MainScreen(Box2DTutorial parent) {
        this.parent = parent;
        controller = new KeyboardController();
        camera = new OrthographicCamera(32, 24);
        sb = new SpriteBatch();
        sb.setProjectionMatrix(camera.combined);

        atlas = parent.assetManager.manager.get("images/game/game.atlas");

        playerTex = atlas.findRegion("player");

        model = new B2DModel(controller, camera, parent.assetManager);
        debugRenderer = new Box2DDebugRenderer(true, true, true, true,
                true, true);

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(controller);
    }

    @Override
    public void render(float delta) {
        model.logicStep(delta);
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        debugRenderer.render(model.world, camera.combined);
        sb.begin();
        sb.draw(playerTex, model.player.getPosition().x - 1, model.player.getPosition().y - 1, 2, 2);
        sb.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
