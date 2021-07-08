package kr.jclab.javautils.systeminformation.platform.linux;

import kr.jclab.javautils.systeminformation.model.OSInformation;
import kr.jclab.javautils.systeminformation.osinfo.OSInfoBase;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;

public class LinuxOSInfo implements OSInfoBase {
    private static final List<String> DEBIAN_CODE_NAMES = Arrays.asList(
            "buzz", "rex", "bo", "hamm", "slink", "potato", "woody", "sarge", "etch", "lenny", "squeeze", "wheezy", "jessie", "stretch", "buster", "bullseye", "Bookworm"
    );

    private static List<String> readTextFile(File file) throws IOException {
        ArrayList<String> lines = new ArrayList<>();
        lines.ensureCapacity(32);
        try (FileInputStream fis = new FileInputStream(file)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new BufferedInputStream(fis)));
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line.trim());
            }
        }
        return lines;
    }

    private static boolean isEmptyString(String s) {
        return s == null || s.isEmpty();
    }

    @Override
    public OSInformation read() throws IOException {
        final File machineIdFile = new File("/etc/machine-id");
        final File osReleaseFile = new File("/etc/os-release");
        final File lsbReleaseFile = new File("/etc/lsb-release");
        final File debianVersionFile = new File("/etc/debian_version");

        OSInformation.Builder builder = OSInformation.builder();

        if (machineIdFile.exists()) {
            BasicFileAttributeView fileAttribute = Files.getFileAttributeView(
                    machineIdFile.toPath(),
                    BasicFileAttributeView.class,
                    LinkOption.NOFOLLOW_LINKS
            );
            BasicFileAttributes basicFileAttributes = fileAttribute.readAttributes();
            builder.installedAt(basicFileAttributes.creationTime().toMillis() / 1000);
        }

        Map<String, String> osReleaseMap = new HashMap<>();
        for (String line : readTextFile(osReleaseFile)) {
            String[] tokens = line.split("=", 2);
            if (tokens.length != 2) continue;
            for (int i=0; i<tokens.length; i++) tokens[i] = tokens[i].trim().replaceAll("\"", "");
            if ("ID".equalsIgnoreCase(tokens[0])) {
                builder.identity(tokens[1]);
            } else if ("PRETTY_NAME".equalsIgnoreCase(tokens[0])) {
                builder.productName(tokens[1]);
            } else if ("VERSION".equalsIgnoreCase(tokens[0])) {
                builder.releaseId(tokens[1]);
            } else if ("ID_LIKE".equalsIgnoreCase(tokens[0])) {
                builder.distFamily(tokens[1]);
            }
            osReleaseMap.put(tokens[0].toUpperCase(), tokens[1]);
        }
        builder.linuxOsRelease(Collections.unmodifiableMap(osReleaseMap));

        if ((isEmptyString(builder.getProductName()) || isEmptyString(builder.getReleaseId())) && lsbReleaseFile.exists()) {
            for (String line : readTextFile(lsbReleaseFile)) {
                String[] tokens = line.split("=", 2);
                if (tokens.length != 2) continue;
                for (int i=0; i<tokens.length; i++) tokens[i] = tokens[i].trim().replaceAll("\"", "");
                if ("DISTRIB_RELEASE".equalsIgnoreCase(tokens[0])) {
                    builder.releaseId(tokens[1]);
                } else if ("DISTRIB_DESCRIPTION".equalsIgnoreCase(tokens[0])) {
                    builder.productName(tokens[1]);
                }
            }
        }

        if (debianVersionFile.exists()) {
            String version = readTextFile(debianVersionFile)
                    .stream()
                    .filter(v -> !v.isEmpty())
                    .collect(Collectors.joining());
            builder.distBaseVersion(version);
            if (isEmptyString(builder.getDistFamily())) {
                builder.distFamily("debian");
            }
        }

        return builder.build();
    }
}
