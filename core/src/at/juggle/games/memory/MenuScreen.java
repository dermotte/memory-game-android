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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * @author Mathias Lux, mathias@juggle.at
 * Date: 13.02.12
 * Time: 10:09
 */
public class MenuScreen extends AbstractScreen {
    private float l, h;
    private Texture logoTexture;
    private float scaleFont = 1f;
    GlyphLayout fontLayout = new GlyphLayout();

    public MenuScreen(AssetManager assets) {
        super(assets);
        // adapt menu font to smaller screens:
        if (MemoryGame.HEIGHT < 479) scaleFont = 0.8f;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        assets.font.getData().setScale(scaleFont, scaleFont);
        assets.font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        h = MemoryGame.HEIGHT;
        l = assets.font.getLineHeight() * scaleFont + 18;

        if (Gdx.input.justTouched()) {
            // (x,y) from top left corner
            float x = Gdx.input.getX()/ ((float) Gdx.graphics.getWidth())* ((float) MemoryGame.WIDTH);
            float y = Gdx.input.getY()/ ((float) Gdx.graphics.getHeight())* ((float) MemoryGame.HEIGHT);
            if (x > l && x < 2 * MemoryGame.WIDTH / 3) {
                if (y > l + 5 && y < 2 * l - 10) { // new Game
                    assets.options.gameState = GameOptions.STATE_GAME;
                } else if (y > 2 * l + 5 && y < 3 * l - 10) { // number of cards changed
                    assets.options.nextNumberOfCards();
                } else if (y > 3 * l + 5 && y < 4 * l - 10) { // Mode
                    assets.options.toggleGameMode();
                } else if (y > 4 * l + 5 && y < 5 * l - 10) { // Sound
                    assets.options.toggleSoundOn();
                } else if (y > 5 * l + 5 && y < 6 * l - 10) { // credits
                    assets.options.gameState = GameOptions.STATE_CREDITS;
                }
            }
        }

        // key listener ...
        if (assets.options.buttonPressTimeout < 0) {
            if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyPressed(Input.Keys.BACK)) { // use the back button
                Gdx.app.exit();
            }
        } else assets.options.buttonPressTimeout -= Gdx.graphics.getDeltaTime();

        // do the drawing ...
        spriteBatch.begin();
        // draw background ...
        spriteBatch.draw(assets.background, 0, 0, MemoryGame.WIDTH, MemoryGame.HEIGHT);

        spriteBatch.setColor(Color.WHITE);
        if (MemoryGame.WIDTH / 3 - 20 < assets.logo.getWidth()) { // draw logo scaled
            float logoWidth = (int) (MemoryGame.WIDTH / 3f - 20f);
            spriteBatch.draw(assets.logo, MemoryGame.WIDTH - logoWidth - logoWidth/4, logoWidth/4, logoWidth, logoWidth);
        } else {
            spriteBatch.draw(assets.logo, MemoryGame.WIDTH - assets.logo.getWidth() - assets.logo.getWidth()/4, assets.logo.getWidth()/4);
        }


        assets.font.draw(spriteBatch, "> New Game", l, h - l);
        assets.font.draw(spriteBatch, "> Cards: ", l, h - 2 * l);
        assets.font.draw(spriteBatch, assets.options.getNumberOfCards() + "", 2 * MemoryGame.WIDTH / 3 - getStringWidth(assets.font, assets.options.getNumberOfCards() + ""), h - 2 * l);
        assets.font.draw(spriteBatch, "> Mode: ", l, h - 3 * l);
        assets.font.draw(spriteBatch, assets.options.getGameModeString(), 2 * MemoryGame.WIDTH / 3 - getStringWidth(assets.font, assets.options.getGameModeString()), h - 3 * l);
        assets.font.draw(spriteBatch, "> Sound: ", l, h - 4 * l);
        assets.font.draw(spriteBatch, (assets.options.soundOn)?"On":"Off", 2 * MemoryGame.WIDTH / 3 - getStringWidth(assets.font, (assets.options.soundOn)?"On":"Off"), h - 4 * l);
        assets.font.draw(spriteBatch, "> Credits", l, h - 5 * l);

        spriteBatch.end();
    }

    private float getStringWidth(BitmapFont font, String str) {
        fontLayout.setText(font, str);
        return fontLayout.width;
    }

    private float getStringHeight(BitmapFont font, String str) {
        fontLayout.setText(font, str);
        return fontLayout.height;
    }
}
