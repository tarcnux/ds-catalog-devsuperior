package br.com.tarcnux.dscatalog.services;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.tarcnux.dscatalog.entities.Product;
import br.com.tarcnux.dscatalog.repositories.ProductRepository;
import br.com.tarcnux.dscatalog.services.exceptions.DatabaseException;
import br.com.tarcnux.dscatalog.services.exceptions.ResourceNotFountException;
import br.com.tarcnux.dscatalog.tests.Factory;

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
	
	private PageImpl<Product> page; //Represents a data page
	private Product product;
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		dependentId = 2L;
		nonExistingId = 3L;
		product = Factory.createProduct();
		page = new PageImpl<>(List.of(product));
		
		Mockito.when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);
		
		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product); //Acho que retorna um DTO
		
		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		
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
