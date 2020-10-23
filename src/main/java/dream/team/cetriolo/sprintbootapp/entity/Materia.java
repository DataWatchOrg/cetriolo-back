package dream.team.cetriolo.sprintbootapp.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import dream.team.cetriolo.sprintbootapp.controller.View;

@Entity
@Table(name = "mat_materia")
public class Materia {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mat_id")
    private Long id;

    @JsonView({View.UsuarioResumo.class, View.MateriaResumo.class})
    @Column(name = "mat_nome")
    private String nome;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "materias")
    private Set<Usuario> usuarios;

    @JsonIgnore
    @OneToMany(mappedBy = "materia", fetch = FetchType.LAZY)
    private Set<Tarefa> tarefas;

    public Long getId() {
        return this.id;
    }

    public String getNome() {
        return this.nome;
    }

    public Set<Usuario> getUsuarios() {
        return this.usuarios;
    }

    public Set<Tarefa> getTarefas() {
        return this.tarefas;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setUsuarios(Set<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

     public void getTarefas(Set<Tarefa> tarefas) {
        this.tarefas = tarefas;
    }
}