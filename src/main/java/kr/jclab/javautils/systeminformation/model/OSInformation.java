package kr.jclab.javautils.systeminformation.model;

import java.util.Map;

@lombok.Getter
@lombok.ToString
@lombok.Builder(builderClassName = "Builder")
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

    /**
     * install date (unix time, seconds)
     */
    private final Long installedAt;

    private final Map<String, String> linuxOsRelease;

    private final Map<String, String> windowsOsCurrentVersion;

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
    }
}
