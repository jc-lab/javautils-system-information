package kr.jclab.javautils.systeminformation;

import kr.jclab.javautils.systeminformation.bootinfo.BootInfoBase;
import kr.jclab.javautils.systeminformation.model.OSArch;
import kr.jclab.javautils.systeminformation.model.OSInformation;
import kr.jclab.javautils.systeminformation.model.OSType;
import kr.jclab.javautils.systeminformation.osinfo.OSInfoBase;
import kr.jclab.javautils.systeminformation.platform.linux.LinuxBootInfo;
import kr.jclab.javautils.systeminformation.platform.linux.LinuxOSInfo;
import kr.jclab.javautils.systeminformation.platform.windows.WindowsBootInfo;
import kr.jclab.javautils.systeminformation.platform.windows.WindowsOSInfo;
import org.apache.commons.lang3.ArchUtils;
import org.apache.commons.lang3.SystemUtils;

import java.io.IOException;
import java.util.stream.Stream;

public class SystemInformation {
    private final OSInfoBase osInfoReader;
    private final BootInfoBase bootInfoReader;

    public OSType getOSType() {
        final String name = SystemUtils.OS_NAME;
        OSType.PredefinedType type = OSType.PredefinedType.Unknown;
        if (SystemUtils.IS_OS_LINUX) {
            type = OSType.PredefinedType.Linux;
            boolean isAndroid = Stream.of(
                    System.getProperty("java.vendor"),
                    System.getProperty("java.vm.vendor")
            )
                    .filter(v -> (v != null) && !v.isEmpty())
                    .anyMatch(v -> v.contains("The Android Project"));
            if (isAndroid) {
                type = OSType.PredefinedType.Android;
            }
        } else if (SystemUtils.IS_OS_WINDOWS) {
            type = OSType.PredefinedType.Windows;
        } else if (SystemUtils.IS_OS_MAC) {
            type = OSType.PredefinedType.Darwin;
        }
        return new OSType(type, name);
    }

    public OSArch getOSArch() {
        OSArch.PredefinedType type = OSArch.PredefinedType.Unknown;
        if (ArchUtils.getProcessor().isX86()) {
            if (ArchUtils.getProcessor().is32Bit()) {
                type = OSArch.PredefinedType.X86_32;
            } else if (ArchUtils.getProcessor().is64Bit()) {
                type = OSArch.PredefinedType.X86_64;
            }
        } else if (SystemUtils.OS_ARCH.equalsIgnoreCase("aarch64")) {
            type = OSArch.PredefinedType.AArch64;
        }
        return new OSArch(type, SystemUtils.OS_ARCH);
    }

    public SystemInformation() {
        OSInfoBase osInfoReader = null;
        BootInfoBase bootInfoReader = null;

        if (SystemUtils.IS_OS_WINDOWS) {
            osInfoReader = new WindowsOSInfo();
            bootInfoReader = new WindowsBootInfo();
        } else if (SystemUtils.IS_OS_LINUX) {
            osInfoReader = new LinuxOSInfo();
            bootInfoReader = new LinuxBootInfo();
        }

        this.osInfoReader = osInfoReader;
        this.bootInfoReader = bootInfoReader;
    }

    public OSInformation getOSInformation() throws IOException {
        return this.osInfoReader.read();
    }

    public OSInfoBase getOsInfoReader() {
        return osInfoReader;
    }

    public BootInfoBase getBootInfoReader() {
        return bootInfoReader;
    }
}
