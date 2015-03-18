package de.hochschuletrier.gdw.ws1415.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by oliver on 18.03.15.
 */
public class FallingRockComponent extends Component implements Pool.Poolable{
    public boolean falling;
    public int id;


    @Override
    public void reset() {
        falling = false;
        id = 0;
    }
}
