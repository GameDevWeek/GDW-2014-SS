package de.hochschuletrier.gdw.ss14.ecs.components;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;
import de.hochschuletrier.gdw.commons.gdx.physix.*;

/**
 * Created by Daniel Dreher on 02.10.2014.
 */
public class WoolPhysicsComponent extends PhysicsComponent
{
    
    public boolean isSeen = false;
    private Vector2 initPosition;
    private float radius;
    private float rotation;
    private float friction = 1000f;
    private float restitution = 0.1f;
    public short mask, category, group;

    public WoolPhysicsComponent(Vector2 position, float radius, float rotation, short mask, short category, short group)
    {
        this.initPosition = position;
        this.radius = radius;
        this.rotation = rotation;
        this.mask = mask;
        this.category = category;
        this.group = group;

    }

    @Override
    public void initPhysics(PhysixManager manager) {

        PhysixFixtureDef fixturedef = new PhysixFixtureDef(manager).density(10)
                .friction(friction).restitution(restitution).mask(mask).category(category).groupIndex(group);
        
        physicsBody = new PhysixBodyDef(BodyDef.BodyType.DynamicBody, manager)
                .position(initPosition).fixedRotation(true).angle(rotation).create();

        physicsBody.setAngularVelocity(0);
        physicsBody.setMassData(100);
        
        physicsBody.createFixture(fixturedef.shapeCircle(radius));
        //physicsBody.createFixture(fixturedef.shapeBox(width, height));
        setPhysicsBody(physicsBody);
        physicsBody.setOwner(this);
    }
}
