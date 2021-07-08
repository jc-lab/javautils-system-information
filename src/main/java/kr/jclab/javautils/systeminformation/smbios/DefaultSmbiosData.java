package kr.jclab.javautils.systeminformation.smbios;

import kr.jclab.javautils.systeminformation.model.SmbiosBIOSInformation;
import kr.jclab.javautils.systeminformation.model.SmbiosBaseboardInformation;
import kr.jclab.javautils.systeminformation.model.SmbiosSystemInformation;

public class DefaultSmbiosData implements SmbiosData {
    private SmbiosBaseboardInformation baseboardInformation;
    private SmbiosBIOSInformation biosInformation;
    private SmbiosSystemInformation systemInformation;

    @Override
    public SmbiosBaseboardInformation getBaseboardInformation() {
        return null;
    }

    @Override
    public SmbiosBIOSInformation getBIOSInformation() {
        return null;
    }

    @Override
    public SmbiosSystemInformation getSystemInformation() {
        return null;
    }
}
