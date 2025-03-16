package com.edugo.edugo_tcc.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.edugo.edugo_tcc.model.Aluno;

@Repository
public interface AlunoRepositoy extends JpaRepository<Aluno, UUID>{

}
