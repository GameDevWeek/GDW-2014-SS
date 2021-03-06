package de.hochschuletrier.gdw.ss14.ecs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;

import de.hochschuletrier.gdw.commons.ai.behaviourtree.engine.Behaviour;
import de.hochschuletrier.gdw.commons.ai.behaviourtree.engine.BehaviourManager;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.ss14.ecs.ai.DogBehaviour;
import de.hochschuletrier.gdw.ss14.ecs.ai.DogBehaviour.DogBlackboard;
import de.hochschuletrier.gdw.ss14.ecs.components.*;
import de.hochschuletrier.gdw.ss14.ecs.systems.CatContactSystem;
import de.hochschuletrier.gdw.ss14.ecs.systems.DogContactSystem;
import de.hochschuletrier.gdw.ss14.ecs.systems.WorldObjectsSystem;
import de.hochschuletrier.gdw.ss14.game.Game;
import de.hochschuletrier.gdw.ss14.physics.ICatStateListener;
import de.hochschuletrier.gdw.ss14.physics.ICollisionListener;
import de.hochschuletrier.gdw.ss14.states.*;

import java.io.File;
import java.util.ArrayList;

public class EntityFactory{

    public static int constructBalk(){
        int entity = manager.createEntity();

        return entity;
    }

    public static int constructBox(){
        int entity = manager.createEntity();

        return entity;
    }

    public static int constructBroom(){
        int entity = manager.createEntity();

        return entity;
    }

