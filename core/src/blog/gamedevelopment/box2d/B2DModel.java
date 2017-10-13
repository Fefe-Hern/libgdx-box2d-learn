package blog.gamedevelopment.box2d;

import blog.gamedevelopment.box2d.input.KeyboardController;
import blog.gamedevelopment.box2d.loader.B2DAssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class B2DModel {
    public World world;
    public Body player;
    public boolean isSwimming = false;

    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;
    Vector3 mousePos;

    private Body bodyD;
    private Body bodyS;
    private Body bodyK;

    private KeyboardController controller;
    private B2DAssetManager assetManager;
    Sound ping;
    Sound boing;

    public static final int BOING_SOUND = 0;
    public static final int PING_SOUND = 1;


    public B2DModel(KeyboardController controller, OrthographicCamera camera, B2DAssetManager assetManager) {
        this.assetManager = assetManager;
        this.controller = controller;
        this.camera = camera;
        world = new World(new Vector2(0, -10f), true);
        world.setContactListener(new B2dContactListener(this));

        assetManager.queueAddSounds();
        assetManager.manager.finishLoading();
        ping = assetManager.manager.get("sounds/ping.wav", Sound.class);
        boing = assetManager.manager.get("sounds/boing.wav", Sound.class);

        createFloor();
        //createObject();
        //createMovingObject();

        // get our body factory singleton and store it in bodyFactory
        BodyFactory bodyFactory = BodyFactory.getInstance(world);

        /*
        bodyFactory.makeCircleBody(1, 1, 2, BodyFactory.RUBBER, BodyType.DynamicBody, false);
        bodyFactory.makeCircleBody(4, 1, 2, BodyFactory.STEEL, BodyType.DynamicBody, false);
        bodyFactory.makeCircleBody(-4, 1, 2, BodyFactory.STONE, BodyType.DynamicBody, false);
        bodyFactory.makeBoxPolyBody(3, 3, 3, 2, BodyFactory.WOOD, BodyType.DynamicBody);

        Vector2[] vertices = new Vector2[5];
        vertices[0] = new Vector2(0f, 2f);
        vertices[1] = new Vector2(4f, 1f);
        vertices[2] = new Vector2(5f, 2f);
        vertices[3] = new Vector2(4f, 4f);
        vertices[4] = new Vector2(0f, 3f);
        bodyFactory.makePolygonShapeBody(vertices, -1, 2, BodyFactory.RUBBER, BodyType.DynamicBody);

        */
        // Player
        player = bodyFactory.makeBoxPolyBody(1, -1, 2, 2, BodyFactory.WOOD, BodyType.DynamicBody, false);

        // Water
        Body water = bodyFactory.makeBoxPolyBody(1, -8, 40, 8, BodyFactory.RUBBER, BodyType.StaticBody, false);
        bodyFactory.makeAllFixturesSensors(water);
        water.setUserData("SEA");
    }

    public void logicStep(float delta) {
        if(controller.left) {
            player.applyForceToCenter(-10, 0, true);
        } else if (controller.right) {
            player.applyForceToCenter(10, 0, true);
        } else if (controller.up) {
            player.applyForceToCenter(0, 10, true);
        } else if (controller.down) {
            player.applyForceToCenter(0, -10, true);
        }

        if(isSwimming) {
            player.applyForceToCenter(0, 50, true);
        }

        if(controller.mouse1Down && pointIntersectsBody(player, controller.mouseLocation)) {
            System.out.println("Player was clicked.");
        }
        world.step(delta, 3, 3);
    }

    private void createObject() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.position.set(0, 0);

        bodyD = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1, 1);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        bodyD.createFixture(shape, 0.0f);
        shape.dispose();
    }

    private void createFloor() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.StaticBody;
        bodyDef.position.set(0, -10);
        bodyS = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(50, 1);
        bodyS.createFixture(shape, 0.0f);
        shape.dispose();
    }

    private void createMovingObject() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.KinematicBody;
        bodyDef.position.set(0, -12);

        bodyK = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1, 1);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        bodyK.createFixture(fixtureDef);
        shape.dispose();

        bodyK.setLinearVelocity(0, 0.75f);
    }

    public boolean pointIntersectsBody(Body body, Vector2 mouseLocation) {
        mousePos = new Vector3(mouseLocation, 0);
        camera.unproject(mousePos);
        if(body.getFixtureList().first().testPoint(mousePos.x, mousePos.y)) {
            return true;
        }
        return false;
    }

    public void playSound(int sound) {
        switch(sound) {
            case BOING_SOUND:
                boing.play();
                break;
            case PING_SOUND:
                ping.play();
                break;
        }
    }
}
