package br.com.tarcnux.dscatalog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.tarcnux.dscatalog.repositories.ProductRepository;
import br.com.tarcnux.dscatalog.services.exceptions.DatabaseException;
import br.com.tarcnux.dscatalog.services.exceptions.ResourceNotFountException;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {
	/**
	 * For a Service Unit Test, it's necessary mock the repositories.
	 * It's not allowed instantiate these objects, here.
	 * Integration test instantiate the repositories into the context.
	 */
	
	@InjectMocks //From Mockito
	private ProductService service;
	
	//Emulate the object behavior
	@Mock
	private ProductRepository repository;
	
	private long existingId;
	private long nonExistingId;
	private long dependentId;
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		dependentId = 2L;
		nonExistingId = 1000L;
		
		
		// Setup the mocked behavior
		// When call method deleteById for an existing Id, do nothing
		Mockito.doNothing().when(repository).deleteById(existingId);
		
		// Emulate the exception throw when try to delete non existing ID
		// the repository layer throws this exception but the service try catch throw another pne
		Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);
		
		// Emulate the exception of database violation foreign key
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
	}
	
	@Test
	public void deletShouldThrowDatabaseExceptionWhenIdIsDependent() {
		//The service layer expect the resource not found exception
		Assertions.assertThrows(DatabaseException.class, () -> {
			service.delete(dependentId);
		});
		Mockito.verify(repository, Mockito.times(1)).deleteById(dependentId);
	}
	
	@Test
	public void deletShouldThrowResourceNotFountExceptionWhenIdDoesNotExist() {
		//The service layer expect the resource not found exception
		Assertions.assertThrows(ResourceNotFountException.class, () -> {
			service.delete(nonExistingId);
		});
		Mockito.verify(repository, Mockito.times(1)).deleteById(nonExistingId);
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
		});
		
		// Verify if the mocked method was called
		// The method must be called once
		Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
	}	

}
