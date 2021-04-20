package com.formaciondbi.microservicios.examenes.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.formaciondbi.microservicios.examenes.repository.AsignaturaRepository;
import com.formaciondbi.microservicios.examenes.repository.ExamenRepository;
import com.formaciondbi.microservicios.generics.examenes.Asignatura;
import com.formaciondbi.microservicios.generics.examenes.Examen;
import com.formaciondbi.microservicios.generics.services.ServicesImpl;

@Service
public class ExamenServicesImpl extends ServicesImpl<Examen, Long> implements ExamenServices{

	@Autowired
	protected ExamenRepository examenRepository;
	
	@Autowired
	protected AsignaturaRepository asignaturaRepository;
	
	@Override
	@Transactional(readOnly = true)
	public List<Examen> findByNombreExamen(String term) {
		
		return examenRepository.findByNombreExamen(term);
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<Asignatura> findAllAsignaturas() {
		
		return asignaturaRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Long> findExamenesIdsConRespuestasByPreguntaIds(List<Long> preguntaIds) {
		return examenRepository.findExamenesIdsConRespuestasByPreguntaIds(preguntaIds);
	}

}
