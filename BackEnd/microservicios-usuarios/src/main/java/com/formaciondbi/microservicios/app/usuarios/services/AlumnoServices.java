package com.formaciondbi.microservicios.app.usuarios.services;

import java.util.List;


import com.formaciondbi.microservicios.generics.models.entity.Alumno;
import com.formaciondbi.microservicios.generics.services.Services;

public interface AlumnoServices extends Services<Alumno, Long>{
	
	public List<Alumno> finByNombreOrApellido(String term);
	
	public Iterable<Alumno> findAllById(Iterable<Long> ids);
	
	public void eliminarCursoAlumnoPorId( Long id);
}
