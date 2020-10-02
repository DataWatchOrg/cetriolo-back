package dream.team.cetriolo.sprintbootapp.entity;

import java.util.Set;

import dream.team.cetriolo.sprintbootapp.controller.View;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name = "usu_usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JsonView(View.UsuarioResumo.class)
    @Column(name = "usu_nome")
    private String nome;

    @JsonView(View.UsuarioResumo.class)
    @Column(name = "usu_email")
    private String email;

    @Column(name = "usu_telefone")
    private String telefone;

    @JsonView(View.UsuarioResumo.class)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "uma_usuario_materia",
        joinColumns = { @JoinColumn(name = "usu_id") },
        inverseJoinColumns = { @JoinColumn(name = "mat_id") })
    private Set<Materia> materias;



    public Long getId() {
        return this.id;
    }

    public String getNome() {
        return this.nome;
    }

    public String getEmail() {
        return this.email;
    }

    public String getTelefone() {
        return this.telefone;
    }

    public Set<Materia> getMaterias() {
        return this.materias;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setMaterias(Set<Materia> materias) {
        this.materias = materias;
    }
}