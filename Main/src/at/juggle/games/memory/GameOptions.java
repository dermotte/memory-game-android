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
import com.badlogic.gdx.Preferences;

/**
 * @author Mathias Lux, mathias@juggle.at
 * Date: 13.02.12
 * Time: 10:36
 */
public class GameOptions {
    public static final int STATE_MENU = 0;
    public static final int STATE_GAME = 1;
    public static final int STATE_CREDITS = 2;
    public static final int STATE_PAUSED = 4;
    public static final int STATE_OPTIONS = 8;

    public static final int MODE_CLASSICAL = 0;
    public static final int MODE_FLASH = 1;

    public float buttonPressTimeout = 0f;
    
    public static boolean isFreeVersion = true;

    // configure like this: { number of cards, rows, cols }
    private int[][] numberOfCardsOptions;
    protected int numberOfCardsIndex = 2;

    protected int gameState = STATE_MENU;

    protected int gameMode = MODE_CLASSICAL;

    protected boolean soundOn = true;

    public GameOptions() {
        // configure like this: { number of cards, rows, cols }
        if (Gdx.graphics.getWidth()>800) {
            numberOfCardsOptions = new int[][]{
//                    new int[] {4, 2, 2},
                    new int[] {6, 2, 3},
                    new int[] {12, 3, 4},
                    new int[] {18, 3, 6},
                    new int[] {20, 4, 5},
                    new int[] {24, 4, 6},
                    new int[] {28, 4, 7},
                    new int[] {32, 4, 8}
            };
        } else {
            numberOfCardsOptions = new int[][]{
                    new int[] {4, 2, 2},
                    new int[] {6, 2, 3},
                    new int[] {12, 3, 4},
                    new int[] {18, 3, 6},
                    new int[] {20, 4, 5}
            };
        }
    }

    public void nextNumberOfCards() {
        numberOfCardsIndex++;
        numberOfCardsIndex = numberOfCardsIndex % numberOfCardsOptions.length;
    }

    public int getNumberOfRows() {
        return numberOfCardsOptions[numberOfCardsIndex][1];
    }

    public int getNumberOfCols() {
        return numberOfCardsOptions[numberOfCardsIndex][2];
    }

    public int getNumberOfCards() {
        return numberOfCardsOptions[numberOfCardsIndex][0];
    }

    public String getGameModeString() {
        return ((gameMode==0)?"Classic":"Flash");
    }

    public void toggleGameMode() {
        if (gameMode==0) gameMode=1;
        else gameMode =0;
    }

    public void toggleSoundOn() {
        soundOn = !soundOn;
        Preferences preferences = Gdx.app.getPreferences("memory_free_hd.prefs");
        preferences.putBoolean("soundOn", soundOn);
        preferences.flush();
    }
}
