package kr.jclab.javautils.systeminformation.model;

import java.util.UUID;

@lombok.Getter
@lombok.ToString
public class SmbiosSystemInformation {
    private final String manufacturer;
    private final String productName;
    private final String version;
    private final String serialNumber;
    private final String skuNumber;
    private final UUID uuid;

    @lombok.Builder(builderClassName = "Builder")
    public SmbiosSystemInformation(String manufacturer, String productName, String version, String serialNumber, String skuNumber, UUID uuid) {
        this.manufacturer = manufacturer;
        this.productName = productName;
        this.version = version;
        this.serialNumber = serialNumber;
        this.skuNumber = skuNumber;
        this.uuid = uuid;
    }
}
