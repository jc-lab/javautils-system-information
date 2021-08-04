package kr.jclab.javautils.systeminformation.smbios;

import kr.jclab.javautils.systeminformation.model.SmbiosInformation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SmbiosParser {
    private final Map<Integer, DmiParsable<?>> parsers;

    public SmbiosParser(Map<Integer, DmiParsable<?>> parsers) {
        this.parsers = parsers;
    }

    public SmbiosInformation parse(DMIHeader header, DMIData data, SmbiosInformation old) {
        int type = header.getType() & 0xff;
        DmiParsable<?> parser = this.parsers.get(type);
        if (parser == null) {
            return null;
        }
        return parser.parse(data, old);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Map<Integer, DmiParsable<?>> parsers = new HashMap<>();

        public Builder addParser(DmiParsable<?> parser) {
            this.parsers.put(parser.getDmiType(), parser);
            return this;
        }

        public SmbiosParser build() {
            return new SmbiosParser(Collections.unmodifiableMap(this.parsers));
        }
    }
}
