package kr.jclab.javautils.systeminformation.smbios;

import kr.jclab.javautils.systeminformation.model.SmbiosBIOSInformation;
import kr.jclab.javautils.systeminformation.model.SmbiosBaseboardInformation;
import kr.jclab.javautils.systeminformation.model.SmbiosSystemInformation;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@lombok.Getter
@lombok.Setter
public class SMBIOSReader {
    protected SmbiosBIOSInformation biosInformation = null;
    protected SmbiosBaseboardInformation baseboardInformation = null;
    protected SmbiosSystemInformation systemInformation = null;
    protected boolean perfect = false;

    public SMBIOSReader() {
    }

    protected void dmiParse(DMIHeader header, DMIData data) {
        final byte[] raw = data.getRaw();
        switch (header.getType()) {
            case 0: // 00 BIOS Information
                this.biosInformation = SmbiosBIOSInformation.builder()
                        .vendor(Optional.ofNullable(data.getDmiString(raw[0x0])).orElse(""))
                        .version(Optional.ofNullable(data.getDmiString(raw[0x1])).orElse(""))
                        .date(Optional.ofNullable(data.getDmiString(raw[0x4])).orElse(""))
                        .build();
                break;
            case 1: // 01 BIOS Information
                this.systemInformation = SmbiosSystemInformation.builder()
                        .manufacturer(Optional.ofNullable(data.getDmiString(raw[0x0])).orElse(""))
                        .productName(Optional.ofNullable(data.getDmiString(raw[0x1])).orElse(""))
                        .version(Optional.ofNullable(data.getDmiString(raw[0x2])).orElse(""))
                        .serialNumber(data.getDmiString(raw[0x3]))
                        .uuid(createUUIDFromBytes(Arrays.copyOfRange(raw, 0x4, 0x4 + 16)))
                        .skuNumber(data.getDmiString(raw[0x15]))
                        .build();
                break;
            case 2:
                this.baseboardInformation = SmbiosBaseboardInformation.builder()
                        .manufacturer(Optional.ofNullable(data.getDmiString(raw[0x0])).orElse(""))
                        .productName(Optional.ofNullable(data.getDmiString(raw[0x1])).orElse(""))
                        .version(Optional.ofNullable(data.getDmiString(raw[0x2])).orElse(""))
                        .serialNumber(Optional.ofNullable(data.getDmiString(raw[0x3])).orElse(""))
                        .assetTag(Optional.ofNullable(data.getDmiString(raw[0x4])).orElse(""))
                        .build();
                break;
        }
    }

    public void process(ByteBuffer buffer, int totalLength) throws IOException {
        while (buffer.hasRemaining() && buffer.position() < totalLength) {
            final DMIData data = new DMIData(buffer);
            final DMIHeader header = data.getHeader();
            if (header.getHandle() != (short)0xffff && header.getLength() > 0) {
                dmiParse(header, data);
            }
        }
    }

    private UUID createUUIDFromBytes(byte[] data) {
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
