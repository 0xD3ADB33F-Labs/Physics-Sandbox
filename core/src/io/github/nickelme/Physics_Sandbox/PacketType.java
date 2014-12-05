package io.github.nickelme.Physics_Sandbox;

import java.nio.ByteBuffer;

public enum PacketType {
	ISALIVE (0, IsAlivePacket.class),
	AMALIVE (1, AmAlivePacket.class);
	
	
	private int type;
	private Class<Packet> packclass;
	
	PacketType(int ptype, Class pclass){
		type = ptype;
		packclass = pclass;
	}
	
	public byte[] getType(){
		return ByteBuffer.allocate(4).putInt(type).array();
	}
	
	public Class<Packet> getPacketClass(){
		return packclass;
	}
}
