import kr.jclab.javautils.systeminformation.platform.windows.WindowsSMBIOS;
import kr.jclab.javautils.systeminformation.smbios.SMBIOSReader;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class SMBIOSReaderTests {
    @Test
    public void smbiosDecodeTestSample01() throws Exception {
        SMBIOSReader reader = new SMBIOSReader();

        ByteBuffer buffer = ByteBuffer
                .wrap(SMBIOSResources.DMI_SAMPLES.get(0).getKey())
                .order(ByteOrder.LITTLE_ENDIAN);
        reader.process(buffer, buffer.remaining());

        assert "Intel Corporation".equals(reader.getBaseboardInformation().getManufacturer());
        assert "440BX Desktop Reference Platform".equals(reader.getBaseboardInformation().getProductName());
        assert "None".equals(reader.getBaseboardInformation().getVersion());
        assert "None".equals(reader.getBaseboardInformation().getSerialNumber());
        assert "".equalsIgnoreCase(reader.getBaseboardInformation().getAssetTag());
        assert "Phoenix Technologies LTD".equals(reader.getBiosInformation().getVendor());
        assert "6.00".equals(reader.getBiosInformation().getVersion());
        assert "02/27/2020".equals(reader.getBiosInformation().getDate());
        assert "VMware, Inc.".equals(reader.getSystemInformation().getManufacturer());
        assert "VMware Virtual Platform".equals(reader.getSystemInformation().getProductName());
        assert "None".equals(reader.getSystemInformation().getVersion());
        assert "VMware-56 4d e5 9d 0a 71 05 5a-6f ec e7 09 01 dd 1f 18".equals(reader.getSystemInformation().getSerialNumber());
        assert "9DE54D56-710A-5A05-6FEC-E70901DD1F18".equalsIgnoreCase(reader.getSystemInformation().getUuid().toString());
    }

    @Test
    public void smbiosDecodeTestWindowsSample01() throws Exception {
        SMBIOSReader reader = new SMBIOSReader();

        ByteBuffer buffer = ByteBuffer
                .wrap(SMBIOSResources.WINDOWS_DMI_SAMPLES.get(0).getKey())
                .order(ByteOrder.LITTLE_ENDIAN);
        WindowsSMBIOS.RawSMBIOSData rawSMBIOSData = new WindowsSMBIOS.RawSMBIOSData(buffer);
        reader.process(buffer, buffer.remaining());

        assert "MSI".equals(reader.getBaseboardInformation().getManufacturer());
        assert "X99A RAIDER (MS-7885)".equals(reader.getBaseboardInformation().getProductName());
        assert "5.0".equals(reader.getBaseboardInformation().getVersion());
        assert "To be filled by O.E.M.".equals(reader.getBaseboardInformation().getSerialNumber());
        assert "To be filled by O.E.M.".equalsIgnoreCase(reader.getBaseboardInformation().getAssetTag());
        assert "American Megatrends Inc.".equals(reader.getBiosInformation().getVendor());
        assert "P.30".equals(reader.getBiosInformation().getVersion());
        assert "04/12/2016".equals(reader.getBiosInformation().getDate());
        assert "MSI".equals(reader.getSystemInformation().getManufacturer());
        assert "MS-7885".equals(reader.getSystemInformation().getProductName());
        assert "5.0".equals(reader.getSystemInformation().getVersion());
        assert "Default string".equals(reader.getSystemInformation().getSerialNumber());
        assert "Default string".equals(reader.getSystemInformation().getSkuNumber());
        assert "aaaaaaaa-aaaa-aaaa-aaaa-4ccc6a25cded".equalsIgnoreCase(reader.getSystemInformation().getUuid().toString());
    }
}
