package kr.jclab.javautils.systeminformation.smbios;

import kr.jclab.javautils.systeminformation.exception.NativeApiErrorException;
import kr.jclab.javautils.systeminformation.platform.linux.LinuxSMBIOS;
import kr.jclab.javautils.systeminformation.platform.windows.WindowsSMBIOS;
import org.apache.commons.lang3.SystemUtils;

import java.io.IOException;

public class SMBIOS implements SMBIOSBase {
    private final SMBIOSBase parent;

    public SMBIOS() {
        this(null);
    }

    public SMBIOS(SMBIOSBase instance) {
        if (instance == null) {
            if (SystemUtils.IS_OS_WINDOWS) {
                instance = new WindowsSMBIOS();
            } else {
                instance = new LinuxSMBIOS();
            }
        }
        this.parent = instance;
    }

    @Override
    public void read(SMBIOSReader reader) throws NativeApiErrorException, IOException {
        this.parent.read(reader);
    }

    public static SMBIOSReader newDefaultReader() {
        return new SMBIOSReader();
    }
}
