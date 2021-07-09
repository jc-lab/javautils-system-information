import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Map;

public class SMBIOSResources {
    public static Base64.Decoder b64Decoder = Base64.getDecoder();
    public static final ArrayList<Map.Entry<byte[], Object>> DMI_SAMPLES;
    public static final ArrayList<Map.Entry<byte[], Object>> WINDOWS_DMI_SAMPLES;

    private static byte[] readBase64Resource(String path) {
        try (InputStream is = SMBIOSResources.class.getResourceAsStream(path)) {
            assert is != null;
            byte[] text = IOUtils.readFully(is, is.available());
            return b64Decoder.decode(
                    new String(text).replaceAll(" |\t|\r|\n", "")
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static {
        DMI_SAMPLES = new ArrayList<>();
        {
            byte[] data = readBase64Resource("/dmi-sample-01.txt");
            DMI_SAMPLES.add(new AbstractMap.SimpleEntry<>(data, null));
        }

        WINDOWS_DMI_SAMPLES = new ArrayList<>();
        {
            // SMBIOS 3.0
            byte[] data = readBase64Resource("/windows-dmi-sample-01.txt");
            WINDOWS_DMI_SAMPLES.add(new AbstractMap.SimpleEntry<>(data, null));
        }
    }
}
