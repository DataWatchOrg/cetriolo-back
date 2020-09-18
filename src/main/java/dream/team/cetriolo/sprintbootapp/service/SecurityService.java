package dream.team.cetriolo.sprintbootapp.service;

import dream.team.cetriolo.sprintbootapp.entity.Usuario;

public interface SecurityService {
    
    public Usuario criarUsuario(String nome, String email, String telefone, String materia);
    
}