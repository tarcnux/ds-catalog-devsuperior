package br.com.tarcnux.dscatalog.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.tarcnux.dscatalog.dto.ProductDTO;
import br.com.tarcnux.dscatalog.services.ProductService;

@RestController
@RequestMapping(value = "/products")
public class ProductResource {
	
	@Autowired
	private ProductService service;
	
	/**
	 * Pageable
	 * Parâmetros: page, size, sort
	 */
	@GetMapping
	public ResponseEntity<Page<ProductDTO>> findAll(Pageable pageable) {
		Page<ProductDTO> pages = service.findAllPaged(pageable);
		
		return ResponseEntity.ok().body(pages);
	}
	
	@GetMapping(value = "/{idProduct}")
	public ResponseEntity<ProductDTO> findById(@PathVariable Long idProduct) {
		ProductDTO dto = service.findById(idProduct);
		return ResponseEntity.ok().body(dto);
	}
	
	@PostMapping
	public ResponseEntity<ProductDTO> insert(@RequestBody ProductDTO dto){
		dto = service.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(dto.getId()).toUri();
		
		return ResponseEntity.created(uri).body(dto);
	}
	
	@PutMapping(value = "/{idProduct}")
	public ResponseEntity<ProductDTO> update(@PathVariable Long idProduct, @RequestBody ProductDTO dto){
		dto = service.update(idProduct, dto);
		
		
		return ResponseEntity.ok().body(dto);
	}
	
	@DeleteMapping(value = "/{idProduct}")
	public ResponseEntity<Void> delete(@PathVariable Long idProduct){
		service.delete(idProduct);
		return ResponseEntity.noContent().build();
	}
	
}