    public static int constructCat(Vector2 pos, float maxVelocity, float middleVelocity, float minVelocity, float acceleration, short mask, short category){
        int entity = manager.createEntity();
        CatPhysicsComponent catPhysix = new CatPhysicsComponent(pos, 25, 50, 0, .2f, 0f, mask, category, (short)0);

        //catPhysix: position(x,y), width, height, rota, friction[0-1][ice-rubber], restitution[0-1][rock-ball]
        ICollisionListener contactSystem = (CatContactSystem) Game.engine.getSystemOfType(CatContactSystem.class);
        catPhysix.collisionListeners.add(contactSystem);

        MovementComponent catMove = new MovementComponent(maxVelocity, middleVelocity, minVelocity, acceleration);
        catMove.slidingLock = false;
        InputComponent catInput = new InputComponent();
        catPhysix.initPhysics(phyManager);
        //SlideMass sm = new SlideMass(catPhysix.physicsBody.getBody());
        //sm.init(phyManager);
        CatPropertyComponent catProperty = new CatPropertyComponent();
        catProperty.lastCheckPoint = pos;
        ICatStateListener stateSystem = (WorldObjectsSystem) Game.engine.getSystemOfType(WorldObjectsSystem.class);
        catProperty.StateListener.add(stateSystem);

        JumpDataComponent jumpDataComponent = new JumpDataComponent();

        //catPhysix.physicsBody.setLinearVelocity(catMove.velocity, catMove.velocity);
        AnimationComponent catAnimation = new AnimationComponent();

        catAnimation.animation.put(CatStateEnum.HIT.ordinal(), assetManager.getAnimation("hit"));
        catAnimation.animation.put(CatStateEnum.IDLE.ordinal(), assetManager.getAnimation("idle"));
        catAnimation.animation.put(CatStateEnum.WALK.ordinal(), assetManager.getAnimation("walk"));
        catAnimation.speedUpFactor.put(CatStateEnum.WALK.ordinal(), 40f);
        catAnimation.animation.put(CatStateEnum.RUN.ordinal(), assetManager.getAnimation("run"));
        catAnimation.speedUpFactor.put(CatStateEnum.RUN.ordinal(), 100f);
        catAnimation.animation.put(CatStateEnum.SLIDE_LEFT.ordinal(), assetManager.getAnimation("slide_left"));
        catAnimation.animation.put(CatStateEnum.SLIDE_RIGHT.ordinal(), assetManager.getAnimation("slide_right"));
        catAnimation.animation.put(CatStateEnum.CRASH.ordinal(), assetManager.getAnimation("crash"));
        catAnimation.animation.put(CatStateEnum.FALL.ordinal(), assetManager.getAnimation("fall"));
        catAnimation.animation.put(CatStateEnum.DIE.ordinal(), assetManager.getAnimation("die"));
        catAnimation.animation.put(CatStateEnum.DIE2.ordinal(), assetManager.getAnimation("die2"));
        catAnimation.animation.put(CatStateEnum.JUMP.ordinal(), assetManager.getAnimation("jump"));
        catAnimation.animation.put(CatStateEnum.JUMPING_IN_BOX.ordinal(), assetManager.getAnimation("cat_jumping_in_box"));
        catAnimation.animation.put(CatStateEnum.PLAYS_WITH_WOOL.ordinal(), assetManager.getAnimation("cat_plays_with_wool"));

        CameraComponent cam = new CameraComponent();
        cam.cameraZoom = 1.0f;

        CatPropertyComponent catProperties = new CatPropertyComponent();
        catProperties.setState(CatStateEnum.IDLE);

        ShadowComponent shadow = new ShadowComponent();
        shadow.alpha = 0.5f;
        shadow.z = 1.0f;

        ParticleEmitterComponent particleEmitComp = new ParticleEmitterComponent();
        particleEmitComp.particleTintColor = new Color(0.5f, 0.5f, 0.5f, 0.5f);
        particleEmitComp.emitRadius = 10f;
        particleEmitComp.emitterType = ParticleEmitterTypeEnum.PawParticleEmitter;
        particleEmitComp.particleLifetime = 20f;
        particleEmitComp.emitInterval = 0.2f;
        particleEmitComp.minimumParticleDistance = 15.0f;

        manager.addComponent(entity, jumpDataComponent);
        manager.addComponent(entity, catProperties);
        manager.addComponent(entity, catAnimation);
        manager.addComponent(entity, new RenderComponent());
        manager.addComponent(entity, catProperty);
        manager.addComponent(entity, catPhysix);
        manager.addComponent(entity, catMove);
        manager.addComponent(entity, catInput);
        manager.addComponent(entity, new PlayerComponent());
        manager.addComponent(entity, cam);
        manager.addComponent(entity, shadow);
        manager.addComponent(entity, particleEmitComp);
        manager.addComponent(entity, new LightComponent());
        //manager.addComponent(entity, new ConePhysicsComponent(catPhysix.getPosition(), 100,100,100));
        //manager.addComponent(entity, new HitAnimationComponent());

        return entity;
    }

    public static int constructCatbox(Vector2 pos, short mask, short category){
        int entity = manager.createEntity();

        CatBoxPhysicsComponent catBoxPhysicsComponent = new CatBoxPhysicsComponent(pos, 80.0f, 80.0f, 0.0f, mask, category, (short) 0);
        catBoxPhysicsComponent.initPhysics(phyManager);

        RenderComponent renderComponent = new RenderComponent();
        renderComponent.texture = new TextureRegion(assetManager.getTexture("catbox"));

        CatBoxComponent catBoxComponent = new CatBoxComponent();

        manager.addComponent(entity, catBoxPhysicsComponent);
        manager.addComponent(entity, renderComponent);
        manager.addComponent(entity, catBoxComponent);

        return entity;
    }

