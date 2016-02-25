package at.juggle.games.memory;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MemoryGame extends ApplicationAdapter {
	public final static int WIDTH = 1280;
	public final static int HEIGHT = 720;
	private OrthographicCamera camera;

	private AssetManager manager;

	private GameOptions options;

	private CreditsScreen creditsScreen;
	private MenuScreen menuScreen;
	private GameScreen gameScreen;


	SpriteBatch batch;
	Texture img;
	
	@Override
	public void create() {
		// management ...
		options = new GameOptions();
		manager = new AssetManager(options);
		// screens ...
		creditsScreen = new CreditsScreen(manager);
		menuScreen = new MenuScreen(manager);
		gameScreen = new GameScreen(manager);

		// handle back button
		Gdx.input.setCatchBackKey(true);

		batch = new SpriteBatch();
		camera = new OrthographicCamera(WIDTH, HEIGHT);
		camera.position.set(WIDTH / 2, HEIGHT / 2, 0);
	}


	@Override
	public void render () {
		Gdx.gl.glClearColor(0.065f, 0.105f, 0.225f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		if (options.gameState != GameOptions.STATE_GAME && options.gameState != GameOptions.STATE_PAUSED) gameScreen.resetGame();
		if (options.gameState == GameOptions.STATE_MENU) menuScreen.render(batch);
		if (options.gameState == GameOptions.STATE_GAME) gameScreen.render(batch);
		if (options.gameState == GameOptions.STATE_CREDITS) creditsScreen.render(batch);

	}
}


