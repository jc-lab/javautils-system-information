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

    public static int readByInt32(byte[] data, int offset) {
        int x = 0;
        x |= (data[offset + 0] & 0xff);
        x |= (data[offset + 1] & 0xff) << 8;
        x |= (data[offset + 2] & 0xff) << 16;
        x |= (data[offset + 3] & 0xff) << 24;
        return x;
    }

    public static int readByUint16(byte[] data, int offset) {
        int x = 0;
        x |= (data[offset + 0] & 0xff);
        x |= (data[offset + 1] & 0xff) << 8;
        return x;
    }
}
