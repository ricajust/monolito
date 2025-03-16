package com.edugo.edugo_tcc.dto;

import lombok.Data;

@Data
public class DesempenhoDTO {
    private Long id;
    private AlunoDTO aluno;
    private DisciplinaDTO disciplina;
    private Double nota;
    private int faltas;
}
