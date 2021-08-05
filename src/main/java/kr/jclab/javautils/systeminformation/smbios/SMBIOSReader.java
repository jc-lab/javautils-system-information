package kr.jclab.javautils.systeminformation.smbios;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import kr.jclab.javautils.systeminformation.model.*;

public class SMBIOSReader {
    private final SmbiosParser smbiosParser;
    private final Map<Integer, SmbiosInformation> smbiosStore = new HashMap<>();

    @lombok.Setter
    @lombok.Getter
    protected boolean perfect = false;

    public SMBIOSReader(SmbiosParser smbiosParser) {
        this.smbiosParser = smbiosParser;
    }

    public SMBIOSReader() {
        this(StaticHolder.DEFAULT_PARSER);
    }

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
        this.smbiosStore.compute(header.getType() & 0xff, (k, old) -> this.smbiosParser.parse(header, data, old));
    }

    public <T extends SmbiosInformation> T getSmbiosInformation(Integer dmiType) {
        return (T)this.smbiosStore.get(dmiType);
    }

    public <T extends SmbiosInformation> T getSmbiosInformation(DmiType dmiType) {
        return this.getSmbiosInformation(dmiType.getValue());
    }

    private static class StaticHolder {
        public static SmbiosParser DEFAULT_PARSER;
        static {
            DEFAULT_PARSER = SmbiosParser.builder()
                    .addParser(new SmbiosBIOS.Parser())
                    .addParser(new SmbiosBaseboard.Parser())
                    .addParser(new SmbiosSystem.Parser())
                    .addParser(new SmbiosMemoryDevice.Parser())
                    .addParser(new SmbiosProcessor.Parser())
                    .addParser(new SmbiosChassis.Parser())
                    .build();
        }
    }
}
