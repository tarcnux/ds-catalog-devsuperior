package br.com.tarcnux.dscatalog.resources.exception;

import java.io.Serializable;
import java.time.Instant;

public class StandardError implements Serializable{
	
	private static final long serialVersionUID = 1L;
	/**
	 *     "timestamp": "2022-03-22T23:03:32.167+00:00",
    "status": 500,
    "error": "Internal Server Error",
    "message": "",
    "path": "/categories/5"
	 */
	
	private Instant timestamp;
	private Integer status;
	private String error;
	private String message;
	private String path;

	public StandardError() {
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
}
