package kr.jclab.javautils.systeminformation.model;

@lombok.Getter
@lombok.ToString
public class SmbiosBaseboardInformation {
    private final String manufacturer;
    private final String productName;
    private final String version;
    private final String serialNumber;
    private final String assetTag;

    @lombok.Builder(builderClassName = "Builder")
    public SmbiosBaseboardInformation(String manufacturer, String productName, String version, String serialNumber, String assetTag) {
        this.manufacturer = manufacturer;
        this.productName = productName;
        this.version = version;
        this.serialNumber = serialNumber;
        this.assetTag = assetTag;
    }
}
