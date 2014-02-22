package de.hochschuletrier.gdw.ws1314.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.assets.FontX;
import de.hochschuletrier.gdw.commons.gdx.assets.ImageX;
import de.hochschuletrier.gdw.commons.gdx.state.GameState;
import de.hochschuletrier.gdw.commons.gdx.utils.DefaultOrthoCameraController;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.commons.utils.FpsCalculator;
import de.hochschuletrier.gdw.ws1314.game.Game;

/**
 * Menu state
 *
 * @author Santo Pfingsten
 */
public class GameplayState extends GameState implements InputProcessor {

    private Game game;
    private Sound click;
    private ImageX crosshair;
    private FontX verdana_24;
    private final Vector2 cursor = new Vector2();
    private final FpsCalculator fpsCalc = new FpsCalculator(200, 100, 16);
	private DefaultOrthoCameraController controller;
	private OrthographicCamera camera;
	private InputMultiplexer inputs;
    public GameplayState() {
    }

    @Override
    public void init(AssetManagerX assetManager) {
        super.init(assetManager);

        crosshair = assetManager.getImageX("crosshair");
        click = assetManager.getSound("click");
        verdana_24 = assetManager.getFontX("verdana_24");
		camera = new OrthographicCamera();
		camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		controller = new DefaultOrthoCameraController(camera);
		inputs = new InputMultiplexer(this, controller);
		DrawUtil.batch.setProjectionMatrix(camera.combined);
        game = new Game();
    }

    @Override
    public void render() {
		DrawUtil.batch.setProjectionMatrix(camera.combined);

        DrawUtil.fillRect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Color.GRAY);

        game.render();

        crosshair.draw(cursor.x - crosshair.getWidth() * 0.5f, cursor.y - crosshair.getHeight() * 0.5f);

        verdana_24.drawRight(String.format("%.2f FPS", fpsCalc.getFps()), Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - verdana_24.getLineHeight());
    }

    @Override
    public void update(float delta) {

		controller.update();
        game.update(delta);
        fpsCalc.addFrame();
    }

    @Override
    public void onEnter() {
		Gdx.input.setInputProcessor(inputs);
    }

    @Override
    public void onLeave() {
    }

    @Override
    public void dispose() {
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        game.addBall(screenX, screenY);
        click.play();
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        cursor.set(screenX, screenY);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        cursor.set(screenX, screenY);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        cursor.set(screenX, screenY);
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}