package kr.jclab.javautils.systeminformation.model;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import kr.jclab.javautils.systeminformation.smbios.DMIData;

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

    public static SmbiosInformation parse(DMIData data) {
        final byte[] raw = data.getRaw();

        return SmbiosSystem.builder()
            .manufacturer(Optional.ofNullable(data.getDmiString(raw[0x0])).orElse(""))
            .productName(Optional.ofNullable(data.getDmiString(raw[0x1])).orElse(""))
            .version(Optional.ofNullable(data.getDmiString(raw[0x2])).orElse(""))
            .serialNumber(data.getDmiString(raw[0x3]))
            .uuid(createUUIDFromBytes(Arrays.copyOfRange(raw, 0x4, 0x4 + 16)))
            .skuNumber(data.getDmiString(raw[0x15]))
            .build();
    }

    private static UUID createUUIDFromBytes(byte[] data) {
        long msb = 0;
        long lsb = 0;
        msb |= ((long)(data[0] & 0xff)) << 32L;
        msb |= ((long)(data[1] & 0xff)) << 40L;
        msb |= ((long)(data[2] & 0xff)) << 48L;
        msb |= ((long)(data[3] & 0xff)) << 56L;
        msb |= ((long)(data[4] & 0xff)) << 16L;
        msb |= ((long)(data[5] & 0xff)) << 24L;
        msb |= data[6] & 0xff;
        msb |= ((long)(data[7] & 0xff)) << 8L;
        for (int i = 8; i < 16; i++)
            lsb = (lsb << 8) | (data[i] & 0xff);
        return new UUID(msb, lsb);
    }
}
