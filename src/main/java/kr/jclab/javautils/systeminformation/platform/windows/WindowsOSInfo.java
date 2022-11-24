package kr.jclab.javautils.systeminformation.platform.windows;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinReg;
import com.sun.jna.ptr.IntByReference;
import kr.jclab.javautils.systeminformation.model.OSInformation;
import kr.jclab.javautils.systeminformation.osinfo.OSInfoBase;
import org.apache.commons.lang3.ArchUtils;

import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WindowsOSInfo implements OSInfoBase {
    private final Pattern WINDOWS_10_PATTERN = Pattern.compile("Windows 10", Pattern.CASE_INSENSITIVE);

    @Override
    public boolean isRealOsArchIs64Bit() {
        if (ArchUtils.getProcessor().is64Bit()) {
            return true;
        }

        Kernel32 kernel32 = Kernel32.INSTANCE;
        IntByReference result = new IntByReference();
        kernel32.IsWow64Process(kernel32.GetCurrentProcess(), result);
        return result.getValue() != 0;
    }

    @Override
    public OSInformation read() {
        TreeMap<String, Object> objects = Advapi32Util.registryGetValues(WinReg.HKEY_LOCAL_MACHINE, "SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion");
        Object installDateObj = objects.get("InstallDate");
        Object productNameObj = objects.get("ProductName");
        Object releaseIdObj = objects.get("ReleaseId");
        Object currentBuildNumberObj = objects.get("CurrentBuildNumber");

        long installDate = 0;
        if (installDateObj instanceof Long) {
            installDate = (Long) installDateObj;
        } else if (installDateObj instanceof Number) {
            installDate = ((Number) installDateObj).longValue();
        }

        int currentBuildNumber = 0;
        if (currentBuildNumberObj instanceof String) {
            currentBuildNumber = Integer.parseInt((String) currentBuildNumberObj);
        } else if (currentBuildNumberObj instanceof Number) {
            currentBuildNumber = ((Number) currentBuildNumberObj).intValue();
        }

        String productName = (String) productNameObj;

        if (currentBuildNumber >= 22000) {
            Matcher matcher = WINDOWS_10_PATTERN.matcher(productName);
            if (matcher.find()) {
                productName = matcher.replaceAll("Windows 11");
            }
        }

        return OSInformation.builder()
                .identity("windows")
                .installedAt(installDate)
                .productName(productName)
                .releaseId((String)releaseIdObj)
                .build();
    }
}
