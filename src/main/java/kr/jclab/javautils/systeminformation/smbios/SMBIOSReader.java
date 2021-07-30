package kr.jclab.javautils.systeminformation.smbios;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.jclab.javautils.systeminformation.model.SmbiosInformation;

@lombok.Getter
@lombok.Setter
public class SMBIOSReader {
    protected boolean perfect = false;
    protected Map<DmiType, List<SmbiosInformation>> smbiosStore = new HashMap<>();

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
        if (dmiType == null) {
            // System.err.println(header.getType() + " is not supported");
        } else {
            smbiosStore.computeIfAbsent(dmiType, smbios -> {
                List<SmbiosInformation> smbiosInformationList = new ArrayList<>();
                smbiosInformationList.add(dmiType.parse(data));
                return smbiosInformationList;
            });
            smbiosStore.computeIfPresent(dmiType, (k, smbiosInformationList) -> {
                smbiosInformationList.add(dmiType.parse(data));
                return smbiosInformationList;
            });
        }
    }
}
