package com.edugo.edugo_tcc.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.edugo.edugo_tcc.model.Aluno;
import com.edugo.edugo_tcc.model.Disciplina;
import com.edugo.edugo_tcc.model.Matricula;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Long>{
    List<Matricula> findByAlunoAndStatus(Aluno aluno, String status);
    //Em inglês, pois a convenção do Spring Data JPA para palavras-chave como existsBy é baseada em inglês.
    boolean existsByAlunoAndDisciplinaAndStatus(Aluno aluno, Disciplina disciplina, String status);
    
    @Query("SELECT m FROM Matricula m JOIN m.aluno a WHERE a.cpf = :cpf")
    List<Matricula> findByAlunoCpf(@Param("cpf") String cpf);
}