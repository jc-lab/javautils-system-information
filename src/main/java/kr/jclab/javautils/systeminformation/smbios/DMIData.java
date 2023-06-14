package kr.jclab.javautils.systeminformation.smbios;

import kr.jclab.javautils.systeminformation.util.ByteBufferUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class DMIData {
	/**
	 * Reference SMBIOS 2.6, chapter 3.1.2.
	 * For v2.1 and later, handle values in the range 0FF00h to 0FFFFh are reserved for
	 * use by this specification.
	 */
	public static short SMBIOS_HANDLE_RESERVED_BEGIN = (short) 0xFF00;

	/*
	 * Reference SMBIOS 2.6, chapter 3.1.3
	 * Each text string is limited to 64 significant characters due to system MIF limitations
	 */
	public static int SMBIOS_STRING_MAX_LENGTH =  64;

	/**
	 * Inactive type is added from SMBIOS 2.2. Reference SMBIOS 2.6, chapter 3.3.43.
	 * Upper-level software that interprets the SMBIOS structure-table should bypass an
	 * Inactive structure just like a structure type that the software does not recognize.
	 */
	public static byte SMBIOS_TYPE_INACTIVE = 0x7e;

	/*
	 * End-of-table type is added from SMBIOS 2.2. Reference SMBIOS 2.6, chapter 3.3.44.
	 * The end-of-table indicator is used in the last physical structure in a table
	 */
	public static byte SMBIOS_TYPE_END_OF_TABLE = 0x7f;

	private final DMIHeader header;
	private final byte[] raw;
	private final ArrayList<String> strings;

	private final boolean endMarked;

	public DMIData(ByteBuffer buffer) throws IOException {
		byte[] temp;
		int stringCount = 0;

		this.header = new DMIHeader(buffer);
		int dataSize = header.getLength() - header.getStructureSize();

		// Check for end marker
		if (header.getType() == SMBIOS_TYPE_END_OF_TABLE) {
			this.endMarked = true;
			this.raw = null;
			this.strings = null;
			return ;
		}
		this.endMarked = false;

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

	public boolean isEndMarked() {
		return endMarked;
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
		try {
			String s = this.strings.get(id - 1);
			return s.equalsIgnoreCase(" ") ? "" : s.trim();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}
