package com.log.project.app.server.forwarder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class Log {

	private Long timeStamp;
	private String hostName;
	private String logMessage;

	public Log(Long timeStamp, String hostName, String logMessage)
	{
		this.timeStamp = timeStamp;
		this.hostName = hostName;
		this.logMessage = logMessage;
	}

	public Long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getLogMessage() {
		return logMessage;
	}

	public void setLogMessage(String logMessage) {
		this.logMessage = logMessage;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	@Override
	public String toString() {
		LocalDateTime date =
				Instant.ofEpochMilli(timeStamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
		return String.format(
				"Log[timeStamp='%s', hostName='%s', logMessage='%s']",
				date.toString(), hostName, logMessage);
	}
}
