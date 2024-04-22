package kr.jclab.javautils.systeminformation.model;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import kr.jclab.javautils.systeminformation.smbios.DMIData;
import kr.jclab.javautils.systeminformation.smbios.DmiParsable;
import kr.jclab.javautils.systeminformation.smbios.DmiType;
import kr.jclab.javautils.systeminformation.util.ByteBufferUtil;

@lombok.Getter
@lombok.ToString
public class SmbiosSystem implements SmbiosInformation {
    private final String manufacturer;
    private final String productName;
    private final String version;
    private final String serialNumber;
    private final String skuNumber;
    private final UUID uuid;

    @lombok.Builder
    public SmbiosSystem(String manufacturer, String productName, String version, String serialNumber, String skuNumber,
        UUID uuid) {
        this.manufacturer = manufacturer;
        this.productName = productName;
        this.version = version;
        this.serialNumber = serialNumber;
        this.skuNumber = skuNumber;
        this.uuid = uuid;
    }

    public static class Parser implements DmiParsable<SmbiosSystem> {
        @Override
        public int getDmiType() {
            return DmiType.SYSTEM.getValue();
        }

        @Override
        public SmbiosSystem parse(DMIData data, SmbiosInformation old) {
            final byte[] raw = data.getRaw();

            SmbiosSystem.SmbiosSystemBuilder builder = SmbiosSystem.builder()
                    .manufacturer(Optional.ofNullable(data.getDmiString(raw[0x0])).orElse(""))
                    .productName(Optional.ofNullable(data.getDmiString(raw[0x1])).orElse(""))
                    .version(Optional.ofNullable(data.getDmiString(raw[0x2])).orElse(""))
                    .serialNumber(data.getDmiString(raw[0x3]))
                    .uuid(ByteBufferUtil.createUUIDFromBytes(Arrays.copyOfRange(raw, 0x4, 0x4 + 16)));

            if (raw.length > 0x15) {
                builder.skuNumber(data.getDmiString(raw[0x15]));
            }

            return builder.build();
        }
    }
}
