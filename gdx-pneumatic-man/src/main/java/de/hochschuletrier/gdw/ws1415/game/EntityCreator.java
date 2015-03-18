package de.hochschuletrier.gdw.ws1415.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.commons.utils.Rectangle;


import de.hochschuletrier.gdw.ws1415.game.components.*;
import de.hochschuletrier.gdw.ws1415.game.systems.AISystem;
import de.hochschuletrier.gdw.ws1415.game.utils.AIType;
import de.hochschuletrier.gdw.ws1415.game.utils.Direction;

import de.hochschuletrier.gdw.ws1415.game.utils.EventBoxType;
import de.hochschuletrier.gdw.ws1415.game.utils.PlatformMode;

public class EntityCreator {

    public static PooledEngine engine;
    public static PhysixSystem physixSystem;

    public static Entity createAndAddPlayer(float x, float y, float rotation) {
        Entity entity = engine.createEntity();

        entity.add(engine.createComponent(AnimationComponent.class));
        entity.add(engine.createComponent(PositionComponent.class));
        entity.add(engine.createComponent(DamageComponent.class));
        entity.add(engine.createComponent(SpawnComponent.class));
        entity.add(engine.createComponent(InputComponent.class));

        float width = GameConstants.getTileSizeX() * 0.9f;
        float height = GameConstants.getTileSizeY() * 1.5f;

        PhysixBodyComponent bodyComponent = engine
                .createComponent(PhysixBodyComponent.class);
        PhysixBodyDef bodyDef = new PhysixBodyDef(BodyDef.BodyType.DynamicBody,
                physixSystem).position(x - width/2, y - height/2).fixedRotation(true);
        bodyComponent.init(bodyDef, physixSystem, entity);
        bodyComponent.getBody().setUserData(bodyComponent);
        PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physixSystem)
                .density(1).friction(0).restitution(0.1f)
                .shapeBox(width, height);
        Fixture fixture = bodyComponent.createFixture(fixtureDef);
        fixture.setUserData(bodyComponent);
        entity.add(bodyComponent);

        JumpComponent jumpComponent = engine.createComponent(JumpComponent.class);
        jumpComponent.jumpImpulse = 20000.0f;
        jumpComponent.restingTime = 0.02f;
        entity.add(jumpComponent);

        MovementComponent moveComponent = engine.createComponent(MovementComponent.class);
        moveComponent.speed = 10000.0f;
        entity.add(moveComponent);

        DestructableBlockComponent blockComp = engine.createComponent(DestructableBlockComponent.class);
        entity.add(blockComp);

