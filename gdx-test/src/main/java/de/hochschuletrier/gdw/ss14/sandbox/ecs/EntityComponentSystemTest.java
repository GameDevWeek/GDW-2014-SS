package de.hochschuletrier.gdw.ss14.sandbox.ecs;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.physics.box2d.*;
import de.hochschuletrier.gdw.commons.gdx.assets.*;
import de.hochschuletrier.gdw.commons.gdx.physix.*;
import de.hochschuletrier.gdw.ss14.sandbox.*;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.components.*;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.systems.*;

/**
 * Created by Dani on 29.09.2014.
 */
public class EntityComponentSystemTest extends SandboxGame
{
    public static final int GRAVITY = 12;
    public static final int BOX2D_SCALE = 40;

    private Engine engine;
    private EntityManager entityManager;
    private PhysixManager physixManager;

    private Texture texture;


    @Override
    public void init(AssetManagerX assetManager)
    {
        engine = new Engine();
        entityManager = new EntityManager();
        physixManager = new PhysixManager(BOX2D_SCALE, 0, GRAVITY);

        PhysixBody body = new PhysixBodyDef(BodyDef.BodyType.StaticBody, physixManager).position(410, 400)
                .fixedRotation(false).create();
        body.createFixture(new PhysixFixtureDef(physixManager).density(1).friction(0.5f).shapeBox(800, 20));

        engine.addSystem(new PhysixRenderSystem(entityManager, physixManager));
        engine.addSystem(new PhysixUpdateSystem(entityManager, physixManager));
        engine.addSystem(new TestRenderSystem(entityManager));

        texture = assetManager.getTexture("logo");
    }

    @Override
    public void dispose()
    {

    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        if (button == 0)
        {
            addBall(screenX, screenY);
        }
        return true;
    }

    public void addBall(int x, int y)
    {
        int entity = entityManager.createEntity();
        BallPhysicsComponent ballPhysicsComponent = new BallPhysicsComponent(x, y, 30.0f);
        TestRenderComponent testRenderComponent = new TestRenderComponent();
        testRenderComponent.texture = texture;
        ballPhysicsComponent.initPhysics(physixManager);
        entityManager.addComponent(entity, ballPhysicsComponent);
        entityManager.addComponent(entity, testRenderComponent);
    }

    @Override
    public void render()
    {
        engine.render();
    }

    @Override
    public void update(float delta)
    {
        engine.update(delta);
    }
}