    /*public static int constructDog(Vector2 pos, float maxVelocity, float middleVelocity, float minVelocity, float acceleration, ArrayList<Vector2> patrolspots, short mask , short category){
        int entity = manager.createEntity();
        CatPhysicsComponent dogPhysix = new CatPhysicsComponent(pos, 50, 100, 0, .2f, 0f, mask, category, (short)-2);
        MovementComponent dogMove = new MovementComponent(maxVelocity, middleVelocity, minVelocity, acceleration);
        InputComponent dogInput = new InputComponent();
        Behaviour verhalten;
        DogPropertyComponent dogState = new DogPropertyComponent(patrolspots);
        dogPhysix.initPhysics(phyManager);
        AnimationComponent dogAnimation = new AnimationComponent();

        dogAnimation.animation.put(DogStateEnum.FALLING.ordinal(), assetManager.getAnimation("pudel_fallen"));
        dogAnimation.animation.put(DogStateEnum.KILLING.ordinal(), assetManager.getAnimation("pudel_beissen"));
        dogAnimation.animation.put(DogStateEnum.WALKING.ordinal(), assetManager.getAnimation("pudel_laufen"));
        dogAnimation.animation.put(DogStateEnum.RUNNING.ordinal(), assetManager.getAnimation("pudel_laufen"));
        dogAnimation.animation.put(DogStateEnum.SITTING.ordinal(), assetManager.getAnimation("pudel_idle"));
        dogAnimation.animation.put(DogStateEnum.JUMPING.ordinal(), assetManager.getAnimation("pudel_springen"));
        
        RenderComponent dogRender = new RenderComponent();
        dogRender.zIndex = 5;

        manager.addComponent(entity, dogAnimation);
        manager.addComponent(entity, dogRender);
        manager.addComponent(entity, dogState);
        manager.addComponent(entity, dogPhysix);
        manager.addComponent(entity, dogMove);
        manager.addComponent(entity, dogInput);
        manager.addComponent(entity, new EnemyComponent());
        //        manager.addComponent(entity, new AnimationComponent());      
        addDogParticleEmitter(entity);

        return entity;
    }*/

    private static void addDogParticleEmitter(int entity){

        ParticleEmitterComponent dogParticleEmitter = new ParticleEmitterComponent();
        dogParticleEmitter.emitInterval = 0.2f;
        dogParticleEmitter.emitRadius = 10f;
        dogParticleEmitter.particleTintColor = new Color(0.5f, 0, 0, 0.75f);

        manager.addComponent(entity, dogParticleEmitter);
    }

