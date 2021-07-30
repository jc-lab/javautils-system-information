package kr.jclab.javautils.systeminformation.model;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import kr.jclab.javautils.systeminformation.smbios.DMIData;
import lombok.AllArgsConstructor;
import lombok.Getter;

@lombok.Getter
@lombok.ToString
@lombok.Builder
@lombok.AllArgsConstructor
public class SmbiosProcessor implements SmbiosInformation {
    private final String socketDesignation;
    private final String id;
    private final String type;
    private final String family;
    private final String manufacturer;
    private final String version;

    public static SmbiosInformation parse(DMIData data) {
        final byte[] raw = data.getRaw();

        return SmbiosProcessor.builder()
            .socketDesignation(Optional.ofNullable(data.getDmiString(raw[0x0])).orElse(""))
            .id(getId(raw))
            .type(getType(raw[1]))
            .family(String.format("%02x", raw[2]))
            .manufacturer(Optional.ofNullable(data.getDmiString(raw[0x3])).orElse(""))
            .version(Optional.ofNullable(data.getDmiString(raw[0x1])).orElse(""))
            .build();
    }

    private static String getType(byte value) {
        Type type = Optional.ofNullable(Type.valueFrom(value)).orElse(Type.UNKNOWN);
        return type.getFullName();
    }

    private static String getId(byte[] raw) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 4; i < 12; i++) {
            stringBuffer.append(String.format("%02x", raw[i]).toUpperCase());
        }

        return stringBuffer.toString();
    }

    @Getter
    @AllArgsConstructor
    public enum Type {
        OTHER(0x1, "Other"),
        UNKNOWN(0x2, "Unknown"),
        CENTRAL_PROCESSOR(0x3, "Central Processor"),
        MATH_PROCESSOR(0x4, "Math Processor"),
        DSP_PROCESSOR(0x5, "DSP Processor"),
        VIDEO_PROCESSOR(0x6, "Video Processor");

        private final int value;
        private final String fullName;

        private static final Map<Integer, Type> ENTRIES = Arrays.stream(Type.values())
            .collect(Collectors.toMap(Type::getValue, v -> v));

        public static Type valueFrom(int value) {
            return ENTRIES.get(value);
        }
    }
}