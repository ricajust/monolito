package com.edugo.edugo_tcc.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class MatriculaResponseDTO {
    private Long id;
    private AlunoInfoDTO aluno;
    private Long disciplinaId;
    private String disciplinaNome;
    private BigDecimal disciplinaValor;
    private String professorNome;
    private LocalDate dataMatricula;
    private String status;
}