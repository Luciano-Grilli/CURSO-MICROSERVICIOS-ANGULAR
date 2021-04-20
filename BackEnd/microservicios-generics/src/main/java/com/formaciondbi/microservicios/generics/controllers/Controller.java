package com.formaciondbi.microservicios.generics.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.Serializable;

public interface Controller<E ,ID extends Serializable> {
    public ResponseEntity<?> getAll();
    //public ResponseEntity<?> getAll(Pageable pageable);
    public ResponseEntity<?> getOne(@PathVariable ID id);
    public ResponseEntity<?> save( E entity,BindingResult result);
    public ResponseEntity<?> update( ID id,  E entity,BindingResult result);
    public ResponseEntity<?> delete(@PathVariable ID id);
    
}
