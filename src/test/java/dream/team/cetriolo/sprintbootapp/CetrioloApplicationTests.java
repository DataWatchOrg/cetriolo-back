package dream.team.cetriolo.sprintbootapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import dream.team.cetriolo.sprintbootapp.entity.Usuario;
import dream.team.cetriolo.sprintbootapp.repository.UsuarioRepository;

@SpringBootTest
@Transactional
@Rollback
class CetrioloApplicationTests {

    @Autowired
    private UsuarioRepository usuarioRepo;

	@Test
	void contextLoads() {
    }
    
    @Test
    void testaInsercao() {
        Usuario usuario = new Usuario();
        usuario.setNome("Ana");
        usuario.setEmail("ana@email.com");
        usuario.setTelefone("984557672");
        usuarioRepo.save(usuario);
        assertNotNull(usuario.getId());
    }

}
