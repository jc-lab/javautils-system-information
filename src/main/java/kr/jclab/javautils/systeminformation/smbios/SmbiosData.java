package kr.jclab.javautils.systeminformation.smbios;

import kr.jclab.javautils.systeminformation.model.SmbiosBIOSInformation;
import kr.jclab.javautils.systeminformation.model.SmbiosBaseboardInformation;
import kr.jclab.javautils.systeminformation.model.SmbiosSystemInformation;

public interface SmbiosData {
    SmbiosBaseboardInformation getBaseboardInformation();
    SmbiosBIOSInformation getBIOSInformation();
    SmbiosSystemInformation getSystemInformation();
}
