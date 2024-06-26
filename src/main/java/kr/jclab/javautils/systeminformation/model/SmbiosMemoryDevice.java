package kr.jclab.javautils.systeminformation.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import kr.jclab.javautils.systeminformation.smbios.DMIData;
import kr.jclab.javautils.systeminformation.smbios.DmiParsable;
import kr.jclab.javautils.systeminformation.smbios.DmiType;
import kr.jclab.javautils.systeminformation.util.ByteBufferUtil;

@lombok.Getter
@lombok.ToString
public class SmbiosMemoryDevice implements SmbiosInformation {
    private final List<MemoryDevice> memoryDevices = new ArrayList<>();

    public static class Parser implements DmiParsable<SmbiosMemoryDevice> {
        @Override
        public int getDmiType() {
            return DmiType.MEMORY_DEVICE.getValue();
        }

        @Override
        public SmbiosMemoryDevice parse(DMIData data, SmbiosInformation old) {
            final byte[] raw = data.getRaw();
            SmbiosMemoryDevice object = (old == null) ? new SmbiosMemoryDevice() : (SmbiosMemoryDevice)old;
            object.memoryDevices.add(MemoryDevice.builder()
                    .formFactor((int)raw[0xA])
                    .deviceLocator(data.getDmiString(raw[0xC]))
                    .bankLocator(data.getDmiString(raw[0xD]))
                    .memoryType(Type.valueFrom(raw[0xE]).getFullName())
                    .manufacturer(data.getDmiString(raw[0x13]))
                    .serialNumber(data.getDmiString(raw[0x14]))
                    .assetTag(data.getDmiString(raw[0x15]))
                    .partNumber(data.getDmiString(raw[0x16]))
                    .size(getSize(data))
                    .speed(getSpeed(data))
                    .build());
            return object;
        }
    }

    private static long getSize(DMIData data) {
        byte[] raw = data.getRaw();
        long size = ByteBufferUtil.readByUint16(raw, 0x8) & 0xffff;

        if (size == 0) {
            // Not installed
            return 0;
        } else if (size == 0xffff) {
            // Unknown
            return -1;
        } else if (size == 0x7fff) {
            // Extended Size
            if (data.getHeader().getLength() < 0x20) {
                // Something wrong
                return -1;
            }
            size = ByteBufferUtil.readByInt32(raw, 0x18) & 0x7fffffffL;
            size *= 1024 * 1024;
        } else {
            boolean flagKb = (size & 0x8000) != 0;
            size &= 0x7fff;
            if (flagKb) {
                // KB
                size *= 1024;
            } else {
                // MB
                size *= 1024 * 1024;
            }
        }

        return size;
    }

    private static int getSpeed(DMIData data) {
        byte[] raw = data.getRaw();
        int speed = ByteBufferUtil.readByUint16(raw, 0x11);

        if (speed == 0xFFFF) {
            if (data.getHeader().getLength() >= 0x5C) {
                speed = ByteBufferUtil.readByInt32(raw, 0x4F);
            } else {
                speed = 0;
            }
        }

        return speed;
    }

    @lombok.ToString
    @lombok.Getter
    @lombok.Builder
    @lombok.AllArgsConstructor
    public static class MemoryDevice {
        private final String bankLocator;
        private final String deviceLocator;
        private final Integer formFactor;
        private final Long size;
        private final String manufacturer;
        private final String serialNumber;
        private final String partNumber;
        private final String assetTag;
        private final String memoryType;
        private final Integer speed;
    }

    @lombok.Getter
    @lombok.AllArgsConstructor
    public enum Type {
        Other(0x1, "Other"),
        Unknown(0x2, "Unknown"),
        DRAM(0x3, "DRAM"),
        EDRAM(0x4, "EDRAM"),
        VRAM(0x5, "VRAM"),
        SRAM(0x6, "SRAM"),
        RAM(0x7, "RAM"),
        ROM(0x8, "ROM"),
        Flash(0x9, "Flash"),
        EEPROM(0xA, "EEPROM"),
        FEPROM(0xB, "FEPROM"),
        EPROM(0xC, "EPROM"),
        CDRAM(0xD, "CDRAM"),
        THREE_D_RAM(0xE, "3DRAM"),
        SDRAM(0xF, "SDRAM"),
        SGRAM(0x10, "SGRAM"),
        RDRAM(0x11, "RDRAM"),
        DDR(0x12, "DDR"),
        DDR2(0x13, "DDR2"),
        DDR2_FB_DIMM(0x14, "DDR2 FB-DIMM"),
        RESERVED_1(0x15, "Reserved"),
        RESERVED_2(0x16, "Reserved"),
        RESERVED_3(0x17, "Reserved"),
        DDR3(0x18, "DDR3"),
        FBD2(0x19, "FBD2"),
        DDR4(0x1A, "DDR4"),
        LPDDR(0x1B, "LPDDR"),
        LPDDR2(0x1C, "LPDDR2"),
        LPDDR3(0x1D, "LPDDR3"),
        LPDDR4(0x1E, "LPDDR4"),
        LOGICAL_NON_VOLATILE_DEVICE(0x1F, "Logical non-volatile device"),
        HBM(0x20, "HBM"),
        HBM2(0x21, "HBM2"),
        DDR5(0x22, "DDR5"),
        LPDDR5(0x23, "LPDDR5");

        private final int value;
        private final String fullName;

        private static final Map<Integer, SmbiosMemoryDevice.Type> ENTRIES = Arrays.stream(
            SmbiosMemoryDevice.Type.values())
            .collect(Collectors.toMap(SmbiosMemoryDevice.Type::getValue, v -> v));

        public static SmbiosMemoryDevice.Type valueFrom(int value) {
            return Optional.ofNullable(ENTRIES.get(value)).orElse(Type.Other);
        }
    }
}
