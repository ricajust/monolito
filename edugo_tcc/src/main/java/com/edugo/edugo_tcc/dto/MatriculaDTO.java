package com.edugo.edugo_tcc.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class MatriculaDTO {
    private Long id;
    private AlunoDTO aluno;
    private DisciplinaDTO disciplina;
    private LocalDate dataMatricula;
    private String status;
}
