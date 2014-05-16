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

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

/**
 * @author Mathias Lux, mathias@juggle.at
 * Date: 13.02.12
 * Time: 10:06
 */
public class DesktopApplication {
    public static void main(String[] argv) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Memory HD";
        // cfg.useGL20 = true;
        cfg.width = 1280;
        cfg.height = 720;
//        cfg.fullscreen = true;
        new LwjglApplication(new MemoryGame(), cfg);
//        new LwjglApplication(new MemoryGame(), "Memory", 1280, 800, false);
    }
}
