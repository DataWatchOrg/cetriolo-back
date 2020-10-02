package dream.team.cetriolo.sprintbootapp.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dream.team.cetriolo.sprintbootapp.entity.Materia;
import dream.team.cetriolo.sprintbootapp.entity.Usuario;
import dream.team.cetriolo.sprintbootapp.repository.MateriaRepository;
import dream.team.cetriolo.sprintbootapp.repository.UsuarioRepository;

@Service("SecurityService")
public class SecurittyServiceImpl implements SecurityService {

    @Autowired
    private UsuarioRepository usuRepo;

    @Autowired
    private MateriaRepository matRepo;

    @Transactional
	public Usuario criarUsuario(String nome, String email, String telefone, String materia) {
        Materia mat = matRepo.findByNome(materia);
        if(mat != null) {
            mat = new Materia();
            mat.setNome(materia);
            matRepo.save(mat);
        }
        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setTelefone(telefone);
        usuario.setMaterias(new HashSet<Materia>());
        usuario.getMaterias().add(mat);
        usuRepo.save(usuario);
        return usuario;
    }
    
    @Override
    public List<Usuario> buscarTodosUsuarios(){
        return usuRepo.findAll();
    }

    @Override
    public Usuario buscarUsuarioPorId(Long id) {
        Optional<Usuario> usuarioOp = usuRepo.findById(id);
        if(usuarioOp.isPresent()){
            return usuarioOp.get();
        } else {
            throw new RuntimeException("Usuário não encontrado!");
        }
    }

    @Override
    public List<Usuario> buscarUsuarioPorNome(String nome) {
        List<Usuario> usuarios = usuRepo.findByNome(nome);
        Optional<List<Usuario>> usuariosOp = Optional.of(usuarios);

        if(!usuariosOp.isEmpty()) {
            return usuariosOp.get();
        } else {
            throw new RuntimeException("Usuário não encontrado!");
        }
    }

    @Transactional
    public String deletarUsuario(Long id) {
        Optional<Usuario> usuarioOp = usuRepo.findById(id);
        if(usuarioOp.isPresent()){
            usuRepo.delete(usuarioOp.get());
            return "Usuário deletado com sucesso!";
        } else {
            throw new RuntimeException("Usuário não encontrado!");
        }
    }

    /* Materia */
    @Override
    public List<Materia> buscarTodasMaterias(){
        return matRepo.findAll();
    }

    @Transactional
	public Materia criarMateria(String nome) {
        Materia materia = matRepo.findByNome(nome);
        if(materia == null) {
            materia = new Materia();
            materia.setNome(nome);
            matRepo.save(materia);
        }
        return materia;
    }
}
