package kr.jclab.javautils.systeminformation.model;

@lombok.ToString
public class OSType {
    public enum PredefinedType {
        Unknown,
        Windows,
        Linux,
        Darwin,
        Android
    }

    public static final String OS_NAME_WINDOWS_PREFIX = "Windows";

    private final PredefinedType type;
    private final String name;

    public OSType(PredefinedType type, String name) {
        this.type = type;
        this.name = name;
    }

    public boolean isWindows() {
        return PredefinedType.Windows.equals(this.type);
    }

    public boolean isLinux() {
        return PredefinedType.Linux.equals(this.type) || PredefinedType.Android.equals(this.type);
    }

    public boolean isDarwin() {
        return PredefinedType.Darwin.equals(this.type);
    }

    public boolean isAndroid() {
        return PredefinedType.Android.equals(this.type);
    }

    public String getName() {
        return name;
    }
}
