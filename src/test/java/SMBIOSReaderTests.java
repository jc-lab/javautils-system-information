import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import kr.jclab.javautils.systeminformation.model.SmbiosBIOS;
import kr.jclab.javautils.systeminformation.model.SmbiosBaseboard;
import kr.jclab.javautils.systeminformation.model.SmbiosChassis;
import kr.jclab.javautils.systeminformation.model.SmbiosMemoryDevice;
import kr.jclab.javautils.systeminformation.model.SmbiosProcessor;
import kr.jclab.javautils.systeminformation.model.SmbiosSystem;
import kr.jclab.javautils.systeminformation.platform.windows.WindowsSMBIOS;
import kr.jclab.javautils.systeminformation.smbios.DmiType;
import kr.jclab.javautils.systeminformation.smbios.SMBIOSReader;

public class SMBIOSReaderTests {

    static SMBIOSReader linuxSampleReader;
    static SMBIOSReader windowsSampleReader;

    @BeforeAll
    static void setup() throws IOException {
        /* LINUX */
        linuxSampleReader = new SMBIOSReader();

        ByteBuffer buffer = ByteBuffer
            .wrap(SMBIOSResources.DMI_SAMPLES.get(0).getValue())
            .order(ByteOrder.LITTLE_ENDIAN);
        linuxSampleReader.process(buffer, buffer.remaining());

        /* WINDOWS */
        windowsSampleReader = new SMBIOSReader();

        buffer = ByteBuffer
            .wrap(SMBIOSResources.WINDOWS_DMI_SAMPLES.get(0).getValue())
            .order(ByteOrder.LITTLE_ENDIAN);
        WindowsSMBIOS.RawSMBIOSData rawSMBIOSData = new WindowsSMBIOS.RawSMBIOSData(buffer);
        windowsSampleReader.process(buffer, buffer.remaining());
    }

    @Test
    public void samplesReadTest() throws IOException {
        for (Map.Entry<String, byte[]> item : SMBIOSResources.DMI_SAMPLES) {
            SMBIOSReader reader = new SMBIOSReader();

            System.out.println("FILE: " + item.getKey());

            ByteBuffer buffer = ByteBuffer
                    .wrap(item.getValue())
                    .order(ByteOrder.LITTLE_ENDIAN);
            reader.process(buffer, buffer.remaining());
        }
    }

    @Test
    public void getSmbiosInformation_withBaseboardOfDmiType() {
        SmbiosBaseboard smbiosBaseboard = windowsSampleReader.getSmbiosInformation(DmiType.BASEBOARD);

        assert "MSI".equals(smbiosBaseboard.getManufacturer());
        assert "X99A RAIDER (MS-7885)".equals(smbiosBaseboard.getProductName());
        assert "5.0".equals(smbiosBaseboard.getVersion());
        assert "To be filled by O.E.M.".equals(smbiosBaseboard.getSerialNumber());
        assert "To be filled by O.E.M.".equalsIgnoreCase(smbiosBaseboard.getAssetTag());
    }

    @Test
    public void getSmbiosInformation_withBiosOfDmiType() {
        SmbiosBIOS smbiosBIOS =  windowsSampleReader.getSmbiosInformation(DmiType.BIOS);

        assert "American Megatrends Inc.".equals(smbiosBIOS.getVendor());
        assert "P.30".equals(smbiosBIOS.getVersion());
        assert "04/12/2016".equals(smbiosBIOS.getDate());
    }

    @Test
    public void getSmbiosInformation_withSystemOfDmiType() {
        SmbiosSystem smbiosSystem =  windowsSampleReader.getSmbiosInformation(DmiType.SYSTEM);

        assert "MSI".equals(smbiosSystem.getManufacturer());
        assert "MS-7885".equals(smbiosSystem.getProductName());
        assert "5.0".equals(smbiosSystem.getVersion());
        assert "Default string".equals(smbiosSystem.getSerialNumber());
        assert "Default string".equals(smbiosSystem.getSkuNumber());
        assert "aaaaaaaa-aaaa-aaaa-aaaa-4ccc6a25cded".equalsIgnoreCase(smbiosSystem.getUuid().toString());
    }

    @Test
    public void getSmbiosInformation_withChassisOfDmiType() {
        SmbiosChassis smbiosChassis =  windowsSampleReader.getSmbiosInformation(DmiType.CHASSIS);

        assert "MSI".equals(smbiosChassis.getManufacturer());
        assert "Desktop".equals(smbiosChassis.getType());
        assert "Not Present".equals(smbiosChassis.getLock());
        assert "5.0".equals(smbiosChassis.getVersion());
        assert "Default string".equals(smbiosChassis.getSerialNumber());
        assert "Default string".equals(smbiosChassis.getAssetTag());
    }

    @Test
    public void getSmbiosInformation_withProcessorOfDmiType() {
        SmbiosProcessor smbiosProcessors = windowsSampleReader.getSmbiosInformation(DmiType.PROCESSOR);

        for (SmbiosProcessor.Processor processor : smbiosProcessors.getProcessors()) {
            if (processor.getSocketDesignation().equalsIgnoreCase("SOCKET 0")) {
                assert "F2060300FFFBEBBF".equalsIgnoreCase(processor.getId());
                assert "Central Processor".equalsIgnoreCase(processor.getType());
                assert "b3".equals(processor.getFamily());
                assert "Intel".equalsIgnoreCase(processor.getManufacturer());
                assert "Intel(R) Core(TM) i7-5960X CPU @ 3.00GHz".equalsIgnoreCase(processor.getVersion());
            }
        }
    }

    @Test
    public void getSmbiosInformation_withMemoryDeviceOfDmiType() {
        SmbiosMemoryDevice smbiosMemoryDevices = windowsSampleReader.getSmbiosInformation(DmiType.MEMORY_DEVICE);

        assert 8 == smbiosMemoryDevices.getMemoryDevices().size();

        for (SmbiosMemoryDevice.MemoryDevice memoryDevice : smbiosMemoryDevices.getMemoryDevices()) {
            if (memoryDevice.getDeviceLocator().equalsIgnoreCase("DIMM_A1")) {
                assert "NODE 1".equalsIgnoreCase(memoryDevice.getBankLocator());
                assert 9 == memoryDevice.getFormFactor();
                assert 8589934592L == memoryDevice.getSize();
                assert "Samsung".equalsIgnoreCase(memoryDevice.getManufacturer());
                assert "9246D640".equalsIgnoreCase(memoryDevice.getSerialNumber());
                assert "M378A1G43EB1-CPB".equalsIgnoreCase(memoryDevice.getPartNumber());
                assert "DIMM_A1_AssetTag".equalsIgnoreCase(memoryDevice.getAssetTag());
                assert "DDR4".equalsIgnoreCase(memoryDevice.getMemoryType());
                assert 2133 == memoryDevice.getSpeed();
            }
        }
    }
}
