package kr.jclab.javautils.systeminformation.smbios;

import java.nio.ByteBuffer;

public class DMIHeader {
	public static final int STRUCTURE_SIZE = 4;

	private final byte type;
	private final byte length;
	private final short handle;

	public DMIHeader(ByteBuffer buffer) {
		this.type = buffer.get();
		this.length = buffer.get();
		this.handle = buffer.getShort();
	}

	public int getStructureSize() {
		return STRUCTURE_SIZE;
	}

	public byte getType() {
		return type;
	}

	public int getLength() {
		return ((int)length) & 0xFF;
	}

	public short getHandle() {
		return handle;
	}
}
