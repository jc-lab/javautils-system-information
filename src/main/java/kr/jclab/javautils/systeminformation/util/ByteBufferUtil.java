package kr.jclab.javautils.systeminformation.util;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.UUID;

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

    public static UUID createUUIDFromBytes(byte[] data) {
        long msb = 0;
        long lsb = 0;
        msb |= ((long)(data[0] & 0xff)) << 32L;
        msb |= ((long)(data[1] & 0xff)) << 40L;
        msb |= ((long)(data[2] & 0xff)) << 48L;
        msb |= ((long)(data[3] & 0xff)) << 56L;
        msb |= ((long)(data[4] & 0xff)) << 16L;
        msb |= ((long)(data[5] & 0xff)) << 24L;
        msb |= data[6] & 0xff;
        msb |= ((long)(data[7] & 0xff)) << 8L;
        for (int i = 8; i < 16; i++)
            lsb = (lsb << 8) | (data[i] & 0xff);
        return new UUID(msb, lsb);
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
