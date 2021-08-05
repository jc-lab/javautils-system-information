package kr.jclab.javautils.systeminformation.model;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import kr.jclab.javautils.systeminformation.smbios.DMIData;
import kr.jclab.javautils.systeminformation.smbios.DmiParsable;
import kr.jclab.javautils.systeminformation.smbios.DmiType;

@lombok.Getter
@lombok.ToString
public class SmbiosChassis implements SmbiosInformation {
    private final String manufacturer;
    private final String type;
    private final String lock;
    private final String version;
    private final String serialNumber;
    private final String assetTag;

    @lombok.Builder
    public SmbiosChassis(String manufacturer, String type, String lock, String version, String serialNumber,
        String assetTag) {
        this.manufacturer = manufacturer;
        this.type = type;
        this.lock = lock;
        this.version = version;
        this.serialNumber = serialNumber;
        this.assetTag = assetTag;
    }

    private static String getType(byte value) {
        Type type = Optional.ofNullable(Type.valueFrom(value)).orElse(Type.UNKNOWN);
        return type.getFullName();
    }

    private static String getLock(byte value) {
        Lock lock = Optional.ofNullable(Lock.valueFrom(value)).orElse(Lock.NOT_PRESENT);
        return lock.getFullName();
    }

    public static class Parser implements DmiParsable<SmbiosChassis> {
        @Override
        public int getDmiType() {
            return DmiType.CHASSIS.getValue();
        }

        @Override
        public SmbiosChassis parse(DMIData data, SmbiosInformation old) {
            final byte[] raw = data.getRaw();

            return SmbiosChassis.builder()
                .manufacturer(Optional.ofNullable(data.getDmiString(raw[0x0])).orElse(""))
                .type(getType(raw[0x1]))
                .lock(getLock((byte)(raw[0x1] >> 7)))
                .version(Optional.ofNullable(data.getDmiString(raw[0x2])).orElse(""))
                .serialNumber(Optional.ofNullable(data.getDmiString(raw[0x3])).orElse(""))
                .assetTag(Optional.ofNullable(data.getDmiString(raw[0x4])).orElse(""))
                .build();
        }
    }

    @lombok.Getter
    @lombok.AllArgsConstructor
    public enum Type {
        OTHER(0x1, "Other"),
        UNKNOWN(0x2, "Unknown"),
        DESKTOP(0x3, "Desktop"),
        LOW_PROFILE_DESKTOP(0x4, "Low Profile Desktop"),
        PIZZA_BOX(0x5, "Pizza Box"),
        MINI_TOWER(0x6, "Mini Tower"),
        TOWER(0x7, "Tower"),
        PORTABLE(0x8, "Portable"),
        LAPTOP(0x9, "Laptop"),
        NOTEBOOK(0xA, "Notebook"),
        HAND_HELD(0xB, "Hand Held"),
        DOCKING_STATION(0xC, "Docking Station"),
        ALL_IN_ONE(0xD, "All In One"),
        SUB_NOTEBOOK(0xE, "Sub Notebook"),
        SPACE_SAVING(0xF, "Space-saving"),
        LUNCH_BOX(0x10, "Lunch Box"),
        MAIN_SERVER_CHASSIS(0x11,
            "Main Server Chassis"),
        EXPANSION_CHASSIS(0x12, "Expansion Chassis"),
        SUB_CHASSIS(0x13, "Sub Chassis"),
        BUS_EXPANSION_CHASSIS(0x14, "Bus Expansion Chassis"),
        PERIPHERAL_CHASSIS(0x15, "Peripheral Chassis"),
        RAID_CHASSIS(0x16, "RAID Chassis"),
        RACK_MOUNT_CHASSIS(0x17, "Rack Mount Chassis"),
        SEALED_CASE_PC(0x18, "Sealed-case PC"),
        MULTI_SYSTEM(0x19, "Multi-system"),
        COMPACT_PCI(0x1A, "CompactPCI"),
        ADVANCED_TCA(0x1B, "AdvancedTCA"),
        BLADE(0x1C, "Blade"),
        BLADE_ENCLOSING(0x1D, "Blade Enclosing"),
        TABLET(0x1E, "Tablet"),
        CONVERTIBLE(0x1F, "Convertible"),
        DETACHABLE(0x20, "Detachable"),
        IOT_GATEWAY(0x21, "IoT Gateway"),
        EMBEDDED_PC(0x22, "Embedded PC"),
        MINI_PC(0x23, "Mini PC"),
        STICK_PC(0x24, "Stick PC");

        private final int value;
        private final String fullName;

        private static final Map<Integer, SmbiosChassis.Type> ENTRIES = Arrays.stream(SmbiosChassis.Type.values())
            .collect(Collectors.toMap(SmbiosChassis.Type::getValue, v -> v));

        public static SmbiosChassis.Type valueFrom(int value) {
            return ENTRIES.get(value);
        }
    }

    @lombok.Getter
    @lombok.AllArgsConstructor
    public enum Lock {
        NOT_PRESENT(0x0, "Not Present"),
        PRESENT(0x1, "Present");

        private final int value;
        private final String fullName;

        private static final Map<Integer, SmbiosChassis.Lock> ENTRIES = Arrays.stream(SmbiosChassis.Lock.values())
            .collect(Collectors.toMap(SmbiosChassis.Lock::getValue, v -> v));

        public static SmbiosChassis.Lock valueFrom(int value) {
            return ENTRIES.get(value);
        }
    }
}