    public static int constructSmartDog(Vector2 pos, float maxVelocity, float middleVelocity, float minVelocity, float acceleration, ArrayList<Vector2> patrolspots, short mask, short category){
        int entity = manager.createEntity();
        DogPhysicsComponent dogPhysix = new DogPhysicsComponent(pos, 50, 100, 0, 1, 0, mask, category, (short) -2);
        MovementComponent dogMove = new MovementComponent(maxVelocity, middleVelocity, minVelocity, acceleration);
        InputComponent dogInput = new InputComponent();
        
        

        DogPropertyComponent dogState = new DogPropertyComponent(patrolspots);
        dogPhysix.initPhysics(phyManager);
        ICollisionListener contactSystem = (DogContactSystem) Game.engine.getSystemOfType(DogContactSystem.class);
        dogPhysix.collisionListeners.add(contactSystem);
        AnimationComponent dogAnimation = new AnimationComponent();

        double animationSelektor = Math.random();
        if (animationSelektor < 0.3) {
            dogAnimation.animation.put(DogStateEnum.FALLING.ordinal(), assetManager.getAnimation("pudel_fallen"));
            dogAnimation.animation.put(DogStateEnum.KILLING.ordinal(), assetManager.getAnimation("pudel_beissen"));
            dogAnimation.animation.put(DogStateEnum.WALKING.ordinal(), assetManager.getAnimation("pudel_laufen"));
            dogAnimation.animation.put(DogStateEnum.RUNNING.ordinal(), assetManager.getAnimation("pudel_laufen"));
            dogAnimation.animation.put(DogStateEnum.SITTING.ordinal(), assetManager.getAnimation("pudel_idle"));
            dogAnimation.animation.put(DogStateEnum.JUMPING.ordinal(), assetManager.getAnimation("pudel_springen"));
        } else if (animationSelektor > 0.6) {
            dogAnimation.animation.put(DogStateEnum.FALLING.ordinal(), assetManager.getAnimation("mops_fallen"));
            dogAnimation.animation.put(DogStateEnum.KILLING.ordinal(), assetManager.getAnimation("mops_beissen"));
            dogAnimation.animation.put(DogStateEnum.WALKING.ordinal(), assetManager.getAnimation("mops_laufen"));
            dogAnimation.animation.put(DogStateEnum.RUNNING.ordinal(), assetManager.getAnimation("mops_laufen"));
            dogAnimation.animation.put(DogStateEnum.SITTING.ordinal(), assetManager.getAnimation("mops_idle"));
            dogAnimation.animation.put(DogStateEnum.JUMPING.ordinal(), assetManager.getAnimation("mops_springen"));
        } else {
            dogAnimation.animation.put(DogStateEnum.FALLING.ordinal(), assetManager.getAnimation("chihuahua_fallen"));
            dogAnimation.animation.put(DogStateEnum.KILLING.ordinal(), assetManager.getAnimation("chihuahua_beissen"));
            dogAnimation.animation.put(DogStateEnum.WALKING.ordinal(), assetManager.getAnimation("chihuahua_laufen"));
            dogAnimation.animation.put(DogStateEnum.RUNNING.ordinal(), assetManager.getAnimation("chihuahua_laufen"));
            dogAnimation.animation.put(DogStateEnum.SITTING.ordinal(), assetManager.getAnimation("chihuahua_idle"));
            dogAnimation.animation.put(DogStateEnum.JUMPING.ordinal(), assetManager.getAnimation("chihuahua_springen"));
        }

        RenderComponent dogRender = new RenderComponent();
        dogRender.zIndex = 5;

        manager.addComponent(entity, dogAnimation);
        manager.addComponent(entity, dogRender);
        manager.addComponent(entity, dogState);
        manager.addComponent(entity, dogPhysix);
        manager.addComponent(entity, dogMove);
        manager.addComponent(entity, dogInput);
        manager.addComponent(entity, new EnemyComponent());

        DogBehaviour.DogBlackboard localBlackboard = new DogBlackboard(manager);
        Behaviour verhalten = new DogBehaviour("SmartDog", localBlackboard, true, entity);
        BehaviourComponent bComp = new BehaviourComponent(verhalten, behaviourManager);
        manager.addComponent(entity, bComp);
        addDogParticleEmitter(entity);

        //        manager.addComponent(entity, new AnimationComponent());
        return entity;

    }

    public static void constructLaserPointer(Vector2 pos){
        int entity = manager.createEntity();

        LaserPointerComponent laser = new LaserPointerComponent(pos);

        manager.addComponent(entity, laser);
    }

    public static void constructDoor(Vector2 pos, short mask, short category){
        int entity = manager.createEntity();
        DoorPhysicsComponent doorPhysix = new DoorPhysicsComponent(pos, 80.0f, 80.0f, 0.0f, mask, category, (short) 0);
        manager.addComponent(entity, doorPhysix);
        doorPhysix.initPhysics(phyManager);
    }

    public static void constructFood(){
        int entity = manager.createEntity();
    }

    public static int constructFloor(PhysixBodyDef bodydef, PhysixFixtureDef fixturedef, GroundTypeState type){
        int entity = manager.createEntity();
        GroundPhysicsComponent puddlephys = new GroundPhysicsComponent(bodydef, fixturedef);
        manager.addComponent(entity, new GroundPropertyComponent(type));
        manager.addComponent(entity, puddlephys);
        puddlephys.initPhysics(phyManager);

        return entity;
    }

    public static void constructLamp(){
        int entity = manager.createEntity();
    }

    public static int constructPuddleOfBlood(PhysixBodyDef bodydef, PhysixFixtureDef fixturedef){
        int entity = manager.createEntity();
        JumpablePhysicsComponent puddlephys = new JumpablePhysicsComponent(bodydef, fixturedef.groupIndex((short) -2));
        manager.addComponent(entity, puddlephys);
        manager.addComponent(entity, new JumpablePropertyComponent(JumpableState.bloodpuddle));
        puddlephys.initPhysics(phyManager);

        return entity;
    }

