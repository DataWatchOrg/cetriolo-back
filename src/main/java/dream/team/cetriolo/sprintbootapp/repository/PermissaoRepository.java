package dream.team.cetriolo.sprintbootapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dream.team.cetriolo.sprintbootapp.entity.Permissao;

public interface PermissaoRepository extends JpaRepository<Permissao, Long> {
    
    public Permissao findByNome(String permissao);

}