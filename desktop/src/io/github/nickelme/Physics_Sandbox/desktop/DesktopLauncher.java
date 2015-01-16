package io.github.nickelme.Physics_Sandbox.desktop;

import javax.swing.JFrame;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import io.github.nickelme.Physics_Sandbox.PhysicsSandboxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		
		
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Physics Sandbox Game";
		config.height = 720;
		config.width = 1280;
		config.vSyncEnabled = false;
		config.foregroundFPS = 0;
		new LwjglApplication(new PhysicsSandboxGame(), config);
	}
}
