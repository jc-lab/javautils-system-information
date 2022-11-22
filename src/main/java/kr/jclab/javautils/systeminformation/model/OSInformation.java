package kr.jclab.javautils.systeminformation.model;

import java.util.Map;

@lombok.Getter
@lombok.ToString
public class OSInformation {
    /**
     * Windows: windows
     * Linux: debian/redhat
     */
    private final String distFamily;

    /**
     * Linux: /etc/debian_version
     */
    private final String distBaseVersion;

    /**
     * Linux: /etc/os-release > ID
     * Windows: windows
     */
    private final String identity;

    /**
     * Linux: /etc/os-release > PRETTY_NAME
     * Windows: (e.g.) Windows 10 Pro
     */
    private final String productName;

    /**
     * Linux: /etc/os-release > VERSION
     * Windows: (e.g.) 1809
     */
    private final String releaseId;

    private final int currentBuildNumber;

    /**
     * install date (unix time, milliseconds)
     */
    private final Long installedAt;

    private final Map<String, String> linuxOsRelease;

    @lombok.Builder(builderClassName = "Builder")
    public OSInformation(String distFamily, String distBaseVersion, String identity, String productName, String releaseId, int currentBuildNumber, Long installedAt, Map<String, String> linuxOsRelease) {
        this.distFamily = distFamily;
        this.distBaseVersion = distBaseVersion;
        this.identity = identity;
        this.productName = productName;
        this.releaseId = releaseId;
        this.currentBuildNumber = currentBuildNumber;
        this.installedAt = installedAt;
        this.linuxOsRelease = linuxOsRelease;
    }

    public static class Builder {
        public String getDistFamily() {
            return distFamily;
        }

        public String getProductName() {
            return productName;
        }

        public String getReleaseId() {
            return releaseId;
        }

        public int getCurrentBuildNumber() {
            return currentBuildNumber;
        }
    }
}