    public static int constructPuddleOfWater(PhysixBodyDef bodydef, PhysixFixtureDef fixturedef){
        int entity = manager.createEntity();
        JumpablePhysicsComponent puddlephys = new JumpablePhysicsComponent(bodydef, fixturedef.groupIndex((short) -2));
        manager.addComponent(entity, puddlephys);
        manager.addComponent(entity, new JumpablePropertyComponent(JumpableState.waterpuddle));
        puddlephys.initPhysics(phyManager);

        return entity;
    }

    public static int constructDeadzone(PhysixBodyDef bodydef, PhysixFixtureDef fixturedef){
        int entity = manager.createEntity();
        JumpablePhysicsComponent puddlephys = new JumpablePhysicsComponent(bodydef, fixturedef.groupIndex((short) -3));
        manager.addComponent(entity, puddlephys);
        manager.addComponent(entity, new JumpablePropertyComponent(JumpableState.deadzone));
        puddlephys.initPhysics(phyManager);

        return entity;
    }

    public static int constructStairs(Vector2 pos, float width, float height, int direction, short mask, short category){
        int entity = manager.createEntity();

        StairsPhysicsComponent stairsPhysicsComponent = new StairsPhysicsComponent(pos, width, height, 0.0f, mask, category, (short) 0);
        stairsPhysicsComponent.initPhysics(phyManager);
        stairsPhysicsComponent.physicsBody.getFixtureList().forEach((f)->{
            Filter fil = f.getFilterData();
            fil.categoryBits = category;
            fil.maskBits = mask;
        });

        stairsPhysicsComponent.owner = entity;

        StairComponent stairComponent = new StairComponent();
        stairComponent.changeFloorDirection = direction;

        manager.addComponent(entity, stairsPhysicsComponent);
        manager.addComponent(entity, stairComponent);
        return entity;
    }

    public static int constructVase(){
        int entity = manager.createEntity();
        return entity;
    }

    public static void constructWool(Vector2 pos, short mask, short category){
        int entity = manager.createEntity();

        WoolPhysicsComponent woolPhysicsComponent = new WoolPhysicsComponent(pos, 20.0f, 0.0f, mask, category, (short) 0);
        RenderComponent woolRenderComponent = new RenderComponent();
        woolRenderComponent.texture = new TextureRegion(assetManager.getTexture("wool"));
        woolPhysicsComponent.initPhysics(phyManager);
        woolPhysicsComponent.owner = entity;
        manager.addComponent(entity, woolPhysicsComponent);
        manager.addComponent(entity, new WoolPropertyComponent());
        manager.addComponent(entity, woolRenderComponent);

        //        CatPhysicsComponent catPhysix = new CatPhysicsComponent(pos, 25, 50, 0, 0f, 0f);
        //        catPhysix.initPhysics(phyManager);
        //        manager.addComponent(entity, catPhysix);

        //RenderComponent renderComponent = new RenderComponent();
        //renderComponent.texture = assetManager.getTexture();
        //manager.addComponent(entity, renderComponent);
    }

    public static void constructMapChangeObj(Vector2 pos, float width, float height, File nextMap){
        int entity = manager.createEntity();

        ExitMapPhysicsComponent exit = new ExitMapPhysicsComponent(pos, width, height);
        ExitMapPropertyComponent exitProps = new ExitMapPropertyComponent(nextMap);
        exit.initPhysics(phyManager);
        manager.addComponent(entity, exit);
    }

    public static void constructFinish(Vector2 pos, float width, float height){
        int entity = manager.createEntity();

        FinishPhysicsComponent finishPhysicsComponent = new FinishPhysicsComponent(pos, width, height, 0.0f);
        finishPhysicsComponent.initPhysics(phyManager);

        manager.addComponent(entity, finishPhysicsComponent);
    }

    public static EntityManager manager;

    public static PhysixManager phyManager;

    public static AssetManagerX assetManager;

    public static BehaviourManager behaviourManager;
}
