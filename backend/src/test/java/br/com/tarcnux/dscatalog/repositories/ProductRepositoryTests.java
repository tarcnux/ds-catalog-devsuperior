package br.com.tarcnux.dscatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import br.com.tarcnux.dscatalog.entities.Product;

/**
 * Carrega somente os componentes relacionados ao Spring Data JPA. 
 * Cada teste é transacional e dá rollback ao final. (teste de unidade: repository)
 * @author tarcnux
 *
 */
@DataJpaTest
class ProductRepositoryTests {

	@Autowired
	private ProductRepository repository;	
	
	private Long existingId;
	private Long nonExistingId;
	
	//Fixture
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
	}
	
	@Test
	void deleteShouldDeleteObjectWhenIdExists() {
		
		//Arrange
		//Inside of BeforeEach
		
		//Act
		repository.deleteById(existingId);
		Optional<Product> result = repository.findById(existingId);
		
		//Assertions
		Assertions.assertFalse(result.isPresent());			
	}
	
	@Test
	void deleteShouldThrowsEmptyResultDataAccessExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
			//Arrange
			//Inside of BeforeEach
			
			//Act
			repository.deleteById(nonExistingId);
		});
	}

}
