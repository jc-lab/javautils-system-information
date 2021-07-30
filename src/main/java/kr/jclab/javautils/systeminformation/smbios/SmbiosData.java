package kr.jclab.javautils.systeminformation.smbios;

import kr.jclab.javautils.systeminformation.model.SmbiosBIOS;
import kr.jclab.javautils.systeminformation.model.SmbiosBaseboard;
import kr.jclab.javautils.systeminformation.model.SmbiosSystem;

public interface SmbiosData {
    SmbiosBaseboard getBaseboardInformation();
    SmbiosBIOS getBIOSInformation();
    SmbiosSystem getSystemInformation();
}
