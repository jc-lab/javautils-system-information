package kr.jclab.javautils.systeminformation.exception;

public class NativeApiErrorException extends Exception {
    public NativeApiErrorException() {
        super();
    }

    public NativeApiErrorException(String message) {
        super(message);
    }

    public NativeApiErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public NativeApiErrorException(Throwable cause) {
        super(cause);
    }

    public NativeApiErrorException(String message, Throwable cause, boolean enableSuppression,
                                   boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
