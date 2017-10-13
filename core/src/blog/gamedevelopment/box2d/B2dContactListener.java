package blog.gamedevelopment.box2d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class B2dContactListener implements ContactListener {
    private B2DModel parent;

    public B2dContactListener(B2DModel parent) {
        this.parent = parent;
    }

    @Override
    public void beginContact(Contact contact) {
        System.out.println("Contact");
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        System.out.println(fa.getBody().getType() + " has hit " + fb.getBody().getType());

        if(fa.getBody().getUserData() == "SEA") {
            parent.isSwimming = true;
            return;
        } else if(fb.getBody().getUserData() == "SEA") {
            parent.isSwimming = true;
            return;
        }

        if(fa.getBody().getType() == BodyDef.BodyType.StaticBody) {
            shootUpInAir(fa, fb);
        } else if(fb.getBody().getType() == BodyDef.BodyType.StaticBody) {
            shootUpInAir(fb, fa);
        } else {

        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if(fa.getBody().getUserData() == "SEA") {
            parent.isSwimming = false;
            return;
        } else if(fb.getBody().getUserData() == "SEA") {
            parent.isSwimming = false;
            return;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    private void shootUpInAir(Fixture staticFixture, Fixture otherFixture) {
        System.out.println("Adding Force");
        otherFixture.getBody().applyForceToCenter(new Vector2(-5, -500), true);
        parent.playSound(B2DModel.BOING_SOUND);
    }
}
