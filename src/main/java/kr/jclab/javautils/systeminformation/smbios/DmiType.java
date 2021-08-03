package kr.jclab.javautils.systeminformation.smbios;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import kr.jclab.javautils.systeminformation.model.SmbiosBIOS;
import kr.jclab.javautils.systeminformation.model.SmbiosBaseboard;
import kr.jclab.javautils.systeminformation.model.SmbiosInformation;
import kr.jclab.javautils.systeminformation.model.SmbiosMemoryDevice;
import kr.jclab.javautils.systeminformation.model.SmbiosProcessor;
import kr.jclab.javautils.systeminformation.model.SmbiosSystem;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DmiType implements DmiParsable {
    BIOS(0) {
        @Override
        public SmbiosInformation parse(DMIData data) {
            return SmbiosBIOS.parse(data);
        }
    },
    SYSTEM(1) {
        @Override
        public SmbiosInformation parse(DMIData data) {
            return SmbiosSystem.parse(data);
        }
    },
    BASEBOARD(2) {
        @Override
        public SmbiosInformation parse(DMIData data) {
            return SmbiosBaseboard.parse(data);
        }
    },
    CHASSIS(3) {
        @Override
        public SmbiosInformation parse(DMIData data) {
            return null;
        }
    },
    PROCESSOR(4) {
        @Override
        public SmbiosInformation parse(DMIData data) {
            return SmbiosProcessor.parse(data);
        }
    },
    MEMORY_CONTROLLER(5) {
        @Override
        public SmbiosInformation parse(DMIData data) {
            return null;
        }
    },
    MEMORY_MODULE(6) {
        @Override
        public SmbiosInformation parse(DMIData data) {
            return null;
        }
    },
    CACHE(7) {
        @Override
        public SmbiosInformation parse(DMIData data) {
            return null;
        }
    },
    PORT_CONNECTOR(8) {
        @Override
        public SmbiosInformation parse(DMIData data) {
            return null;
        }
    },
    MEMORY_DEVICE(17) {
        @Override
        public SmbiosInformation parse(DMIData data) {
            return SmbiosMemoryDevice.parse(data);
        }
    };

    private static final Map<Integer, DmiType> ENTRIES = Arrays.stream(DmiType.values())
        .collect(Collectors.toMap(DmiType::getValue, v -> v));

    private final int value;

    public static DmiType valueFrom(int value) {
        return ENTRIES.get(value);
    }
}
