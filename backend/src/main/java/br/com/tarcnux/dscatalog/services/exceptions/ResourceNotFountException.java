package br.com.tarcnux.dscatalog.services.exceptions;

public class ResourceNotFountException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public ResourceNotFountException(String message) {
		super(message);
	}
}
