/*
Copyright 2012-2014 Mathias Lux, mathias@juggle.at

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package at.juggle.games.memory;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import javax.microedition.khronos.opengles.GL10;

/**
 * @author Mathias Lux, mathias@juggle.at
 * Date: 13.02.12
 * Time: 10:07
 */
public class MemoryGame implements ApplicationListener{
    public final static int WIDTH = 1280;
    public final static int HEIGHT = 720;
    private OrthographicCamera camera;

    /** manages all assets */
    private AssetManager manager;

    /** manages all available options in the game */
    private GameOptions options;

    /* all the game screens */
    private CreditsScreen creditsScreen;
    private MenuScreen menuScreen;
    private GameScreen gameScreen;

    /* SpriteBatch */
    private SpriteBatch batch;

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

    public void resize(int i, int i1) {

    }

    public void render() {
        Gdx.gl.glClearColor(0.065f, 0.105f, 0.225f, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        if (options.gameState != GameOptions.STATE_GAME && options.gameState != GameOptions.STATE_PAUSED) gameScreen.resetGame();
        if (options.gameState == GameOptions.STATE_MENU) menuScreen.render(batch);
        if (options.gameState == GameOptions.STATE_GAME) gameScreen.render(batch);
        if (options.gameState == GameOptions.STATE_CREDITS) creditsScreen.render(batch);
    }

    public void pause() {
    }

    public void resume() {
    }

    public void dispose() {
    }
}
