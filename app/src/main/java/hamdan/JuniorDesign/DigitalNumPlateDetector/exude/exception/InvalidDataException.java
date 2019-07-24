package hamdan.JuniorDesign.DigitalNumPlateDetector.exude.exception;

import android.os.Build;
import android.support.annotation.RequiresApi;

public class InvalidDataException extends Exception {

    public InvalidDataException(String message) {
        super(message);
    }

    public InvalidDataException(Throwable cause) {
        super(cause);
    }

    public InvalidDataException(String message, Throwable cause) {
        super(message, cause);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public InvalidDataException(String message, Throwable cause,
                                boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
