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
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.LinkedList;

/**
 * @author Mathias Lux, mathias@juggle.at
 * Date: 13.02.12
 * Time: 10:53
 */
public class GameScreen extends AbstractScreen {
    private GameModel model = null;
    private int numRows;
    private int numCols;
    private float offset = 16;
    // temp vars ...
    private float h;
    private float cardX, cardY;
    private float cardHeight;

    private ParticleEffect fireworksLeft, fireworksRight;

    GlyphLayout fontLayout = new GlyphLayout();

    public GameScreen(AssetManager assets) {
        super(assets);
    }

    public void startGame() {
        model = new GameModel(assets.options.getNumberOfCards());
        numRows = assets.options.getNumberOfRows();
        numCols = assets.options.getNumberOfCols();
        assets.shuffleIcons();
        fireworksLeft = new ParticleEffect(assets.fireworks);
        fireworksRight = new ParticleEffect(assets.fireworks);
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        if (model == null) {
            startGame();
        }
        // key listener ...
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyPressed(Input.Keys.BACK)) { // get back to menu ...
            assets.options.buttonPressTimeout = 1f;
            assets.options.gameState = GameOptions.STATE_MENU;
        }

        h = MemoryGame.HEIGHT;

        cardHeight = (h - (numRows + 1) * offset) / numRows;

        // determine which card has been clicked and react in the model ...
        // but only if the intro animation is over ...
        if (model.introAnimation < 0) {
            compute(offset, cardHeight);
            // one time covering action after the animation.
            if (model.needsToBeCovered) {
                model.coverCards();
                model.needsToBeCovered = false;
            }
        }
        else model.introAnimation -= Gdx.graphics.getDeltaTime();
        // game time;
        if (!model.finished) model.time += Gdx.graphics.getDeltaTime();

        // the drawing

        spriteBatch.begin();
        // draw background ...
        spriteBatch.draw(assets.background, 0, 0, MemoryGame.WIDTH, MemoryGame.HEIGHT);

        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                cardX = c * (cardHeight - cardHeight / 5.5f);
                cardY = offset + r * (cardHeight + offset);
                float cardTime = model.introAnimationLength / ((float) model.numCards);
                if (cardTime * (c + numCols * r) < model.introAnimationLength - model.introAnimation) {             // if is for the start animation ... also compensates the first click :)
                    // draw the card ...
                    if (model.getState((c + numCols * r)) == 0) {
                        spriteBatch.draw(assets.cardBackMark, cardX, cardY, cardHeight, cardHeight);
                    } else if (model.getState((c + numCols * r)) == 1) {
                        spriteBatch.draw(assets.cardBack, cardX, cardY, cardHeight, cardHeight);
                    } else if (model.getState((c + numCols * r)) >= 2) {
                        spriteBatch.draw(assets.card, cardX, cardY, cardHeight, cardHeight);

                        if (assets.icons == null) {
                            String str = "" + model.icon[(c + numCols * r)];
                            assets.font.draw(spriteBatch, str,
                                    cardX + cardHeight / 2 - getStringWidth(assets.font, str) / 2,
                                    cardY + cardHeight / 2 + getStringHeight(assets.font, str) / 2);
                        } else {
                            float iconSize = cardHeight / 2;
                            spriteBatch.draw(assets.icons[model.icon[(c + numCols * r)]],
                                    cardX + cardHeight / 2 - iconSize / 2,
                                    cardY + cardHeight / 2 - iconSize / 2,
                                    iconSize, iconSize);

                        }

                        if (model.getState((c + numCols * r)) == 3 && !model.finished) {
                            spriteBatch.draw(assets.check, cardX + cardHeight - 2f * cardHeight / 5.5f, cardY + cardHeight / 16f, 32, 32);
                        }
                    }
                    // draw the icon ...
                }
            }
        }

        // draw time and moves ...
        if (model.introAnimation < 0 & !model.finished) {
            String moves = model.numberOfMoves + " tries";
            int secs = ((int) Math.floor(model.time)) % 60;
            int min = ((int) Math.floor(model.time)) / 60;
            String timeString = min + ":" + (secs < 10 ? "0" : "") + secs;
            assets.font.draw(spriteBatch, moves, MemoryGame.WIDTH - getStringWidth(assets.font, moves) - 10, offset + assets.font.getLineHeight());
            assets.font.draw(spriteBatch, timeString, MemoryGame.WIDTH - getStringWidth(assets.font, timeString) - 10, offset + assets.font.getLineHeight() * 2);
        }

