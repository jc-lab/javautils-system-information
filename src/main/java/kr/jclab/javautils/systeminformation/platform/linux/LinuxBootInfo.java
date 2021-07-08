package kr.jclab.javautils.systeminformation.platform.linux;

import kr.jclab.javautils.systeminformation.bootinfo.BootInfoBase;
import kr.jclab.javautils.systeminformation.model.FirmwareType;

import java.io.File;

public class LinuxBootInfo implements BootInfoBase {
    private final File efiDirectory = new File("/sys/firmware/efi");
    @Override
    public FirmwareType getFirmwareType() {
        if (efiDirectory.exists() && efiDirectory.isDirectory()) {
            return FirmwareType.Uefi;
        } else {
            return FirmwareType.Legacy;
        }
    }
}
