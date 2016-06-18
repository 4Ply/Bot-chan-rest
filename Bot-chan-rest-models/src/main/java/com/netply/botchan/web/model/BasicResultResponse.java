package com.netply.botchan.web.model;

public class BasicResultResponse {
    private boolean result;
    private String sessionKey;
    private String reason;


    BasicResultResponse() {

    }

    public BasicResultResponse(String failureReason) {
        this.result = false;
        this.sessionKey = null;
        this.reason = failureReason;
    }

    public BasicResultResponse(boolean result, String sessionKey) {
        this.result = result;
        this.sessionKey = sessionKey;
        reason = "Success";
    }

    public boolean isResult() {
        return result;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BasicResultResponse that = (BasicResultResponse) o;

        if (result != that.result) return false;
        if (sessionKey != null ? !sessionKey.equals(that.sessionKey) : that.sessionKey != null) return false;
        return reason != null ? reason.equals(that.reason) : that.reason == null;
    }

    @Override
    public int hashCode() {
        int result1 = (result ? 1 : 0);
        result1 = 31 * result1 + (sessionKey != null ? sessionKey.hashCode() : 0);
        result1 = 31 * result1 + (reason != null ? reason.hashCode() : 0);
        return result1;
    }

    @Override
    public String toString() {
        return "BasicResultResponse{" +
                "result=" + result +
                ", sessionKey='" + sessionKey + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }
}
