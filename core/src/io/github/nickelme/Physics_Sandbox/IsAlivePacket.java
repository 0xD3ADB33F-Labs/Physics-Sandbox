package io.github.nickelme.Physics_Sandbox;

import java.nio.ByteBuffer;

public class IsAlivePacket extends Packet{

	public IsAlivePacket(ByteBuffer buf) {
		super(buf);
		System.out.println("New user running version: " + buf.getInt());
	}
	
}