package com.formaciondbi.microservicios.generics.services;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.formaciondbi.microservicios.generics.repository.Repository;


public class ServicesImpl<E,ID extends Serializable> implements Services<E,ID> {

	@Autowired
	protected Repository<E, ID> repository;

	
	@Override
	@Transactional
	public Iterable<E> findAll() throws Exception {
		
		try {
			Iterable<E>entities = repository.findAll();
			return entities;
			
		}catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		
	}

	@Override
	@Transactional
	public E findById(ID id) throws Exception {
		try {
			Optional<E> entities = repository.findById(id);
			
			return entities.get();
			
		}catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	@Override
	@Transactional
	public E update(ID id, E entidad) throws Exception {
		try {
			Optional<E> entityOptional = repository.findById(id);
			E entity = entityOptional.get();
			entity = repository.save(entidad);
			return entity;
				
		}catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	@Override
	@Transactional
	public E save(E entity) throws Exception {
		try {
			entity = repository.save(entity);
			
			return entity;
			
		}catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	@Override
	@Transactional
	public boolean deleteById(ID id) throws Exception {
		
		try {
			if(repository.existsById(id)) {
				repository.deleteById(id);
				return true;
			}else {
				throw new Exception();
			}
			
		}catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Page<E> findAll(Pageable pageable) throws Exception {
		
		return repository.findAll(pageable);
	}

	


}
