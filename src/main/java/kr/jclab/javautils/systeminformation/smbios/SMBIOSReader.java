package kr.jclab.javautils.systeminformation.smbios;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import kr.jclab.javautils.systeminformation.model.SmbiosInformation;

public class SMBIOSReader {

    private final Map<DmiType, SmbiosInformation> smbiosStore = new HashMap<>();

    @lombok.Setter
    @lombok.Getter
    protected boolean perfect = false;

    public void process(ByteBuffer buffer, int totalLength) throws IOException {
        while (buffer.hasRemaining() && buffer.position() < totalLength) {
            final DMIData data = new DMIData(buffer);
            if (data.isEndMarked()) {
                break;
            }
            final DMIHeader header = data.getHeader();
            if (header.getHandle() != (short)0xffff && header.getLength() > 0) {
                dmiParse(header, data);
            }
        }
    }

    protected void dmiParse(DMIHeader header, DMIData data) {
        DmiType dmiType = DmiType.valueFrom(header.getType());
        if (dmiType != null) {
            smbiosStore.computeIfPresent(dmiType, (k, smbiosInformation) -> {
                SmbiosInformation another = dmiType.parse(data);
                smbiosInformation.addInformation(another);
                return smbiosInformation;
            });
            smbiosStore.computeIfAbsent(dmiType, smbios -> dmiType.parse(data));
        }
    }

    public <T extends SmbiosInformation> T getSmbiosInformation(DmiType dmiType) {
        return (T)this.smbiosStore.get(dmiType);
    }
}
