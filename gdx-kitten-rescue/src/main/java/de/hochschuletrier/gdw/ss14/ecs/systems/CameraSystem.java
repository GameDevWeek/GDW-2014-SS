package de.hochschuletrier.gdw.ss14.ecs.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVarFloat;

import de.hochschuletrier.gdw.commons.gdx.cameras.orthogonal.LimitedSmoothCamera;
import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.components.CameraComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.PhysicsComponent;

public class CameraSystem extends ECSystem {
        
    // Use the default screen center distance of the CameraComponent as start value
    private CVarFloat cvarMaxScreenCenterDistance = new CVarFloat("cam_maxScreenCenterDistance", 
                                                            new CameraComponent().maxScreenCenterDistance, 
                                                            1, 500, 0, "Maximum distance from the center");
    
  /**
   * 
   * @param em          the entity manager
   * @param minBounds   minBounds of the screen
   * @param maxBounds   maxBounds of the screen
   * @param catEntity   the entity which belongs to the cat
   * @param priority    must be set so that the camera system will be updated right before the rendering system
   */
  public CameraSystem( EntityManager em, int priority ) {
      
      super(em, priority); 
      Main.getInstance().console.register(cvarMaxScreenCenterDistance);
  }
    
  @Override
  public void update( float delta ) {
      
      Array<Integer> entityArray = entityManager.getAllEntitiesWithComponents(CameraComponent.class);

      for (Integer entity : entityArray) {
          
          PhysicsComponent physComp = entityManager.getComponent(entity, PhysicsComponent.class);         
          CameraComponent camComp = entityManager.getComponent(entity, CameraComponent.class);
          
          Vector2 camera2DPos = new Vector2(camComp.smoothCamera.getPosition().x, camComp.smoothCamera.getPosition().y);
          Vector2 newDest = camera2DPos.cpy();
          
          if (physComp != null) {
              
              Vector2 followPos = physComp.getPosition().cpy();       
              
              float followFactor = 0.0f;                          
              float centerDistance = followPos.cpy().sub(camera2DPos).len();
              float maxCenterDistance = camComp.maxScreenCenterDistance * camComp.smoothCamera.getZoom();
              
              /*cvarMaxScreenCenterDistance.get()*/
              
              // If farther away than max distance, move towards the cat to contain max distance
              if (centerDistance > maxCenterDistance) {
                  
                  float moveFactor = (centerDistance - maxCenterDistance) / centerDistance;
                  newDest.add(followPos.cpy().sub(camera2DPos).scl(moveFactor));
                  
                  followFactor = 0.0f;
                  centerDistance = maxCenterDistance;
              }     
              else {
                  followFactor = Math.abs(centerDistance / maxCenterDistance);                                         
              }
              
              // Use the followspeedCurvePower to model the exponential curve
              followFactor = (float) Math.pow(followFactor, camComp.followspeedCurvePower);
              
              // Move camera towards the follow transform position by followFactor per movetimeFromMaxDistanceToCenter
              float timeFactor = delta / camComp.movetimeFromMaxDistanceToCenter;
              newDest.add(followPos.cpy().sub(camera2DPos).scl(followFactor * timeFactor));        
              
          }
          
          if ((camComp.minBound != null) && (camComp.maxBound != null))
              camComp.smoothCamera.setBounds(camComp.minBound.x, camComp.minBound.y, camComp.maxBound.x, camComp.maxBound.y);
          
          camComp.smoothCamera.setDestination(newDest.x, newDest.y);    
          camComp.smoothCamera.setZoom(camComp.cameraZoom);
          //smoothCamera.update(delta);
          camComp.smoothCamera.updateForced();
                    
          camComp.smoothCamera.bind();
      }
  }
  
  @Override
  public void render() {
      
  }
  
    @Override
    public void shutdown() {
        Main.getInstance().console.unregister(cvarMaxScreenCenterDistance);
    }

  /*public void setBounds( Vector2 minBounds, Vector2 maxBounds ) {
      
      smoothCamera.setBounds(minBounds.x, minBounds.y, maxBounds.x, maxBounds.y);
  }*/
}
