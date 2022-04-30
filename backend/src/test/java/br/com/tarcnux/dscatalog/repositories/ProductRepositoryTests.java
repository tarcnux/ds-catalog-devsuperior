package br.com.tarcnux.dscatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
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
	
	@Test
	void deleteShouldDeleteObjectWhenIdExists() {
		
		//Arrange
		Long existingId = 1L;
		
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
			Long nonExistingId = 1000L;
			
			//Act
			repository.deleteById(nonExistingId);
		});
	}

}
