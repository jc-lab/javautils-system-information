package kr.jclab.javautils.systeminformation.model;

@lombok.ToString
public class OSArch {
    public enum PredefinedType {
        Unknown,
        X86_32,
        X86_64,
        AArch64
    }

    private final PredefinedType type;
    private final String name;

    public OSArch(PredefinedType type, String name) {
        this.type = type;
        this.name = name;
    }

    public PredefinedType getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
