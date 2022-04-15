package br.com.tarcnux.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.tarcnux.dscatalog.dto.CategoryDTO;
import br.com.tarcnux.dscatalog.dto.ProductDTO;
import br.com.tarcnux.dscatalog.entities.Category;
import br.com.tarcnux.dscatalog.entities.Product;
import br.com.tarcnux.dscatalog.repositories.CategoryRepository;
import br.com.tarcnux.dscatalog.repositories.ProductRepository;
import br.com.tarcnux.dscatalog.services.exceptions.DatabaseException;
import br.com.tarcnux.dscatalog.services.exceptions.ResourceNotFountException;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository repository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {
		Page<Product> pageCategories = repository.findAll(pageRequest);
		
		return 	pageCategories.map(x -> new ProductDTO(x));
	}

	@Transactional
	public ProductDTO findById(Long idProduct) {
		Optional<Product> obj = repository.findById(idProduct);
		Product entity = obj.orElseThrow(() -> new ResourceNotFountException("Categoria não encontrada"));
		return new ProductDTO(entity, entity.getCategories());
	}
	
	@Transactional
	public ProductDTO insert(ProductDTO dto) {
		
		var entity = new Product();
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);
		
		return new ProductDTO(entity, entity.getCategories());
	}

	@Transactional
	public ProductDTO update(Long idProduct, ProductDTO dto) {
		
		try {
			Product entity = repository.getOne(idProduct);
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new ProductDTO(entity, entity.getCategories());
		} catch(EntityNotFoundException e) {
			throw new ResourceNotFountException("Identificador não encontrado: " + idProduct);
		}		
	}

	private void copyDtoToEntity(ProductDTO dto, Product entity) {
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setDate(dto.getDate());
		entity.setImgUrl(dto.getImgUrl());
		entity.setPrice(dto.getPrice());
		
		entity.getCategories().clear();
		for( CategoryDTO catDto : dto.getCategories()) {
			Category category = categoryRepository.getOne(catDto.getId());
			entity.getCategories().add(category);
		}
	}

	public void delete(Long idProduct) {
		try {
			repository.deleteById(idProduct);
		} catch(EmptyResultDataAccessException e) {
			throw new ResourceNotFountException("Identificador não encontrado: " + idProduct);
		} catch(DataIntegrityViolationException e) {
			throw new DatabaseException("Violação de Integridade - Categoria não foi apagada.");
		}
		
	}

}
