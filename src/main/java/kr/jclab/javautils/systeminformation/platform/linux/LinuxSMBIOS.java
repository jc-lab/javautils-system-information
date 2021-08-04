package kr.jclab.javautils.systeminformation.platform.linux;

import kr.jclab.javautils.systeminformation.exception.NativeApiErrorException;
import kr.jclab.javautils.systeminformation.model.SmbiosBIOS;
import kr.jclab.javautils.systeminformation.model.SmbiosBaseboard;
import kr.jclab.javautils.systeminformation.model.SmbiosSystem;
import kr.jclab.javautils.systeminformation.smbios.DmiType;
import kr.jclab.javautils.systeminformation.smbios.SMBIOSBase;
import kr.jclab.javautils.systeminformation.smbios.SMBIOSReader;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.UUID;

public class LinuxSMBIOS implements SMBIOSBase {
    @Override
    public void read(SMBIOSReader reader) throws NativeApiErrorException, IOException {
        FirmwareReader firmwareReader = new FirmwareReader();
        ProcfsReader procfsReader = new ProcfsReader();
        if (firmwareReader.load(reader)) {
            reader.setPerfect(true);
        } else {
            if (procfsReader.load()) {
                reader.setPerfect(false);
            } else {
                throw new NativeApiErrorException("Permission denied");
            }
        }
    }

    private static class ProcfsReader {
        private int dirtyCount = 0;
        private SmbiosBIOS biosInformation = null;
        private SmbiosSystem systemInformation = null;
        private SmbiosBaseboard baseboardInformation = null;

        public int getDirtyCount() {
            return this.dirtyCount;
        }

        private String simpleReadFile(File file) {
            try (FileInputStream fis = new FileInputStream(file)) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
                return reader.readLine();
            } catch (IOException e) {
                dirtyCount++;
                return null;
            }
        }

        private UUID readUuidFile(File file) {
            String v = simpleReadFile(file);
            if (v == null) {
                return null;
            }
            try {
                return UUID.fromString(v);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return null;
            }
        }

        boolean load() {
            File dir = new File("/sys/devices/virtual/dmi/id/");
            this.biosInformation = SmbiosBIOS.builder()
                    .vendor(simpleReadFile(new File(dir, "bios_vendor")))
                    .date(simpleReadFile(new File(dir, "bios_date")))
                    .version(simpleReadFile(new File(dir, "bios_version")))
                    .build();
            this.systemInformation = SmbiosSystem.builder()
                    .manufacturer(simpleReadFile(new File(dir, "sys_vendor")))
                    .productName(simpleReadFile(new File(dir, "product_name")))
                    .version(simpleReadFile(new File(dir, "product_version")))
                    .serialNumber(simpleReadFile(new File(dir, "product_serial")))
                    .uuid(readUuidFile(new File(dir, "product_uuid")))
                    .skuNumber(simpleReadFile(new File(dir, "product_sku")))
                    .build();
            this.baseboardInformation = SmbiosBaseboard.builder()
                    .manufacturer(simpleReadFile(new File(dir, "board_vendor")))
                    .productName(simpleReadFile(new File(dir, "board_name")))
                    .version(simpleReadFile(new File(dir, "board_version")))
                    .serialNumber(simpleReadFile(new File(dir, "board_serial")))
                    .assetTag(simpleReadFile(new File(dir, "board_asset_tag")))
                    .build();
            return dir.isDirectory();
        }

        public SmbiosBIOS getBiosInformation() {
            return biosInformation;
        }

        public SmbiosSystem getSystemInformation() {
            return systemInformation;
        }

        public SmbiosBaseboard getBaseboardInformation() {
            return baseboardInformation;
        }
    }

    private static class FirmwareReader {
        boolean load(SMBIOSReader smbiosReader) throws IOException {
            File file = new File("/sys/firmware/dmi/tables/DMI");
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try (InputStream inputStream = new FileInputStream(file)) {
                byte[] buf = new byte[1024];
                int readlen;
                while((readlen = inputStream.read(buf)) > 0) {
                    byteArrayOutputStream.write(buf, 0, readlen);
                }
            } catch (IOException e) {
                return false;
            }
            ByteBuffer buffer = ByteBuffer
                    .wrap(byteArrayOutputStream.toByteArray())
                    .order(ByteOrder.LITTLE_ENDIAN);
            smbiosReader.process(buffer, buffer.remaining());
            return true;
        }
    }
}
