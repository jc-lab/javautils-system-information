package kr.jclab.javautils.systeminformation.model;

import java.util.Optional;

import kr.jclab.javautils.systeminformation.smbios.DMIData;
import kr.jclab.javautils.systeminformation.smbios.DmiParsable;
import kr.jclab.javautils.systeminformation.smbios.DmiType;

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

    public static class Parser implements DmiParsable<SmbiosBIOS> {
        @Override
        public int getDmiType() {
            return DmiType.BIOS.getValue();
        }

        @Override
        public SmbiosBIOS parse(DMIData data, SmbiosInformation old) {
            final byte[] raw = data.getRaw();

            SmbiosBIOS.SmbiosBIOSBuilder builder = SmbiosBIOS.builder();
            if (raw.length >= 1) {
                builder.vendor(Optional.ofNullable(data.getDmiString(raw[0x0])).orElse(""));
            }
            if (raw.length >= 2) {
                builder.version(Optional.ofNullable(data.getDmiString(raw[0x1])).orElse(""));
            }
            if (raw.length >= 5) {
                builder.date(Optional.ofNullable(data.getDmiString(raw[0x4])).orElse(""));
            }
            return builder.build();
        }
    }
}
