package blog.gamedevelopment.box2d.views;

import blog.gamedevelopment.box2d.Box2DTutorial;
import blog.gamedevelopment.box2d.loader.B2DAssetManager;
import blog.gamedevelopment.box2d.loader.LoadingBarPart;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class LoadingScreen implements Screen {

    private Box2DTutorial parent;
    private Stage stage;
    private SpriteBatch sb;
    private TextureAtlas atlas;
    private TextureAtlas.AtlasRegion title;
    private TextureAtlas.AtlasRegion dash;
    private Animation<TextureRegion> flameAnimation;
    private TextureRegion background;

    Image titleImage;
    Table table;
    Table loadingTable;

    public final int IMAGE = 0;		// loading images
    public final int FONT = 1;		// loading fonts
    public final int PARTY = 2;		// loading particle effects
    public final int SOUND = 3;		// loading sounds
    public final int MUSIC = 4;		// loading music
    public final int DONE = 5;

    private int currentLoadingStage;
    public float countDown = 5f;
    private float stateTime;

    public LoadingScreen(Box2DTutorial parent) {
        this.parent = parent;
        stage = new Stage(new ScreenViewport());
        sb = new SpriteBatch();
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        currentLoadingStage = 0;

        loadAssets();
        stateTime = 0f;
        parent.assetManager.queueAddImages();
        System.out.println("Loading images....");
    }

    @Override
    public void show() {
        titleImage = new Image(title);
        table = new Table();
        table.setFillParent(true);
        table.setDebug(false);
        table.setBackground(new TiledDrawable(background));

        loadingTable = new Table();
        loadingTable.add(new LoadingBarPart(dash, flameAnimation));
        loadingTable.add(new LoadingBarPart(dash, flameAnimation));
        loadingTable.add(new LoadingBarPart(dash, flameAnimation));
        loadingTable.add(new LoadingBarPart(dash, flameAnimation));
        loadingTable.add(new LoadingBarPart(dash, flameAnimation));
        loadingTable.add(new LoadingBarPart(dash, flameAnimation));
        loadingTable.add(new LoadingBarPart(dash, flameAnimation));
        loadingTable.add(new LoadingBarPart(dash, flameAnimation));
        loadingTable.add(new LoadingBarPart(dash, flameAnimation));
        loadingTable.add(new LoadingBarPart(dash, flameAnimation));
        table.add(titleImage).align(Align.center).pad(10, 0, 0, 0).colspan(10);
        table.row();
        table.add(loadingTable).width(400);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        /*stateTime += delta;
        TextureRegion currentFrame = flameAnimation.getKeyFrame(stateTime, true);

        // Start spritebatch and draw logo
        sb.begin();
        drawLoadingBar(currentLoadingStage * 2, currentFrame);
        sb.draw(title, 135, 250);
        sb.end();*/

        // Check if asset manager finished loading
        if(parent.assetManager.manager.update()) {
            currentLoadingStage += 1;
            if(currentLoadingStage <= 5) {
                loadingTable.getCells().get((currentLoadingStage - 1) * 2).getActor().setVisible(true);
                loadingTable.getCells().get((currentLoadingStage - 1) * 2 + 1).getActor().setVisible(true);
            }
            switch(currentLoadingStage) {
                case FONT:
                    System.out.println("Loading Fonts....");
                    parent.assetManager.queueAddFonts();
                    break;
                case PARTY:
                    System.out.println("Loading particle effects...");
                    parent.assetManager.queueAddParticleEffects();
                    break;
                case SOUND:
                    System.out.println("Loading sounds....");
                    parent.assetManager.queueAddSounds();
                    break;
                case MUSIC:
                    System.out.println("Loading music");
                    parent.assetManager.queueAddMusic();
                    break;
                case DONE:
                    System.out.println("Finished");
                    break;
            }
            if(currentLoadingStage > 5) {
                countDown -= delta;
                currentLoadingStage = DONE;
                if(countDown < 0) {
                    parent.changeScreen(Box2DTutorial.MENU);
                }
            }
        }

        stage.act();
        stage.draw();
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

    private void drawLoadingBar(int stage, TextureRegion currentFrame) {
        for (int i = 0; i < stage; i++) {
            sb.draw(currentFrame, 50 + (i*50), 150, 50, 50);
            sb.draw(dash, 35 + (i*50), 140, 80, 80);
        }
    }

    private void loadAssets() {
        parent.assetManager.queueAddLoadingImages();
        parent.assetManager.manager.finishLoading();

        atlas = parent.assetManager.manager.get("images/loading/loading.atlas");
        title = atlas.findRegion("title");
        dash = atlas.findRegion("dash");
        flameAnimation = new Animation<TextureRegion>(0.07f, atlas.findRegions("flames"), Animation.PlayMode.LOOP);
        background = atlas.findRegion("background");
    }
}
