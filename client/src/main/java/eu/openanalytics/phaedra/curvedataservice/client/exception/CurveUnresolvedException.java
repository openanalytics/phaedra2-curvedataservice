package eu.openanalytics.phaedra.curvedataservice.client.exception;

public class CurveUnresolvedException extends Exception {

    public CurveUnresolvedException(String message) {
        super(message);
    }

    public CurveUnresolvedException(String message, Throwable cause) {
        super(message, cause);
    }
}
