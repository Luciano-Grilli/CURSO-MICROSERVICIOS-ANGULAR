package com.formaciondbi.microservicios.cursos3.repository;
import com.formaciondbi.microservicios.generics.repository.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import com.formaciondbi.microservicios.cursos3.entity.Cursos;

public interface CursosRepository extends Repository<Cursos, Long> {

	@Query("select c from Cursos c join fetch c.cursoAlumno a where a.alumnoId=?1")
	public Cursos findCursoByAlumnoId(Long id);

	@Modifying
	@Query("delete from CursoAlumno ca where ca.alumnoId=?1")
	public void eliminarCursoAlumnoPorId(Long id);

}
