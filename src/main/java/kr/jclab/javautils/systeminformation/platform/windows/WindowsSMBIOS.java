package kr.jclab.javautils.systeminformation.platform.windows;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import kr.jclab.javautils.systeminformation.exception.NativeApiErrorException;
import kr.jclab.javautils.systeminformation.model.SmbiosBIOSInformation;
import kr.jclab.javautils.systeminformation.model.SmbiosBaseboardInformation;
import kr.jclab.javautils.systeminformation.model.SmbiosSystemInformation;
import kr.jclab.javautils.systeminformation.smbios.SMBIOSBase;
import kr.jclab.javautils.systeminformation.smbios.SMBIOSReader;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class WindowsSMBIOS implements SMBIOSBase {
    private final Kernel32Ex kernel32 = Kernel32Ex.INSTANCE;

    private boolean perfect = false;
    private SmbiosBIOSInformation biosInformation = null;
    private SmbiosSystemInformation systemInformation = null;
    private SmbiosBaseboardInformation baseboardInformation = null;

    @lombok.Getter
    public static class RawSMBIOSData {
        private final byte used20CallingMethod;
        private final byte smbiosMajorVersion;
        private final byte smbiosMinorVersion;
        private final byte dmiRevision;
        private final int length;

        public RawSMBIOSData(ByteBuffer buffer) {
            this.used20CallingMethod = buffer.get();
            this.smbiosMajorVersion = buffer.get();
            this.smbiosMinorVersion = buffer.get();
            this.dmiRevision = buffer.get();
            this.length = buffer.getInt();
        }
    }

    @Override
    public void read(SMBIOSReader reader) throws NativeApiErrorException, IOException {
        int bufferSize = this.kernel32.GetSystemFirmwareTable(
                Kernel32Ex.RSMB,
                0,
                Pointer.NULL,
                0
        );
        ByteBuffer buffer = ByteBuffer
                .allocateDirect(bufferSize)
                .order(ByteOrder.LITTLE_ENDIAN);
        bufferSize = this.kernel32.GetSystemFirmwareTable(
                Kernel32Ex.RSMB,
                0,
                Native.getDirectBufferPointer(buffer),
                buffer.capacity()
        );
        if (bufferSize < 0) {
            throw new NativeApiErrorException("buffer size < 0");
        }
        buffer.limit(bufferSize);

        final RawSMBIOSData rawSMBIOSData = new RawSMBIOSData(buffer);
        reader.process(buffer, rawSMBIOSData.getLength());
        reader.setPerfect(true);
    }
}
