package dream.team.cetriolo.sprintbootapp.controller;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import dream.team.cetriolo.sprintbootapp.entity.Materia;
import dream.team.cetriolo.sprintbootapp.service.SecurityService;


@RestController
@RequestMapping(value = "/materia")
@CrossOrigin
public class MateriaController {

    @Autowired
    private SecurityService securityService;

    @GetMapping
    public List<Materia> buscarTodasMaterias() {
        return securityService.buscarTodasMaterias();
    }

    @GetMapping(value = "/{id}")
    public Materia buscaMateriaPorId(@PathVariable("id") Long id) {
        return securityService.buscarMateriaPorId(id);
    }

    @PostMapping
    public ResponseEntity<Materia> cadastrarNovoUsuario(@RequestBody Materia materia,
        UriComponentsBuilder uriComponentsBuilder) {

        materia  = securityService.criarMateria(materia.getNome());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(
            uriComponentsBuilder.path(
                "/materia/" + materia.getId()).build().toUri());

        return new ResponseEntity<Materia>(materia, responseHeaders, HttpStatus.CREATED);

    }
    
}