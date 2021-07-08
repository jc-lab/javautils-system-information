package kr.jclab.javautils.systeminformation.platform.windows;

import com.sun.jna.*;
import com.sun.jna.platform.EnumConverter;
import com.sun.jna.platform.win32.HighLevelMonitorConfigurationAPI;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.win32.W32APIOptions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public interface Kernel32Ex extends Kernel32 {
    enum FIRMWARE_TYPE {
        FirmwareTypeUnknown,
        FirmwareTypeBios,
        FirmwareTypeUefi,
        FirmwareTypeMax
    }

    Map<String, Object> Kernel32Ex_OPTIONS = Collections.unmodifiableMap(new HashMap<String, Object>() {
        {
            putAll(W32APIOptions.DEFAULT_OPTIONS);
            put(Library.OPTION_TYPE_MAPPER, new DefaultTypeMapper() {
                {
                    addTypeConverter(FIRMWARE_TYPE.class, new EnumConverter<>(FIRMWARE_TYPE.class));
                }
            });
        }
    });
    Kernel32Ex INSTANCE = Native.load("kernel32", Kernel32Ex.class, Kernel32Ex_OPTIONS);

    int RSMB = 0x52534d42;

    int ERROR_INVALID_FUNCTION = 0x1;
    int ERROR_INVALID_PARAMETER = 87;
    int ERROR_NOACCESS = 998;

    /* UINT */ int GetSystemFirmwareTable(
            int dwFirmwareTableProviderSignature,
            int dwFirmwareTableID,
            Pointer pFirmwareTableBuffer,
            int dwBufferSize
    );

    /* BOOL */ boolean GetFirmwareType(
            Pointer firmwareTypePointer
    );

    /* DWORD */ int GetFirmwareEnvironmentVariable(
            String lpName,
            String lpGuid,
            Pointer pBuffer,
            int nSize
    );

    default FIRMWARE_TYPE GetFirmwareType() {
        FIRMWARE_TYPE result = FIRMWARE_TYPE.FirmwareTypeUnknown;
        byte[] byteValues = new byte[Native.POINTER_SIZE];
        Memory valuePtr = new Memory(Native.POINTER_SIZE);
        valuePtr.clear();
        this.GetFirmwareType(valuePtr);
        valuePtr.read(0, byteValues, 0, byteValues.length);
        for (int i = 0; i < byteValues.length; i++) {
            if (byteValues[i] != 0) {
                switch (byteValues[i]) {
                    case 1:
                        result = FIRMWARE_TYPE.FirmwareTypeBios;
                        break;
                    case 2:
                        result = FIRMWARE_TYPE.FirmwareTypeUefi;
                        break;
                    case 3:
                        result = FIRMWARE_TYPE.FirmwareTypeMax;
                        break;
                }
                break;
            }
        }
        return result;
    }
}
