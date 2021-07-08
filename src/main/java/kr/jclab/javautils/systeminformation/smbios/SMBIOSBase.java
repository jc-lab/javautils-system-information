package kr.jclab.javautils.systeminformation.smbios;

import kr.jclab.javautils.systeminformation.exception.NativeApiErrorException;

import java.io.IOException;

public interface SMBIOSBase {
    void read(SMBIOSReader reader) throws NativeApiErrorException, IOException;
}
