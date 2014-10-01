package de.hochschuletrier.gdw.ss14.ecs.systems;

import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.components.CatPropertyComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.DogPropertyComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.InputComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.MovementComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.PhysicsComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.PlayerComponent;

public class MovementSystem extends ECSystem{

    public int minDistance = 50;

    public MovementSystem(EntityManager entityManager) {
        super(entityManager);
        // TODO Auto-generated constructor stub

    }

    @Override
    public void render() {
        // TODO Auto-generated method stub

    }

    @Override
    public void update(float delta) {
        // TODO Auto-generated method stub
        Array<Integer> compos = entityManager.getAllEntitiesWithComponents(MovementComponent.class, PhysicsComponent.class, InputComponent.class);

        for (Integer integer : compos) {
            MovementComponent moveCompo = entityManager.getComponent(integer, MovementComponent.class);
            PhysicsComponent phyCompo = entityManager.getComponent(integer, PhysicsComponent.class);
            InputComponent inputCompo = entityManager.getComponent(integer, InputComponent.class);
            PlayerComponent playerCompo = entityManager.getComponent(integer, PlayerComponent.class);
            CatPropertyComponent catStateCompo = null;
            DogPropertyComponent dogStateCompo = null;
//            if(playerCompo != null ) {
//                catStateCompo = entityManager.getComponent(integer, CatPropertyComponent.class);
//                if(moveCompo.velocity == 0)
//                    catStateCompo.state = CatStateEnum.IDLE;
//                else if (moveCompo.velocity > 0 && moveCompo.velocity < moveCompo.MIDDLE_VELOCITY)
//                    catStateCompo.state = CatStateEnum.LAUFEN;
//                else if(moveCompo.velocity > moveCompo.MIDDLE_VELOCITY && moveCompo.velocity < moveCompo.MAX_VELOCITY)
//                    catStateCompo.state = CatStateEnum.RENNEN;
//            } else {
//                dogStateCompo = entityManager.getComponent(integer, DogPropertyComponent.class);
//                if(moveCompo.velocity == 0)
//                    dogStateCompo.state = DogStateEnum.SITTING;
//                else if (moveCompo.velocity > 0 && moveCompo.velocity < moveCompo.MIDDLE_VELOCITY)
//                    dogStateCompo.state = DogStateEnum.WALKING;
//                else if(moveCompo.velocity > moveCompo.MIDDLE_VELOCITY && moveCompo.velocity < moveCompo.MAX_VELOCITY)
//                    dogStateCompo.state = DogStateEnum.RUNNING;
//            }
            moveCompo.directionVec = inputCompo.whereToGo.sub(phyCompo.getPosition());

            float distance = moveCompo.directionVec.len();

            System.out.println("DISTANCE: " +  distance + " VELOCITY: " + moveCompo.velocity);


//            if(distance <= minDistance){
//                if(playerCompo != null)
//                    catStateCompo.state = CatStateEnum.SPRINGEN;
//                else
//                    dogStateCompo.state = DogStateEnum.KILLING;
//            }


            if(distance >= 200){

                moveCompo.velocity += moveCompo.ACCELERATION * delta;

                /*
                 * Falls durch die letze Berechnung die Velocity höher als die Maximale Velocity berechnet wurde, setzen
                 * wir  unsere velocity auf MAX_VELOCITY
                 */
                if(moveCompo.velocity >= moveCompo.MAX_VELOCITY){
                    moveCompo.velocity = moveCompo.MAX_VELOCITY;
                }

            }else if(distance >= 100){

                //moveCompo.velocity +=  moveCompo.ACCELERATION * delta;

                /*
                 * Falls wir von unserem Stand aus losgehen soll unsere Katze beschleunigen, bis sie "geht"
                 */
                if(moveCompo.velocity >= moveCompo.MIDDLE_VELOCITY){
                    //
                    moveCompo.velocity += moveCompo.DAMPING * delta;
                    if(moveCompo.velocity <= moveCompo.MIDDLE_VELOCITY){
                        moveCompo.velocity = moveCompo.MIDDLE_VELOCITY;
                    }
                /*
                 * Falls unsere Katze aus dem "Rennen" aus zu nah an unseren Laserpointer kommt, soll
                 * sie stetig langsamer werden
                 */
                }else if(moveCompo.velocity < moveCompo.MIDDLE_VELOCITY){
                    moveCompo.velocity +=  moveCompo.ACCELERATION * delta;
                    if(moveCompo.velocity >= moveCompo.MIDDLE_VELOCITY){
                        moveCompo.velocity = moveCompo.MIDDLE_VELOCITY;
                    }
                }

            }else{
                //
                    moveCompo.velocity +=  moveCompo.DAMPING * 1.5f * delta;
                    if(moveCompo.velocity <= moveCompo.MIN_VELOCITY){
                        moveCompo.velocity = 0;
                    }
            }

            //Normalizing DirectionVector for Movement
            moveCompo.directionVec = moveCompo.directionVec.nor();
            float angle = (float)Math.atan2(-moveCompo.directionVec.x, moveCompo.directionVec.y);
            phyCompo.setRotation(angle);
            phyCompo.setVelocityX(moveCompo.directionVec.x * moveCompo.velocity);
            phyCompo.setVelocityY(moveCompo.directionVec.y * moveCompo.velocity);
        }

    }
}
