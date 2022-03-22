package br.com.tarcnux.dscatalog.services.exceptions;

public class EntityNotFountException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public EntityNotFountException(String message) {
		super(message);
	}
}
