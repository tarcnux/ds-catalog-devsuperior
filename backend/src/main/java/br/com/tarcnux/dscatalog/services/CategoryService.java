package br.com.tarcnux.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.tarcnux.dscatalog.dto.CategoryDTO;
import br.com.tarcnux.dscatalog.entities.Category;
import br.com.tarcnux.dscatalog.repositories.CategoryRepository;
import br.com.tarcnux.dscatalog.services.exceptions.DatabaseException;
import br.com.tarcnux.dscatalog.services.exceptions.ResourceNotFountException;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository repository;
	
	@Transactional(readOnly = true)
	public Page<CategoryDTO> findAllPaged(Pageable pageable) {
		Page<Category> pageCategories = repository.findAll(pageable);
		
		return 	pageCategories.map(x -> new CategoryDTO(x));
	}

	@Transactional
	public CategoryDTO findById(Long idCategory) {
		Optional<Category> obj = repository.findById(idCategory);
		Category entity = obj.orElseThrow(() -> new ResourceNotFountException("Categoria não encontrada"));
		return new CategoryDTO(entity);
	}
	
	@Transactional
	public CategoryDTO insert(CategoryDTO dto) {
		
		var entity = new Category();
		entity.setName(dto.getName());
		entity = repository.save(entity);
		
		return new CategoryDTO(entity);
	}

	@Transactional
	public CategoryDTO update(Long idCategory, CategoryDTO dto) {
		
		try {
			Category entity = repository.getOne(idCategory);
			entity.setName(dto.getName());
			entity = repository.save(entity);
			return new CategoryDTO(entity);
		} catch(EntityNotFoundException e) {
			throw new ResourceNotFountException("Identificador não encontrado: " + idCategory);
		}		
	}

	public void delete(Long idCategory) {
		try {
			repository.deleteById(idCategory);
		} catch(EmptyResultDataAccessException e) {
			throw new ResourceNotFountException("Identificador não encontrado: " + idCategory);
		} catch(DataIntegrityViolationException e) {
			throw new DatabaseException("Violação de Integridade - Categoria não foi apagada.");
		}
		
	}

}
