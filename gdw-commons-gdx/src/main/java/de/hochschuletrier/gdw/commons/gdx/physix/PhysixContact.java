package de.hochschuletrier.gdw.commons.gdx.physix;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import java.util.function.Consumer;

/**
 *
 * @author Santo Pfingsten
 */
public class PhysixContact {
    private Contact contact;
    private boolean isA;
    
    void run(Contact contact, Consumer<PhysixEntity> consumer) {
        this.contact = contact;
        isA = true;
        PhysixEntity owner = getMyPhysixBody().getOwner();
        if(owner != null)
            consumer.accept(owner);
        isA = false;
        owner = getMyPhysixBody().getOwner();
        if(owner != null)
            consumer.accept(owner);
    }
    
    public boolean isSelfFixtureA() {
        return isA;
    }
    
    public Fixture getMyFixture() {
        return (isA ? contact.getFixtureA() : contact.getFixtureB());
    }
    
    public Fixture getOtherFixture() {
        return (isA ? contact.getFixtureB() : contact.getFixtureA());
    }

	public int getMyChildIndex () {
		return isA ? contact.getChildIndexA() : contact.getChildIndexB();
	}

	public int getOtherChildIndex () {
		return isA ? contact.getChildIndexB() : contact.getChildIndexA();
	}
    
    public PhysixBody getMyPhysixBody() {
        return (PhysixBody)getMyFixture().getBody().getUserData();
    }
    
    public PhysixBody getOtherPhysixBody() {
        return (PhysixBody)getOtherFixture().getBody().getUserData();
    }
    
    
	public WorldManifold getWorldManifold () {
		return contact.getWorldManifold();
	}

	public boolean isTouching () {
		return contact.isTouching();
	}

	/** Enable/disable this contact. This can be used inside the pre-solve contact listener. The contact is only disabled for the
	 * current time step (or sub-step in continuous collisions). */
	public void setEnabled (boolean flag) {
		contact.setEnabled(flag);
	}

	/** Has this contact been disabled? */
	public boolean isEnabled () {
		return contact.isEnabled();
	}
    
	/** Override the default friction mixture. You can call this in b2ContactListener::PreSolve. This value persists until set or
	 * reset. */
	public void setFriction (float friction) {
		contact.setFriction(friction);
	}

	/** Get the friction. */
	public float getFriction () {
		return contact.getFriction();
	}

	/** Reset the friction mixture to the default value. */
	public void resetFriction () {
		contact.resetFriction();
	}

	/** Override the default restitution mixture. You can call this in b2ContactListener::PreSolve. The value persists until you set
	 * or reset. */
	public void setRestitution (float restitution) {
		contact.setRestitution(restitution);
	}

	/** Get the restitution. */
	public float getRestitution () {
		return contact.getRestitution();
	}

	/** Reset the restitution to the default value. */
	public void resetRestitution () {
		contact.ResetRestitution();
	}
		
	/** Get the tangent speed. */
	public float getTangentSpeed () {
		return contact.getTangentSpeed();
	}

	/** Set the tangent speed. */
	public void setTangentSpeed (float speed) {
		contact.setTangentSpeed(speed);
	}
}
