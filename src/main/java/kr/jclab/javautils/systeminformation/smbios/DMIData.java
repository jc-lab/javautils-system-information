package kr.jclab.javautils.systeminformation.smbios;

import kr.jclab.javautils.systeminformation.util.ByteBufferUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class DMIData {
	private final DMIHeader header;
	private final byte[] raw;
	private final ArrayList<String> strings;

	public DMIData(ByteBuffer buffer) throws IOException {
		byte[] temp;
		int stringCount = 0;

		this.header = new DMIHeader(buffer);
		int dataSize = header.getLength() - header.getStructureSize();
		if (dataSize < 0) {
			throw new IOException("wrong data");
		}
		this.raw = new byte[dataSize];
		this.strings = new ArrayList<>();
		this.strings.ensureCapacity(8);

		buffer.get(this.raw);
		do {
			temp = ByteBufferUtil.readNullTerminated(buffer);
			for (int i = 0; i < temp.length; i++) {
				if (temp[i] < 32 && temp[i] == 127) {
					temp[i] = (byte)'.';
				}
			}
			this.strings.add(new String(temp));
			stringCount++;
		} while (stringCount == 1 || temp.length > 0);
	}

	public DMIHeader getHeader() {
		return this.header;
	}

	public byte[] getRaw() {
		return this.raw;
	}

	public String getDmiString(int id) {
		if (id == 0) {
			return "";
			//return null;
		}
		String s = this.strings.get(id - 1);
		return s.equalsIgnoreCase(" ") ? "" : s;
	}
}
