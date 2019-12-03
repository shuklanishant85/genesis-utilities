package com.genesis.script.perf.model;

public class ResultSet {
	private long threadCount;
	private String time;
	private String threadPerSec;
	private String averageTimeInSec;
	private String minTime;
	private String maxTime;
	private String errorCount;
	private String activeThreads;
	private String startedThreads;
	private String finishedThreads;

	/**
	 * @return the threadCount
	 */
	public long getThreadCount() {
		return threadCount;
	}

	/**
	 * @param threadCount
	 *            the threadCount to set
	 */
	public void setThreadCount(long threadCount) {
		this.threadCount = threadCount;
	}

	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}

	/**
	 * @param time
	 *            the time to set
	 */
	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * @return the threadPerSec
	 */
	public String getThreadPerSec() {
		return threadPerSec;
	}

	/**
	 * @param threadPerSec
	 *            the threadPerSec to set
	 */
	public void setThreadPerSec(String threadPerSec) {
		this.threadPerSec = threadPerSec;
	}

	/**
	 * @return the averageTimeInSec
	 */
	public String getAverageTimeInSec() {
		return averageTimeInSec;
	}

	/**
	 * @param averageTimeInSec
	 *            the averageTimeInSec to set
	 */
	public void setAverageTimeInSec(String averageTimeInSec) {
		this.averageTimeInSec = averageTimeInSec;
	}

	/**
	 * @return the minTime
	 */
	public String getMinTime() {
		return minTime;
	}

	/**
	 * @param minTime
	 *            the minTime to set
	 */
	public void setMinTime(String minTime) {
		this.minTime = minTime;
	}

	/**
	 * @return the maxTime
	 */
	public String getMaxTime() {
		return maxTime;
	}

	/**
	 * @param maxTime
	 *            the maxTime to set
	 */
	public void setMaxTime(String maxTime) {
		this.maxTime = maxTime;
	}

	/**
	 * @return the errorCount
	 */
	public String getErrorCount() {
		return errorCount;
	}

	/**
	 * @param errorCount
	 *            the errorCount to set
	 */
	public void setErrorCount(String errorCount) {
		this.errorCount = errorCount;
	}

	/**
	 * @return the activeThreads
	 */
	public String getActiveThreads() {
		return activeThreads;
	}

	/**
	 * @param activeThreads
	 *            the activeThreads to set
	 */
	public void setActiveThreads(String activeThreads) {
		this.activeThreads = activeThreads;
	}

	/**
	 * @return the startedThreads
	 */
	public String getStartedThreads() {
		return startedThreads;
	}

	/**
	 * @param startedThreads
	 *            the startedThreads to set
	 */
	public void setStartedThreads(String startedThreads) {
		this.startedThreads = startedThreads;
	}

	/**
	 * @return the finishedThreads
	 */
	public String getFinishedThreads() {
		return finishedThreads;
	}

	/**
	 * @param finishedThreads
	 *            the finishedThreads to set
	 */
	public void setFinishedThreads(String finishedThreads) {
		this.finishedThreads = finishedThreads;
	}

}
