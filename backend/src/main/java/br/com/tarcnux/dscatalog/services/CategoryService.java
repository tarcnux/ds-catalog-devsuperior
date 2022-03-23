package br.com.tarcnux.dscatalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.tarcnux.dscatalog.dto.CategoryDTO;
import br.com.tarcnux.dscatalog.entities.Category;
import br.com.tarcnux.dscatalog.repositories.CategoryRepository;
import br.com.tarcnux.dscatalog.services.exceptions.EntityNotFountException;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository repository;
	
	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll() {
		List<Category> listCategories = repository.findAll();
		return 	listCategories
				.stream().map(x -> new CategoryDTO(x))
				.collect(Collectors.toList());
	}

	@Transactional
	public CategoryDTO findById(Long idCategory) {
		Optional<Category> obj = repository.findById(idCategory);
		Category entity = obj.orElseThrow(() -> new EntityNotFountException("Categoria não encontrada"));
		return new CategoryDTO(entity);
	}
	
	@Transactional
	public CategoryDTO insert(CategoryDTO dto) {
		
		var entity = new Category();
		entity.setName(dto.getName());
		entity = repository.save(entity);
		
		return new CategoryDTO(entity);
	}

}
