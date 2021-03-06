package de.hochschuletrier.gdw.ss14.sandbox.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.ss14.sandbox.SandboxGame;
import de.hochschuletrier.gdw.ss14.sandbox.Test.Entity.EntityFactory;
import de.hochschuletrier.gdw.ss14.sandbox.Test.System.DogInputSystem;
import de.hochschuletrier.gdw.ss14.sandbox.Test.System.InputSystem;
import de.hochschuletrier.gdw.ss14.sandbox.Test.System.MovementSystem;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.Engine;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.systems.PhysixRenderSystem;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.systems.PhysixUpdateSystem;


public class Spielwelt extends SandboxGame{

	private static final Logger logger = LoggerFactory.getLogger(Spielwelt.class);
	private Engine engine;
	private EntityManager manager;
	private PhysixManager phyManager;

	@Override
	public void dispose() {
	}

	@Override
	public void init(AssetManagerX assetManager) {
		engine = new Engine();
		manager = new EntityManager();
		phyManager = new PhysixManager(3,0,0);
		EntityFactory ef = new EntityFactory(manager, phyManager, assetManager);
		int entity = manager.createEntity();
		EntityFactory.constructCat(new Vector2(200,200), 150, 75, 0, 50.0f);
		EntityFactory.constructDog(new Vector2(200,200), 150, 75, 50, 50.0f);

		engine.addSystem(new MovementSystem(manager));
		engine.addSystem(new PhysixRenderSystem(manager,phyManager));
		engine.addSystem(new InputSystem(manager));
		engine.addSystem(new DogInputSystem(manager));
		engine.addSystem(new PhysixUpdateSystem(manager, phyManager));
	}

	@Override
	public void render() {
		engine.render();
	}

	@Override
	public void update(float delta) {
		engine.update(delta);
	}
}
