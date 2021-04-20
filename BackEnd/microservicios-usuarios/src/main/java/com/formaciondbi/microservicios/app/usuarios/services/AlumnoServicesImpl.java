package com.formaciondbi.microservicios.app.usuarios.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.formaciondbi.microservicios.app.usuarios.client.CursoFeignClient;
import com.formaciondbi.microservicios.app.usuarios.repository.AlumnoRepository;
import com.formaciondbi.microservicios.generics.models.entity.Alumno;
import com.formaciondbi.microservicios.generics.services.ServicesImpl;

@Service
public class AlumnoServicesImpl extends ServicesImpl<Alumno, Long> implements AlumnoServices {

	@Autowired
	protected AlumnoRepository alumnoRepository;
	
	@Autowired
	protected CursoFeignClient clientCurso;
	
	@Override
	@Transactional(readOnly = true)
	public List<Alumno> finByNombreOrApellido(String term) {
		
		return alumnoRepository.finByNombreOrApellido(term);
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<Alumno> findAllById(Iterable<Long> ids) {
		
		return alumnoRepository.findAllById(ids);
	}

	@Override
	public void eliminarCursoAlumnoPorId(Long id) {
		clientCurso.eliminarCursoAlumnoPorId(id);
		
	}

	@Override
	@Transactional
	public boolean deleteById(Long id) throws Exception {
		this.eliminarCursoAlumnoPorId(id);
		return super.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<Alumno> findAll() throws Exception {
		return alumnoRepository.findAllByOrderByIdAsc();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Alumno> findAll(Pageable pageable) throws Exception {
		return alumnoRepository.findAllByOrderByIdAsc(pageable);
	}

	
}
