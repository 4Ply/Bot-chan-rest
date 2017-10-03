package com.netply.botchan.web.rest.user;

import java.util.Objects;

public class PlatformUser {
    private String clientID;
    private String platform;


    public PlatformUser(String clientID, String platform) {
        this.clientID = clientID;
        this.platform = platform;
    }

    public String getClientID() {
        return clientID;
    }

    public String getPlatform() {
        return platform;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlatformUser that = (PlatformUser) o;
        return Objects.equals(clientID, that.clientID) &&
                Objects.equals(platform, that.platform);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientID, platform);
    }

    @Override
    public String toString() {
        return "PlatformUser{" +
                "clientID='" + clientID + '\'' +
                ", platform='" + platform + '\'' +
                '}';
    }
}
