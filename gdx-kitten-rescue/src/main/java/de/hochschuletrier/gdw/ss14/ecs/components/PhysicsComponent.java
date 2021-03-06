package de.hochschuletrier.gdw.ss14.ecs.components;

import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.physix.*;

/**
 * Created by Daniel Dreher on 30.09.2014.
 */

// use this class as parent for other physicsComponents (e.g. CatPhysicsComponent, DogPhysicsComponent, ...)
public class PhysicsComponent extends PhysixEntity implements Component
{
    public boolean flaggedForRemoval = false;
    public PhysixManager physixManager;

    // entity owning this component
    public int owner = -1;

    @Override
    public void initPhysics(PhysixManager manager)
    {
        physixManager = manager;
        // initialize bodies and fixtures here
        // don't forget to use setPhysicsBody!
    }
    
    public Vector2 defaultPosition = new Vector2();
    public float defaultRotation = 0.0f;
    
    @Override
    public Vector2 getPosition() {
        if (physicsBody != null)
            return super.getPosition();
        else
            return defaultPosition;
    }
    
    @Override
    public float getRotation() {
        if (physicsBody != null)
            return super.getRotation();
        else
            return defaultRotation;
    }
}
