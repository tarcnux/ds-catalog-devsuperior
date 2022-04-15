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

import br.com.tarcnux.dscatalog.dto.ProductDTO;
import br.com.tarcnux.dscatalog.entities.Product;
import br.com.tarcnux.dscatalog.repositories.ProductRepository;
import br.com.tarcnux.dscatalog.services.exceptions.DatabaseException;
import br.com.tarcnux.dscatalog.services.exceptions.ResourceNotFountException;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository repository;
	
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
		//entity.setName(dto.getName());
		entity = repository.save(entity);
		
		return new ProductDTO(entity);
	}

	@Transactional
	public ProductDTO update(Long idProduct, ProductDTO dto) {
		
		try {
			Product entity = repository.getOne(idProduct);
			//entity.setName(dto.getName());
			entity = repository.save(entity);
			return new ProductDTO(entity);
		} catch(EntityNotFoundException e) {
			throw new ResourceNotFountException("Identificador não encontrado: " + idProduct);
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