//        spriteBatch.draw(assets.undo, MemoryGame.WIDTH - assets.undo.getWidth() - 10, MemoryGame.HEIGHT - assets.undo.getHeight() - 10);

        if (model.finished) {

            spriteBatch.draw(assets.grey, 0, 0, MemoryGame.WIDTH, MemoryGame.HEIGHT);

            fireworksLeft.findEmitter("fireworks").setPosition(MemoryGame.WIDTH /4, 0);
            fireworksLeft.findEmitter("fireworks").draw(spriteBatch, Gdx.graphics.getDeltaTime());
            fireworksRight.findEmitter("fireworks").setPosition(3*MemoryGame.WIDTH /4, 0);
            fireworksRight.findEmitter("fireworks").draw(spriteBatch, Gdx.graphics.getDeltaTime());

            spriteBatch.draw(assets.positive, MemoryGame.WIDTH / 2 - assets.positive.getWidth() / 2, MemoryGame.HEIGHT / 2 - assets.positive.getHeight() / 2);
//            assets.font.getData().scale(2f);
            assets.font.draw(spriteBatch, "Congratulations!",
                    MemoryGame.WIDTH / 2 - getStringWidth(assets.font, "Congratulations!") / 2,
                    MemoryGame.HEIGHT - assets.font.getLineHeight() * 2);
//            assets.font.getData().scale(1f);
            int secs = ((int) Math.floor(model.time)) % 60;
            int min = ((int) Math.floor(model.time)) / 60;
            String timeString = "Solved in " + min + ":" + (secs < 10 ? "0" : "") + secs + " with " + model.numberOfMoves + " tries";
            assets.smallFont.draw(spriteBatch, timeString,
                    MemoryGame.WIDTH / 2 - getStringWidth(assets.smallFont, timeString) / 2,
                    assets.smallFont.getLineHeight() * 2);

        }

        spriteBatch.end();
    }

    private void compute(float offset, float cardHeight) {
        if (Gdx.input.justTouched()) {
            // go to menu if game is finished.
            if (model.finished) assets.options.gameState = GameOptions.STATE_MENU;


            // (x,y) from top left corner
            float x = Gdx.input.getX()/ ((float) Gdx.graphics.getWidth())* ((float) MemoryGame.WIDTH);
            float y = Gdx.input.getY()/ ((float) Gdx.graphics.getHeight())* ((float) MemoryGame.HEIGHT);

            // row might be easier:
            int hitRow = -1;
            for (int r = 0; r < numRows; r++) {
                if (y > offset + r * (offset + cardHeight) && y < offset + (r + 1) * (offset + cardHeight)) {
                    hitRow = numRows - 1 - r;
                }
            }
            // now for the cols:
            int hitCol = -1;
            for (int c = 0; c < numCols; c++) {
                if (x > c * (cardHeight - cardHeight / 5.5f) && x < (c + 1) * (cardHeight - cardHeight / 5.5f)) {
                    hitCol = c;
                }
            }
            // adapt model ..
            if (hitCol > -1 && hitRow > -1) {
                model.turnCard(hitCol + numCols * hitRow);
            }
        }
    }

    public void resetGame() {
        model = null;
    }

    private float getStringWidth(BitmapFont font, String str) {
        fontLayout.setText(font, str);
        return fontLayout.width;
    }

    private float getStringHeight(BitmapFont font, String str) {
        fontLayout.setText(font, str);
        return fontLayout.height;
    }


    class GameModel {
        int[] state;
        int[] icon;
        int numCards;
        float introAnimation;
        float introAnimationLength = 1; // intro animation in seconds ...
        int numberOfMoves;
        float time;
        boolean finished, needsToBeCovered;

        GameModel(int numCards) {
            time = -introAnimationLength;
            this.numCards = numCards;
            state = new int[numCards];
            icon = new int[numCards];
            // checks if flash mode is enabled. If so we need to cover the cards after the initial animation and set "needsToBeCovered" to false
            if (assets.options.gameMode==GameOptions.MODE_CLASSICAL) needsToBeCovered = false;
            else needsToBeCovered = true;
            for (int i = 0; i < state.length; i++) {
                if (assets.options.gameMode==GameOptions.MODE_CLASSICAL) state[i] = 0;
                else state[i] = 2;
            }

            // randomize the icons on the cards.
            LinkedList<Integer> icons = new LinkedList<Integer>();
            for (int i = 0; i < numCards / 2; i++) {
                icons.add(i);
                icons.add(i);
            }
            for (int i = 0; i < icon.length; i++) {
                icon[i] = icons.remove((int) Math.floor(Math.random() * icons.size()));
            }
            introAnimation = introAnimationLength;
            numberOfMoves = 0;
            finished = false;
        }

        public void coverCards() {
            for (int i = 0; i < state.length; i++) {
                state[i] = 0;
            }
        }

        public int getState(int card) {
            // 0 ... new and face down
            // 1 ... visited but face down
            // 2 ... turned but not found in a pair
            // 3 ... found as part of a pair
            return state[card];
        }

        public void turnCard(int card) {
            // check if there are already two cards turned ...
            int countTurned = 0;
            boolean found = false;
            for (int i = 0; i < state.length; i++) {
                if (state[i] == 2) {
                    countTurned++;
                }
            }
            if (countTurned == 0) {
                if (state[card] < 2) {
                    state[card] = 2;
//                    numberOfMoves++;
                    if (assets.options.soundOn) assets.sndFlipCard.play();
                }
            } else if (countTurned == 1) {
                int iconTurned = icon[card], lastTurned = card;
                for (int i = 0; i < state.length; i++) {
                    if (state[i] == 2 && i != card) {
                        if (iconTurned == icon[i]) {
                            // Player has found a pair of cards ...
                            // ------------------------------------
                            state[i] = 3;
                            state[lastTurned] = 3;
                            found = true;
                            numberOfMoves++;
                            if (assets.options.soundOn) assets.sndFlipCard.play();
                            if (assets.options.soundOn) assets.sndDing.play();
                        }
                    }
                }
                if (!found & state[card] < 2) {
                    state[card] = 2;
                    numberOfMoves++;
                    if (assets.options.soundOn) assets.sndFlipCard.play();
                }
            } else {
                for (int i = 0; i < state.length; i++) {
                    if (state[i] == 2) {
                        state[i] = 1;
                    }
                }
            }
            finished = true;
            for (int i = 0; i < state.length; i++) {
                finished = finished & state[i] == 3;
            }
            if (finished) {
                fireworksLeft.start();
                fireworksRight.start();
                if (assets.options.soundOn) assets.sndCheer.play();
            }
        }
    }
}
