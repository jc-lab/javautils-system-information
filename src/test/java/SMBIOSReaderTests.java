import kr.jclab.javautils.systeminformation.smbios.SMBIOSReader;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

public class SMBIOSReaderTests {
    @Test
    public void totalInformationsTest() throws Exception {
        SMBIOSReader reader = new SMBIOSReader();

        ByteBuffer buffer = ByteBuffer.wrap(SMBIOSResources.DMISample);
        reader.process(buffer, buffer.remaining());

        assert "Intel Corporation".equals(reader.getBaseboardInformation().getManufacturer());
        assert "440BX Desktop Reference Platform".equals(reader.getBaseboardInformation().getProductName());
        assert "None".equals(reader.getBaseboardInformation().getVersion());
        assert "None".equals(reader.getBaseboardInformation().getSerialNumber());
        assert reader.getBaseboardInformation().getAssetTag() == null;
        assert "Phoenix Technologies LTD".equals(reader.getBiosInformation().getVendor());
        assert "6.00".equals(reader.getBiosInformation().getVersion());
        assert "02/27/2020".equals(reader.getBiosInformation().getDate());
        assert "VMware, Inc.".equals(reader.getSystemInformation().getManufacturer());
        assert "VMware Virtual Platform".equals(reader.getSystemInformation().getProductName());
        assert "None".equals(reader.getSystemInformation().getVersion());
        assert "VMware-56 4d e5 9d 0a 71 05 5a-6f ec e7 09 01 dd 1f 18".equals(reader.getSystemInformation().getSerialNumber());
        assert "9DE54D56-710A-5A05-6FEC-E70901DD1F18".equalsIgnoreCase(reader.getSystemInformation().getUuid().toString());
    }
}
