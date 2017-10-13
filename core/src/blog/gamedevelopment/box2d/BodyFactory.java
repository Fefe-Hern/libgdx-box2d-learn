package blog.gamedevelopment.box2d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class BodyFactory {
    private final float DEGTORAD = 0.0174533f;

    private World world;
    private static BodyFactory thisInstance;

    public static final int STEEL = 0;
    public static final int WOOD = 1;
    public static final int RUBBER = 2;
    public static final int STONE = 3;

    // Singleton
    private BodyFactory(World world) {
        this.world = world;
    }

    public static BodyFactory getInstance(World world) {
        if(thisInstance == null) {
            thisInstance = new BodyFactory(world);
        }
        return thisInstance;
    }

    public static FixtureDef makeFixture(int material, Shape shape) {
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        switch(material) {
            case STEEL:
                fixtureDef.density = 1f;
                fixtureDef.friction = 0.3f;
                fixtureDef.restitution = 0.1f;
                break;
            case WOOD:
                fixtureDef.density = 0.5f;
                fixtureDef.friction = 0.7f;
                fixtureDef.restitution = 0.3f;
                break;
            case RUBBER:
                fixtureDef.density = 1f;
                fixtureDef.friction = 0f;
                fixtureDef.restitution = 1f;
                break;
            case STONE:
                fixtureDef.density = 1f;
                fixtureDef.friction = 0.9f;
                fixtureDef.restitution = 0.01f;
                break;
            default:
                fixtureDef.density = 7f;
                fixtureDef.friction = 0.5f;
                fixtureDef.restitution = 0.3f;
                break;
        }

        return fixtureDef;
    }

    // Box Bodies

    public Body makeBoxPolyBody(float posX, float posY, float width, float height, int material, BodyType bodyType) {
        return makeBoxPolyBody(posX, posY, width, height, material, bodyType, false);
    }


    public Body makeBoxPolyBody(float posX, float posY, float width, float height, int material, BodyType bodyType, boolean fixedRotation) {
        // create definition
        BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.type = bodyType;
        boxBodyDef.position.x = posX;
        boxBodyDef.position.y = posY;
        boxBodyDef.fixedRotation = fixedRotation;

        Body boxBody = world.createBody(boxBodyDef);
        PolygonShape poly = new PolygonShape();
        poly.setAsBox(width / 2, height / 2);
        boxBody.createFixture(makeFixture(material, poly));
        poly.dispose();

        return boxBody;
    }

    // Circle Bodies
    // Overloaded constructors with several default values.

    public Body makeCircleBody(float posX, float posY, float radius, int material) {
        return makeCircleBody(posX, posY, radius, material, BodyType.DynamicBody, false);
    }

    public Body makeCircleBody(float posX, float posY, float radius, int material, BodyType bodyType) {
        return makeCircleBody(posX, posY, radius, material, bodyType, false);
    }

    public Body makeCircleBody(float posX, float posY, float radius,
                               int material, BodyType bodyType, boolean fixedRotation) {
        // create definition
        BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.type = bodyType;
        boxBodyDef.position.x = posX;
        boxBodyDef.position.y = posY;
        boxBodyDef.fixedRotation = fixedRotation;

        // Create body to attach definition
        Body boxBody = world.createBody(boxBodyDef);
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(radius / 2);
        boxBody.createFixture(makeFixture(material, circleShape));
        circleShape.dispose();

        return boxBody;
    }

    // Polygon Shapes ! important
    // Concave vs Convex
    public Body makePolygonShapeBody(Vector2[] vertices, float posX, float posY, int material, BodyType bodyType) {
        BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.type = bodyType;
        boxBodyDef.position.x = posX;
        boxBodyDef.position.y = posY;

        Body boxBody = world.createBody(boxBodyDef);

        PolygonShape polygon = new PolygonShape();
        polygon.set(vertices);
        boxBody.createFixture(makeFixture(material, polygon));
        polygon.dispose();

        return boxBody;
    }


    // Cone Shapes. Can be utilized for things like acting as a sensor for view.
    public void makeConeSensor(Body body, float size) {
        FixtureDef fixtureDef =  new FixtureDef();
        // fixtureDef.isSensor = true; //will add in future.

        PolygonShape polygon = new PolygonShape();

        float radius = size;
        Vector2[] vertices = new Vector2[5];
        vertices[0] = new Vector2(0, 0);
        for (int i = 2; i < 6; i++) {
            float angle = (float) (i / 6.0 * 145 * DEGTORAD);
            vertices[i-1] = new Vector2(radius * ((float)Math.cos(angle)), radius * ((float)Math.sin(angle)));
        }
        polygon.set(vertices);
        fixtureDef.shape = polygon;
        body.createFixture(fixtureDef);
        polygon.dispose();
    }

    public void makeAllFixturesSensors(Body body) {
        for (Fixture fix : body.getFixtureList()) {
            fix.setSensor(true);
        }
    }
}
