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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * @author Mathias Lux, mathias@juggle.at
 * Date: 13.02.12
 * Time: 10:09
 */
public class CreditsScreen extends AbstractScreen{
    String credits = "Game Design & Dev\n" +
            "Mathias Lux\n" +
            "mathias@juggle.at\n" +
            "(c) 2012-2016\n" +
            "\n" +
            "Many thanks to the libGdx project\n" +
            "http://libgdx.badlogicgames.com/\n" +
            "\n" +
            "This game is licensed under \n" +
            "Apache License v2.0\n" +
            "Find it at at github:\n" +
            "dermotte/memory-game-android\n" +
            "\n" +
            "** Visuals:\n" +
            "\n" +
            "Animal, vacation, positive\n" +
            "& check icons by\n" +
            "www.visualpharm.com\n" +
            "\n" +
            "Vehicles by cemagraphics (cc)\n" +
            "http://cemagraphics.deviantart.com/\n" +
            "\n";
    String[] creditsLines = credits.split("\\n");
    BitmapFont font = assets.font;
    float offset = - (creditsLines.length) * font.getLineHeight();
    float minTimeShown = 2f;

    public CreditsScreen(AssetManager assets) {
        super(assets);
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        float h = MemoryGame.HEIGHT;
        float l = font.getLineHeight();

        minTimeShown -= Gdx.graphics.getDeltaTime();
        if (Gdx.input.justTouched() & minTimeShown < 0) {
            assets.options.gameState = GameOptions.STATE_MENU;
            minTimeShown = 2f;
        }
        // key listener ...
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyPressed(Input.Keys.BACK)) { // get back to menu ...
            assets.options.gameState = GameOptions.STATE_MENU;
        }


        offset += Gdx.graphics.getDeltaTime()*35f;
        if (offset > MemoryGame.HEIGHT)  offset = - (creditsLines.length) * font.getLineHeight();
        spriteBatch.begin();
        // draw background ...
        spriteBatch.draw(assets.background, 0, 0, MemoryGame.WIDTH, MemoryGame.HEIGHT);

        spriteBatch.setColor(Color.WHITE);
        for (int i = 0; i < creditsLines.length; i++) {
            String line = creditsLines[i];
            font.draw(spriteBatch, line, 16, offset + (creditsLines.length-i)*font.getLineHeight());
        }
        // draw gradients for the fx
        spriteBatch.draw(assets.gradientBottom, 0, 0, MemoryGame.WIDTH, assets.gradientBottom.getHeight());
        spriteBatch.draw(assets.gradientTop, 0, MemoryGame.HEIGHT-assets.gradientTop.getHeight(), MemoryGame.WIDTH, assets.gradientTop.getHeight());
        spriteBatch.end();
    }
}
