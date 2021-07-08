package kr.jclab.javautils.systeminformation.model;

@lombok.Getter
@lombok.ToString
public class SmbiosBIOSInformation {
    private final String vendor;
    private final String version;
    private final String date;

    @lombok.Builder(builderClassName = "Builder")
    public SmbiosBIOSInformation(String vendor, String version, String date) {
        this.vendor = vendor;
        this.version = version;
        this.date = date;
    }
}
