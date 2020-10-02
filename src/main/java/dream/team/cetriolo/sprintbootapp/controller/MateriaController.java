package dream.team.cetriolo.sprintbootapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}