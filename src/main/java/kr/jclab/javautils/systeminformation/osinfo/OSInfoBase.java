package kr.jclab.javautils.systeminformation.osinfo;

import kr.jclab.javautils.systeminformation.model.OSInformation;

import java.io.IOException;

public interface OSInfoBase {
    OSInformation read() throws IOException;
    boolean isRealOsArchIs64Bit();
}
