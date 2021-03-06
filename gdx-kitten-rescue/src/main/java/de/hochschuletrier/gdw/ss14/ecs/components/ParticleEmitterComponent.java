package de.hochschuletrier.gdw.ss14.ecs.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.ss14.states.ParticleEmitterTypeEnum;

/**
 * @author Milan Rüll
 */
public class ParticleEmitterComponent implements Component {
    
    // The lifetime of the emitter in seconds (if has limited lifetime)
    public boolean hasLimitedLifetime = false;
    public float lifetimeLeft = 2.0f;
    
    public float particleLifetime = 10.0f;
    
    public float emitInterval = 0.15f;
    public float emitRadius = 0f;
    
    public Color particleTintColor = null;
    
    public float minimumParticleDistance = 0.0f;
    public Vector2 lastParticlePos = new Vector2();
    
    // Attributes needed for color fading
    /*public Color fadeColor = null;
    public float fadingTimeLeft = 0.0f;
    
    public void fadeColor( Color newColor, float time ) {
        /*if (fadeColor != null)
            particleTintColor = fadeColor;*/
        /*
        fadeColor = newColor.cpy();
        fadingTimeLeft = time;
    }*/
    
    /*public boolean isDirectionalEmitter = false;
    public Vector2 emitDirection;
    public float emitAngle;*/
    
    public ParticleEmitterTypeEnum emitterType = ParticleEmitterTypeEnum.LiquidParticleEmitter;
}
