package dream.team.cetriolo.sprintbootapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dream.team.cetriolo.sprintbootapp.entity.Tarefa;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    public Tarefa findByNomeArquivo(String nomeArquivo);

    public List<Tarefa> findByUsuarioId(Long id);

    public List<Tarefa> findByMateriaNome(String materia);
    
}