        engine.addEntity(entity);
        return entity;
    }

    /**
     *  Enemy FIXME: there are more different types of enemies, implement them
     */
    public static Entity createAndAddEnemy(float x, float y, Direction direction, AIType type) {
        Entity entity = engine.createEntity();

        float width = GameConstants.getTileSizeX();
        float height = GameConstants.getTileSizeY();

        entity.add(engine.createComponent(DamageComponent.class));
        entity.add(engine.createComponent(AIComponent.class));
        entity.add(engine.createComponent(AnimationComponent.class));
        entity.add(engine.createComponent(PositionComponent.class));
        entity.add(engine.createComponent(SpawnComponent.class));

        PhysixBodyComponent pbc = new PhysixBodyComponent();
        PhysixBodyDef pbdy = new PhysixBodyDef(BodyDef.BodyType.DynamicBody,
                physixSystem).position(x - width/2, y - height/2).fixedRotation(true);
        PhysixFixtureDef pfx = new PhysixFixtureDef(physixSystem).density(1)
                .friction(1f).shapeBox(width, height).restitution(0.1f);
        Fixture fixture = pbc.createFixture(pfx);
        fixture.setUserData(pbdy);
        pbc.init(pbdy, physixSystem, entity);
        entity.add(pbc);

        AIComponent ai = new AIComponent();
        ai.type = type;
        entity.add(ai);

        DirectionComponent d = new DirectionComponent();
        d.facingDirection = direction;
        entity.add(d);

        engine.addEntity(entity);
        return entity;
    }

    public static Entity createAndAddEventBox(EventBoxType type, float x, float y) {
        Entity box = engine.createEntity();

        box.add(engine.createComponent(TriggerComponent.class));
        box.add(engine.createComponent(PositionComponent.class));

        engine.addEntity(box);
        return box;
    }


    /**
     *  Indestructable Block
     */
    public static Entity createAndAddInvulnerableFloor(Rectangle rect) {
        float width = rect.width * GameConstants.getTileSizeX();
        float height = rect.height * GameConstants.getTileSizeY();
        float x = rect.x * GameConstants.getTileSizeX() + width / 2;
        float y = rect.y * GameConstants.getTileSizeY() + height / 2;

        Entity entity = engine.createEntity();

        entity.add(defineBoxPhysixBodyComponent(entity, x, y, width, height,
                true, 1f, 1f, 0.1f));

        DestructableBlockComponent blockComp = engine.createComponent(DestructableBlockComponent.class);
        entity.add(blockComp);

        engine.addEntity(entity);
        return entity;
    }

    /**
     * This is the Block who'll fall down onto the player
     */
    public static Entity createTrapBlock(float x, float y, int trapId) {

        Entity entity = engine.createEntity();

        PhysixBodyComponent bodyComponent = engine.createComponent(PhysixBodyComponent.class);
        PhysixBodyDef bodyDef = new PhysixBodyDef(BodyDef.BodyType.KinematicBody, physixSystem).position(x, y).fixedRotation(true);
        bodyComponent.init(bodyDef, physixSystem, entity);
        PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physixSystem)
                .density(1).friction(1f).shapeBox(GameConstants.getTileSizeX(), GameConstants.getTileSizeY())
                .restitution(0.1f);
        Fixture fixture = bodyComponent.createFixture(fixtureDef);
        fixture.setUserData(entity);
        entity.add(bodyComponent);

        FallingRockComponent rockComponent = new FallingRockComponent();
        rockComponent.falling = false;
        rockComponent.id = trapId;

        engine.addEntity(entity);
        return entity;
    }

    /**
     * This is a Sensor to trigger a falling block
     */
    public static Entity createTrapSensor(float x, float y, float dx, float dy, Entity rock) {

        Entity entity = engine.createEntity();

        PhysixBodyComponent bodyComponent = engine.createComponent(PhysixBodyComponent.class);
        PhysixBodyDef bodyDef = new PhysixBodyDef(BodyDef.BodyType.KinematicBody, physixSystem).position(x, y).fixedRotation(true);
        bodyComponent.init(bodyDef, physixSystem, entity);
        PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physixSystem)
                .density(1).friction(1f)
                .shapeBox(dx, dy)
                .restitution(0.1f)
                .sensor(true);
        Fixture fixture = bodyComponent.createFixture(fixtureDef);
        fixture.setUserData(entity);
        entity.add(bodyComponent);

        FallingRockTriggerComponent rockComponent = new FallingRockTriggerComponent();
        rockComponent.rockEntity = rock;

        engine.addEntity(entity);
        return entity;
    }

    /**
     *  Destructable Block
     */
    public static Entity createAndAddVulnerableFloor(float x, float y) {
        Entity entity = engine.createEntity();

        entity.add(defineBoxPhysixBodyComponent(entity, x, y,
                GameConstants.getTileSizeX(), GameConstants.getTileSizeY(),
                true, 1f, 1f, 0.1f));

        DestructableBlockComponent blockComp = engine.createComponent(DestructableBlockComponent.class);
        entity.add(blockComp);

        HealthComponent Health = engine.createComponent(HealthComponent.class);
        Health.Value = 0;
        entity.add(Health);

        engine.addEntity(entity);
        return entity;

    }

    public static Entity createAndAddLava(Rectangle rect) {
        Entity entity = engine.createEntity();

        float width = rect.width * GameConstants.getTileSizeX();
        float height = rect.height * GameConstants.getTileSizeY();
        float x = rect.x * GameConstants.getTileSizeX() + width / 2;
        float y = rect.y * GameConstants.getTileSizeY() + height / 2;

        entity.add(defineBoxPhysixBodyComponent(entity, x, y,
               width, height,
                true, 1f, 1f, 0.1f));

        KillsPlayerOnContactComponent killComponent = engine
                .createComponent(KillsPlayerOnContactComponent.class);
        entity.add(killComponent);

        PositionComponent positionComponent = engine
                .createComponent(PositionComponent.class);
        entity.add(positionComponent);

        engine.addEntity(entity);
        return entity;
    }
    
    private static PhysixBodyComponent defineBoxPhysixBodyComponent(
            Entity entity, float x, float y, float width, float height,
            boolean fixedRotation, float density, float friction,
            float restitution) {
        PhysixBodyComponent bodyComponent = engine
                .createComponent(PhysixBodyComponent.class);
        PhysixBodyDef bodyDef = new PhysixBodyDef(BodyDef.BodyType.StaticBody,
                physixSystem).position(x, y).fixedRotation(fixedRotation);
        bodyComponent.init(bodyDef, physixSystem, entity);
        PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physixSystem)
                .density(density).friction(friction).shapeBox(width, height)
                .restitution(restitution);
        Fixture fixture = bodyComponent.createFixture(fixtureDef);
        fixture.setUserData(entity);
        return bodyComponent;
    }
    
    
    /**
     *  Moveable Platform
     */
    private static Entity createPlatformBlock(float x, float y, int travelDistance, Direction dir, PlatformMode mode) {
        Entity entity = engine.createEntity();

        PhysixBodyComponent bodyComponent = engine
                .createComponent(PhysixBodyComponent.class);
        PhysixBodyDef bodyDef = new PhysixBodyDef(BodyDef.BodyType.KinematicBody,
                physixSystem).position(x, y).fixedRotation(true);
        bodyComponent.init(bodyDef, physixSystem, entity);
        PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physixSystem)
                .density(1).friction(1f).shapeBox(GameConstants.getTileSizeX(), GameConstants.getTileSizeY())
                .restitution(0.1f);
        Fixture fixture = bodyComponent.createFixture(fixtureDef);
        fixture.setUserData(entity);
        entity.add(bodyComponent);

        DestructableBlockComponent blockComp = engine.createComponent(DestructableBlockComponent.class);
        entity.add(blockComp);

        PlatformComponent pl = new PlatformComponent();
        pl.travelDistance = travelDistance * GameConstants.getTileSizeX();
        pl.mode = mode;
        pl.startPos = new Vector2(x, y);

        DirectionComponent d = new DirectionComponent();
        d.facingDirection = dir;

        entity.add(pl);
        entity.add(d);

        engine.addEntity(entity);
        return entity;
    }

    public static Entity DestructablePlattformBlock(float x, float y, int travelDistance, Direction dir, float speed, PlatformMode mode, int hitpoints) {
        Entity e = createPlatformBlock(x,y, travelDistance, dir, mode);
        HealthComponent h = new HealthComponent();
        h.Value = hitpoints;
        e.add(h);
        DestructableBlockComponent b = new DestructableBlockComponent();
        e.add(b);
        return e;
    }

    public static Entity IndestructablePlattformBlock(float x, float y, int travelDistance, Direction dir, float speed, PlatformMode mode) {
        return createPlatformBlock(x,y, travelDistance, dir, mode);
    }

    public static Entity createAndAddSpike(PooledEngine engine, PhysixSystem physixSystem, float x, float y, float width, float height, Direction direction) {
        Entity entity = engine.createEntity();


        float angle;
        if (direction == Direction.RIGHT) {
            angle = (float) Math.PI/2;
        } else if (direction == Direction.UP) {
            angle = (float) Math.PI;
        } else if (direction == Direction.DOWN) {
            angle = 0;
        } else {
            angle = (float) Math.PI*3/2;
        }

        PhysixBodyComponent bodyComponent = new PhysixBodyComponent();
        PhysixBodyDef bodyDef = new PhysixBodyDef(BodyDef.BodyType.StaticBody, physixSystem).position(x, y).fixedRotation(true);
        bodyComponent.init(bodyDef, physixSystem, entity);
        bodyComponent.getBody().setUserData(bodyComponent);

        PhysixFixtureDef fixtureDefSpikeGround = new PhysixFixtureDef(physixSystem)
                .density(1)
                .friction(1f)
                .shapeBox(width, height * 0.8f, new Vector2(0, -height*0.2f), angle)
                .restitution(0.1f);
        Fixture fixtureSpikeGround = bodyComponent.createFixture(fixtureDefSpikeGround);
        fixtureSpikeGround.setUserData(bodyComponent);

        PhysixFixtureDef fixtureDefBottomSpike = new PhysixFixtureDef(physixSystem)
                .density(1)
                .friction(1f)
                .shapeBox(width, height * 0.2f, new Vector2(0, height*0.8f), angle)
                .restitution(0.1f);
        Fixture fixtureBottomSpike = bodyComponent.createFixture(fixtureDefBottomSpike);
        fixtureBottomSpike.setUserData(bodyComponent);

        entity.add(bodyComponent);

        DestructableBlockComponent blockComp = new DestructableBlockComponent();
        entity.add(blockComp);

        engine.addEntity(entity);
        return entity;

    }
}
