package dream.team.cetriolo.sprintbootapp.entity;

import dream.team.cetriolo.sprintbootapp.controller.View;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name = "tar_tarefa")
public class Tarefa {
    
    @Id
    @Column(name = "tar_id")
    private Long id;

    @JsonView(View.TarefaResumo.class)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usu_id")
    private Usuario usuario;

    @JsonView(View.TarefaResumo.class)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mat_id")
    private Materia materia;

    @JsonView(View.TarefaResumo.class)
    @Column(name = "tar_nome_arquivo")
    private String nomeArquivo;

    @JsonView(View.TarefaResumo.class)
    @Column(name = "tar_nota")
    private Integer nota;

    public Long getId() {
        return this.id;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public Materia getMateria() {
        return this.materia;
    }

    public String getNomeArquivo() {
        return this.nomeArquivo;
    }

    public Integer getNota() {
        return this.nota;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public void setNota(Integer nota){
        this.nota = nota;
    }
}