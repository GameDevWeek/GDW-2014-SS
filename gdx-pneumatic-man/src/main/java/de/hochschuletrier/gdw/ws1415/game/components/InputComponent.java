package de.hochschuletrier.gdw.ws1415.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class InputComponent extends Component implements Pool.Poolable
{
    public int direction = 0; // -1 heißt links, 0 heißt stehenbleiben, 1 heißt rechts
    
    public boolean jump = false;
    @Override
    public void reset()
    {
        
    }

}
