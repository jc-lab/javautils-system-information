package kr.jclab.javautils.systeminformation.model;

import java.util.Optional;

import kr.jclab.javautils.systeminformation.smbios.DMIData;
import kr.jclab.javautils.systeminformation.smbios.DmiParsable;
import kr.jclab.javautils.systeminformation.smbios.DmiType;

@lombok.Getter
@lombok.ToString
public class SmbiosBaseboard implements SmbiosInformation {
    private final String manufacturer;
    private final String productName;
    private final String version;
    private final String serialNumber;
    private final String assetTag;

    @lombok.Builder
    public SmbiosBaseboard(String manufacturer, String productName, String version, String serialNumber,
        String assetTag) {
        this.manufacturer = manufacturer;
        this.productName = productName;
        this.version = version;
        this.serialNumber = serialNumber;
        this.assetTag = assetTag;
    }

    public static class Parser implements DmiParsable<SmbiosBaseboard> {
        @Override
        public int getDmiType() {
            return DmiType.BASEBOARD.getValue();
        }

        @Override
        public SmbiosBaseboard parse(DMIData data, SmbiosInformation old) {
            final byte[] raw = data.getRaw();

            SmbiosBaseboard.SmbiosBaseboardBuilder builder = SmbiosBaseboard.builder();
            if (raw.length >= 1) {
                builder.manufacturer(Optional.ofNullable(data.getDmiString(raw[0x0])).orElse(""));
            }
            if (raw.length >= 2) {
                builder.productName(Optional.ofNullable(data.getDmiString(raw[0x1])).orElse(""));
            }
            if (raw.length >= 3) {
                builder.version(Optional.ofNullable(data.getDmiString(raw[0x2])).orElse(""));
            }
            if (raw.length >= 4) {
                builder.serialNumber(Optional.ofNullable(data.getDmiString(raw[0x3])).orElse(""));
            }
            if (raw.length >= 5) {
                builder.assetTag(Optional.ofNullable(data.getDmiString(raw[0x4])).orElse(""));
            }

            return builder.build();
        }
    }
}
