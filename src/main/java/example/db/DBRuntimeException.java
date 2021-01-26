package example.db;

public class DBRuntimeException extends RuntimeException {
    public DBRuntimeException() {
    }

    public DBRuntimeException(String message) {
        super(message);
    }

    public DBRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public DBRuntimeException(Throwable cause) {
        super(cause);
    }

    public DBRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
