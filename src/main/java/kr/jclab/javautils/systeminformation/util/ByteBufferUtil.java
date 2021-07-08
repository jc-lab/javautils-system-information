package kr.jclab.javautils.systeminformation.util;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class ByteBufferUtil {
	public static byte peekByte(ByteBuffer buffer) {
		int pos = buffer.position();
		byte v = buffer.get();
		buffer.position(pos);
		return v;
	}

	public static byte[] readNullTerminated(ByteBuffer buffer) {
		byte v;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		while ((v = buffer.get()) != 0) {
			byteArrayOutputStream.write(v);
		}
		return byteArrayOutputStream.toByteArray();
	}
}
