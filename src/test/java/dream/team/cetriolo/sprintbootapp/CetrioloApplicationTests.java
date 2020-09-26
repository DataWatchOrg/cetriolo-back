package dream.team.cetriolo.sprintbootapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import dream.team.cetriolo.sprintbootapp.entity.Materia;
import dream.team.cetriolo.sprintbootapp.entity.Usuario;
import dream.team.cetriolo.sprintbootapp.repository.MateriaRepository;
import dream.team.cetriolo.sprintbootapp.repository.UsuarioRepository;
import dream.team.cetriolo.sprintbootapp.service.SecurityService;

@SpringBootTest
@Transactional
@Rollback
class CetrioloApplicationTests {

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private MateriaRepository materiaRepo;

    @Autowired
    private SecurityService secService;

	@Test
	void contextLoads() {
    }
    
    @Test
    void testaInsercaoUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNome("Gabriel");
        usuario.setEmail("gabriel@email.com");
        usuario.setTelefone("984557672");
        usuario.setMaterias(new HashSet<Materia>());
        Materia materia = new Materia();
        materia.setNome("BD");
        materiaRepo.save(materia);
        usuario.getMaterias().add(materia);
        usuarioRepo.save(usuario);
        assertNotNull(usuario.getMaterias().iterator().next().getId());
    }

    @Test
    void testaInsercaoMateria() {
        Usuario usuario = new Usuario();
        usuario.setNome("Matias");
        usuario.setEmail("matias@email.com");
        usuario.setTelefone("984557672");
        usuarioRepo.save(usuario);
        Materia materia = new Materia();
        materia.setNome("Matematica");
        materia.setUsuarios(new HashSet<Usuario>());
        materia.getUsuarios().add(usuario);
        materiaRepo.save(materia);
        assertNotNull(materia.getUsuarios().iterator().next().getId());
    }


    @Test
    void testaMateria() {
        Usuario usuario = usuarioRepo.findById(1L).get();
        assertEquals("Algoritmos", usuario.getMaterias().iterator().next().getNome());
    }

    @Test
    void testaUsuario() {
        Materia materia = materiaRepo.findById(1L).get();
        assertEquals("Ana", materia.getUsuarios().iterator().next().getNome());
    }

    @Test
    void testaConsultaPorNomeEEmail() {
        Usuario usuario = usuarioRepo.findByNomeAndEmail("Ana", "ana@email.com");
        assertEquals(1L, usuario.getId());
    }

    @Test
    void testaConsultaUsuarioNomeMateria() {
        List<Usuario> usuarios = usuarioRepo.findByMateriasNome("Algoritmos");
        assertFalse(usuarios.isEmpty());
    }

    @Test
    void testaServicoCriaUsuario() {
        Usuario usuario = secService.criarUsuario("Dede", "dedemeikg@gmail.com", "129112938982", "Gestão de Projetos");
        assertNotNull(usuario);
    }
}
