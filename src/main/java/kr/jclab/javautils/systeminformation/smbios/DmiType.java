package kr.jclab.javautils.systeminformation.smbios;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DmiType {
    BIOS(0),
    SYSTEM(1),
    BASEBOARD(2),
    CHASSIS(3),
    PROCESSOR(4),
    MEMORY_CONTROLLER(5),
    MEMORY_MODULE(6),
    CACHE(7),
    PORT_CONNECTOR(8),
    MEMORY_DEVICE(17);

    private static final Map<Integer, DmiType> ENTRIES = Arrays.stream(DmiType.values())
        .collect(Collectors.toMap(DmiType::getValue, v -> v));

    private final int value;

    public static DmiType valueFrom(int value) {
        return ENTRIES.get(value);
    }
}
