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

@lombok.Getter
@lombok.ToString
public class SmbiosProcessor implements SmbiosInformation {
    private final List<Processor> processors = new ArrayList<>();

    public static class Parser implements DmiParsable<SmbiosProcessor> {
        @Override
        public int getDmiType() {
            return DmiType.PROCESSOR.getValue();
        }

        @Override
        public SmbiosProcessor parse(DMIData data, SmbiosInformation old) {
            final byte[] raw = data.getRaw();
            SmbiosProcessor object = (old == null) ? new SmbiosProcessor() : (SmbiosProcessor)old;
            object.processors.add(SmbiosProcessor.Processor.builder()
                    .socketDesignation(Optional.ofNullable(data.getDmiString(raw[0x0])).orElse(""))
                    .id(getId(raw))
                    .type(getType(raw[1]))
                    .family(String.format("%02x", raw[2]))
                    .manufacturer(Optional.ofNullable(data.getDmiString(raw[0x3])).orElse(""))
                    .version(Optional.ofNullable(data.getDmiString(raw[0x1])).orElse(""))
                    .build());
            return object;
        }
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

    @lombok.ToString
    @lombok.Getter
    @lombok.Builder
    @lombok.AllArgsConstructor
    public static class Processor {
        private final String socketDesignation;
        private final String id;
        private final String type;
        private final String family;
        private final String manufacturer;
        private final String version;
    }

    @lombok.Getter
    @lombok.AllArgsConstructor
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
