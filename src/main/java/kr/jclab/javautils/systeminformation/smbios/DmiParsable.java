package kr.jclab.javautils.systeminformation.smbios;

import kr.jclab.javautils.systeminformation.model.SmbiosInformation;

public interface DmiParsable {

    SmbiosInformation parse(DMIData data);
}
