package kr.jclab.javautils.systeminformation.platform.windows;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import kr.jclab.javautils.systeminformation.bootinfo.BootInfoBase;
import kr.jclab.javautils.systeminformation.model.FirmwareType;

public class WindowsBootInfo implements BootInfoBase {
    @Override
    public FirmwareType getFirmwareType() {
        try {
            Kernel32Ex.FIRMWARE_TYPE firmwareType = Kernel32Ex.INSTANCE.GetFirmwareType();
            switch (firmwareType) {
                case FirmwareTypeBios:
                    return FirmwareType.Legacy;
                case FirmwareTypeUefi:
                    return FirmwareType.Uefi;
                default:
                    return FirmwareType.Unknown;
            }
        } catch (Throwable e) {
            Kernel32Ex.INSTANCE.GetFirmwareEnvironmentVariable(
                    "",
                    "{00000000-0000-0000-0000-000000000000}",
                    Pointer.NULL,
                    0
            );
            int errorCode = Native.getLastError();
            if (errorCode == Kernel32Ex.ERROR_INVALID_FUNCTION) {
                return FirmwareType.Legacy;
            } else if (errorCode == 0 || errorCode == Kernel32Ex.ERROR_NOACCESS) {
                return FirmwareType.Uefi;
            }
        }
        return FirmwareType.Unknown;
    }
}
