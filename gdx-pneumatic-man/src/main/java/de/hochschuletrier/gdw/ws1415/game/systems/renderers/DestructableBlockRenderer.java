package de.hochschuletrier.gdw.ws1415.game.systems.renderers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1415.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1415.game.components.AnimationComponent;
import de.hochschuletrier.gdw.ws1415.game.components.DestructableBlockComponent;
import de.hochschuletrier.gdw.ws1415.game.components.HealthComponent;
import de.hochschuletrier.gdw.ws1415.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1415.game.systems.SortedFamilyRenderSystem;

public class DestructableBlockRenderer extends SortedFamilyRenderSystem.Renderer {

    @SuppressWarnings("unchecked")
	public DestructableBlockRenderer() {
        super(Family.all(AnimationComponent.class, DestructableBlockComponent.class, HealthComponent.class).get());
    }

	@Override
	public void render(Entity entity, float deltaTime) {	
		AnimationComponent animation = ComponentMappers.animation.get(entity);
        PositionComponent position = ComponentMappers.position.get(entity);
        HealthComponent health = ComponentMappers.health.get(entity);
        
        TextureRegion keyFrame = animation.animation.getKeyFrame(health.Value < 0 ? 0 : health.Value);
        int w = keyFrame.getRegionWidth();
        int h = keyFrame.getRegionHeight();
        DrawUtil.batch.draw(keyFrame, position.x - w * 0.5f, position.y - h * 0.5f, w * 0.5f, h * 0.5f, w, h, 1, 1, position.rotation);
	}
}
