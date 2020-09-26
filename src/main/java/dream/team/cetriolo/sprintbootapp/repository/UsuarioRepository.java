package dream.team.cetriolo.sprintbootapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dream.team.cetriolo.sprintbootapp.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    public Usuario findByNomeAndEmail(String nome, String email);
    
    public List<Usuario> findByMateriasNome(String materia);

    public List<Usuario> findByNome(String nome);
}