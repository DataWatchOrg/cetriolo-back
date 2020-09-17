package dream.team.cetriolo.sprintbootapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dream.team.cetriolo.sprintbootapp.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
}