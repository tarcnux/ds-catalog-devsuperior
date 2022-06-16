package br.com.tarcnux.dscatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import br.com.tarcnux.dscatalog.entities.Product;
import br.com.tarcnux.dscatalog.tests.Factory;

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
	private Long countTotalProducts;
	
	//Fixture
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalProducts = 25L; //There is 25 Total Products inserted on Data Base
	}
	
	@Test
	public void saveShouldPersistWithAutoincrementWhenIdIsNull() {
		Product product = Factory.createProduct();
		product.setId(null);
		
		product = repository.save(product);
		
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(countTotalProducts + 1, product.getId());
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
	
	@Test
	public void findByIdShouldReturnNonEmptyOptinalWhenIdExists() {
		Optional<Product> result = repository.findById(existingId);
		Assertions.assertTrue(result.isPresent());
	}
	
	@Test
	public void findByIdShouldReturnEmptyOptinalWhenIdEDoesNotxists() {
		Optional<Product> result = repository.findById(nonExistingId);
		Assertions.assertTrue(result.isEmpty());
	}

}
