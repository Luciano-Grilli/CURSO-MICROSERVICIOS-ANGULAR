package com.formaciondbi.microservicios.generics.repository;

import org.springframework.data.repository.PagingAndSortingRepository;



public interface Repository<E,ID> extends PagingAndSortingRepository<E,ID>{

}	
