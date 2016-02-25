package at.juggle.games.memory.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import at.juggle.games.memory.MemoryGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Memory";
		config.width = 1280;
		config.height = 720;
		new LwjglApplication(new MemoryGame(), config);
	}
}
