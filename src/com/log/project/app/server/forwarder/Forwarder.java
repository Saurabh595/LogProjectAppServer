package com.log.project.app.server.forwarder;

public class Forwarder {

    private Integer id;
    
    private String hostName;

    private long lastReadPosition;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public long getLastReadPosition() {
        return lastReadPosition;
    }

    public void setLastReadPosition(long lastReadPosition) {
        this.lastReadPosition = lastReadPosition;
    }
}

