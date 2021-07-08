package kr.jclab.javautils.systeminformation.platform.windows;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;
import kr.jclab.javautils.systeminformation.model.OSInformation;
import kr.jclab.javautils.systeminformation.osinfo.OSInfoBase;

import java.util.TreeMap;

public class WindowsOSInfo implements OSInfoBase {
    @Override
    public OSInformation read() {
        TreeMap<String, Object> objects =  Advapi32Util.registryGetValues(WinReg.HKEY_LOCAL_MACHINE, "SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion");
        Object installDateObj = objects.get("InstallDate");
        Object productNameObj = objects.get("ProductName");
        Object releaseIdObj = objects.get("ReleaseId");

        long installDate = 0;
        if (installDateObj instanceof Long) {
            installDate = (Long) installDateObj;
        } else if (installDateObj instanceof Integer) {
            installDate = ((Integer) installDateObj).longValue();
        }

        return OSInformation.builder()
                .identity("windows")
                .installedAt(installDate)
                .productName((String)productNameObj)
                .releaseId((String)releaseIdObj)
                .build();
    }
}
