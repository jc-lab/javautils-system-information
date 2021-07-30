import kr.jclab.javautils.systeminformation.model.SmbiosBIOS;
import kr.jclab.javautils.systeminformation.model.SmbiosBaseboard;
import kr.jclab.javautils.systeminformation.model.SmbiosSystem;
import kr.jclab.javautils.systeminformation.platform.windows.WindowsSMBIOS;
import kr.jclab.javautils.systeminformation.smbios.DmiType;
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

        SmbiosBaseboard smbiosBaseboard = (SmbiosBaseboard)reader.getSmbiosStore().get(DmiType.BASEBOARD);
        SmbiosBIOS smbiosBIOS = (SmbiosBIOS)reader.getSmbiosStore().get(DmiType.BIOS);
        SmbiosSystem smbiosSystem = (SmbiosSystem)reader.getSmbiosStore().get(DmiType.SYSTEM);

        assert "Intel Corporation".equals(smbiosBaseboard.getManufacturer());
        assert "440BX Desktop Reference Platform".equals(smbiosBaseboard.getProductName());
        assert "None".equals(smbiosBaseboard.getVersion());
        assert "None".equals(smbiosBaseboard.getSerialNumber());
        assert "".equalsIgnoreCase(smbiosBaseboard.getAssetTag());
        assert "Phoenix Technologies LTD".equals(smbiosBIOS.getVendor());
        assert "6.00".equals(smbiosBIOS.getVersion());
        assert "02/27/2020".equals(smbiosBIOS.getDate());
        assert "VMware, Inc.".equals(smbiosSystem.getManufacturer());
        assert "VMware Virtual Platform".equals(smbiosSystem.getProductName());
        assert "None".equals(smbiosSystem.getVersion());
        assert "VMware-56 4d e5 9d 0a 71 05 5a-6f ec e7 09 01 dd 1f 18".equals(smbiosSystem.getSerialNumber());
        assert "9DE54D56-710A-5A05-6FEC-E70901DD1F18".equalsIgnoreCase(smbiosSystem.getUuid().toString());
    }

    @Test
    public void smbiosDecodeTestWindowsSample01() throws Exception {
        SMBIOSReader reader = new SMBIOSReader();

        ByteBuffer buffer = ByteBuffer
                .wrap(SMBIOSResources.WINDOWS_DMI_SAMPLES.get(0).getKey())
                .order(ByteOrder.LITTLE_ENDIAN);
        WindowsSMBIOS.RawSMBIOSData rawSMBIOSData = new WindowsSMBIOS.RawSMBIOSData(buffer);
        reader.process(buffer, buffer.remaining());

        SmbiosBaseboard smbiosBaseboard = (SmbiosBaseboard)reader.getSmbiosStore().get(DmiType.BASEBOARD);
        SmbiosBIOS smbiosBIOS = (SmbiosBIOS)reader.getSmbiosStore().get(DmiType.BIOS);
        SmbiosSystem smbiosSystem = (SmbiosSystem)reader.getSmbiosStore().get(DmiType.SYSTEM);

        assert "MSI".equals(smbiosBaseboard.getManufacturer());
        assert "X99A RAIDER (MS-7885)".equals(smbiosBaseboard.getProductName());
        assert "5.0".equals(smbiosBaseboard.getVersion());
        assert "To be filled by O.E.M.".equals(smbiosBaseboard.getSerialNumber());
        assert "To be filled by O.E.M.".equalsIgnoreCase(smbiosBaseboard.getAssetTag());
        assert "American Megatrends Inc.".equals(smbiosBIOS.getVendor());
        assert "P.30".equals(smbiosBIOS.getVersion());
        assert "04/12/2016".equals(smbiosBIOS.getDate());
        assert "MSI".equals(smbiosSystem.getManufacturer());
        assert "MS-7885".equals(smbiosSystem.getProductName());
        assert "5.0".equals(smbiosSystem.getVersion());
        assert "Default string".equals(smbiosSystem.getSerialNumber());
        assert "Default string".equals(smbiosSystem.getSkuNumber());
        assert "aaaaaaaa-aaaa-aaaa-aaaa-4ccc6a25cded".equalsIgnoreCase(smbiosSystem.getUuid().toString());
    }
}
