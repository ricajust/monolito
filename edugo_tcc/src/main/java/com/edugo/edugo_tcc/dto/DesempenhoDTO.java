package com.edugo.edugo_tcc.dto;

import java.util.UUID;
import lombok.Data;

@Data
public class DesempenhoDTO {
    private UUID id;
    private AlunoDTO aluno;
    private DisciplinaDTO disciplina;
    private Double nota;
    private int faltas;
}
