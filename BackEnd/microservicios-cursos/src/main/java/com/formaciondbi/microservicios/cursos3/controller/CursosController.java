package com.formaciondbi.microservicios.cursos3.controller;

import com.formaciondbi.microservicios.cursos3.entity.CursoAlumno;
import com.formaciondbi.microservicios.cursos3.entity.Cursos;
import com.formaciondbi.microservicios.cursos3.services.CursosService;
import com.formaciondbi.microservicios.generics.controllers.ControllerImpl;
import com.formaciondbi.microservicios.generics.examenes.Examen;
import com.formaciondbi.microservicios.generics.models.entity.Alumno;
import com.formaciondbi.microservicios.generics.services.ServicesImpl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CursosController extends ControllerImpl<Cursos, ServicesImpl<Cursos, Long>> {

	@Value("${config.balanceador.test}")
	private String balanceadorTest;
	
	@Autowired
	private CursosService cursoService; 

	@GetMapping("/balanceador-test")
	public ResponseEntity<?> balanceadorTest() {
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("balanceador", balanceadorTest);
		try {
			response.put("cursos", servicio.findAll());
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("no anda");
		}

		return ResponseEntity.ok(response);
	}

	@Override
	@RequestMapping("/{id}")
	public ResponseEntity<?> getOne(@PathVariable Long id) {
		try {
			Cursos curso = servicio.findById(id);

			if (curso.getCursoAlumno().isEmpty() == false) {
				List<Long> ids = curso.getCursoAlumno().stream().map(ca -> {
					return ca.getAlumnoId();
				}).collect(Collectors.toList());

				List<Alumno> alumnos = (List<Alumno>) cursoService.obtenerAlumnosPorCurso(ids);
				curso.setAlumno(alumnos);

			}

			return ResponseEntity.status(HttpStatus.OK).body(curso);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("{\"error\":\"error por favor intente mas tarde.\"}");
		}
	}

	@Override
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		try {
			// obtener list cursos -> c/u get list cursosAlumnos -> c/cur crear alumno
			// y set alumno id de curA y agregar el alumno al curso
			List<Cursos> cursos =  ((Collection<Cursos>) servicio.findAll()).stream().map(c -> {
				c.getCursoAlumno().forEach(ca -> {
					Alumno alumno = new Alumno();
					alumno.setId(ca.getAlumnoId());
					c.addAlumno(alumno);
				});
				return c;
			}).collect(Collectors.toList());

			return ResponseEntity.status(HttpStatus.OK).body(cursos);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("{\"error\":\"error por favor intente mas tarde.\"}");
		}
	}

	@GetMapping("/paged")
	public ResponseEntity<?> getAll(Pageable pageable) {
		try {
			Page<Cursos> cursos = servicio.findAll(pageable).map(curso -> {
				curso.getCursoAlumno().forEach(ca -> {
					Alumno alumno = new Alumno();
					alumno.setId(ca.getAlumnoId());
					curso.addAlumno(alumno);
				});
				return curso;
			});
			return ResponseEntity.status(HttpStatus.OK).body(cursos);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("{\"error\":\"error por favor intente mas tarde.\"}");
		}
	}

	@PutMapping("/{id}/asignar-alumnos")
	public ResponseEntity<?> asignarAlumnos(@PathVariable Long id, @RequestBody List<Alumno> alumnos) throws Exception {
		Cursos op;
		try {
			op = this.servicio.findById(id);

		} catch (Exception e) {

			return ResponseEntity.notFound().build();
		}

		Cursos dbCurso = op;
		alumnos.forEach(a -> {

			CursoAlumno cursoAlumno = new CursoAlumno();
			cursoAlumno.setAlumnoId(a.getId());
			cursoAlumno.setCurso(dbCurso);
			dbCurso.addCursoAlumno(cursoAlumno);
		});

		return ResponseEntity.status(HttpStatus.CREATED).body(this.servicio.save(dbCurso));

	}

	@PutMapping("/{id}/eliminar-alumno")
	public ResponseEntity<?> eliminarAlumno(@PathVariable Long id, @RequestBody Alumno alumno) throws Exception {
		Cursos op;
		try {
			op = this.servicio.findById(id);
		} catch (Exception e) {

			return ResponseEntity.notFound().build();
		}

		Cursos dbCurso = op;
		CursoAlumno cursoAlumno = new CursoAlumno();
		cursoAlumno.setAlumnoId(alumno.getId());
		dbCurso.removeCursoAlumno(cursoAlumno);
		return ResponseEntity.status(HttpStatus.CREATED).body(this.servicio.save(dbCurso));
	}

	@GetMapping("/alumno/{id}")
	public ResponseEntity<?> buscarPorAlumnoId(@PathVariable Long id) {
		Cursos curso = cursoService.findCursoByAlumnoId(id);

		if (curso != null) {
			List<Long> examenesId = (List<Long>) cursoService.examenesIdsRespondidosPorAlumno(id);
			if (examenesId != null && examenesId.size() > 0) {
				List<Examen> examenes = curso.getExamenes().stream().map(examen -> {
					if (examenesId.contains(examen.getId())) {
						examen.setRespondido(true);
					}
					return examen;
				}).collect(Collectors.toList());
				curso.setExamenes(examenes);
			}

		}
		return ResponseEntity.ok(curso);
	}

	@PutMapping("/{id}/asignar-examenes")
	public ResponseEntity<?> asignarExamenes(@PathVariable Long id, @RequestBody List<Examen> examenes)
			throws Exception {
		Cursos op;
		try {
			op = this.servicio.findById(id);

		} catch (Exception e) {

			return ResponseEntity.notFound().build();
		}

		Cursos dbCurso = op;
		examenes.forEach(e -> {
			dbCurso.addExamen(e);
		});

		return ResponseEntity.status(HttpStatus.CREATED).body(this.servicio.save(dbCurso));

	}

	@PutMapping("/{id}/eliminar-examen")
	public ResponseEntity<?> eliminarExamen(@PathVariable Long id, @RequestBody Examen examen) throws Exception {
		Cursos op;
		try {
			op = this.servicio.findById(id);
		} catch (Exception e) {

			return ResponseEntity.notFound().build();
		}

		Cursos dbCurso = op;
		dbCurso.removeExamen(examen);
		return ResponseEntity.status(HttpStatus.CREATED).body(this.servicio.save(dbCurso));
	}

	@DeleteMapping("/eliminar-alumno/{id}")
	public ResponseEntity<?> eliminarCursoAlumnoPorId(@PathVariable Long id) {
		this.cursoService.eliminarCursoAlumnoPorId(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
