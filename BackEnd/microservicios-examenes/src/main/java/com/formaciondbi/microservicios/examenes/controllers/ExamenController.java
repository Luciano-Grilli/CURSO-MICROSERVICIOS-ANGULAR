package com.formaciondbi.microservicios.examenes.controllers;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.formaciondbi.microservicios.examenes.services.ExamenServices;
import com.formaciondbi.microservicios.generics.controllers.ControllerImpl;
import com.formaciondbi.microservicios.generics.examenes.Examen;
import com.formaciondbi.microservicios.generics.examenes.Pregunta;
import com.formaciondbi.microservicios.generics.services.ServicesImpl;

@RestController
public class ExamenController extends ControllerImpl<Examen, ServicesImpl<Examen,Long>>{

	@Autowired
	protected ExamenServices examenServices;
	//this.servicio o examenServices 
	
	@GetMapping("/respondidos-por-preguntas")
	public ResponseEntity<?> findExamenesIdsPorPreguntasIdRespondidas(@RequestParam List<Long> preguntaIds){
		return ResponseEntity.status(HttpStatus.OK).body(examenServices.findExamenesIdsConRespuestasByPreguntaIds(preguntaIds));
		/*
		try {
			return ResponseEntity.status(HttpStatus.OK).body(examenServices.findExamenesIdsConRespuestasByPreguntaIds(preguntaIds));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("no anda: "+ e.getMessage());
			
		}*/
	}
	
	@Override
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@Valid @PathVariable Long id,@RequestBody Examen entity,BindingResult result){
		
		Examen examenDb;
		
		try {
			examenDb = this.servicio.findById(id);
			
		} catch (Exception e) {
			
			return ResponseEntity.notFound().build();
		}	
		
		examenDb.setNombre(entity.getNombre());;
		
		/*
		List<Pregunta> eliminadas = new ArrayList<>();
		
		op.getPreguntas().forEach(p ->{
			if (!entity.getPreguntas().contains(p)) {
				eliminadas.add(p);
			}
		});
		
		eliminadas.forEach(p ->{
			op.removePregunta(p);
		});
		*/
		
		List<Pregunta>eliminadas = examenDb.getPreguntas()
		.stream()
		.filter(pdb -> !entity.getPreguntas().contains(pdb))
		.collect(Collectors.toList());
		
		
		eliminadas.forEach(examenDb::removePregunta);
		
		examenDb.setPreguntas(entity.getPreguntas());
		examenDb.setAsignaturaHija(entity.getAsignaturaHija());
		examenDb.setAsignaturaPadre(entity.getAsignaturaPadre());
		
		try {
			return ResponseEntity.status(HttpStatus.CREATED).body(servicio.save(examenDb));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("no anda: "+ e.getMessage());
			
		}
	}
	
	@GetMapping("/filtrar/{term}")
	public ResponseEntity<?> filtrar(@PathVariable String term){
		return ResponseEntity.ok(examenServices.findByNombreExamen(term));
	}
	
	@GetMapping("/asignaturas")
	public ResponseEntity<?> listarAsignaturas(){
		return ResponseEntity.ok(examenServices.findAllAsignaturas());
	}


	
}
