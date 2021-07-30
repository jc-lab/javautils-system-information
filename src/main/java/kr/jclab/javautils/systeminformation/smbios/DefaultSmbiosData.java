package kr.jclab.javautils.systeminformation.smbios;

import kr.jclab.javautils.systeminformation.model.SmbiosBIOS;
import kr.jclab.javautils.systeminformation.model.SmbiosBaseboard;
import kr.jclab.javautils.systeminformation.model.SmbiosSystem;

public class DefaultSmbiosData implements SmbiosData {
    private SmbiosBaseboard baseboardInformation;
    private SmbiosBIOS biosInformation;
    private SmbiosSystem systemInformation;

    @Override
    public SmbiosBaseboard getBaseboardInformation() {
        return null;
    }

    @Override
    public SmbiosBIOS getBIOSInformation() {
        return null;
    }

    @Override
    public SmbiosSystem getSystemInformation() {
        return null;
    }
}
