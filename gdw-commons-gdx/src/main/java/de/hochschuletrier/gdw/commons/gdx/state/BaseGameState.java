package de.hochschuletrier.gdw.commons.gdx.state;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;

/**
 * The most basic game state
 *
 * @author Santo Pfingsten
 */
public class BaseGameState<T extends BaseGameState> {

    protected AssetManagerX assetManager;

    public void init(AssetManagerX assetManager) {
        this.assetManager = assetManager;
    }

    public void render() {
    }

    public void update(float delta) {
    }

    public void onEnter(T previousState) {
    }

    public void onEnterComplete() {
    }

    public void onLeave(T nextState) {
    }

    public void onLeaveComplete() {
    }

    public void dispose() {
    }

}
