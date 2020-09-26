package dream.team.cetriolo.sprintbootapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dream.team.cetriolo.sprintbootapp.entity.Usuario;
import dream.team.cetriolo.sprintbootapp.service.SecurityService;

@RestController
@RequestMapping(value = "/usuario")
@CrossOrigin
public class UsuarioController {
    
    @Autowired
    private SecurityService securityService;

    @GetMapping
    public List<Usuario> buscarTodosUsuarios() {
        return securityService.buscarTodosUsuarios();
    }

    @GetMapping(value = "/{id}")
    public Usuario buscarUsuarioPorId(@PathVariable("id") Long id) {
        return securityService.buscarUsuarioPorId(id);
    }

    @GetMapping(value = "/nome")
    public List<Usuario> buscarUsuarioPorNome(@RequestParam("nome") String nome) {
        return securityService.buscarUsuarioPorNome(nome);
    }

    @PostMapping
    public Usuario cadastrarNovoUsuario(@RequestBody Usuario usuario) {
        return securityService.criarUsuario(usuario.getNome(), usuario.getEmail(), usuario.getTelefone(), "");
    }
}