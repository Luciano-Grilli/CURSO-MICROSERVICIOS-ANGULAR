package com.formaciondbi.microservicios.app.usuarios.controllers;

import java.io.IOException;
import java.util.List;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.formaciondbi.microservicios.app.usuarios.services.AlumnoServices;
import com.formaciondbi.microservicios.generics.controllers.ControllerImpl;
import com.formaciondbi.microservicios.generics.models.entity.Alumno;
import com.formaciondbi.microservicios.generics.services.ServicesImpl;

@RestController
public class AlumnoController extends ControllerImpl<Alumno, ServicesImpl<Alumno,Long>>{

	@Autowired
	protected AlumnoServices alumnoServices;
	
	@GetMapping("/filtrar/{term}")
	public ResponseEntity<?> filtrar(@PathVariable String term){
		
		return ResponseEntity.ok(alumnoServices.finByNombreOrApellido(term));
	}

	@GetMapping("alumnos-por-curso")
	public ResponseEntity<?> obtenerAlumnosPorCurso(@RequestParam List<Long> ids){
		return ResponseEntity.ok(alumnoServices.findAllById(ids));
	}
	
	

	@PostMapping("/crear-con-foto")
	public ResponseEntity<?> saveConFoto(@Valid Alumno alumno, BindingResult result, @RequestParam MultipartFile archivo) throws IOException {
		if (!archivo.isEmpty()) {
			alumno.setFoto(archivo.getBytes());
		}
		return super.save(alumno, result);
	}

	
	@PutMapping("/editar-con-foto/{id}")
	public ResponseEntity<?> updateConFoto(@PathVariable Long id, Alumno alumno, @RequestParam MultipartFile archivo) throws Exception {
		
		Alumno op;
		try {
			op = this.alumnoServices.findById(id);
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"error por favor intente mas tarde.\"}");
	        
			//return ResponseEntity.notFound().build();
		}
		
		Alumno dbAlumno = op;
		dbAlumno.setApellido(alumno.getApellido());
		dbAlumno.setNombre(alumno.getNombre());
		dbAlumno.setEmail(alumno.getEmail());
		
		if (!archivo.isEmpty()) {
			dbAlumno.setFoto(archivo.getBytes());
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(alumnoServices.save(dbAlumno));
			
	}
	
	@GetMapping("/uploads/img/{id}")
	public ResponseEntity<?> verFoto(@PathVariable Long id){
		Alumno op;
		try {
			op = this.alumnoServices.findById(id);
			
			if (op.getFoto() == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"imagen null.\"}");
		        
			}
			
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"error por favor intente mas tarde.\"}");
	        
			
		}
		Resource imagen = new ByteArrayResource(op.getFoto());
		
		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imagen);
	}

	
	
}

