package dream.team.cetriolo.sprintbootapp.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dream.team.cetriolo.sprintbootapp.entity.Materia;
import dream.team.cetriolo.sprintbootapp.entity.Usuario;
import dream.team.cetriolo.sprintbootapp.entity.Permissao;
import dream.team.cetriolo.sprintbootapp.entity.Tarefa;
import dream.team.cetriolo.sprintbootapp.repository.MateriaRepository;
import dream.team.cetriolo.sprintbootapp.repository.UsuarioRepository;
import dream.team.cetriolo.sprintbootapp.repository.PermissaoRepository;
import dream.team.cetriolo.sprintbootapp.repository.TarefaRepository;

/* Usuario */

@Service("SecurityService")
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    private UsuarioRepository usuRepo;

    @Autowired
    private MateriaRepository matRepo;

    @Autowired
    private TarefaRepository tarRepo;

    @Autowired
    private PermissaoRepository perRepo;

    @Autowired
    private PasswordEncoder passEncoder;

    @Transactional
    public Usuario criarUsuario(String nome, String email, String telefone, String materia, String senha) {
        Materia mat = matRepo.findByNome(materia);
        if (mat == null) {
            mat = new Materia();
            mat.setNome(materia);
            matRepo.save(mat);
        }
        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setTelefone(telefone);
        usuario.setSenha(passEncoder.encode(senha));
        usuario.setMaterias(new HashSet<Materia>());
        usuario.getMaterias().add(mat);
        usuRepo.save(usuario);
        return usuario;
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public List<Usuario> buscarTodosUsuarios() {
        return usuRepo.findAll();
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public Usuario buscarUsuarioPorId(Long id) {
        Optional<Usuario> usuarioOp = usuRepo.findById(id);
        if (usuarioOp.isPresent()) {
            return usuarioOp.get();
        } else {
            throw new RuntimeException("Usuário não encontrado!");
        }
    }

    @Override
    public List<Usuario> buscarUsuarioPorNome(String nome) {
        List<Usuario> usuarios = usuRepo.findByNome(nome);
        Optional<List<Usuario>> usuariosOp = Optional.of(usuarios);

        if (!usuariosOp.isEmpty()) {
            return usuariosOp.get();
        } else {
            throw new RuntimeException("Usuário não encontrado!");
        }
    }

    @Transactional
    public String deletarUsuario(Long id) {
        Optional<Usuario> usuarioOp = usuRepo.findById(id);
        if (usuarioOp.isPresent()) {
            usuRepo.delete(usuarioOp.get());
            return "Usuário deletado com sucesso!";
        } else {
            throw new RuntimeException("Usuário não encontrado!");
        }
    }

    @Transactional
    public Usuario alterarUsuario(Long id, String nome, String email, String telefone) {
        Optional<Usuario> usuarioOp = usuRepo.findById(id);
        if (usuarioOp.isPresent()) {
            usuarioOp.get().setNome(nome);
            usuarioOp.get().setEmail(email);
            usuarioOp.get().setTelefone(telefone);
            return usuarioOp.get();
        } else {
            Usuario usuario = new Usuario();
            usuario.setNome(nome);
            usuario.setEmail(email);
            usuario.setTelefone(telefone);
            usuRepo.save(usuario);
            return usuario;
        }
    }

    /* Materia */

    @Override
    public List<Materia> buscarTodasMaterias() {
        return matRepo.findAll();
    }

    @Transactional
    public Materia criarMateria(String nome) {
        Materia materia = matRepo.findByNome(nome);
        if (materia == null) {
            materia = new Materia();
            materia.setNome(nome);
            matRepo.save(materia);
        }
        return materia;
    }

    @Override
    public Materia buscarMateriaPorId(Long id) {
        Optional<Materia> materiaOp = matRepo.findById(id);
        if (materiaOp.isPresent()) {
            return materiaOp.get();
        } else {
            throw new RuntimeException("Matéria não encontrada!");
        }
    }

    /* Tarefa */

    @Transactional
    public Tarefa criarTarefa(Long usuarioID, Long materiaID, String nomeArquivo, Integer nota) {
        Optional<Usuario> usu = usuRepo.findById(usuarioID);
        Optional<Materia> mat = matRepo.findById(materiaID);
        if (usu.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado!");
        }
        if (mat.isEmpty()) {
            throw new RuntimeException("Matéria não encontrada!");
        }
        Tarefa tarefa = new Tarefa();
        tarefa.setUsuario(usu.get());
        tarefa.setMateria(mat.get());
        tarefa.setNomeArquivo(nomeArquivo);
        tarefa.setNota(nota);
        tarRepo.save(tarefa);
        return tarefa;
    }

    @Override
    public List<Tarefa> buscarTodasTarefas() {
        return tarRepo.findAll();
    }

    /* Permissão */

    @Transactional
    public Permissao criarPermissao(String nome) {
        Permissao permissao = perRepo.findByNome(nome);
        if (permissao == null) {
            permissao = new Permissao();
            permissao.setNome(nome);
            perRepo.save(permissao);
        }
        return permissao;
    }

    @Override
    public List<Permissao> buscarTodasPermissoes() {
        return perRepo.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuRepo.findByEmail(username);
        if (usuario == null) {
            throw new UsernameNotFoundException("Email não encontado.");
        }

        return User.builder().username(username).password(usuario.getSenha())
                    .authorities(usuario.getPermissao().getNome()).build();
    }
}
