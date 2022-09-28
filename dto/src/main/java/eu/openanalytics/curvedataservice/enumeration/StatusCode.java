package eu.openanalytics.curvedataservice.enumeration;

public enum StatusCode {
    SCHEDULED, // the calculation for this result is scheduled and may already be running. The calculation is not yet completed.
    SUCCESS, // the calculation succeeded
    FAILURE // the calculation failed
}
