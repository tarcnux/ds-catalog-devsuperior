package br.com.tarcnux.dscatalog.resources;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import br.com.tarcnux.dscatalog.dto.CategoryDTO;
import br.com.tarcnux.dscatalog.services.CategoryService;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {
	
	@Autowired
	private CategoryService service;
	
	@GetMapping
	public ResponseEntity<List<CategoryDTO>> findAll() {
		List<CategoryDTO> list = service.findAll();
		
		return ResponseEntity.ok().body(list);
	}
	
	@GetMapping(value = "/{idCategory}")
	public ResponseEntity<CategoryDTO> findById(@PathVariable Long idCategory) {
		CategoryDTO dto = service.findById(idCategory);
		return ResponseEntity.ok().body(dto);
	}
	
	@PostMapping
	public ResponseEntity<CategoryDTO> insert(@RequestBody CategoryDTO dto){
		dto = service.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(dto.getId()).toUri();
		
		return ResponseEntity.created(uri).body(dto);
	}
	
	@PutMapping(value = "/{idCategory}")
	public ResponseEntity<CategoryDTO> update(@PathVariable Long idCategory, @RequestBody CategoryDTO dto){
		dto = service.update(idCategory, dto);
		
		
		return ResponseEntity.ok().body(dto);
	}
	
	@DeleteMapping(value = "/{idCategory}")
	public ResponseEntity<Void> delete(@PathVariable Long idCategory){
		service.delete(idCategory);
		return ResponseEntity.noContent().build();
	}
	
}
