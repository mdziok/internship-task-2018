package pl.codewise.internships;

import java.time.LocalTime;

public class Message {

    private final String userAgent;
    private final int errorCode;
    private final LocalTime time;

    public Message(String userAgent, int errorCode) {
        this.userAgent = userAgent;
        this.errorCode = errorCode;
        this.time = LocalTime.now();
    }

    public String getUserAgent() {
        return userAgent;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public boolean isValid(LocalTime now) {
        return time.isAfter(now.minusMinutes(5));
    }

    public LocalTime getTime() {
        return time;
    }
}
