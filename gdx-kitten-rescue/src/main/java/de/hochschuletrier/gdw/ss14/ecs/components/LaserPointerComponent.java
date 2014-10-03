package de.hochschuletrier.gdw.ss14.ecs.components;

import com.badlogic.gdx.math.Vector2;


public class LaserPointerComponent implements Component{

    public boolean isVisible;
    public Vector2 position;
    
    public enum InputState {
        KEYBOARD,
        MOUSE,
        GAMEPAD,
    }
    
    public InputState input;
    
    public LaserPointerComponent(Vector2 position){
        isVisible = true;
        input = InputState.MOUSE;
        this.position = position;
    }

}
