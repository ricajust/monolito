package com.edugo.edugo_tcc.repository;

import java.util.UUID;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.edugo.edugo_tcc.model.Aluno;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, UUID>{
    Optional<Aluno> findByCpf(String cpf);
}
