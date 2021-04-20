package com.formaciondbi.microservicios.cursos3.services;

import java.util.List;

import com.formaciondbi.microservicios.cursos3.entity.Cursos;
import com.formaciondbi.microservicios.generics.models.entity.Alumno;
import com.formaciondbi.microservicios.generics.services.Services;

public interface CursosService extends Services<Cursos, Long> {

	public Cursos findCursoByAlumnoId(Long id);

	public Iterable<Long> examenesIdsRespondidosPorAlumno(Long alumnoId);

	public Iterable<Alumno> obtenerAlumnosPorCurso(List<Long> ids);

	public void eliminarCursoAlumnoPorId(Long id);

}
