package com.formaciondbi.microservicios.generics.services;

import java.io.Serializable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface Services<E,ID extends Serializable> {
	
	public Iterable<E> findAll() throws Exception;
	
	public Page<E> findAll(Pageable pageable) throws Exception;
	
	public E findById(ID id) throws Exception;
	
	public E update(ID id, E entity)throws Exception;
	
	public E save(E entity) throws Exception;
	
	public boolean deleteById(ID id) throws Exception;
}
