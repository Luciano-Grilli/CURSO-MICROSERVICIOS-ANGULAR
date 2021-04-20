package com.formaciondbi.microservicios.generics.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.*;

import com.formaciondbi.microservicios.generics.services.ServicesImpl;

public abstract class ControllerImpl<E , S extends ServicesImpl<E,Long>> implements Controller<E, Long> {

    @Autowired
    protected S servicio;


    @GetMapping("")
    public ResponseEntity<?> getAll(){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(servicio.findAll());

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"error por favor intente mas tarde.\"}");
        }
    }
    
    @GetMapping("/paged")
    public ResponseEntity<?> getAll(Pageable pageable){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(servicio.findAll(pageable));

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"error por favor intente mas tarde.\"}");
        }
    }


    @RequestMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(servicio.findById(id));

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"error por favor intente mas tarde.\"}");
        }
    }

    @PostMapping("")
    public ResponseEntity<?> save(@Valid @RequestBody E entity,BindingResult result){
        if (result.hasErrors()) {
			return this.validar(result);
		}
    	
    	try {
            return ResponseEntity.status(HttpStatus.OK).body(servicio.save(entity));

        }catch (Exception e){
            //return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"error por favor intente mas tarde.\"}");
        	return this.validar(result);
        }
    }
    
    protected ResponseEntity<?> validar(BindingResult result){
    	Map<String, Object> errores = new HashMap<>();
    	result.getFieldErrors().forEach(err -> {
    		errores.put(err.getField()," El campo "+ err.getField()+ " " + err.getDefaultMessage());
    	});
    	return ResponseEntity.badRequest().body(errores);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @PathVariable Long id,@RequestBody E entity,BindingResult result){
    	if (result.hasErrors()) {
			return this.validar(result);
		}
        try {
            return ResponseEntity.status(HttpStatus.OK).body(servicio.update(id,entity));

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"error por favor intente mas tarde.\"}");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        try {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(servicio.deleteById(id));

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"error por favor intente mas tarde.\"}");
        }
    }
}
