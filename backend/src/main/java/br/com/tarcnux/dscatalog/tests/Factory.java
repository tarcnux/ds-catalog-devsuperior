package br.com.tarcnux.dscatalog.tests;

import java.time.Instant;

import br.com.tarcnux.dscatalog.dto.ProductDTO;
import br.com.tarcnux.dscatalog.entities.Category;
import br.com.tarcnux.dscatalog.entities.Product;

public class Factory {
	
	public static Product createProduct() {
		Product product = new Product(1L, "Phone", "Good Phone", 800.99,"https://img.com/impg.png", Instant.parse("2022-06-16T16:22:22Z"));
		product.getCategories().add(new Category(2L,"Electronics"));
		return product;
	}
	
	public static ProductDTO createProductDTO() {
		Product product = createProduct();
		return new ProductDTO(product, product.getCategories());
	}

}
