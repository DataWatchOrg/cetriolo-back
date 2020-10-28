package dream.team.cetriolo.sprintbootapp.controller;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import dream.team.cetriolo.sprintbootapp.entity.Permissao;
import dream.team.cetriolo.sprintbootapp.service.SecurityService;

@RestController
@RequestMapping(value = "/permissao")
@CrossOrigin
public class PermissaoController {
    
    @Autowired
    private SecurityService securityService;

    @JsonView(View.PermissaoResumo.class)
    @GetMapping
    public List<Permissao> buscarTodasPermissoes() {
        return securityService.buscarTodasPermissoes();
    }

    @JsonView(View.PermissaoResumo.class)
    @PostMapping
    public ResponseEntity<Permissao> cadastrarNovaPermissao(@RequestBody Permissao permissao, 
        UriComponentsBuilder uriComponentsBuilder) {
        
        permissao  = securityService.criarPermissao(permissao.getNome());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(
            uriComponentsBuilder.path(
                "/permissao/" + permissao.getId()).build().toUri());

        return new ResponseEntity<Permissao>(permissao, responseHeaders, HttpStatus.CREATED);
    }
}