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

import dream.team.cetriolo.sprintbootapp.entity.Tarefa;
import dream.team.cetriolo.sprintbootapp.service.SecurityService;

@RestController
@RequestMapping(value = "/tarefa")
@CrossOrigin
public class TarefaController {
    
    @Autowired
    private SecurityService securityService;

    @JsonView(View.TarefaResumo.class)
    @GetMapping
    public List<Tarefa> buscarTodasTarefas() {
        return securityService.buscarTodasTarefas();
    }

    @JsonView(View.TarefaResumo.class)
    @PostMapping
    public ResponseEntity<Tarefa> cadastrarNovaTarefa(@RequestBody Tarefa tarefa, 
        UriComponentsBuilder uriComponentsBuilder) {
        
        tarefa  = securityService.criarTarefa(tarefa.getUsuario().getEmail(), tarefa.getMateria().getId(), tarefa.getNomeArquivo(), tarefa.getNota());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(
            uriComponentsBuilder.path(
                "/tarefa/" + tarefa.getId()).build().toUri());

        return new ResponseEntity<Tarefa>(tarefa, responseHeaders, HttpStatus.CREATED);
    }
}