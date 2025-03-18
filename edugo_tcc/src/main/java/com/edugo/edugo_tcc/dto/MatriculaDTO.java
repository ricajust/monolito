package com.edugo.edugo_tcc.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class MatriculaDTO {
    private Long id;
    private AlunoDTO aluno;
    private List<DisciplinaDTO> disciplinas;
    private LocalDate dataMatricula;
    private String status;
}
