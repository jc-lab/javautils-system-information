package kr.jclab.javautils.systeminformation.model;

import java.util.Optional;

import kr.jclab.javautils.systeminformation.smbios.DMIData;

@lombok.Getter
@lombok.ToString
public class SmbiosBaseboard extends SmbiosInformation {
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

    public static SmbiosInformation parse(DMIData data) {
        final byte[] raw = data.getRaw();

        return SmbiosBaseboard.builder()
            .manufacturer(Optional.ofNullable(data.getDmiString(raw[0x0])).orElse(""))
            .productName(Optional.ofNullable(data.getDmiString(raw[0x1])).orElse(""))
            .version(Optional.ofNullable(data.getDmiString(raw[0x2])).orElse(""))
            .serialNumber(Optional.ofNullable(data.getDmiString(raw[0x3])).orElse(""))
            .assetTag(Optional.ofNullable(data.getDmiString(raw[0x4])).orElse(""))
            .build();
    }
}
