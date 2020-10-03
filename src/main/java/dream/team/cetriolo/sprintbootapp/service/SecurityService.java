package dream.team.cetriolo.sprintbootapp.service;

import java.util.List;

import dream.team.cetriolo.sprintbootapp.entity.Materia;
import dream.team.cetriolo.sprintbootapp.entity.Usuario;

public interface SecurityService {
    
    /* Usuário */

    public Usuario criarUsuario(String nome, String email, String telefone, String materia);

    public List<Usuario> buscarTodosUsuarios();

    public Usuario buscarUsuarioPorId(Long id);

    public List<Usuario> buscarUsuarioPorNome(String nome);

    public String deletarUsuario(Long id);


    /* Matéria */

    public Materia criarMateria(String nome);

    public List<Materia> buscarTodasMaterias();

    public Materia buscarMateriaPorId(Long id);
}