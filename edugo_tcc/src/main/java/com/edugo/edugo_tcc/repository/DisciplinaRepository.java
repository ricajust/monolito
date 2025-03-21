package com.edugo.edugo_tcc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edugo.edugo_tcc.model.Disciplina;

@Repository
public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {

}
