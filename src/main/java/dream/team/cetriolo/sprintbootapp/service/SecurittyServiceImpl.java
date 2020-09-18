package dream.team.cetriolo.sprintbootapp.service;

import java.util.HashSet;

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
}