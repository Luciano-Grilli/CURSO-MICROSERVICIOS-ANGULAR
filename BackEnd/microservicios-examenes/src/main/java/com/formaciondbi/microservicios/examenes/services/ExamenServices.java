package com.formaciondbi.microservicios.examenes.services;

import java.util.List;

import com.formaciondbi.microservicios.generics.examenes.Asignatura;
import com.formaciondbi.microservicios.generics.examenes.Examen;
import com.formaciondbi.microservicios.generics.services.Services;

public interface ExamenServices extends Services<Examen, Long>{

	public List<Examen> findByNombreExamen(String term);
	
	public Iterable<Asignatura> findAllAsignaturas();
	
	public List<Long> findExamenesIdsConRespuestasByPreguntaIds(List<Long> preguntaIds);
}
