package dream.team.cetriolo.sprintbootapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dream.team.cetriolo.sprintbootapp.entity.Materia;

public interface MateriaRepository extends JpaRepository<Materia, Long> {
    
    public Materia findByNome(String materia);

}