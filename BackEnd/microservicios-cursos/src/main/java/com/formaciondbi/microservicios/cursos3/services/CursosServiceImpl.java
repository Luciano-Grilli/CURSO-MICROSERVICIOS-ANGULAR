package com.formaciondbi.microservicios.cursos3.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.formaciondbi.microservicios.cursos3.clients.AlumnoFeingClient;
import com.formaciondbi.microservicios.cursos3.clients.RespuestaFeignClient;
import com.formaciondbi.microservicios.cursos3.entity.Cursos;
import com.formaciondbi.microservicios.cursos3.repository.CursosRepository;
import com.formaciondbi.microservicios.generics.models.entity.Alumno;
import com.formaciondbi.microservicios.generics.services.ServicesImpl;

@Service
public class CursosServiceImpl extends ServicesImpl<Cursos, Long> implements CursosService {

	@Autowired
	private CursosRepository cursoRepository;

	@Autowired
	private RespuestaFeignClient client;

	@Autowired
	private AlumnoFeingClient clientAlumno;

	/*
	public CursosServiceImpl(BaseRepository<Cursos, Long> Repository) {
		super(Repository);
	}
	*/

	@Override
	@Transactional(readOnly = true)
	public Cursos findCursoByAlumnoId(Long id) {

		return cursoRepository.findCursoByAlumnoId(id);
	}

	@Override
	public Iterable<Long> examenesIdsRespondidosPorAlumno(Long alumnoId) {

		return client.examenesIdsRespondidosPorAlumno(alumnoId);
	}

	@Override
	public Iterable<Alumno> obtenerAlumnosPorCurso(List<Long> ids) {
		return clientAlumno.obtenerAlumnosPorCurso(ids);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Cursos> findAll(Pageable pageable) throws Exception {

		return cursoRepository.findAll(pageable);
	}

	@Override
	@Transactional
	public void eliminarCursoAlumnoPorId(Long id) {
		cursoRepository.eliminarCursoAlumnoPorId(id);

	}

}
