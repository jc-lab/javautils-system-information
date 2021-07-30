package kr.jclab.javautils.systeminformation.model;

import java.util.Optional;

import kr.jclab.javautils.systeminformation.smbios.DMIData;

@lombok.Getter
@lombok.ToString
public class SmbiosBIOS implements SmbiosInformation {
    private final String vendor;
    private final String version;
    private final String date;

    @lombok.Builder
    public SmbiosBIOS(String vendor, String version, String date) {
        this.vendor = vendor;
        this.version = version;
        this.date = date;
    }

    public static SmbiosInformation parse(DMIData data) {
        final byte[] raw = data.getRaw();

        return SmbiosBIOS.builder()
            .vendor(Optional.ofNullable(data.getDmiString(raw[0x0])).orElse(""))
            .version(Optional.ofNullable(data.getDmiString(raw[0x1])).orElse(""))
            .date(Optional.ofNullable(data.getDmiString(raw[0x4])).orElse(""))
            .build();
    }
}
