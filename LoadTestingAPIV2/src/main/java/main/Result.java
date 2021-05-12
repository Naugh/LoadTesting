package main;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Result {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String timeStamp = null;
	private String elapsed = null;
	private String label = null;
	private String responseCode = null;
	private String responseMessage = null;
	private String threadName = null;
	private String dataType = null;
	private String success = null;
	private String failureMessage = null;
	private String bytes = null;
	private String sentBytes = null;
	private String grpThreads = null;
	private String allThreads = null;
	private String latency = null;
	private String idleTime = null;
	private String connect = null;

	public Result(){
	}

	public Result(String timeStamp, String elapsed, String label, String responseCode, String responseMessage,
			String threadName, String dataType, String success, String failureMessage, String bytes, String sentBytes,
			String grpThreads, String allThreads, String latency, String idleTime, String connect) {
		this.timeStamp = timeStamp;
		this.elapsed = elapsed;
		this.label = label;
		this.responseCode = responseCode;
		this.responseMessage = responseMessage;
		this.threadName = threadName;
		this.dataType = dataType;
		this.success = success;
		this.failureMessage = failureMessage;
		this.bytes = bytes;
		this.sentBytes = sentBytes;
		this.grpThreads = grpThreads;
		this.allThreads = allThreads;
		this.latency = latency;
		this.idleTime = idleTime;
		this.connect = connect;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getElapsed() {
		return elapsed;
	}

	public void setElapsed(String elapsed) {
		this.elapsed = elapsed;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public String getThreadName() {
		return threadName;
	}

	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getFailureMessage() {
		return failureMessage;
	}

	public void setFailureMessage(String failureMessage) {
		this.failureMessage = failureMessage;
	}

	public String getBytes() {
		return bytes;
	}

	public void setBytes(String bytes) {
		this.bytes = bytes;
	}

	public String getSentBytes() {
		return sentBytes;
	}

	public void setSentBytes(String sentBytes) {
		this.sentBytes = sentBytes;
	}

	public String getGrpThreads() {
		return grpThreads;
	}

	public void setGrpThreads(String grpThreads) {
		this.grpThreads = grpThreads;
	}

	public String getAllThreads() {
		return allThreads;
	}

	public void setAllThreads(String allThreads) {
		this.allThreads = allThreads;
	}

	public String getLatency() {
		return latency;
	}

	public void setLatency(String latency) {
		this.latency = latency;
	}

	public String getIdleTime() {
		return idleTime;
	}

	public void setIdleTime(String idleTime) {
		this.idleTime = idleTime;
	}

	public String getConnect() {
		return connect;
	}

	public void setConnect(String connect) {
		this.connect = connect;
	}

}
