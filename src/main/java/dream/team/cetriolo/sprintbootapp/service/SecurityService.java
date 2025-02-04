package dream.team.cetriolo.sprintbootapp.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import dream.team.cetriolo.sprintbootapp.entity.Materia;
import dream.team.cetriolo.sprintbootapp.entity.Usuario;
import dream.team.cetriolo.sprintbootapp.entity.Permissao;
import dream.team.cetriolo.sprintbootapp.entity.Tarefa;

public interface SecurityService extends UserDetailsService {
    
    /* Usuário */

    public Usuario criarUsuario(String nome, String email, String telefone, String materia, String senha, String permissao, String race);

    public List<Usuario> buscarTodosUsuarios();

    public Usuario buscarUsuarioPorId(Long id);

    public List<Usuario> buscarUsuarioPorNome(String nome);

    public Usuario buscarUsuarioPorEmail(String email);

    public String deletarUsuario(Long id);

    public Usuario alterarUsuario(Long id, String nome, String email, String telephone, String race);

    /* Matéria */

    public Materia criarMateria(String nome);

    public List<Materia> buscarTodasMaterias();

    public Materia buscarMateriaPorId(Long id);

    /* Tarefa */

    public Tarefa criarTarefa(String usuarioEmail, Long materiaID, String nomeArquivo, Integer nota);

    public List<Tarefa> buscarTodasTarefas();

    /* Permissão */

    public Permissao criarPermissao(String nome);

    public List<Permissao> buscarTodasPermissoes();
}