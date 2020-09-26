package dream.team.cetriolo.sprintbootapp.service;

import java.util.List;

import dream.team.cetriolo.sprintbootapp.entity.Usuario;

public interface SecurityService {
    
    public Usuario criarUsuario(String nome, String email, String telefone, String materia);

    public List<Usuario> buscarTodosUsuarios();

    public Usuario buscarUsuarioPorId(Long id);

    public List<Usuario> buscarUsuarioPorNome(String nome);
}