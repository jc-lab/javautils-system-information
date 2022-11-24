package kr.jclab.javautils.systeminformation.platform.windows;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinReg;
import com.sun.jna.ptr.IntByReference;
import kr.jclab.javautils.systeminformation.model.OSInformation;
import kr.jclab.javautils.systeminformation.osinfo.OSInfoBase;
import org.apache.commons.lang3.ArchUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WindowsOSInfo implements OSInfoBase {
    private final Pattern WINDOWS_10_PATTERN = Pattern.compile("Windows 10", Pattern.CASE_INSENSITIVE);

    private final Set<String> WHITE_LITE = Stream.of(
            "BaseBuildRevisionNumber",
            "BuildBranch",
            "BuildGUID",
            "BuildLab",
            "BuildLabEx",
            "CompositionEditionID",
            "CurrentBuild",
            "CurrentBuildNumber",
            "CurrentMajorVersionNumber",
            "CurrentMinorVersionNumber",
            "CurrentType",
            "CurrentVersion",
            "DisplayVersion",
            "EditionID",
            "EditionSubManufacturer",
            "EditionSubVersion",
            "EditionSubstring",
            "InstallDate",
            "InstallationType",
            "PathName",
            "ProductName",
            "ReleaseId",
            "SoftwareType",
            "SystemRoot",
            "UBR"
    ).collect(Collectors.toSet());

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
        TreeMap<String, Object> objects = this.readCurrentVersionFromRegistry();
        return parseFromCurrentVersion(objects);
    }

    public OSInformation parseFromCurrentVersion(TreeMap<String, Object> objects) {
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

        Map<String, String> currentVersionMap = objects
                .entrySet()
                .stream()
                .filter(v -> WHITE_LITE.contains(v.getKey()))
                .map(v -> new AbstractMap.SimpleEntry<>(v.getKey(), objectToString(v.getValue())))
                .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));

        return OSInformation.builder()
                .identity("windows")
                .installedAt(installDate)
                .productName(productName)
                .releaseId((String)releaseIdObj)
                .windowsOsCurrentVersion(currentVersionMap)
                .build();
    }

    public TreeMap<String, Object> readCurrentVersionFromRegistry() {
        int samFlags = this.isWow64() ? WinNT.KEY_WOW64_64KEY : 0;
        return Advapi32Util.registryGetValues(
                WinReg.HKEY_LOCAL_MACHINE,
                "SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion",
                samFlags
        );
    }

    public boolean isWow64() {
        Kernel32 kernel32 = Kernel32.INSTANCE;
        IntByReference result = new IntByReference();
        if (kernel32.IsWow64Process(kernel32.GetCurrentProcess(), result)) {
            return result.getValue() != 0;
        }
        return false;
    }

    private String objectToString(Object input) {
        if (input == null) return null;
        return input.toString();
    }
}
