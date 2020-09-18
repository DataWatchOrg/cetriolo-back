package dream.team.cetriolo.sprintbootapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import dream.team.cetriolo.sprintbootapp.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    public Usuario findByNomeAndEmail(String nome, String email);
 
    @Query("select u from Usuario u where u.nome = ?1 and u.email = ?2")
    public Usuario buscarUsuarioPorNomeEEmail(String nome, String email);
    
    public List<Usuario> findByMateriasNome(String materia);

    @Query("select u from Usuario u inner join u.materias m where m.nome = ?1")
    public List<Usuario> buscaMateriasPorNome(String materia);


}