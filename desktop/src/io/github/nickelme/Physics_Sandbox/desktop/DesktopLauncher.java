package io.github.nickelme.Physics_Sandbox.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import io.github.nickelme.Physics_Sandbox.PhysicsSandboxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = 600;
		config.width = 1000;
		new LwjglApplication(new PhysicsSandboxGame(), config);
	}
}
